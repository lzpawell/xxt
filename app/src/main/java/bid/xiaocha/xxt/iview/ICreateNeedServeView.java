package bid.xiaocha.xxt.iview;


import bid.xiaocha.xxt.presenter.CreateNeedServePresenter;
import bid.xiaocha.xxt.presenter.ICreateNeedServePresenter;

/**
 * Created by 55039 on 2017/10/17.
 */

public interface ICreateNeedServeView {
    void showLoading();

    void showCreateNeedServeResult(ICreateNeedServePresenter.CreateNeedServeResult createNeedServeResult);

    void dismissLoading();
}
