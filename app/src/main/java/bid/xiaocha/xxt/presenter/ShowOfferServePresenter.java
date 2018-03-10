package bid.xiaocha.xxt.presenter;

import java.util.List;

import bid.xiaocha.xxt.iview.IShowServeView;
import bid.xiaocha.xxt.model.OfferServeEntity;
import bid.xiaocha.xxt.util.JwtUtil;

/**
 * Created by 55039 on 2017/11/23.
 */

public class ShowOfferServePresenter implements IShowOfferServePresenter {

    private IShowServeView<OfferServeEntity> view;

    public ShowOfferServePresenter(IShowServeView view) {
        this.view = view;
    }
    @Override
    public void getOfferServesByPage(int pageNum, String whatSort) {
        OfferServeEntity.getOfferServesByPage(pageNum, whatSort, new OfferServeEntity.OnGetOfferServesByPageResult() {
            @Override
            public void getOfferServesByPageSuccess(Boolean isHaveMove, List<OfferServeEntity> offerServeList) {
                view.showServeListSuccess(offerServeList,isHaveMove);
            }

            @Override
            public void getOfferServesByPageFail() {
                view.showServeListFail();
            }


        });
    }
    public void getMyOfferServesByPage(int pageNum, String whatSort,String userId){
        OfferServeEntity.getMyOfferServesByPage(JwtUtil.getJwt(), pageNum, whatSort, userId, new OfferServeEntity.OnGetOfferServesByPageResult() {
            @Override
            public void getOfferServesByPageSuccess(Boolean isHaveMove, List<OfferServeEntity> OfferServeList) {
                view.showServeListSuccess(OfferServeList,isHaveMove);
            }

            @Override
            public void getOfferServesByPageFail() {
                view.showServeListFail();
            }
        });
    }
}
