package bid.xiaocha.xxt.ui.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.android.tu.loadingdialog.LoadingDialog;

import bid.xiaocha.xxt.R;
import bid.xiaocha.xxt.databinding.ActivityCreateNeedServeBinding;
import bid.xiaocha.xxt.databinding.ContentCreateNeedServeBinding;
import bid.xiaocha.xxt.iview.ICreateNeedServeView;
import bid.xiaocha.xxt.model.AddressEntity;
import bid.xiaocha.xxt.presenter.CreateNeedServePresenter;
import bid.xiaocha.xxt.presenter.ICreateNeedServePresenter;
import bid.xiaocha.xxt.util.CommonUtils;
import bid.xiaocha.xxt.util.UITool;

import static bid.xiaocha.xxt.presenter.ICreateNeedServePresenter.CreateNeedServeResult.FAILED_NETWORK_ERROR;
import static bid.xiaocha.xxt.presenter.ICreateNeedServePresenter.CreateNeedServeResult.FAILED_UNKNOWN_ERROR;
import static bid.xiaocha.xxt.presenter.ICreateNeedServePresenter.CreateNeedServeResult.NOT_ENOUGH_MONEY;
import static bid.xiaocha.xxt.presenter.ICreateNeedServePresenter.CreateNeedServeResult.SUCCESS;
import static bid.xiaocha.xxt.util.CommonUtils.getTextFromViewCheckNotNull;
import static bid.xiaocha.xxt.util.CommonUtils.showToast;

public class CreateNeedServeActivity extends AppCompatActivity implements View.OnClickListener,ICreateNeedServeView {
    private ContentCreateNeedServeBinding contentBinding;
    private ActivityCreateNeedServeBinding activityBinding;
    private CreateNeedServePresenter createNeedServePresenter;
    private android.os.Handler uiHandler = new android.os.Handler();
    private final static int CHOOSE_REQUEST_CODE = 1;
    private AddressEntity address = null;
    private LoadingDialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityBinding = DataBindingUtil.setContentView(this,R.layout.activity_create_need_serve);
        contentBinding = activityBinding.content;
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        initView();

    }

    private void initView() {
        getSupportActionBar().setTitle("我需要这个服务");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        contentBinding.btnNeedServiceSave.setOnClickListener(this);
        contentBinding.lyChooseAddress.setOnClickListener(this);
        createNeedServePresenter = new CreateNeedServePresenter(this);
        loadingDialog = UITool.createLoadingDialog(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ly_choose_address:
                Intent intent = new Intent(this,ShowOrChooseAddressActivity.class);
                Bundle bundle = new Bundle();
                bundle.putBoolean("isChoosing",true);
                intent.putExtras(bundle);
                startActivityForResult(intent,CHOOSE_REQUEST_CODE);
                break;
            case R.id.btn_need_service_save:
                int type = 0;
                String title = "";
                String content = "";
                double price = 0;
                try {
                    type = 0;
                    title = getTextFromViewCheckNotNull(contentBinding.edtNeedServiceTitle);
                    content = getTextFromViewCheckNotNull(contentBinding.edtNeedServiceContent);
                    price = Double.parseDouble(getTextFromViewCheckNotNull(contentBinding.edtNeedServicePrice));
                } catch (CommonUtils.CanNotNullExcetion canNotNullExcetion) {
                    showToast("亲，你还有信息没完善哦");
                    return;
                }
                if (address == null){
                    showToast("亲，你还没选择地址");
                    return;
                }

                createNeedServePresenter.createNeedServe(type,title,content,price,address);
                break;

        }
    }

    @Override
    public void showLoading() {
        UITool.showLoadingDialog(loadingDialog);
    }

    @Override
    public void showCreateNeedServeResult(final ICreateNeedServePresenter.CreateNeedServeResult createNeedServeResult) {
        uiHandler.post(new Runnable() {
            @Override
            public void run() {
                if (createNeedServeResult == null){
                    showToast("null");
                    return;
                }
                if (createNeedServeResult == NOT_ENOUGH_MONEY){
                    showToast("不够钱");
                    return;
                }
                if (createNeedServeResult == FAILED_NETWORK_ERROR){
                    showToast("网络错误");
                    return;
                }
                if (createNeedServeResult == FAILED_UNKNOWN_ERROR){
                    showToast("未知错误，请重试");
                }
                if (createNeedServeResult == SUCCESS){
                    showToast("创建成功");
                    finish();
                    return;
                }
            }
        });
    }

    @Override
    public void dismissLoading() {
        UITool.dismissLoadingDialog(loadingDialog);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CHOOSE_REQUEST_CODE){
            if (resultCode == ShowOrChooseAddressActivity.CHOOSE_RESULT_CODE){
                long addressId = data.getExtras().getLong("addressId");
                address = AddressEntity.getAddressEntity(addressId);
                contentBinding.tvChooseAddress.setVisibility(View.GONE);
                contentBinding.tvNeedServePhone.setText(address.getPhone());
                contentBinding.tvNeedServePlace.setText(address.getPlace());
                contentBinding.tvNeedServeUserName.setText(address.getUserName());
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
