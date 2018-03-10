package bid.xiaocha.xxt.presenter;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import bid.xiaocha.xxt.iview.IShowOrderDetailView;
import bid.xiaocha.xxt.model.ActiveOrderEntity;
import bid.xiaocha.xxt.model.FinishedOrderEntity;
import bid.xiaocha.xxt.model.UserEntity;
import bid.xiaocha.xxt.service.NetService;
import bid.xiaocha.xxt.util.JwtUtil;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static bid.xiaocha.xxt.iview.IControlOrderView.ERROR_SHOW_STRING;
import static bid.xiaocha.xxt.iview.IControlOrderView.SUCCESS_RETURN_ACTIVE_ORDER;
import static bid.xiaocha.xxt.iview.IControlOrderView.SUCCESS_RETURN_FINISHED_ORDER;

/**
 * Created by 55039 on 2017/12/7.
 */

public class ShowOrderDetailPresenter implements IShowOrderDetailPresenter {
    private IShowOrderDetailView view;
    private Gson gson;

    public ShowOrderDetailPresenter(IShowOrderDetailView view) {
        this.view = view;
        gson = new Gson();
    }
    @Override
    public void getOrderDetail(String orderId, short orderType) {
        String userId = UserEntity.getCurrentUser().getUserId();
        Call<Object> call = NetService.getInstance().getOrderByOrderId(JwtUtil.getJwt(), orderId, userId, orderType);
        call.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                if (response.code() == 200) {
                    Object object = response.body();
                    JSONObject json;
                    ActiveOrderEntity activeOrder = null;
                    FinishedOrderEntity finishedOrder = null;
                    int result = -1;
                    try {
                        json = new JSONObject(object.toString());
                        if (json.has("result"))
                            result = json.getInt("result");
                        if (json.has("activeOrder")) {
                            activeOrder = gson.fromJson(json.getString("activeOrder"),ActiveOrderEntity.class);
                        }
                        if (json.has("finishedOrder")) {
                            finishedOrder = gson.fromJson(json.getString("finishedOrder"),FinishedOrderEntity.class);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        view.showOrderDetailResult(ERROR_SHOW_STRING,"解析数据失败请重试");
                    }
                    switch (result){
                        case -1:
                            view.showOrderDetailResult(ERROR_SHOW_STRING,"获取数据，失败请重试");
                            break;
                        case 0:
                            if (activeOrder != null){
                                view.showOrderDetailResult(SUCCESS_RETURN_ACTIVE_ORDER,activeOrder);
                                return;
                            }
                            if (finishedOrder != null){
                                view.showOrderDetailResult(SUCCESS_RETURN_FINISHED_ORDER,finishedOrder);
                                return;
                            }
                            view.showOrderDetailResult(ERROR_SHOW_STRING,"出现错误");
                            break;
                        case 1:
                            view.showOrderDetailResult(ERROR_SHOW_STRING,"你没有权利发出查询请求");
                         break;
                        case 2:
                            view.showOrderDetailResult(ERROR_SHOW_STRING,"订单出错，请刷新后重试");
                            break;
                    }
                }else{
                    onFailure(call,new Throwable(response.code()+""));
                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                t.printStackTrace();
                view.showOrderDetailResult(ERROR_SHOW_STRING,"网络错误");
            }
        });
    }
}
