package bid.xiaocha.xxt.presenter;

/**
 * Created by 55039 on 2017/12/7.
 */

public interface IShowOrderDetailPresenter {
    void getOrderDetail(String orderId,short orderType);
    short ACTIVE_ORDER = 1;
    short FINISHED_ORDER = 2;
}
