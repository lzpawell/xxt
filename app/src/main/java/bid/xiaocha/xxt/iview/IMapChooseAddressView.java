package bid.xiaocha.xxt.iview;

/**
 * Created by 55039 on 2017/11/4.
 */

public interface IMapChooseAddressView {
    void showLoading();
    void showGetPlaceResult(boolean isSuccess,String place);
    void dismissLoading();
}
