package bid.xiaocha.xxt.presenter;

import java.util.List;

import bid.xiaocha.xxt.iview.INeedServeManagerView;
import bid.xiaocha.xxt.model.NeedServeEntity;
import bid.xiaocha.xxt.model.UserEntity;
import bid.xiaocha.xxt.util.JwtUtil;

/**
 * Created by 55039 on 2017/11/5.
 */

public class NeedServeManagerPresenter implements INeedServeManagerPresenter {

    private INeedServeManagerView view;

    public NeedServeManagerPresenter(INeedServeManagerView view) {
        this.view = view;
    }

    @Override
    public void getNeedServesByPage(int pageNum, String whatSort) {
                NeedServeEntity.getMyNeedServesByPage(JwtUtil.getJwt(),pageNum, whatSort, UserEntity.getCurrentUser().getUserId(), new NeedServeEntity.OnGetNeedServesByPageResult() {
                    @Override
                    public void getNeedServesByPageSuccess(Boolean isHaveMove, List<NeedServeEntity> needServeList) {
                        view.showNeedServeListSuccess(needServeList,isHaveMove);
                    }

            @Override
            public void getNeedServesByPageFail() {
                view.showNeedServeListFail();
            }
        });
    }
}