package bid.xiaocha.xxt.rongCloud.Message;

import android.os.Parcel;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

import bid.xiaocha.xxt.rongCloud.RongManager;
import io.rong.common.ParcelUtils;
import io.rong.imlib.MessageTag;
import io.rong.imlib.model.MessageContent;

/**
 * Created by 55039 on 2017/11/12.
 */

@MessageTag(value = RongManager.CONFIRM_ORDER_MESSAGE,flag = MessageTag.ISCOUNTED | MessageTag.ISPERSISTED )
public class ConfirmOrderMessage extends MessageContent {
    private String title;
    private String senderNickname;
    private int serveType;
    private String orderId;
    private String senderId;
    public ConfirmOrderMessage(){}
    public ConfirmOrderMessage(byte[] data){
        String jsonStr = null;
        try {
            jsonStr = new String(data,"UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        try {
            JSONObject json = new JSONObject(jsonStr);
            if (json.has("title"))
                title = json.optString("title");
            if (json.has("senderNickname"))
                senderNickname = json.optString("senderNickname");
            if (json.has("serveType"))
                serveType = json.optInt("serveType");
            if (json.has("orderId"))
                orderId = json.optString("orderId");
            if (json.has("senderId"))
                senderId = json.optString("senderId");

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    @Override
    public byte[] encode() {
        JSONObject json = new JSONObject();
        try {
            json.put("title",title);
            json.put("senderNickname",senderNickname);
            json.put("serveType",serveType);
            json.put("orderId",orderId);
            json.put("senderId",senderId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            return json.toString().getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        ParcelUtils.writeToParcel(dest,title);
        ParcelUtils.writeToParcel(dest,serveType);
        ParcelUtils.writeToParcel(dest,senderNickname);
        ParcelUtils.writeToParcel(dest,orderId);
        ParcelUtils.writeToParcel(dest,senderId);
    }
    public ConfirmOrderMessage(Parcel in){
        title = ParcelUtils.readFromParcel(in);
        serveType = ParcelUtils.readIntFromParcel(in);
        senderNickname = ParcelUtils.readFromParcel(in);
        orderId = ParcelUtils.readFromParcel(in);
        senderId = ParcelUtils.readFromParcel(in);
    }
    public static final Creator<ConfirmOrderMessage> CREATOR = new Creator<ConfirmOrderMessage>() {

        @Override
        public ConfirmOrderMessage createFromParcel(Parcel source) {
            return new ConfirmOrderMessage(source);
        }

        @Override
        public ConfirmOrderMessage[] newArray(int size) {
            return new ConfirmOrderMessage[size];
        }
    };

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSenderNickname() {
        return senderNickname;
    }

    public void setSenderNickname(String senderNickname) {
        this.senderNickname = senderNickname;
    }

    public int getServeType() {
        return serveType;
    }

    public void setServeType(int serveType) {
        this.serveType = serveType;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }
    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

}
