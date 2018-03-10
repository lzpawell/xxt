package bid.xiaocha.xxt.presenter;


import android.accounts.NetworkErrorException;

import bid.xiaocha.xxt.iview.ICreateOfferServeView;
import bid.xiaocha.xxt.model.AddressEntity;
import bid.xiaocha.xxt.model.NeedServeEntity;
import bid.xiaocha.xxt.model.OfferServeEntity;
import bid.xiaocha.xxt.model.UserEntity;
import bid.xiaocha.xxt.service.NetService;
import bid.xiaocha.xxt.util.JwtUtil;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by 55039 on 2017/10/17.
 */

public class CreateOfferServePresenter implements ICreateOfferServePresenter  {

    private ICreateOfferServeView view;

    public CreateOfferServePresenter(ICreateOfferServeView view) {
        this.view = view;
    }

    @Override
    public void createOfferServe(int type, String title, String content, double price, AddressEntity address) {
        view.showLoading();

        UserEntity userEntity = UserEntity.getCurrentUser();
        OfferServeEntity offerServeEntity = new OfferServeEntity();
        offerServeEntity.setPublishUserId(userEntity.getUserId());
        offerServeEntity.setContent(content);
        offerServeEntity.setPhone(address.getPhone());
        offerServeEntity.setPrice(price);
        offerServeEntity.setPlace(address.getPlace());
        offerServeEntity.setType(type);
        offerServeEntity.setTitle(title);
        offerServeEntity.setLatitude(address.getLatitude());
        offerServeEntity.setLongitude(address.getLongitude());
        offerServeEntity.setUserName(address.getUserName());


        Call<ICreateOfferServePresenter.CreateOfferServeResult> resultCall = NetService.getInstance().createOfferServe(JwtUtil.getJwt(),offerServeEntity);
        resultCall.enqueue(new Callback<ICreateOfferServePresenter.CreateOfferServeResult>() {
            @Override
            public void onResponse(Call<ICreateOfferServePresenter.CreateOfferServeResult> call, Response<ICreateOfferServePresenter.CreateOfferServeResult> response) {
                if (response.code() == 200) {
                    view.dismissLoading();
                    ICreateOfferServePresenter.CreateOfferServeResult createOfferServeResult = response.body();
                    view.showCreateOfferServeResult(createOfferServeResult);
                }else{
                    onFailure(call,new Throwable(response.code()+""));
                }
            }

            @Override
            public void onFailure(Call<ICreateOfferServePresenter.CreateOfferServeResult> call, Throwable t) {
                view.dismissLoading();
                t.printStackTrace();
                if (t instanceof NetworkErrorException){
                    view.showCreateOfferServeResult(ICreateOfferServePresenter.CreateOfferServeResult.FAILED_NETWORK_ERROR);
                }else{
                    view.showCreateOfferServeResult(ICreateOfferServePresenter.CreateOfferServeResult.FAILED_UNKNOWN_ERROR);
                }
            }
        });

    }
}
