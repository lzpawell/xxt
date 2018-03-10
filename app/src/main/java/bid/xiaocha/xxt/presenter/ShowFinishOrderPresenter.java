package bid.xiaocha.xxt.presenter;

import java.util.List;

import bid.xiaocha.xxt.iview.IShowOrderView;
import bid.xiaocha.xxt.model.ActiveOrderEntity;
import bid.xiaocha.xxt.model.FinishedOrderEntity;
import bid.xiaocha.xxt.model.UserEntity;
import bid.xiaocha.xxt.util.JwtUtil;

/**
 * Created by lenovo-pc on 2017/12/3.
 */

public class ShowFinishOrderPresenter implements IShowOrderPresenter {
    private IShowOrderView view;
    public ShowFinishOrderPresenter(IShowOrderView view){
        this.view = view;
    }
    @Override
    public void getOrderByPage(int pageNum) {
        ActiveOrderEntity.getFinishOrderByPage(JwtUtil.getJwt(), UserEntity.getCurrentUser().getUserId(), pageNum, new ActiveOrderEntity.OnGetOrderByPageResult<FinishedOrderEntity>() {
            @Override
            public void getOrderByPage(Boolean isHaveMove, List<FinishedOrderEntity> orderServeList) {
                view.showOrderListSuccess(orderServeList,isHaveMove);
            }

            @Override
            public void getOrderByPageFail() {
                view.showOrderListFail();
            }
        });
    }
}
