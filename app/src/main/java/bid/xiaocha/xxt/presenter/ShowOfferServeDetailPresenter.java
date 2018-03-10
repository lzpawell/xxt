package bid.xiaocha.xxt.presenter;

import org.json.JSONException;
import org.json.JSONObject;

import bid.xiaocha.xxt.iview.IShowNeedServeDetailView;
import bid.xiaocha.xxt.iview.IShowOfferServeDetailView;
import bid.xiaocha.xxt.model.GetResultByPage;
import bid.xiaocha.xxt.model.NeedServeEntity;
import bid.xiaocha.xxt.model.OfferServeEntity;
import bid.xiaocha.xxt.model.ServeCommentEntity;
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

import static bid.xiaocha.xxt.model.ActiveOrderEntity.OFFER_SERVE;

/**
 * Created by 55039 on 2017/11/5.
 */

public class ShowOfferServeDetailPresenter implements IShowOfferServeDetailPresenter {
    private IShowOfferServeDetailView view;
    private OfferServeEntity serve;


    public ShowOfferServeDetailPresenter(IShowOfferServeDetailView view,OfferServeEntity serve){
        this.view=view;
        this.serve = serve;
    }

    public void getServeInfoAndComment(final String serveId){
        Call<OfferServeEntity> call = NetService.getInstance().getOfferServeByServeId(JwtUtil.getJwt(), serveId);
        call.enqueue(new Callback<OfferServeEntity>() {
            @Override
            public void onResponse(Call<OfferServeEntity> call, Response<OfferServeEntity> response) {
                if (response.code() == 200){
                    view.showServeInfo(response.body());
                    getServeCommentByPages(serveId,"null",0);
                }else{
                    onFailure(call,new Throwable(response.code() + ""));
                }
            }
            @Override
            public void onFailure(Call<OfferServeEntity> call, Throwable t) {
                t.printStackTrace();
                view.showServeInfo(null);
            }
        });
    }
    public void getServeCommentByPages(String serveId,int pageNum){
        getServeCommentByPages(serveId,"null",pageNum);
    }

    @Override
    public void getServeCommentByPages(String serveId, String whatSort, int pageNum) {
        if (serveId == null ||serveId.equals("")){
            view.showServeCommentListFail();
            return;
        }
        Call<GetResultByPage<ServeCommentEntity>> call = NetService.getInstance().getServeCommentByPage(JwtUtil.getJwt(), pageNum, whatSort, serveId);
        call.enqueue(new Callback<GetResultByPage<ServeCommentEntity>>() {
            @Override
            public void onResponse(Call<GetResultByPage<ServeCommentEntity>> call, Response<GetResultByPage<ServeCommentEntity>> response) {
                if (response.code() == 200){
                    view.showServeCommentListSuccess(response.body().getDataList(),response.body().isHaveMore());
                }else{
                    onFailure(call,new Throwable(response.code() + ""));
                }
            }

            @Override
            public void onFailure(Call<GetResultByPage<ServeCommentEntity>> call, Throwable t) {
                t.printStackTrace();
                view.showServeCommentListFail();
            }
        });
    }

    @Override
    public void requestCreateOrder() {
        view.showLoading();
        final UserEntity userEntity = UserEntity.getCurrentUser();
        Call<Object> call = NetService.getInstance().createOfferOrder(JwtUtil.getJwt(), serve.getServeId(), userEntity.getUserId());
        call.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
             if (response.code() == 200){
                 Object object = response.body();
                 JSONObject json;
                 int result = 3;
                 String orderId = "";
                 double money = 0;
                 try {
                     json = new JSONObject(object.toString());
                     result = json.getInt("result");
                     if (json.has("orderId"))
                        orderId = json.getString("orderId");
                     if (json.has("requireMoney"))
                         money = json.getDouble("requireMoney");
                 } catch (JSONException e) {
                     e.printStackTrace();
                     view.dismissLoading();
                     view.showRequestCreateOrderResult(view.SERVE_ERROR);
                 }
                 switch (result){
                     case 0:
                         Message message = new Message();
                         RequestCreateOrderMessage content = new RequestCreateOrderMessage();
                         content.setServeTitle(serve.getTitle());
                         content.setServeType(OFFER_SERVE);
                         content.setSenderNickname(userEntity.getNickName());
                         content.setSenderId(userEntity.getUserId());
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
                         view.dismissLoading();
                         view.showNotEnoughMoney(money);
                         break;
                     case 3:
                         view.dismissLoading();
                         view.showRequestCreateOrderResult(view.SERVE_ERROR);
                         break;
                 }
             }else {
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
