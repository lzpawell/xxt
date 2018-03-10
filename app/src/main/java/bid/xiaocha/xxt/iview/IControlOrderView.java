package bid.xiaocha.xxt.iview;

/**
 * Created by 55039 on 2017/12/4.
 */

public interface IControlOrderView {
    void showLoading();
    void dismissLoading();
    void showResult(short resultCode,Object result, int position);

    short SUCCESS_RETURN_ACTIVE_ORDER = 0;
    short SUCCESS_RETURN_FINISHED_ORDER = 1;
    short ERROR_SHOW_STRING = 2;


}