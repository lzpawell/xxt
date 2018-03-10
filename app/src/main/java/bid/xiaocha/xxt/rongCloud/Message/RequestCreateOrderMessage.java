package bid.xiaocha.xxt.rongCloud.Message;

import android.os.Parcel;
import android.util.Log;

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

@MessageTag(value = RongManager.REQUEST_CREAT_ORDER_MESSAGE,flag = MessageTag.ISCOUNTED | MessageTag.ISPERSISTED )
public class RequestCreateOrderMessage extends MessageContent {
    private String serveTitle;
    private String senderNickname;
    private String senderId;
    private int serveType;
    private String orderId;
    public RequestCreateOrderMessage(){}
    public RequestCreateOrderMessage(byte[] data){
        String jsonStr = null;
        try {
            jsonStr = new String(data,"UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        try {
            JSONObject json = new JSONObject(jsonStr);
            if (json.has("senderId"))
                senderId = json.optString("senderId");
            if (json.has("serveTitle"))
                serveTitle = json.optString("serveTitle");
            if (json.has("senderNickname"))
                senderNickname = json.optString("senderNickname");
            if (json.has("serveType"))
                serveType = json.optInt("serveType");
            if (json.has("orderId"))
                orderId = json.optString("orderId");

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    @Override
    public byte[] encode() {
        JSONObject json = new JSONObject();
        try {
            json.put("requesterId",senderId);
            json.put("serveTitle",serveTitle);
            json.put("senderNickname",senderNickname);
            json.put("serveType",serveType);
            json.put("orderId",orderId);
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
        ParcelUtils.writeToParcel(dest,serveTitle);
        ParcelUtils.writeToParcel(dest,serveType);
        ParcelUtils.writeToParcel(dest,senderId);
        ParcelUtils.writeToParcel(dest,senderNickname);
        ParcelUtils.writeToParcel(dest,orderId);
    }
    public RequestCreateOrderMessage(Parcel in){
        Log.i("ssx",ParcelUtils.readFromParcel(in));
        Log.i("ssx",ParcelUtils.readIntFromParcel(in)+"");
        for (int i =0;i<3;i++) {
            Log.i("ssx",ParcelUtils.readFromParcel(in));
        }

//            serveTitle = ParcelUtils.readFromParcel(in);
//            serveType = ParcelUtils.readIntFromParcel(in);
//            senderId = ParcelUtils.readFromParcel(in);
//            senderNickname = ParcelUtils.readFromParcel(in);
//            orderId = ParcelUtils.readFromParcel(in);
    }

    public static final Creator<RequestCreateOrderMessage> CREATOR = new Creator<RequestCreateOrderMessage>() {

        @Override
        public RequestCreateOrderMessage createFromParcel(Parcel source) {
            return new RequestCreateOrderMessage(source);
        }

        @Override
        public RequestCreateOrderMessage[] newArray(int size) {
            return new RequestCreateOrderMessage[size];
        }
    };


    public String getServeTitle() {
        return serveTitle;
    }

    public void setServeTitle(String serveTitle) {
        this.serveTitle = serveTitle;
    }

    public String getSenderNickname() {
        return senderNickname;
    }

    public void setSenderNickname(String senderNickname) {
        this.senderNickname = senderNickname;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
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
}
