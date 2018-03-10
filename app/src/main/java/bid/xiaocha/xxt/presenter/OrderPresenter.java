package bid.xiaocha.xxt.presenter;

import java.util.ArrayList;
import java.util.List;

import bid.xiaocha.xxt.iview.IOrderView;
import bid.xiaocha.xxt.model.ActiveOrderEntity;

/**
 * Created by 55039 on 2017/10/15.
 */

public class OrderPresenter implements IOrderPresenter {
    private IOrderView orderView;
    public int numInAPage = 10;

    public OrderPresenter(IOrderView orderView) {
        this.orderView = orderView;
    }
    public OrderPresenter(IOrderView orderView,int numInAPage) {
        this.orderView = orderView;
        this.numInAPage = numInAPage;
    }

    @Override
    public void getOrdersByPages(final int pageNum, final boolean isRefresh) {
        List<ActiveOrderEntity> datalist = new ArrayList<ActiveOrderEntity>();
        boolean isHaveMore = true;
        //TODO:获取第pageNum页的数据
        if (pageNum <= 10){
            for (int i = (pageNum-1)*10 ; i < pageNum*10 ; i++){
                ActiveOrderEntity activeOrderEntity = new ActiveOrderEntity();
//                activeOrderEntity.setAaa(i);
                datalist.add(activeOrderEntity);
            }
        }else{
            isHaveMore = false;
        }

        orderView.showOrders(datalist,isRefresh,isHaveMore);
    }
}
