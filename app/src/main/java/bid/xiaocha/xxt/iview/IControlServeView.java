package bid.xiaocha.xxt.iview;

/**
 * Created by lenovo-pc on 2017/12/9.
 */

public interface IControlServeView {
    void showLoading();
    void dismissLoading();
    void showResult(short resultCode,Object result, int position);

    short CHANGE_SUCCESS = 1;
    short ERROR_SHOW_STRING = 2;
    short DELETE_SUCCESS = 3;
}
