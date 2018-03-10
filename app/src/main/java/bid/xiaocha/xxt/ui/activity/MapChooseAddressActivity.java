package bid.xiaocha.xxt.ui.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.help.Inputtips;
import com.amap.api.services.help.InputtipsQuery;
import com.amap.api.services.help.Tip;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import bid.xiaocha.xxt.R;
import bid.xiaocha.xxt.adater.AddressTipsAdater;
import bid.xiaocha.xxt.databinding.ActivityMapChooseAddressBinding;
import bid.xiaocha.xxt.databinding.ContentMapChooseAddressBinding;
import bid.xiaocha.xxt.databinding.PopupwindowAddressSearchBinding;
import bid.xiaocha.xxt.iview.IMapChooseAddressView;
import bid.xiaocha.xxt.presenter.MapChooseAddressPresenter;
import bid.xiaocha.xxt.util.CommonUtils;
import bid.xiaocha.xxt.util.MapUtil;

import static bid.xiaocha.xxt.util.CommonUtils.showToast;

public class MapChooseAddressActivity extends AppCompatActivity implements View.OnClickListener,IMapChooseAddressView, TextWatcher, Inputtips.InputtipsListener {
    public final static int RESULT_FINISH_CHOOSE_CODE = 1;
    private ActivityMapChooseAddressBinding activityBinding;
    private ContentMapChooseAddressBinding contentBinding;
    private MapChooseAddressPresenter mapChooseAddressPresenter;
    private PopupWindow addressSearchPopupWindow;
    private PopupwindowAddressSearchBinding popupwindowBinding;
    private MapView mapView = null;
    private AMap aMap;
    private Marker marker;
    private LatLng position;

    private String city;
    public MapChooseAddressActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityBinding = DataBindingUtil.setContentView(this,R.layout.activity_map_choose_address);
        contentBinding = activityBinding.content;
        popupwindowBinding = DataBindingUtil.inflate(this.getLayoutInflater(),R.layout.popupwindow_address_search,null,false);
        addressSearchPopupWindow = new PopupWindow(popupwindowBinding.getRoot(), ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT,true);
        setSupportActionBar(activityBinding.toolbar);
        getSupportActionBar().hide();
        initPresenter();
        initView(savedInstanceState);
        initMap();



    }

    private void initPresenter() {
        mapChooseAddressPresenter = new MapChooseAddressPresenter(this,this);
    }


    private void initView(Bundle savedInstanceState) {

        MapUtil.locateAndGetCity(new MapUtil.OnLocateAndGetCityResult() {
            @Override
            public void locateAndGetCityResult(String city) {
                if (city != null){
                    contentBinding.tvCity.setText(city+"");
                    popupwindowBinding.tvAddressCity.setText(city+"");
                    Log.e("aaaa",city+"");
                }
            }
        });
        activityBinding.fab.setOnClickListener(this);
        mapView = contentBinding.map;
        mapView.onCreate(savedInstanceState);

        contentBinding.edtSearch.setOnClickListener(this);
        popupwindowBinding.edtSearch.addTextChangedListener(this);
    }
    private void initMap() {
        if (aMap == null){
            aMap = mapView.getMap();
        }
        MyLocationStyle myLocationStyle = new MyLocationStyle();
        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATE);
        aMap.setMyLocationStyle(myLocationStyle);
        aMap.getUiSettings().setMyLocationButtonEnabled(true);//设置默认定位按钮是否显示，非必需设置。
        aMap.setMyLocationEnabled(true);
        aMap.moveCamera(CameraUpdateFactory.zoomTo(18));
        LatLng latlng = aMap.getCameraPosition().target;
        marker = aMap.addMarker(new MarkerOptions().position(latlng).draggable(false));
        aMap.setOnCameraChangeListener(new AMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition cameraPosition) {
                marker.setPosition(cameraPosition.target);
            }

            @Override
            public void onCameraChangeFinish(CameraPosition cameraPosition) {

            }
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.fab:

                position = marker.getPosition();
                mapChooseAddressPresenter.getPlace(position);
                break;
            case R.id.edt_search:
                CommonUtils.ShowPopupWindow(this,v,addressSearchPopupWindow);
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }
    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView.onResume ()，重新绘制加载地图
        mapView.onResume();
    }
    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView.onPause ()，暂停地图的绘制
        mapView.onPause();
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //在activity执行onSaveInstanceState时执行mMapView.onSaveInstanceState (outState)，保存地图当前的状态
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void showGetPlaceResult(boolean isSuccess, String place) {
        if (isSuccess){
            Intent intent = new Intent();
            Bundle bundle = new Bundle();
            bundle.putDouble("longtitude",position.longitude);
            bundle.putDouble("latitude",position.latitude);
            bundle.putString("gaoDePlace",place);
            intent.putExtras(bundle);
            setResult(RESULT_FINISH_CHOOSE_CODE,intent);
            finish();
        }else{
            showToast("获取地址信息失败");
        }
    }

    @Override
    public void dismissLoading() {

    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        String content = s.toString().trim();//获取自动提示输入框的内容
        InputtipsQuery inputtipsQuery = new InputtipsQuery(content, city);//初始化一个输入提示搜索对象，并传入参数
        inputtipsQuery.setCityLimit(true);//将获取到的结果进行城市限制筛选
        Inputtips inputtips = new Inputtips(this, inputtipsQuery);//定义一个输入提示对象，传入当前上下文和搜索对象
        inputtips.setInputtipsListener(this);//设置输入提示查询的监听，实现输入提示的监听方法onGetInputtips()
        inputtips.requestInputtipsAsyn();//输入查询提示的异步接口实现
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    @Override
    public void onGetInputtips(List<Tip> list, int returnCode) {
        if (returnCode == AMapException.CODE_AMAP_SUCCESS) {//如果输入提示搜索成功
            List<HashMap<String, String>> searchList = new ArrayList<HashMap<String, String>>();
            for (int i = 0; i < list.size(); i++) {
                HashMap<String, String> hashMap = new HashMap<String, String>();
                hashMap.put("name", list.get(i).getName());
                hashMap.put("Longitude", list.get(i).getPoint().getLongitude() + "");
                hashMap.put("Latitude", list.get(i).getPoint().getLatitude() + "");
                hashMap.put("address", list.get(i).getDistrict());//将地址信息取出放入HashMap中
                searchList.add(hashMap);//将HashMap放入表中
            }
            AddressTipsAdater addressTipsAdater = new AddressTipsAdater(this,searchList, this.getLayoutInflater(),addressSearchPopupWindow);
            popupwindowBinding.lvAddressTips.setAdapter(addressTipsAdater);
            addressTipsAdater.notifyDataSetChanged();
        } else {
            showToast(returnCode + "");
        }
    }

}
