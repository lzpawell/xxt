package bid.xiaocha.xxt.presenter;

import java.util.List;

import bid.xiaocha.xxt.iview.IShowOrderView;
import bid.xiaocha.xxt.model.ActiveOrderEntity;
import bid.xiaocha.xxt.model.UserEntity;
import bid.xiaocha.xxt.util.JwtUtil;

/**
 * Created by lenovo-pc on 2017/12/2.
 */

public class ShowActiveOrderPresenter implements IShowOrderPresenter {

    private IShowOrderView view;
    public ShowActiveOrderPresenter(IShowOrderView view){
        this.view = view;
    }
    @Override
    public void getOrderByPage(int pageNum) {
        ActiveOrderEntity.getActiveOrderByPage(JwtUtil.getJwt(), UserEntity.getCurrentUser().getUserId(), pageNum, new ActiveOrderEntity.OnGetOrderByPageResult<ActiveOrderEntity>() {
            @Override
            public void getOrderByPage(Boolean isHaveMove, List<ActiveOrderEntity> orderServeList) {
                     view.showOrderListSuccess(orderServeList,isHaveMove);
            }

            @Override
            public void getOrderByPageFail() {
                     view.showOrderListFail();
            }
        });
    }

}
