package bid.xiaocha.xxt.iview;

import android.graphics.Bitmap;

import bid.xiaocha.xxt.presenter.IMyInfoPresenter;


/**
 * Created by 55039 on 2017/11/1.
 */

public interface IMyInfoView {
    void showLoading();

    void showUpdateResult(boolean result);

    void dismissLoading();
    void showHeadPic(Bitmap bitmap);
}
