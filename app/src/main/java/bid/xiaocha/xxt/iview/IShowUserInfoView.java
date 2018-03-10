package bid.xiaocha.xxt.iview;

import bid.xiaocha.xxt.model.UserEntity;

/**
 * Created by lenovo-pc on 2018/1/13.
 */

public interface IShowUserInfoView {
    void showUserInfo(UserEntity userEntity);
    void showLoading();
    void dismissLoading();
}
