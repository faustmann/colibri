package Utils;

import com.enernoc.open.oadr2.model.v20b.OadrCreatedEvent;
import com.enernoc.open.oadr2.model.v20b.ei.OptTypeType;
import openADR.OADRMsgInfo.MsgInfo_OADRCreatedEvent;
import openADR.OADRMsgInfo.MsgInfo_OADRDistributeEvent;
import openADR.OADRMsgInfo.OADRMsgInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import semanticCore.MsgObj.ColibriMessage;
import semanticCore.MsgObj.ContentMsgObj.Description;
import semanticCore.MsgObj.ContentMsgObj.PutMsg;
import semanticCore.MsgObj.ContentType;
import semanticCore.MsgObj.Header;
import semanticCore.MsgObj.MsgType;
import semanticCore.WebSocketHandling.ServiceDataConfig;

import javax.xml.bind.JAXBException;
import java.io.StringReader;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by georg on 04.07.16.
 * Objects from this class are used to bridge the colibri part with the openADR part.
 * This is only for the direction from colibri to openADR.
 */
public class ColibriToOpenADR {

    private Logger logger = LoggerFactory.getLogger(ColibriToOpenADR.class);

    public Pair<ColibriMessage, OADRMsgInfo> convertColibriMsg(ColibriMessage msg, OpenADRColibriBridge bridge){
        Pair<ColibriMessage, OADRMsgInfo> result = null;

        MsgType type = msg.getMsgType();

        switch (type){
            case PUT_DATA_VALUES:
                result = handle_PUT_DATA_VALUES(msg, bridge);
                break;
            case GET_DATA_VALUES:
                result = handle_GET_DATA_VALUES(msg, bridge);
                break;
            case QUERY_RESULT:
                result = handle_QUERY_RESULT(msg);
                break;
            default:
                result = new Pair<>(null, null);
                break;
        }

        return result;

    }

    private Pair<ColibriMessage, OADRMsgInfo> handle_PUT_DATA_VALUES(ColibriMessage msg, OpenADRColibriBridge bridge){
        logger.info(">>>>>>>handle "+MsgType.PUT_DATA_VALUES + " message");

        StringReader contentReader = new StringReader(msg.getContent());

        MsgInfo_OADRCreatedEvent createdEvent = null;
        String statusCode = "200";

        try {
            PutMsg putMsg = (PutMsg)bridge.getColClient().getJaxbUnmarshaller().unmarshal(contentReader);

            List<Pair<String, String>> dataValues = new ArrayList<>();
            Map<String, Pair<String, String>> parameter = new HashMap<>();

            for(Description description : putMsg.getDescriptions()){
                if(description.getHasValue().size() == 2){
                    dataValues.add(new Pair<String, String>(description.getHasValue().get(0).getResource(),
                            description.getHasValue().get(1).getResource().trim()));
                }

                if(description.getHasParameter().size() == 1){
                    parameter.put(description.getAbout(),
                            new Pair<String, String>(description.getHasParameter().get(0).getResource(),
                            description.getValue().getValue().trim()
                    ));
                }
            }

            Map<String, Boolean> eventStatus = new HashMap<>();

            for(Pair<String, String> dataValue : dataValues){
                String eventID;
                boolean status;

                String parameterValue1;
                String parameterValue2;

                String parameterURL1 = parameter.get(dataValue.getFst()).getFst();
                String parameterURL2 = parameter.get(dataValue.getSnd()).getFst();

                Boolean normalOrder = null;

                // TODO better variable naming
                for(String serviceURL : bridge.getColClient().getKnownServicesHashMap().keySet()){
                    ServiceDataConfig followServiceDataConfig = bridge.getColClient().getKnownServicesHashMap().get(serviceURL).getServiceDataConfig().getFollowUpServiceDataConfig();
                    for(ServiceDataConfig.Parameter configParameter : followServiceDataConfig.getParameters()){
                        for(String type : configParameter.getTypes()){
                            if(type.equals("&colibri;InformationParameter")){
                                String currentMsgIDParameter = configParameter.getName();
                                if(parameterURL1.equals(currentMsgIDParameter)){
                                    normalOrder = true;
                                } else if(parameterURL2.equals(currentMsgIDParameter)){
                                    normalOrder = false;
                                }
                            }
                        }
                    }
                }

                parameterValue1 = parameter.get(dataValue.getFst()).getSnd();
                parameterValue2 = parameter.get(dataValue.getSnd()).getSnd();

                if(normalOrder){
                    eventID = parameterValue1;
                    status = Boolean.parseBoolean(parameterValue2);
                } else {
                    eventID = parameterValue2;
                    status = Boolean.parseBoolean(parameterValue1);
                }

                eventStatus.put(eventID, status);

            }

            for(String key : eventStatus.keySet()){
                Pair<Pair<Date, Date>, MsgInfo_OADRDistributeEvent.Event> elem = bridge.getOpenADREvent(key);
                if( elem != null){
                    createdEvent = new MsgInfo_OADRCreatedEvent();
                    MsgInfo_OADRDistributeEvent.Event event = elem.getSnd();
                    logger.info("event: " + key + " status " + eventStatus.get(key));
                    MsgInfo_OADRCreatedEvent.EventResponse eventResponse = createdEvent.getNewEventResponse();
                    eventResponse.setEventID(key);
                    eventResponse.setOptType(eventStatus.get(key)? OptTypeType.OPT_IN:OptTypeType.OPT_OUT);

                    eventResponse.setModificationNumber(event.getModificationNumber());
                    eventResponse.setRequestID(event.getRequestID());
                    event.setCreatedEventToVTNSent(true);
                    createdEvent.getEventResponses().add(eventResponse);
                } else {
                    logger.error("wrong service url or eventID!");
                    statusCode = "500";
                }

            }

        } catch (JAXBException e) {
            logger.error("invalid syntax!");
            statusCode = "300";
        }

        ColibriMessage reply = bridge.getColClient().getGenSendMessage().gen_STATUS(statusCode, msg.getHeader().getMessageId());

        return new Pair<ColibriMessage, OADRMsgInfo>(reply, createdEvent);

    }

    private Pair<ColibriMessage, OADRMsgInfo> handle_GET_DATA_VALUES(ColibriMessage msg, OpenADRColibriBridge bridge){
        logger.info(">>>>>>>handle "+MsgType.GET_DATA_VALUES + " message");

        boolean onlyOneEventNeeded = false;

        if(!msg.getContent().matches("[^\\?]+\\??(\\&?(from=|to=)\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}Z){0,2}")){
            logger.error("malformed url: " + msg.getContent());
            ColibriMessage reply = bridge.getColClient().getGenSendMessage().gen_STATUS("300", msg.getHeader().getMessageId());
            return new Pair<ColibriMessage, OADRMsgInfo>(reply, null);
        }

        String[] serviceURLParts = msg.getContent().split("\\?");


        String serviceURL = serviceURLParts[0];
        Date fromDate = null;
        Date toDate = null;

        if(serviceURLParts.length == 2){
            String[] dates = serviceURLParts[1].split("&");

            for(String dateStr : dates){
                Date date = TimeDurationConverter.ical2Date(dateStr.split("=")[1]);

                if(dateStr.startsWith("from=")){
                    fromDate = date;
                }
                if(dateStr.startsWith("to=")){
                    toDate = date;
                }
            }
        }

        if(fromDate == null && toDate == null){
            fromDate=Main.testDate;
            onlyOneEventNeeded = true;
        }

        if(!bridge.getColClient().getKnownServicesHashMap().keySet().contains(serviceURL)){
            logger.error("service URL " + serviceURL + " unknown");
            ColibriMessage reply = bridge.getColClient().getGenSendMessage().gen_STATUS("500", msg.getHeader().getMessageId());
            return new Pair<ColibriMessage, OADRMsgInfo>(reply, null);
        }

        List<MsgInfo_OADRDistributeEvent.Event> events= bridge.getOpenADREvents(serviceURL, new Pair<Date, Date>(fromDate, toDate));
        if(onlyOneEventNeeded){
            events = events.subList(0,1);
        }
        PutMsg putMsgContent = bridge.getOpenADRToColibri().convertOpenADREventsToColibriPUTContent(events, bridge);

        Header header = new Header();
        header.setDate(new Date());
        header.setContentType(ContentType.APPLICATION_RDF_XML);
        header.setMessageId(bridge.getColClient().getGenSendMessage().getUniqueMsgID());
        header.setReferenceId(msg.getHeader().getMessageId());

        ColibriMessage reply = new ColibriMessage(MsgType.PUT_DATA_VALUES, header, bridge.getColClient().getGenSendMessage().transformPOJOToXML(putMsgContent));

        logger.info("reply PUT message with service URL " + serviceURL );
        return new Pair<>(reply, null);
    }

    private Pair<ColibriMessage, OADRMsgInfo> handle_QUERY_RESULT(ColibriMessage msg){
        logger.info(">>>>>>>handle "+MsgType.QUERY_RESULT + " message");

        return new Pair<>(null, null);
    }

}