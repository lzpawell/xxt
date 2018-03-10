package bid.xiaocha.xxt.presenter;

import android.util.Log;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import bid.xiaocha.xxt.iview.IControlOrderView;
import bid.xiaocha.xxt.model.ActiveOrderEntity;
import bid.xiaocha.xxt.model.FinishedOrderEntity;
import bid.xiaocha.xxt.model.UserEntity;
import bid.xiaocha.xxt.rongCloud.Message.AcceptOrderMessage;
import bid.xiaocha.xxt.rongCloud.Message.CancelOrderMessage;
import bid.xiaocha.xxt.rongCloud.Message.ConfirmOrderMessage;
import bid.xiaocha.xxt.rongCloud.Message.FinishOrderMessage;
import bid.xiaocha.xxt.rongCloud.Message.RefuseCancelOrderMessage;
import bid.xiaocha.xxt.service.NetService;
import bid.xiaocha.xxt.util.JwtUtil;
import io.rong.imkit.RongIM;
import io.rong.imlib.IRongCallback;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static bid.xiaocha.xxt.iview.IControlOrderView.ERROR_SHOW_STRING;
import static bid.xiaocha.xxt.iview.IControlOrderView.SUCCESS_RETURN_ACTIVE_ORDER;
import static bid.xiaocha.xxt.iview.IControlOrderView.SUCCESS_RETURN_FINISHED_ORDER;

/**
 * Created by 55039 on 2017/12/4.
 */

public class ControlOrderPresenter implements IControlOrderPresenter {
    private IControlOrderView view;
    private static Gson gson = new Gson();

    public ControlOrderPresenter(IControlOrderView view) {
        this.view = view;
    }

    @Override
    public void acceptOrder(String orderId, final int position) {
        view.showLoading();
        final String userId = UserEntity.getCurrentUser().getUserId();
        Call<Object> call = NetService.getInstance().acceptOrder(JwtUtil.getJwt(), orderId, userId);
        call.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                if (response.code() == 200) {
                    Object object = response.body();
                    JSONObject json;
                    ActiveOrderEntity activeOrder = null;
                    int result = -1;
                    try {
                        json = new JSONObject(object.toString());
                        if (json.has("result"))
                            result = json.getInt("result");
                        if (json.has("orderEntity")) {
                            activeOrder = gson.fromJson(json.getString("orderEntity"),ActiveOrderEntity.class);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        view.dismissLoading();
                        view.showResult(ERROR_SHOW_STRING,"解析数据失败请重试",position);
                    }
                    Log.i("acceptOrderResult",result+"");
                    switch (result){
                        case -1:
                            view.dismissLoading();
                            view.showResult(ERROR_SHOW_STRING,"获取数据，失败请重试",position);
                            break;
                        case 0:
                            AcceptOrderMessage content = new AcceptOrderMessage();
                            content.setOrderId(activeOrder.getOrderId());
                            content.setSenderId(userId);
                            content.setSenderNickname(UserEntity.getCurrentUser().getNickName());
                            content.setServeType(activeOrder.getServeType());
                            content.setTitle(activeOrder.getTitle());
                            Message message = new Message();
                            message.setContent(content);
                            message.setTargetId(activeOrder.getServeReceiverId());
                            message.setConversationType(Conversation.ConversationType.PRIVATE);
                            final ActiveOrderEntity finalActiveOrder = activeOrder;
                            RongIM.getInstance().sendMessage(message, "aaa", "bbb", new IRongCallback.ISendMessageCallback() {
                                @Override
                                public void onAttached(Message message) {

                                }

                                @Override
                                public void onSuccess(Message message) {
                                    view.dismissLoading();
                                    view.showResult(SUCCESS_RETURN_ACTIVE_ORDER, finalActiveOrder,position);
                                }

                                @Override
                                public void onError(Message message, RongIMClient.ErrorCode errorCode) {
                                    view.dismissLoading();
                                    view.showResult(ERROR_SHOW_STRING, "接单生成但消息发送异常，请去聊天界面提醒对方完成订单",position);
                                }
                            });
                            break;
                        case 1:
                        case 2:
                        case 4:
                            view.dismissLoading();
                            view.showResult(ERROR_SHOW_STRING,"订单出错，请刷新后重试",position);
                            break;
                        case 3:
                            view.dismissLoading();
                            view.showResult(ERROR_SHOW_STRING,"你没有权利接单",position);
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
                view.showResult(ERROR_SHOW_STRING,"网络错误",position);
            }
        });


    }

    @Override
    public void finishOrder(String orderId, final int position) {
        view.showLoading();
        final String userId = UserEntity.getCurrentUser().getUserId();
        Call<Object> call = NetService.getInstance().finishOrder(JwtUtil.getJwt(), orderId, userId);
        call.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                if (response.code() == 200) {
                    Object object = response.body();
                    JSONObject json;
                    ActiveOrderEntity activeOrder = null;
                    int result = -1;
                    try {
                        json = new JSONObject(object.toString());
                        if (json.has("result"))
                            result = json.getInt("result");
                        if (json.has("orderEntity")) {
                            activeOrder = gson.fromJson(json.getString("orderEntity"),ActiveOrderEntity.class);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        view.dismissLoading();
                        view.showResult(ERROR_SHOW_STRING,"解析数据失败请重试",position);
                    }
                    Log.i("finishOrderResult",result+"");
                    switch (result){
                        case -1:
                            view.dismissLoading();
                            view.showResult(ERROR_SHOW_STRING,"获取数据，失败请重试",position);
                            break;
                        case 0:
                            FinishOrderMessage content = new FinishOrderMessage();
                            content.setOrderId(activeOrder.getOrderId());
                            content.setSenderId(userId);
                            content.setSenderNickname(UserEntity.getCurrentUser().getNickName());
                            content.setServeType(activeOrder.getServeType());
                            content.setTitle(activeOrder.getTitle());
                            Message message = new Message();
                            message.setContent(content);
                            if (userId.equals(activeOrder.getServePublisherId()))
                                message.setTargetId(activeOrder.getServeReceiverId());
                            else
                                message.setTargetId(activeOrder.getServePublisherId());
                            message.setConversationType(Conversation.ConversationType.PRIVATE);
                            final ActiveOrderEntity finalActiveOrder = activeOrder;
                            RongIM.getInstance().sendMessage(message, "aaa", "bbb", new IRongCallback.ISendMessageCallback() {
                                @Override
                                public void onAttached(Message message) {

                                }

                                @Override
                                public void onSuccess(Message message) {
                                    view.dismissLoading();
                                    view.showResult(SUCCESS_RETURN_ACTIVE_ORDER, finalActiveOrder,position);
                                }

                                @Override
                                public void onError(Message message, RongIMClient.ErrorCode errorCode) {
                                    view.dismissLoading();
                                    view.showResult(ERROR_SHOW_STRING, "你已完成订单但消息发送异常，请去聊天界面提醒对方确认完成",position);
                                }
                            });
                            break;
                        case 1:
                        case 2:
                        case 4:
                            view.dismissLoading();
                            view.showResult(ERROR_SHOW_STRING,"订单出错，请刷新后重试",position);
                            break;
                        case 3:
                            view.dismissLoading();
                            view.showResult(ERROR_SHOW_STRING,"你没有权利发出完成请求",position);
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
                view.showResult(ERROR_SHOW_STRING,"网络错误",position);
            }
        });
    }

    @Override
    public void confrimOrder(String orderId, final int position) {
        view.showLoading();
        final String userId = UserEntity.getCurrentUser().getUserId();
        Call<Object> call = NetService.getInstance().confrimOrder(JwtUtil.getJwt(), orderId, userId);
        call.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                if (response.code() == 200) {
                    Object object = response.body();
                    JSONObject json;
                    FinishedOrderEntity finishedOrder = null;
                    int result = -1;
                    try {
                        json = new JSONObject(object.toString());
                        if (json.has("result"))
                            result = json.getInt("result");
                        if (json.has("orderEntity")) {
                            finishedOrder = gson.fromJson(json.getString("orderEntity"),FinishedOrderEntity.class);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        view.dismissLoading();
                        view.showResult(ERROR_SHOW_STRING,"解析数据失败请重试",position);
                    }
                    Log.i("confrimOrderResult",result+"");
                    switch (result){
                        case -1:
                            view.dismissLoading();
                            view.showResult(ERROR_SHOW_STRING,"获取数据，失败请重试",position);
                            break;
                        case 0:
                            ConfirmOrderMessage content = new ConfirmOrderMessage();
                            content.setOrderId(finishedOrder.getOrderId());
                            content.setSenderId(userId);
                            content.setSenderNickname(UserEntity.getCurrentUser().getNickName());
                            content.setServeType(finishedOrder.getServeType());
                            content.setTitle(finishedOrder.getTitle());
                            Message message = new Message();
                            message.setContent(content);
                            if (userId.equals(finishedOrder.getServePublisherId()))
                                message.setTargetId(finishedOrder.getServeReceiverId());
                            else
                                message.setTargetId(finishedOrder.getServePublisherId());
                            message.setConversationType(Conversation.ConversationType.PRIVATE);
                            final FinishedOrderEntity finalFinishedOrder = finishedOrder;
                            RongIM.getInstance().sendMessage(message, "aaa", "bbb", new IRongCallback.ISendMessageCallback() {
                                @Override
                                public void onAttached(Message message) {

                                }

                                @Override
                                public void onSuccess(Message message) {
                                    view.dismissLoading();
                                    view.showResult(SUCCESS_RETURN_FINISHED_ORDER, finalFinishedOrder,position);
                                }

                                @Override
                                public void onError(Message message, RongIMClient.ErrorCode errorCode) {
                                    view.dismissLoading();
                                    view.showResult(ERROR_SHOW_STRING, "你已确认完成订单但消息发送异常",position);
                                }
                            });
                            break;
                        case 1:
                        case 2:
                        case 4:
                        case 5:
                        case 6:
                        case 7:
                            view.dismissLoading();
                            view.showResult(ERROR_SHOW_STRING,"订单出错，请刷新后重试",position);
                            break;
                        case 3:
                            view.dismissLoading();
                            view.showResult(ERROR_SHOW_STRING,"你没有权利发出完成请求",position);
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
                view.showResult(ERROR_SHOW_STRING,"网络错误",position);
            }
        });
    }

    @Override
    public void cancelOrder(String orderId, final int position) {
        view.showLoading();
        final String userId = UserEntity.getCurrentUser().getUserId();
        Call<Object> call = NetService.getInstance().cancelOrder(JwtUtil.getJwt(), orderId, userId);
        call.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                if (response.code() == 200) {
                    final Object object = response.body();
                    JSONObject json;
                    ActiveOrderEntity activeOrder = null;
                    FinishedOrderEntity finishedOrder = null;
                    int result = -1;
                    try {
                        json = new JSONObject(object.toString());
                        if (json.has("result"))
                            result = json.getInt("result");
                        if (json.has("activeOrderEntity")) {
                            activeOrder = gson.fromJson(json.getString("activeOrderEntity"),ActiveOrderEntity.class);
                        }
                        if (json.has("finishedOrderEntity")) {
                            finishedOrder = gson.fromJson(json.getString("finishedOrderEntity"),FinishedOrderEntity.class);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        view.dismissLoading();
                        view.showResult(ERROR_SHOW_STRING,"解析数据失败请重试",position);
                    }
                    Log.i("cancelOrderResult",result+"");
                    switch (result){
                        case -1:
                            view.dismissLoading();
                            view.showResult(ERROR_SHOW_STRING,"获取数据，失败请重试",position);
                            break;
                        case 0:
                            Message message = new Message();
                            CancelOrderMessage content = new CancelOrderMessage();
                            final short flag;
                            final Object order;
                            if (activeOrder!=null) {
                                content.setOrderId(activeOrder.getOrderId());
                                content.setSenderId(userId);
                                content.setSenderNickname(UserEntity.getCurrentUser().getNickName());
                                content.setServeType(activeOrder.getServeType());
                                content.setTitle(activeOrder.getTitle());
                                content.setIsReallyCancel(CancelOrderMessage.WAIT_FOR_CANCEL);
                                if (userId.equals(activeOrder.getServePublisherId()))
                                    message.setTargetId(activeOrder.getServeReceiverId());
                                else
                                    message.setTargetId(activeOrder.getServePublisherId());
                                flag = SUCCESS_RETURN_ACTIVE_ORDER;
                                order = activeOrder;
                            }
                            else {
                                content.setOrderId(finishedOrder.getOrderId());
                                content.setSenderId(userId);
                                content.setSenderNickname(UserEntity.getCurrentUser().getNickName());
                                content.setServeType(finishedOrder.getServeType());
                                content.setTitle(finishedOrder.getTitle());
                                content.setIsReallyCancel(CancelOrderMessage.REALLY_CANCEL);
                                if (userId.equals(finishedOrder.getServePublisherId()))
                                    message.setTargetId(finishedOrder.getServeReceiverId());
                                else
                                    message.setTargetId(finishedOrder.getServePublisherId());
                                flag = SUCCESS_RETURN_FINISHED_ORDER;
                                order = finishedOrder;
                            }
                            message.setContent(content);
                            message.setConversationType(Conversation.ConversationType.PRIVATE);
                            RongIM.getInstance().sendMessage(message, "aaa", "bbb", new IRongCallback.ISendMessageCallback() {
                                @Override
                                public void onAttached(Message message) {

                                }

                                @Override
                                public void onSuccess(Message message) {
                                    view.dismissLoading();
                                    view.showResult(flag, order,position);
                                }

                                @Override
                                public void onError(Message message, RongIMClient.ErrorCode errorCode) {
                                    view.dismissLoading();
                                    if (flag == SUCCESS_RETURN_ACTIVE_ORDER) {
                                        view.showResult(ERROR_SHOW_STRING, "订单处于待取消状态，但消息发送失败，请去聊天界面提醒对方确认取消", position);
                                    }else{
                                        view.showResult(ERROR_SHOW_STRING, "订单已成功过取消，但消息发送失败", position);
                                    }
                                }
                            });
                            break;
                        case 1:
                        case 2:
                        case 4:
                        case 5:
                        case 6:
                        case 7:
                            view.dismissLoading();
                            view.showResult(ERROR_SHOW_STRING,"订单出错，请刷新后重试",position);
                            break;
                        case 8:
                            view.dismissLoading();
                            view.showResult(ERROR_SHOW_STRING,"你没有权限取消该订单",position);
                            break;
                        case 3:
                            view.dismissLoading();
                            view.showResult(ERROR_SHOW_STRING,"你已经取消过该服务",position);
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
                view.showResult(ERROR_SHOW_STRING,"网络错误",position);
            }
        });
    }

    @Override
    public void refuseCancelOrder(String orderId, final int position) {
        view.showLoading();
        final String userId = UserEntity.getCurrentUser().getUserId();
        Call<Object> call = NetService.getInstance().refuseCancelOrder(JwtUtil.getJwt(), orderId, userId);
        call.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                if (response.code() == 200) {
                    Object object = response.body();
                    JSONObject json;
                    ActiveOrderEntity activeOrder = null;
                    int result = -1;
                    try {
                        json = new JSONObject(object.toString());
                        if (json.has("result"))
                            result = json.getInt("result");
                        if (json.has("orderEntity")) {
                            activeOrder = gson.fromJson(json.getString("orderEntity"),ActiveOrderEntity.class);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        view.dismissLoading();
                        view.showResult(ERROR_SHOW_STRING,"解析数据失败请重试",position);
                    }
                    Log.i("refuseCancelOrderResult",result+"");
                    switch (result){
                        case -1:
                            view.dismissLoading();
                            view.showResult(ERROR_SHOW_STRING,"获取数据，失败请重试",position);
                            break;
                        case 0:
                            RefuseCancelOrderMessage content = new RefuseCancelOrderMessage();
                            content.setOrderId(activeOrder.getOrderId());
                            content.setSenderId(userId);
                            content.setSenderNickname(UserEntity.getCurrentUser().getNickName());
                            content.setServeType(activeOrder.getServeType());
                            content.setTitle(activeOrder.getTitle());
                            Message message = new Message();
                            message.setContent(content);
                            if (userId.equals(activeOrder.getServePublisherId()))
                                message.setTargetId(activeOrder.getServeReceiverId());
                            else
                                message.setTargetId(activeOrder.getServePublisherId());
                            message.setConversationType(Conversation.ConversationType.PRIVATE);
                            final ActiveOrderEntity finalActiveOrder = activeOrder;
                            RongIM.getInstance().sendMessage(message, "aaa", "bbb", new IRongCallback.ISendMessageCallback() {
                                @Override
                                public void onAttached(Message message) {

                                }

                                @Override
                                public void onSuccess(Message message) {
                                    view.dismissLoading();
                                    view.showResult(SUCCESS_RETURN_ACTIVE_ORDER, finalActiveOrder,position);
                                }

                                @Override
                                public void onError(Message message, RongIMClient.ErrorCode errorCode) {
                                    view.dismissLoading();
                                    view.showResult(ERROR_SHOW_STRING, "订单已恢复但消息提醒发送异常",position);
                                }
                            });
                            break;
                        case 1:
                        case 2:
                        case 4:
                            view.dismissLoading();
                            view.showResult(ERROR_SHOW_STRING,"订单出错，请刷新后重试",position);
                            break;
                        case 3:
                            view.dismissLoading();
                            view.showResult(ERROR_SHOW_STRING,"你没有权利发出完成请求",position);
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
                view.showResult(ERROR_SHOW_STRING,"网络错误",position);
            }
        });
    }

    @Override
    public void saveComment(String orderId, int score, String comment, final int position) {
        view.showLoading();
        final String userId = UserEntity.getCurrentUser().getUserId();
        Call<Object> call = NetService.getInstance().saveComment(JwtUtil.getJwt(),orderId,userId,comment,score);
        call.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                if (response.code() == 200) {
                    Object object = response.body();
                    JSONObject json;
                    FinishedOrderEntity finishedOrderEntity = null;
                    int result = -1;
                    try {
                        json = new JSONObject(object.toString());
                        if (json.has("result"))
                            result = json.getInt("result");
                        if (json.has("orderEntity")) {
                            finishedOrderEntity = gson.fromJson(json.getString("orderEntity"),FinishedOrderEntity.class);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        view.dismissLoading();
                        view.showResult(ERROR_SHOW_STRING,"解析数据失败请重试",position);
                    }
                    switch (result){
                        case -1:
                            view.dismissLoading();
                            view.showResult(ERROR_SHOW_STRING,"获取数据，失败请重试",position);
                            break;
                        case 0:
                            view.dismissLoading();
                            view.showResult(SUCCESS_RETURN_FINISHED_ORDER, finishedOrderEntity,position);
                            break;
                        case 1:
                        case 4:
                            view.dismissLoading();
                            view.showResult(ERROR_SHOW_STRING,"评价失败，请刷新后重试",position);
                            break;
                        case 2:
                            view.dismissLoading();
                            view.showResult(ERROR_SHOW_STRING,"你已评价该订单",position);
                            break;
                        case 3:
                            view.dismissLoading();
                            view.showResult(ERROR_SHOW_STRING,"你没有权限评价该订单",position);
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
                view.showResult(ERROR_SHOW_STRING,"网络错误",position);
            }
        });
    }

}
