package bid.xiaocha.xxt.presenter;

/**
 * Created by 55039 on 2017/11/5.
 */

public interface IShowNeedServePresenter {
    public void getNeedServesByPage(int pageNum ,String whatSort);
    public void getMyNeedServesByPage(int pageNum, String whatSort,String userId);
}
