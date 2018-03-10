package bid.xiaocha.xxt.iview;

import java.util.List;

import bid.xiaocha.xxt.model.ActiveOrderEntity;

/**
 * Created by lenovo-pc on 2017/12/2.
 */

public interface IShowOrderView<T> {
    void showOrderListSuccess(List<T> orderList, boolean isHaveMore);
    void showOrderListFail();
}
