package bid.xiaocha.xxt.util;

import android.content.Context;
import android.util.Log;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.model.LatLng;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeAddress;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;

/**
 * Created by 55039 on 2017/11/4.
 */

public class MapUtil {
    //定位并获取城市信息
    public static void locateAndGetCity(final OnLocateAndGetCityResult onLocateAndGetCityResult){
        AMapLocationClient mLocationClient = new AMapLocationClient(App.getAppContext());//声明AMapLocationClient类对象
        AMapLocationListener mLocationListener = new AMapLocationListener() {//声明定位回调监听器
            @Override
            public void onLocationChanged(AMapLocation aMapLocation) {
                if (aMapLocation != null) {
                    if (aMapLocation.getErrorCode() == 0) {
//可在其中解析amapLocation获取相应内容。
                        onLocateAndGetCityResult.locateAndGetCityResult(aMapLocation.getCity());
                    }else {
                        //定位失败时，可通过ErrCode（错误码）信息来确定失败的原因，errInfo是错误信息，详见错误码表。
                        onLocateAndGetCityResult.locateAndGetCityResult(null);
                        Log.e("AmapError","location Error, ErrCode:"
                                + aMapLocation.getErrorCode() + ", errInfo:"
                                + aMapLocation.getErrorInfo());
                    }
                }
            }
        };
        mLocationClient.setLocationListener(mLocationListener);
        AMapLocationClientOption mLocationOption = new AMapLocationClientOption();//声明AMapLocationClientOption对象
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        mLocationOption.setOnceLocation(true);
        mLocationOption.setInterval(100000);
        mLocationClient.setLocationOption(mLocationOption);
        mLocationClient.startLocation();
    }
    public interface OnLocateAndGetCityResult{
        void locateAndGetCityResult(String city);
    }

    public static void getPlaceByLatlng(Context context, LatLng latLng, final OnGetPlaceByLatlngResult onGetPlaceByLatlngResult){
        final GeocodeSearch geocodeSearch = new GeocodeSearch(context);
        geocodeSearch.setOnGeocodeSearchListener(new GeocodeSearch.OnGeocodeSearchListener() {
            @Override
            public void onRegeocodeSearched(RegeocodeResult regeocodeResult, int i) {
                if (i == 1000){
                    onGetPlaceByLatlngResult.getPlaceByLatlngResult(true,regeocodeResult.getRegeocodeAddress().getFormatAddress());
                }else{
                    Log.i("getPlaceResult",i+"");
                    onGetPlaceByLatlngResult.getPlaceByLatlngResult(false,"");
                }
            }

            @Override
            public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {

            }
        });

        final RegeocodeQuery query = new RegeocodeQuery(new LatLonPoint(latLng.latitude,latLng.longitude), 200,GeocodeSearch.AMAP);
        new Thread(new Runnable() {
            @Override
            public void run() {
                geocodeSearch.getFromLocationAsyn(query);
            }
        }).run();
    }

    public interface OnGetPlaceByLatlngResult{
        void getPlaceByLatlngResult(boolean isSuccess,String place);
    }

}
