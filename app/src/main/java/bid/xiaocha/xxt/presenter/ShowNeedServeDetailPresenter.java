package bid.xiaocha.xxt.presenter;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import bid.xiaocha.xxt.iview.IShowNeedServeDetailView;
import bid.xiaocha.xxt.model.NeedServeEntity;
import bid.xiaocha.xxt.model.UserEntity;
import bid.xiaocha.xxt.provider.ServeStoreForChat;
import bid.xiaocha.xxt.rongCloud.Message.RequestCreateOrderMessage;
import bid.xiaocha.xxt.service.NetService;
import bid.xiaocha.xxt.util.JwtUtil;
import io.rong.imkit.RongIM;
import io.rong.imlib.IRongCallback;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static bid.xiaocha.xxt.model.ActiveOrderEntity.NEED_SERVE;

/**
 * Created by 55039 on 2017/11/5.
 */

public class ShowNeedServeDetailPresenter implements IShowNeedServeDetailPresenter {
    private IShowNeedServeDetailView view;


    public ShowNeedServeDetailPresenter(IShowNeedServeDetailView view){
        this.view=view;
    }
    @Override
    public void getUserEntity(String userId) {
        view.showLoading();
        UserEntity.getUserByUserId(userId, new UserEntity.OnGetUserByUserIdResult() {
            @Override
            public void getUserByIdResult(boolean result, UserEntity userEntity) {
                if(result){
                    view.showMarkResult(true,userEntity);
                    view.dismissLoading();
                }else {
                    view.showMarkResult(false,null);
                    view.dismissLoading();
                }
            }
        });
    }

    @Override
    public void requestCreateOrder(final NeedServeEntity serve) {
        view.showLoading();
        final String serveId = serve.getServeId();
        final UserEntity currentUser = UserEntity.getCurrentUser();
        Call<Object> call = NetService.getInstance().createNeedOrder(JwtUtil.getJwt(), serveId, currentUser.getUserId());
        call.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                if (response.code() == 200){
                    Object v = response.body();
                    int result = 3;
                    String orderId = "";
                    try {
                        JSONObject json = new JSONObject(v.toString());
                        result = json.getInt("result");
                        if (json.has("orderId"))
                            orderId = json.getString("orderId");
                    } catch (JSONException e) {
                        e.printStackTrace();
                        view.showRequestCreateOrderResult(view.SERVE_ERROR);
                    }
                    switch (result){
                        case 0:
                            final Message message = new Message();
                            RequestCreateOrderMessage content = new RequestCreateOrderMessage();
                            content.setServeTitle(serve.getTitle());
                            content.setServeType(NEED_SERVE);
                            content.setSenderId(currentUser.getUserId());
                            content.setSenderNickname(currentUser.getNickName());
                            content.setOrderId(orderId);
                            message.setTargetId(serve.getPublishUserId());
                            message.setContent(content);
                            message.setConversationType(Conversation.ConversationType.PRIVATE);
                            RongIM.getInstance().sendMessage(message, "aaa", "bbb", new IRongCallback.ISendMessageCallback() {
                                @Override
                                public void onAttached(Message message) {

                                }

                                @Override
                                public void onSuccess(Message message) {
                                    view.dismissLoading();
                                    view.showRequestCreateOrderResult(view.SUCCESS);
                                }

                                @Override
                                public void onError(Message message, RongIMClient.ErrorCode errorCode) {
                                    view.dismissLoading();
                                    view.showRequestCreateOrderResult(view.SEND_MESSAGE_ERROR);
                                }
                            });
                            break;
                        case 1:
                            view.dismissLoading();
                            view.showRequestCreateOrderResult(view.SERVE_UNUSUAL);
                            break;
                        case 2:
                        case 3:
                            view.dismissLoading();
                            view.showRequestCreateOrderResult(view.SERVE_ERROR);
                            break;
                    }
                }else{
                    onFailure(call,new Throwable(response.code()+""));
                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                t.printStackTrace();
                view.dismissLoading();
                view.showRequestCreateOrderResult(view.NET_ERROR);
            }
        });



    }
}
