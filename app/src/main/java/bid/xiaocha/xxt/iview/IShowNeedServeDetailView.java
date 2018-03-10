package bid.xiaocha.xxt.iview;

import bid.xiaocha.xxt.model.UserEntity;

/**
 * Created by 55039 on 2017/11/5.
 */

public interface IShowNeedServeDetailView {
    void showLoading();
    void showMarkResult(boolean isSuccess,UserEntity userEntity);
    void dismissLoading();
    void showRequestCreateOrderResult(short result);
    public final short SUCCESS = 0;
    public final short SEND_MESSAGE_ERROR = 2;
    public final short SERVE_UNUSUAL = 1;//指订单已被接取或暂停
    public final short NET_ERROR = 3;
    public final short SERVE_ERROR = 4;//单纯的服务器错误。
}
