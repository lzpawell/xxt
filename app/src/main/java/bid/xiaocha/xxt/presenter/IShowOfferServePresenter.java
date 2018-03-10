package bid.xiaocha.xxt.presenter;

/**
 * Created by 55039 on 2017/11/23.
 */

public interface IShowOfferServePresenter {
    public void getOfferServesByPage(int pageNum ,String whatSort);
    public void getMyOfferServesByPage(int pageNum, String whatSort,String userId);
}
