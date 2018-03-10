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
import bid.xiaocha.xxt.databinding.ActivityCreateOfferServeBinding;
import bid.xiaocha.xxt.databinding.ContentCreateOfferServeBinding;
import bid.xiaocha.xxt.iview.ICreateOfferServeView;
import bid.xiaocha.xxt.model.AddressEntity;
import bid.xiaocha.xxt.presenter.CreateOfferServePresenter;
import bid.xiaocha.xxt.presenter.ICreateOfferServePresenter;
import bid.xiaocha.xxt.util.CommonUtils;
import bid.xiaocha.xxt.util.UITool;

import static bid.xiaocha.xxt.presenter.ICreateOfferServePresenter.CreateOfferServeResult.SUCCESS;
import static bid.xiaocha.xxt.util.CommonUtils.getTextFromViewCheckNotNull;
import static bid.xiaocha.xxt.util.CommonUtils.showToast;

public class CreateOfferServeActivity extends AppCompatActivity implements View.OnClickListener,ICreateOfferServeView {

    private CreateOfferServePresenter createOfferServePresenter;
    private ActivityCreateOfferServeBinding activityBinding;
    private ContentCreateOfferServeBinding contentBinding;
    private android.os.Handler uiHandler = new android.os.Handler();
    private final static int CHOOSE_REQUEST_CODE = 1;
    private AddressEntity address = null;
    private LoadingDialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityBinding = DataBindingUtil.setContentView(this,R.layout.activity_create_offer_serve);
        contentBinding = activityBinding.content;
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        initView();

    }

    private void initView() {
        getSupportActionBar().setTitle("我能提供这个服务");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        contentBinding.btnOfferServiceSave.setOnClickListener(this);
        contentBinding.lyChooseAddress.setOnClickListener(this);
        createOfferServePresenter = new CreateOfferServePresenter(this);
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
            case R.id.btn_offer_service_save:
                int type;
                String title;
                String content;
                double price;
                try {
                    type = 0;
                    title = getTextFromViewCheckNotNull(contentBinding.edtOfferServiceTitle);
                    content = getTextFromViewCheckNotNull(contentBinding.edtOfferServiceContent);
                    price = Double.parseDouble(getTextFromViewCheckNotNull(contentBinding.edtOfferServicePrice));
                } catch (CommonUtils.CanNotNullExcetion canNotNullExcetion) {
                    showToast("亲，你还有信息没完善哦");
                    return;
                }
                if (address == null){
                    showToast("亲，你还没选择地址");
                    return;
                }

                createOfferServePresenter.createOfferServe(type,title,content,price,address);
                break;

        }
    }

    @Override
    public void showLoading() {
        UITool.showLoadingDialog(loadingDialog);
    }

    @Override
    public void showCreateOfferServeResult(final ICreateOfferServePresenter.CreateOfferServeResult createOfferServeResult) {
        uiHandler.post(new Runnable() {
            @Override
            public void run() {
                if (createOfferServeResult == null){
                    showToast("null");
                    return;
                }
                if (createOfferServeResult == SUCCESS){
                    showToast("创建成功");
                    finish();
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
                contentBinding.tvOfferServePhone.setText(address.getPhone());
                contentBinding.tvOfferServePlace.setText(address.getPlace());
                contentBinding.tvOfferServeUserName.setText(address.getUserName());
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
