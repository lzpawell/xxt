package bid.xiaocha.xxt.presenter;

import bid.xiaocha.xxt.model.AddressEntity;

/**
 * Created by 55039 on 2017/10/17.
 */

public interface ICreateOfferServePresenter {
    void createOfferServe(int type , String title, String content, double price, AddressEntity address);
    enum CreateOfferServeResult{
        SUCCESS,
        FAILED_UNKNOWN_ERROR,
        FAILED_NETWORK_ERROR

    }
}
