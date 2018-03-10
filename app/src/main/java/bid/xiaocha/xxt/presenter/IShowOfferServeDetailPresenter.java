package bid.xiaocha.xxt.presenter;

import bid.xiaocha.xxt.model.NeedServeEntity;

/**
 * Created by 55039 on 2017/11/5.
 */

public interface IShowOfferServeDetailPresenter {
    void requestCreateOrder();
    void getServeInfoAndComment(String serveId);
    void getServeCommentByPages(String serveId, String whatSort ,int pageNum);
}
