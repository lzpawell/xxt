package bid.xiaocha.xxt.presenter;

/**
 * Created by lenovo-pc on 2017/12/9.
 */

public interface IControlServePresenter {
    void startOfferServe(String orderId,int position);//上架订单
    void stopOfferServe(String orderId,int position);//停止订单
    void deleteOfferServe(String orderId,int position);//删除订单
    void startNeedServe(String orderId,int position);//上架订单
    void stopNeedServe(String orderId,int position);//停止订单
    void deleteNeedServe(String orderId,int position);//删除订单
}
