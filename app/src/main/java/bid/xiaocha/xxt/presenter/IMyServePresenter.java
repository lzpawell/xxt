package bid.xiaocha.xxt.presenter;

/**
 * Created by lenovo-pc on 2018/1/13.
 */

public interface IMyServePresenter {
    void updateMyOfferServe(String serveId , long addressId, String content , double price);
    void updateMyNeedServe(String serveId ,long addressId ,String content ,double price);
}
