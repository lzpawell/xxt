package bid.xiaocha.xxt.iview;


/**
 * Created by 55039 on 2017/11/28.
 */

public interface IShowNeedServeRequestView {
    void showLoading();
    void dismissLoading();
    void showSendRefuseRequestResult(int result);
    void showCreateOrderResult(int result);
}
