package bid.xiaocha.xxt.presenter;

import android.util.Log;

import java.util.ArrayList;

import bid.xiaocha.xxt.iview.IShowNeedServeRequestView;
import bid.xiaocha.xxt.provider.ServeStoreForChat;
import io.rong.imkit.RongIM;
import io.rong.imlib.IRongCallback;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;

/**
 * Created by 55039 on 2017/11/28.
 */

public class ShowNeedServeRequestPresenter implements IShowNeedServeRequestPresenter {
    private IShowNeedServeRequestView view;
    private String serveId;
    private String requesterId;
    public final static int SUCCESS = 0;
    public final static int HAVE_NO_SERVE = 1;//本地数据库里面已经没有该人的信息了。
    public final static int SEND_MESSAGE_FAILURE = 2;

    public ShowNeedServeRequestPresenter(IShowNeedServeRequestView view, String serveId, String requesterId) {
        this.view = view;
        this.serveId = serveId;
        this.requesterId = requesterId;
    }

    @Override
    public void agreeRequest() {
        ArrayList<String> requesterIds = ServeStoreForChat.getInstance().getRequesterIds(serveId, ServeStoreForChat.ServeForChatColumns.NEED_SERVE);
        if (!requesterIds.contains(requesterIds)){
            view.showCreateOrderResult(HAVE_NO_SERVE);
            return;
        }

    }

    @Override
    public void refuseRequest() {
        if (ServeStoreForChat.getInstance().getServeCount(requesterId,serveId,ServeStoreForChat.ServeForChatColumns.NEED_SERVE) == 0){
            view.showSendRefuseRequestResult(HAVE_NO_SERVE);
        }else{
            view.showLoading();
            final Message message = new Message();
            message.setTargetId(requesterId);
//            message.setContent(new RequestCreateOrderRefuseMessage());
            message.setConversationType(Conversation.ConversationType.PRIVATE);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    RongIM.getInstance().sendMessage(message, "aaa", "bbb", new IRongCallback.ISendMessageCallback() {
                        @Override
                        public void onAttached(Message message) {
                            view.dismissLoading();
                        }

                        @Override
                        public void onSuccess(Message message) {
                            ServeStoreForChat.getInstance().deleteServes(requesterId,serveId,ServeStoreForChat.ServeForChatColumns.NEED_SERVE);
                            view.dismissLoading();
                            view.showSendRefuseRequestResult(SUCCESS);
                        }

                        @Override
                        public void onError(Message message, RongIMClient.ErrorCode errorCode) {
                            Log.i("sendRefuseFailure",errorCode + "");
                            view.dismissLoading();
                            view.showSendRefuseRequestResult(SEND_MESSAGE_FAILURE);
                        }
                    });
                }
            }).start();
        }
    }
}
