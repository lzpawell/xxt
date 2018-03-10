package bid.xiaocha.xxt.presenter;

/**
 * Created by 55039 on 2017/12/4.
 */

public interface IControlOrderPresenter {
    //position参数用于更新列表，不需要时可传入0
    void acceptOrder(String orderId,int position);//同意接单
    void finishOrder(String orderId,int position);//完成订单
    void confrimOrder(String orderId,int position);//确认完成订单
    void cancelOrder(String orderId,int position);//取消订单
    void refuseCancelOrder(String orderId,int position);//拒绝取消订单
    void saveComment(String orderId,int score,String comment,int position);
}
