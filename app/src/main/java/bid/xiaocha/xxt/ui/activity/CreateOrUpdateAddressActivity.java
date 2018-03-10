package bid.xiaocha.xxt.ui.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;

import com.android.tu.loadingdialog.LoadingDialog;

import bid.xiaocha.xxt.R;
import bid.xiaocha.xxt.databinding.ActivityCreateOrUpdateAddressBinding;
import bid.xiaocha.xxt.databinding.ContentCreateOrUpdateAddressBinding;
import bid.xiaocha.xxt.iview.ICreateOrUpdateAddressView;
import bid.xiaocha.xxt.model.AddressEntity;
import bid.xiaocha.xxt.presenter.CreateOrUpdateAddressPresenter;
import bid.xiaocha.xxt.util.CommonUtils;
import bid.xiaocha.xxt.util.UITool;

import static bid.xiaocha.xxt.util.CommonUtils.getTextFromViewCheckNotNull;
import static bid.xiaocha.xxt.util.CommonUtils.showToast;

public class CreateOrUpdateAddressActivity extends AppCompatActivity implements View.OnClickListener,ICreateOrUpdateAddressView{
    private ActivityCreateOrUpdateAddressBinding activityBinding;
    private ContentCreateOrUpdateAddressBinding contentBinding;
    private double longitude = -1;//经度
    private double latitude = -1;//纬度
    private String gaoDePlace = "";
    private CreateOrUpdateAddressPresenter createOrUpdateAddressPresenter;
    private final static int REQUEST_MAP_CODE = 1;
    private long addressId = -1;
    private LoadingDialog loadingDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityBinding = DataBindingUtil.setContentView(this,R.layout.activity_create_or_update_address);
        contentBinding = activityBinding.content;
        setSupportActionBar(activityBinding.toolbar);
        Bundle bundle = getIntent().getExtras();
        if(bundle != null) {
            longitude = bundle.getDouble("longtitude");
            latitude = bundle.getDouble("latitude");
            gaoDePlace = bundle.getString("gaoDePlace");
            contentBinding.tvPlace.setText(gaoDePlace);
        }
        initPresenter();
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Bundle bundle = getIntent().getExtras();
        if(bundle != null) {
            longitude = bundle.getDouble("longtitude");
            latitude = bundle.getDouble("latitude");
            gaoDePlace = bundle.getString("gaoDePlace");
            contentBinding.tvPlace.setText(gaoDePlace);
        }
    }

    private void initPresenter() {
        createOrUpdateAddressPresenter = new CreateOrUpdateAddressPresenter(this);
    }

    private void initView() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null){
            addressId = bundle.getLong("addressId", -1);
            if (addressId != -1){
                getSupportActionBar().setTitle("修改地址信息");
                AddressEntity addressEntity = AddressEntity.getAddressEntity(addressId);
                if (addressEntity != null){

                    contentBinding.edtPhone.setText(addressEntity.getPhone());
                    contentBinding.edtPlace.setText(addressEntity.getPlace());
                    contentBinding.edtUserName.setText(addressEntity.getUserName());
                    longitude = addressEntity.getLongitude();
                    latitude = addressEntity.getLatitude();
                }
            }else {
                getSupportActionBar().setTitle("新建地址");
            }
        }else{
            getSupportActionBar().setTitle("新建地址");
        }
        contentBinding.lyChooseAddress.setOnClickListener(this);
        contentBinding.btnSave.setOnClickListener(this);
        loadingDialog = UITool.createLoadingDialog(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ly_choose_address:
                Intent intent = new Intent(this,MapChooseAddressActivity.class);
                startActivityForResult(intent,REQUEST_MAP_CODE);
                break;
            case R.id.btn_save:
                String phone = "";
                String place = "";
                String userName = "";
                try {
                    phone = getTextFromViewCheckNotNull(contentBinding.edtPhone);
                    place = getTextFromViewCheckNotNull(contentBinding.edtPlace);
                    userName = getTextFromViewCheckNotNull(contentBinding.edtUserName);
                } catch (CommonUtils.CanNotNullExcetion canNotNullExcetion) {
                    showToast("亲，你还有信息没完善哦");
                    return;
                }
                if (longitude == -1 || latitude == -1){
                    showToast("亲，请先选择地址哦");
                    return;
                }
                createOrUpdateAddressPresenter.saveAddress(addressId,userName,phone,gaoDePlace+place,longitude,latitude);
                break;
        }
    }

    @Override
    public void showLoading() {
        UITool.showLoadingDialog(loadingDialog);
    }

    @Override
    public void showCreateOrUpdateAdressResult(boolean result) {
        if (result){
            finish();
        }else{
            showToast("更新或创建失败");
        }
    }

    @Override
    public void dismissLoading() {
        UITool.dismissLoadingDialog(loadingDialog);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_MAP_CODE){
            if (resultCode == MapChooseAddressActivity.RESULT_FINISH_CHOOSE_CODE){

                Bundle bundle = data.getExtras();
                longitude = bundle.getDouble("longtitude");
                latitude = bundle.getDouble("latitude");
                gaoDePlace = bundle.getString("gaoDePlace");
                contentBinding.tvPlace.setText(gaoDePlace);
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
