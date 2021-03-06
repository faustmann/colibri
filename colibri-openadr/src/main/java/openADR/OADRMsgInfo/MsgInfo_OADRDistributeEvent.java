package openADR.OADRMsgInfo;

import com.enernoc.open.oadr2.model.v20b.ResponseRequiredType;
import com.enernoc.open.oadr2.model.v20b.ei.SignalTypeEnumeratedType;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by georg on 07.06.16.
 * This class holds the important information for an oadrDistributeEvent message.
 */
public class MsgInfo_OADRDistributeEvent implements OADRMsgInfo {

    // identifier for this message
    private String requestID;

    // demand rresponse events
    private List<Event> events;

    public MsgInfo_OADRDistributeEvent(){
        events = new ArrayList<>();
    }

    public String getRequestID() {
        return requestID;
    }

    public void setRequestID(String requestID) {
        this.requestID = requestID;
    }

    public List<Event> getEvents() {
        return events;
    }

    public Event getNewEvent(){
        return new Event();
    }

    public Signal getNewSignal(){
        return new Signal();
    }

    @Override
    public String toString() {
        return "{\"MsgInfo_OADRDistributeEvent\":{"
                + "                        \"requestID\":\"" + requestID + "\"\n"
                + ",                         \"events\":" +"\n"+ events
                + "}}";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getMsgType() {
        return "oadrDistributeEvent";
    }

    /**
     * An object containing all the information for a single event
     */
    public class Event{
        // identifier for this event
        private String eventID;
        // Incremented each time when an event is modified.
        private long modificationNumber;
        /* The priority of the event in relation to other events (The lower the number the
            higher the priority. A value of zero (0) indicates no priority, which is the lowest priority
            by default). */
        private long priority;
        // Anything other than false indicates a test event
        private boolean testEvent;
        // A URI identifying a DR Program
        private String marketContext;
        // Controls when optIn/optOut response is required. Can be always or never
        private ResponseRequiredType responseRequired;
        // The starting time for the activity, data, or state change
        private Date startDate;
        /* Event start times can be randomized using the tolerance object
            in the eiActivePeriod. The subelement startafter defines a
            randomization time window used by the VEN to select a random
            value that is added to the start time of the event. If the
            start time of a one hour event is 3:00pm and the randomization
            window is 5 minutes, if the VEN selected 3 minutes as the
            random value then the event would start at 3:03pm and would
            end at 4:03pm */
        private long toleranceSec;
        // A time period for an event
        private long durationSec;
        // A duration before or after the event start time during load shed should transit
        private long rampUpSec;
        // A duration before or after the event end time during load shed should transit.
        private long recoverySec;
        // An object containing all the information for a single signal in an event
        private List<Signal> signals;
        // The dateTime when the payload was created
        private Date createdDateTime;
        // identifier for the distribute message
        private String requestID;

        private AtomicBoolean createdEventToVTNSent;

        public Event(){
            signals = new ArrayList<>();
            createdEventToVTNSent = new AtomicBoolean(false);
        }

        public long getRampUpSec() {
            return rampUpSec;
        }

        public void setRampUpSec(long rampUpSec) {
            this.rampUpSec = rampUpSec;
        }

        public long getRecoverySec() {
            return recoverySec;
        }

        public void setRecoverySec(long recoverySec) {
            this.recoverySec = recoverySec;
        }

        public String getEventID() {
            return eventID;
        }

        public void setEventID(String eventID) {
            this.eventID = eventID;
        }

        public long getModificationNumber() {
            return modificationNumber;
        }

        public void setModificationNumber(long modificationNumber) {
            this.modificationNumber = modificationNumber;
        }

        public Long getPriority() {
            return priority;
        }

        public void setPriority(Long priority) {
            this.priority = priority;
        }

        public boolean getTestEvent() {
            return testEvent;
        }

        public void setTestEvent(boolean testEvent) {
            this.testEvent = testEvent;
        }

        public String getMarketContext() {
            return marketContext;
        }

        public void setMarketContext(String marketContext) {
            this.marketContext = marketContext;
        }

        public ResponseRequiredType getResponseRequired() {
            return responseRequired;
        }

        public void setResponseRequired(ResponseRequiredType responseRequired) {
            this.responseRequired = responseRequired;
        }

        public Date getStartDate() {
            return startDate;
        }

        public void setStartDate(Date startDate) {
            this.startDate = startDate;
        }

        public long getToleranceSec() {
            return toleranceSec;
        }

        public void setToleranceSec(long toleranceSec) {
            this.toleranceSec = toleranceSec;
        }

        public long getDurationSec() {
            return durationSec;
        }

        public void setDurationSec(long durationSec) {
            this.durationSec = durationSec;
        }

        public List<Signal> getSignals() {
            return signals;
        }

        public Date getCreatedDateTime() {
            return createdDateTime;
        }

        public void setCreatedDateTime(Date createdDateTime) {
            this.createdDateTime = createdDateTime;
        }

        public String getRequestID() {
            return requestID;
        }

        public void setRequestID(String requestID) {
            this.requestID = requestID;
        }

        public boolean isCreatedEventToVTNSent() {
            return createdEventToVTNSent.get();
        }

        public void setCreatedEventToVTNSent(boolean createdEventToVTNSent) {
            this.createdEventToVTNSent.set(true);
        }

        @Override
        public String toString() {
            return "{\"Event\":{"
                    + "                        \"eventID\":\"" + eventID + "\"\n"
                    + ",                         \"modificationNumber\":\"" + modificationNumber + "\"\n"
                    + ",                         \"priority\":\"" + priority + "\"\n"
                    + ",                         \"testEvent\":\"" + testEvent + "\"\n"
                    + ",                         \"marketContext\":\"" + marketContext + "\"\n"
                    + ",                         \"responseRequired\":\"" + responseRequired + "\"\n"
                    + ",                         \"startDate\":" + startDate + "\n"
                    + ",                         \"toleranceSec\":\"" + toleranceSec + "\"\n"
                    + ",                         \"durationSec\":\"" + durationSec + "\"\n"
                    + ",                         \"rampUpSec\":\"" + rampUpSec + "\"\n"
                    + ",                         \"recoverySec\":\"" + recoverySec + "\"\n"
                    + ",                         \"signals\":" +"\n" +  signals
                    + "}}";
        }
    }

    /**
     * An object containing all the information for a single signal in an event
     */
    public class Signal{
        // An enumerated value describing the type of signal such as level or price
        private SignalTypeEnumeratedType signalType;
        // One or more time intervals during the DR event is active
        private List<Interval> intervals;
        // The payloadFloat value of the event interval currently executing.
        private float currentValue;

        public Signal(){
            intervals = new ArrayList<>();
        }

        public SignalTypeEnumeratedType getSignalType() {
            return signalType;
        }

        public void setSignalType(SignalTypeEnumeratedType signalType) {
            this.signalType = signalType;
        }

        public float getCurrentValue() {
            return currentValue;
        }

        public void setCurrentValue(float currentValue) {
            this.currentValue = currentValue;
        }

        public List<Interval> getIntervals() {
            return intervals;
        }

        @Override
        public String toString() {
            return "{\"Signal\":{" +"\n"
                    + "                        \"signalType\":\"" + signalType + "\"\n"
                    + ",                         \"intervals\":"+"\n" + intervals +"\n"
                    + ",                         \"currentValue\":\"" + currentValue + "\"\n"
                    + "}}";
        }
    }
}
