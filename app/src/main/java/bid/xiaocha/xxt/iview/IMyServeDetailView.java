package bid.xiaocha.xxt.iview;

/**
 * Created by lenovo-pc on 2018/1/13.
 */

public interface IMyServeDetailView {
    void showLoading();
    void dismissLoading();
    void showResult(short result);


    short NET_ERROR = 3;
}
