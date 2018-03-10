package bid.xiaocha.xxt.rongCloud;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import bid.xiaocha.xxt.model.UserEntity;
import bid.xiaocha.xxt.rongCloud.Message.AcceptOrderMessage;
import bid.xiaocha.xxt.rongCloud.Message.CancelOrderMessage;
import bid.xiaocha.xxt.rongCloud.Message.ConfirmOrderMessage;
import bid.xiaocha.xxt.rongCloud.Message.FinishOrderMessage;
import bid.xiaocha.xxt.rongCloud.Message.RefuseCancelOrderMessage;
import bid.xiaocha.xxt.rongCloud.Message.RequestCreateOrderMessage;
import bid.xiaocha.xxt.rongCloud.MessageProvider.AcceptOrderMessageItemProvider;
import bid.xiaocha.xxt.rongCloud.MessageProvider.CancelOrderMessageItemProvider;
import bid.xiaocha.xxt.rongCloud.MessageProvider.ConfirmOrderMessageItemProvider;
import bid.xiaocha.xxt.rongCloud.MessageProvider.FinishOrderMessageItemProvider;
import bid.xiaocha.xxt.rongCloud.MessageProvider.RefuseCancelOrderMessageItemProvider;
import bid.xiaocha.xxt.rongCloud.MessageProvider.RequestCreateOrderMessageItemProvider;
import bid.xiaocha.xxt.ui.activity.ShowUserInfoActivity;
import bid.xiaocha.xxt.util.App;
import bid.xiaocha.xxt.util.HeadProtraitUtil;
import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;
import io.rong.imlib.model.UserInfo;

import static bid.xiaocha.xxt.util.CommonUtils.showToast;

/**
 * Created by 55039 on 2017/10/13.
 */

public class RongManager {
    public final static String REQUEST_CREAT_ORDER_MESSAGE="app:RequestCreateOrder";
    public final static String ACCEPT_ORDER_MESSAGE="app:AcceptOrderMessage";
    public final static String FINISH_ORDER_MESSAGE="app:FinishOrderMessage";
    public final static String CONFIRM_ORDER_MESSAGE="app:ConfirmOrderMessage";
    public final static String REFUSE_CANCEL_ORDER_MESSAGE="app:RefuseCancelOrderMessage";
    public final static String CANCEL_ORDER_MESSAGE="app:CancelOrderMessage";
    public static RongManager rongManager;
    public static RongManager getInstance(){
        if (rongManager == null){
            synchronized (RongManager.class){
                if (rongManager == null){
                    rongManager = new RongManager();
                    init();
                }
            }
        }
        return  rongManager;
    }

    private static void init() {
        RongIM.init(App.getAppContext());
        RongIM.setUserInfoProvider(new RongIM.UserInfoProvider() {

            @Override
            public UserInfo getUserInfo(String userId) {
                Log.i("ccc",userId);
                UserEntity.getUserByUserId(userId, new UserEntity.OnGetUserByUserIdResult() {
                    @Override
                    public void getUserByIdResult(boolean result,UserEntity userEntity) {
                        if (result){
                            //http://www.qqw21.com/article/UploadPic/2014-6/20146101122345116.jpg
                            //https://ss1.bdstatic.com/70cFuXSh_Q1YnxGkpoWK1HF6hhy/it/u=94886135,141649056&fm=27&gp=0.jpg
                            String picPath = userEntity.getPicPath();
                            if (userEntity.getPicPath() == null||userEntity.getPicPath() == ""){
                                picPath = "default_head.png";
                            }
                            UserInfo userInfo = new UserInfo(userEntity.getUserId(),userEntity.getNickName(),Uri.parse(HeadProtraitUtil.WEB_HEAD_PATH + picPath));
                            RongIM.getInstance().refreshUserInfoCache(userInfo);
                        }else {
                        }
                    }
                });

                /*
                userInfo = findUserById(userId);
                在回调中重新设置，更新融云信息
                RongIM.getInstance().refreshUserInfoCache(new UserInfo("userId", "啊明", Uri.parse("http://rongcloud-web.qiniudn.com/docs_demo_rongcloud_logo.png")));
                 */
                return null;
            }
        }, true);
        RongIM.setConversationBehaviorListener(new RongIM.ConversationBehaviorListener() {
            @Override
            public boolean onUserPortraitClick(Context context, Conversation.ConversationType conversationType, UserInfo userInfo) {
                Intent intent = new Intent(context, ShowUserInfoActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("userId",userInfo.getUserId());
                intent.putExtras(bundle);
                context.startActivity(intent);
                return true;
            }

            @Override
            public boolean onUserPortraitLongClick(Context context, Conversation.ConversationType conversationType, UserInfo userInfo) {
                return false;
            }

            @Override
            public boolean onMessageClick(Context context, View view, Message message) {
                return false;
            }

            @Override
            public boolean onMessageLinkClick(Context context, String s) {
                return false;
            }

            @Override
            public boolean onMessageLongClick(Context context, View view, Message message) {
                return false;
            }
        });
        /* 以下注册自定义消息*/
        RongIM.registerMessageType(RequestCreateOrderMessage.class);
        RongIM.registerMessageType(AcceptOrderMessage.class);
        RongIM.registerMessageType(CancelOrderMessage.class);
        RongIM.registerMessageType(ConfirmOrderMessage.class);
        RongIM.registerMessageType(FinishOrderMessage.class);
        RongIM.registerMessageType(RefuseCancelOrderMessage.class);
        /* 以下注册消息展示模板*/
        RongIM.getInstance().registerMessageTemplate(new RequestCreateOrderMessageItemProvider());
        RongIM.getInstance().registerMessageTemplate(new AcceptOrderMessageItemProvider());
        RongIM.getInstance().registerMessageTemplate(new CancelOrderMessageItemProvider());
        RongIM.getInstance().registerMessageTemplate(new ConfirmOrderMessageItemProvider());
        RongIM.getInstance().registerMessageTemplate(new FinishOrderMessageItemProvider());
        RongIM.getInstance().registerMessageTemplate(new RefuseCancelOrderMessageItemProvider());

//        RongIM.getInstance().setOnReceiveMessageListener(new RongIMClient.OnReceiveMessageListener() {
//            @Override
//            public boolean onReceived(Message message, int i) {
//                if (message.getObjectName() == REQUEST_CREAT_ORDER_MESSAGE){
//                    RequestCreateOrderMessage requestCreateOrderMessage = (RequestCreateOrderMessage) message.getContent();
//                    ServeStoreForChat.getInstance().insertServe(null,requestCreateOrderMessage.getRequeserId(),requestCreateOrderMessage.getServeId(),requestCreateOrderMessage.getServeType());
//                }
//                return false;
//            }
//        });
    }

    public boolean connect(String token) {
        final Lock lock = new ReentrantLock();
        final AtomicBoolean connectResult = new AtomicBoolean(false);
        Log.i("连接", token);
        RongIM.connect(token, new RongIMClient.ConnectCallback() {
            @Override
            public void onTokenIncorrect() {
                Log.i("连接", "连接失败");
                connectResult.set(false);
            }
            @Override
            public void onSuccess(String userId) {
                Log.i("连接", "连接成功"+userId);
                connectResult.set(true);
            }
            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {
                Log.i("连接", "连接失败"+errorCode.getMessage());
                connectResult.set(false);
            }
        });
        lock.lock();
        return connectResult.get();
    }


    /*
     * 最后一个参数为标题，可以在聊天界面通过 intent.getData().getQueryParameter("title") 获取该值, 再手动设置为标题。
     */
    public void talkWithOtherPeople(Context context,String targetUserId){
        talkWithOtherPeople(context,targetUserId,"");
    }
    public void talkWithOtherPeople(Context context,String targetUserId,String title){
        RongIM.getInstance().startPrivateChat(context,targetUserId,title);
    }


    /**
     * <p>断开与融云服务器的连接，并且不再接收 Push 消息。</p>
     */
    public void logout(){
        RongIM.getInstance().logout();
    }

    /**
     * <p>断开与融云服务器的连接。当调用此接口断开连接后，仍然可以接收 Push 消息。</p>
     */
    public void disconnect(){
        RongIM.getInstance().disconnect();
    }

}
