package openADR.Utils;

/**
 * Created by georg on 06.06.16.
 * Objects from this class store the payload of an openADR message.
 * This means it does not contain information about below communication layers.
 */
public class OADRMsgObject {
    // It signalize the stored openADR message type.
    private String msgType;
    // contains the requestID from an openADR message
    private String ID;
    // openADR message
    Object msg;

    private int resendIteration;

    public OADRMsgObject(String msgType, String ID, Object msg) {
        this.msgType = msgType;
        this.ID = ID;
        this.msg = msg;
    }

    public String getMsgType() {
        return msgType;
    }

    public String getID() {
        return ID;
    }

    public Object getMsg() {
        return msg;
    }

    public int getResendIteration() {
        return resendIteration;
    }

    public void incResendIteration() {
        this.resendIteration++;
    }
}
