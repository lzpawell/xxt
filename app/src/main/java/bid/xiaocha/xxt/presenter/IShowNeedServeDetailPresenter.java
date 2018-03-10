package bid.xiaocha.xxt.presenter;

import bid.xiaocha.xxt.model.NeedServeEntity;
import bid.xiaocha.xxt.model.UserEntity;

/**
 * Created by 55039 on 2017/11/5.
 */

public interface IShowNeedServeDetailPresenter {
    void getUserEntity(String userId);
    void requestCreateOrder(NeedServeEntity serve);
}
