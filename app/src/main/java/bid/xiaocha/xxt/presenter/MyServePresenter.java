package bid.xiaocha.xxt.presenter;

import bid.xiaocha.xxt.iview.IMyServeDetailView;
import bid.xiaocha.xxt.model.AddressEntity;
import bid.xiaocha.xxt.model.UserEntity;
import bid.xiaocha.xxt.service.NetService;
import bid.xiaocha.xxt.util.JwtUtil;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static bid.xiaocha.xxt.iview.IMyServeDetailView.NET_ERROR;

/**
 * Created by lenovo-pc on 2017/12/12.
 */

public class MyServePresenter implements IMyServePresenter {
    private IMyServeDetailView view;

    public MyServePresenter(IMyServeDetailView view){
        this.view = view;
    }


    @Override
    public void updateMyOfferServe(String serveId, long addressId, String content, double price) {
        view.showLoading();
        Call<Short> call = NetService.getInstance().updateOfferServe(JwtUtil.getJwt(), UserEntity.getCurrentUser().getUserId(), serveId, addressId ,content, price);
        call.enqueue(new Callback<Short>() {
            @Override
            public void onResponse(Call<Short> call, Response<Short> response) {
                if(response.code() == 200) {
                    view.showResult(response.body());
                    view.dismissLoading();
                }else {
                    onFailure(call,new Throwable(response.code()+""));
                }
            }

            @Override
            public void onFailure(Call<Short> call, Throwable t) {
                view.dismissLoading();
                view.showResult(NET_ERROR);
            }
        });
    }

    @Override
    public void updateMyNeedServe( String serveId, long addressId, String content, double price) {
        view.showLoading();
        Call<Short> call = NetService.getInstance().updateNeedServe(JwtUtil.getJwt(), UserEntity.getCurrentUser().getUserId(), serveId, addressId ,content, price);
        call.enqueue(new Callback<Short>() {
            @Override
            public void onResponse(Call<Short> call, Response<Short> response) {
                if(response.code() == 200) {
                    view.showResult(response.body());
                    view.dismissLoading();
                }else {
                    onFailure(call,new Throwable(response.code()+""));
                }
            }

            @Override
            public void onFailure(Call<Short> call, Throwable t) {
                view.dismissLoading();
                t.printStackTrace();
                view.showResult(NET_ERROR);
            }
        });
    }
}
