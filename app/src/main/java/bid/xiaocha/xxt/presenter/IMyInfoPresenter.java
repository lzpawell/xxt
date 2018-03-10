package bid.xiaocha.xxt.presenter;

import android.app.Activity;

import bid.xiaocha.xxt.model.UserEntity;

/**
 * Created by 55039 on 2017/11/1.
 */

public interface IMyInfoPresenter {
    void updateMyInfo(UserEntity userEntity);
    void choosePic(Activity activity, int which);
}
