package bid.xiaocha.xxt.iview;

import java.util.List;

import bid.xiaocha.xxt.model.ActiveOrderEntity;

/**
 * Created by 55039 on 2017/10/15.
 */

public interface IOrderView {
    void showOrders(List<ActiveOrderEntity> dataList, boolean isRefresh, boolean isHaveMore);
}
