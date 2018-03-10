package bid.xiaocha.xxt.presenter;

/**
 * Created by 55039 on 2017/11/4.
 */

public interface ICreateOrUpdateAddressPresenter {
    public void saveAddress(long addressId,String userName,String phone,String plcae,double longtitude,double latitude);
}
