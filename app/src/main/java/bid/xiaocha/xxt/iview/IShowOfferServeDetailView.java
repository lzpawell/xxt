package bid.xiaocha.xxt.iview;

import java.util.List;

import bid.xiaocha.xxt.model.OfferServeEntity;
import bid.xiaocha.xxt.model.ServeCommentEntity;
import bid.xiaocha.xxt.model.UserEntity;

/**
 * Created by 55039 on 2017/11/5.
 */

public interface IShowOfferServeDetailView {
    void showLoading();
    void dismissLoading();
    void showRequestCreateOrderResult(short result);
    void showNotEnoughMoney(double money);
    void showServeInfo(OfferServeEntity offerServeEntity);
    void showServeCommentListSuccess(List<ServeCommentEntity> serveCommentList,boolean isHaveMore);
    void showServeCommentListFail();
    public final short SUCCESS = 0;
    public final short SERVE_UNUSUAL = 1;//指订单已暂停
    public final short SEND_MESSAGE_ERROR = 2;
    public final short NET_ERROR = 3;
    public final short SERVE_ERROR = 4;//单纯的服务器错误。
}
