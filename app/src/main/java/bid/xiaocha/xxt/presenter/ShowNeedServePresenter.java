package bid.xiaocha.xxt.presenter;

import java.util.List;

import bid.xiaocha.xxt.iview.IShowServeView;
import bid.xiaocha.xxt.model.NeedServeEntity;
import bid.xiaocha.xxt.util.JwtUtil;

/**
 * Created by 55039 on 2017/11/5.
 */

public class ShowNeedServePresenter implements IShowNeedServePresenter {

    private IShowServeView<NeedServeEntity> view;

    public ShowNeedServePresenter(IShowServeView view) {
        this.view = view;
    }

    @Override
    public void getNeedServesByPage(int pageNum, String whatSort) {
        NeedServeEntity.getNeedServesByPage(pageNum, whatSort, new NeedServeEntity.OnGetNeedServesByPageResult() {
            @Override
            public void getNeedServesByPageSuccess(Boolean isHaveMove, List<NeedServeEntity> needServeList) {
                view.showServeListSuccess(needServeList,isHaveMove);
            }

            @Override
            public void getNeedServesByPageFail() {
                view.showServeListFail();
            }
        });

    }


    public void getMyNeedServesByPage(int pageNum, String whatSort,String userId){
        NeedServeEntity.getMyNeedServesByPage(JwtUtil.getJwt(),pageNum, whatSort, userId, new NeedServeEntity.OnGetNeedServesByPageResult() {
            @Override
            public void getNeedServesByPageSuccess(Boolean isHaveMove, List<NeedServeEntity> needServeList) {
                view.showServeListSuccess(needServeList,isHaveMove);
            }

            @Override
            public void getNeedServesByPageFail() {
                view.showServeListFail();
            }
        });
    }
}
