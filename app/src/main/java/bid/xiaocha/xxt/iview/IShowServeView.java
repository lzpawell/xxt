package bid.xiaocha.xxt.iview;

import java.util.List;

import bid.xiaocha.xxt.model.NeedServeEntity;

/**
 * Created by 55039 on 2017/11/5.
 */

public interface IShowServeView<T> {
    void showServeListSuccess(List<T> ServeList ,boolean isHaveMore);
    void showServeListFail();
}
