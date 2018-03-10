package bid.xiaocha.xxt.iview;

/**
 * Created by 55039 on 2017/12/7.
 */

public interface IShowOrderDetailView {
    void showOrderDetailResult(short resultCode,Object result);
    short SUCCESS_RETURN_ACTIVE_ORDER = 0;
    short SUCCESS_RETURN_FINISHED_ORDER = 1;
    short ERROR_SHOW_STRING = 2;
}
