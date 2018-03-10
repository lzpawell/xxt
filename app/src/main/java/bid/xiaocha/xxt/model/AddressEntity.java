package bid.xiaocha.xxt.model;

import android.location.Address;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import bid.xiaocha.xxt.databinding.ListItemAddressListBinding;
import bid.xiaocha.xxt.service.NetService;
import bid.xiaocha.xxt.util.JwtUtil;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddressEntity {

    private long addressId;
    private String userId;
    private String userName;//“收货人”姓名
    private String phone;
    private String place;
    private double longitude;//经度
    private double latitude;//纬度
    private static List<AddressEntity> addressEntityList = null;

    public long getAddressId() {
        return addressId;
    }

    public void setAddressId(long addressId) {
        this.addressId = addressId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPhone() {
        return phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }
    public String getPlace() {
        return place;
    }
    public void setPlace(String place) {
        this.place = place;
    }
    public double getLongitude() {
        return longitude;
    }
    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
    public double getLatitude() {
        return latitude;
    }
    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }


    public static AddressEntity getAddressEntity(long AddressId){
        AddressEntity addressEntity = null;
        if (addressEntityList == null){
            getAllAddress(null);
        }else{
            for (AddressEntity address : addressEntityList) {
                if (address.getAddressId() == AddressId) {
                    addressEntity = address;
                    break;
                }
            }
        }
        return addressEntity;
    }
    public static List<AddressEntity> getAllAddress(final OnGetAllAddressResult onGetAllAddressResult){
        if (addressEntityList == null){
            getAllAddressFromServices(UserEntity.getCurrentUser().getUserId(), new OnGetAddressByUserIdResult() {
                @Override
                public void getAddressByIdResult(boolean result, List<AddressEntity> list) {
                    if (onGetAllAddressResult != null) {
                        if (result == false) {
                            Log.i("getAddress", "failed");
                            onGetAllAddressResult.getAllAddressResult(null);
                        } else {
                            onGetAllAddressResult.getAllAddressResult(list);
                        }
                    }
                }
            });
        }
        return addressEntityList;
    }


    public static void getAllAddressFromServices(String userId,final OnGetAddressByUserIdResult result){
        final Call<List<AddressEntity>> call = NetService.getInstance().getAddressList(JwtUtil.getJwt(),userId);

        call.enqueue(new Callback<List<AddressEntity>>() {
            @Override
            public void onResponse(Call<List<AddressEntity>> call, Response<List<AddressEntity>> response) {
                if (response.code() == 200) {
                    if (response.body() == null) {
                        addressEntityList = new ArrayList<AddressEntity>();
                    } else {
                        addressEntityList = response.body();
                    }
                    result.getAddressByIdResult(true, addressEntityList);
                }else{
                    onFailure(call,new Throwable(response.code()+""));
                }
            }

            @Override
            public void onFailure(Call<List<AddressEntity>>  call, Throwable t) {
                result.getAddressByIdResult(false,null);
                t.printStackTrace();
            }
        });
    }
    public static void createOrUpdateAddressInService(final AddressEntity addressEntity, final OnUpdateAddressResult result){
        Call<AddressEntity> call = NetService.getInstance().createOrUpdateAddress(JwtUtil.getJwt(),addressEntity);
        call.enqueue(new Callback<AddressEntity>() {
            @Override
            public void onResponse(Call<AddressEntity> call, Response<AddressEntity> response) {
                if(response.code() == 200) {
                    int i = 0;
                    AddressEntity addressEntity = response.body();
                    if (addressEntity == null) {
                        result.updateAddressResult(false);
                        return;
                    }
                    if (addressEntityList != null) {
                        for (AddressEntity address : addressEntityList) {
                            if (address.getAddressId() == addressEntity.getAddressId()) {
                                addressEntityList.remove(address);
                                break;
                            }
                            i++;
                        }
                        addressEntityList.add(i, addressEntity);
                    } else {
                        addressEntityList = new ArrayList<AddressEntity>();
                        addressEntityList.add(addressEntity);
                    }

                    result.updateAddressResult(true);
                } else{
                    onFailure(call, new Throwable(response.code()+""));
                }
            }

            @Override
            public void onFailure(Call<AddressEntity> call, Throwable t) {
                result.updateAddressResult(false);
            }
        });
    }

    public static void deleteAddressInService(final Long addressId, final OnDeleteAddressResult result){
        Call<Boolean> call = NetService.getInstance().deleteAddress(JwtUtil.getJwt(),addressId);
        call.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if(response.code() == 200) {
                    for (AddressEntity address : addressEntityList) {
                        if (address.getAddressId() == addressId) {
                            addressEntityList.remove(address);
                            break;
                        }
                    }
                    result.deleteAddressResult(response.body());
                }else{
                    onFailure(call, new Throwable(response.code()+""));
                }
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                result.deleteAddressResult(false);
            }
        });
    }
    public interface OnGetAllAddressResult{
        void getAllAddressResult(List<AddressEntity> list);
    }
    public interface OnGetAddressByUserIdResult{
        void getAddressByIdResult(boolean result,List<AddressEntity> list);
    }
    public interface OnUpdateAddressResult{
        void updateAddressResult(boolean result);
    }
    public interface OnDeleteAddressResult{
        void deleteAddressResult(boolean result);
    }
    public interface OnCreateAddressResult{
        void createAddressResult(boolean result);
    }

}
