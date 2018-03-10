package bid.xiaocha.xxt.presenter;


import bid.xiaocha.xxt.iview.ICreateOrUpdateAddressView;
import bid.xiaocha.xxt.model.AddressEntity;
import bid.xiaocha.xxt.model.UserEntity;

/**
 * Created by 55039 on 2017/11/4.
 */

public class CreateOrUpdateAddressPresenter implements ICreateOrUpdateAddressPresenter{
    private ICreateOrUpdateAddressView view;

    public CreateOrUpdateAddressPresenter(ICreateOrUpdateAddressView view) {
        this.view = view;
    }

    @Override
    public void saveAddress(long addressId,String userName, String phone, String plcae, double longtitude, double latitude) {
        view.showLoading();
        AddressEntity addressEntity = new AddressEntity();
        addressEntity.setAddressId(addressId);
        addressEntity.setUserId(UserEntity.getCurrentUser().getUserId());
        addressEntity.setUserName(userName);
        addressEntity.setPhone(phone);
        addressEntity.setPlace(plcae);
        addressEntity.setLongitude(longtitude);
        addressEntity.setLatitude(latitude);
        AddressEntity.createOrUpdateAddressInService(addressEntity, new AddressEntity.OnUpdateAddressResult() {
            @Override
            public void updateAddressResult(boolean result) {
                view.dismissLoading();
                view.showCreateOrUpdateAdressResult(result);
            }
        });
    }
}
