package bid.xiaocha.xxt.presenter;

import android.accounts.NetworkErrorException;

import bid.xiaocha.xxt.iview.ICreateNeedServeView;
import bid.xiaocha.xxt.model.AddressEntity;
import bid.xiaocha.xxt.model.NeedServeEntity;
import bid.xiaocha.xxt.model.UserEntity;
import bid.xiaocha.xxt.service.NetService;
import bid.xiaocha.xxt.util.JwtUtil;
import bid.xiaocha.xxt.util.PayUtil;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by 55039 on 2017/10/17.
 */

public class CreateNeedServePresenter implements ICreateNeedServePresenter {
    private ICreateNeedServeView view;

    public CreateNeedServePresenter(ICreateNeedServeView view) {
        this.view = view;
    }

    @Override
    public void createNeedServe(int type, String title, String content, double price, AddressEntity address) {
        view.showLoading();

        UserEntity userEntity = UserEntity.getCurrentUser();
        final NeedServeEntity needServeEntity = new NeedServeEntity();
        needServeEntity.setPublishUserId(userEntity.getUserId());
        needServeEntity.setContent(content);
        needServeEntity.setPhone(address.getPhone());
        needServeEntity.setPrice(price);
        needServeEntity.setPlace(address.getPlace());
        needServeEntity.setType(type);
        needServeEntity.setTitle(title);
        needServeEntity.setLatitude(address.getLatitude());
        needServeEntity.setLongitude(address.getLongitude());
        needServeEntity.setUserName(address.getUserName());

        PayUtil.requestPay(price,new PayUtil.OnPayResultListener() {
            @Override
            public void onPayResultSuccess() {
                Call<CreateNeedServeResult> resultCall = NetService.getInstance().createNeedServe(JwtUtil.getJwt(),needServeEntity);
                resultCall.enqueue(new Callback<CreateNeedServeResult>() {
                    @Override
                    public void onResponse(Call<CreateNeedServeResult> call, Response<CreateNeedServeResult> response) {
                        if (response.code() == 200) {
                            view.dismissLoading();
                            CreateNeedServeResult createNeedServeResult = response.body();
                            view.showCreateNeedServeResult(createNeedServeResult);
                        }else{
                            onFailure(call,new Throwable(response.code()+""));
                        }
                    }

                    @Override
                    public void onFailure(Call<CreateNeedServeResult> call, Throwable t) {
                        view.dismissLoading();
                        t.printStackTrace();
                        if (t instanceof NetworkErrorException){
                            view.showCreateNeedServeResult(CreateNeedServeResult.FAILED_NETWORK_ERROR);
                        }else{
                            view.showCreateNeedServeResult(CreateNeedServeResult.FAILED_UNKNOWN_ERROR);
                        }
                    }

                });
            }

            @Override
            public void onPayResultFailed(short result) {

            }
        });



    }
}
