package bid.xiaocha.xxt.iview;

import bid.xiaocha.xxt.presenter.ICreateOfferServePresenter;

/**
 * Created by 55039 on 2017/10/17.
 */

public interface ICreateOfferServeView {
    void showLoading();
    void showCreateOfferServeResult(ICreateOfferServePresenter.CreateOfferServeResult createOfferServeResult);
    void dismissLoading();
}
