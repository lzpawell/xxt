package bid.xiaocha.xxt.ui.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import com.android.tu.loadingdialog.LoadingDialog;

import bid.xiaocha.xxt.BR;
import bid.xiaocha.xxt.R;
import bid.xiaocha.xxt.databinding.ActivityMyOfferServeDetailBinding;
import bid.xiaocha.xxt.databinding.ContentMyOfferServeDetailBinding;
import bid.xiaocha.xxt.databinding.PopupwindowConfrimBinding;
import bid.xiaocha.xxt.iview.IMyServeDetailView;
import bid.xiaocha.xxt.model.AddressEntity;
import bid.xiaocha.xxt.model.OfferServeEntity;
import bid.xiaocha.xxt.presenter.IMyServePresenter;
import bid.xiaocha.xxt.presenter.MyServePresenter;
import bid.xiaocha.xxt.util.CommonUtils;

import static bid.xiaocha.xxt.model.NeedServeEntity.START_SERVE;
import static bid.xiaocha.xxt.ui.activity.ShowOrChooseAddressActivity.CHOOSE_RESULT_CODE;
import static bid.xiaocha.xxt.util.CommonUtils.showToast;
import static bid.xiaocha.xxt.util.UITool.createLoadingDialog;
import static bid.xiaocha.xxt.util.UITool.dismissLoadingDialog;
import static bid.xiaocha.xxt.util.UITool.showLoadingDialog;

public class MyOfferServeDetailActivity extends AppCompatActivity implements View.OnClickListener,IMyServeDetailView {
    private ActivityMyOfferServeDetailBinding binding;
    private LoadingDialog loadingDialog;
    private ContentMyOfferServeDetailBinding contentBinding;
    private IMyServePresenter presenter;
    private OfferServeEntity offerServeEntity;
    private PopupWindow confrimPopupWindow;
    private PopupwindowConfrimBinding popupwindowConfrimBinding;
    private AddressEntity addressEntity;
    private final static int CHOOSE_REQUEST_CODE = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_my_offer_serve_detail);
        contentBinding = binding.content;
        popupwindowConfrimBinding = DataBindingUtil.inflate(this.getLayoutInflater(),R.layout.popupwindow_confrim,null,false);
        confrimPopupWindow = new PopupWindow(popupwindowConfrimBinding.getRoot(), ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT,true);
        popupwindowConfrimBinding.btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confrimPopupWindow.dismiss();
            }
        });
        setSupportActionBar(binding.toolbar);
        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            String json = bundle.getString("OfferServe","");
            if(!json.equals("")){
                offerServeEntity = OfferServeEntity.getOfferServeEntityFromJson(json);

            }
        }

        initPresenter();
        initView();
    }

    private void initPresenter() {
        presenter = new MyServePresenter(this);
    }

    private void initView() {
        loadingDialog = createLoadingDialog(this);
        contentBinding.setVariable(BR.offerServeDetail,offerServeEntity);
        contentBinding.lyAddress.setClickable(false);
        contentBinding.tvOfferServeUserName.setTextColor(ContextCompat.getColor(this,R.color.Gray));
        contentBinding.tvOfferServePhone.setTextColor(ContextCompat.getColor(this,R.color.Gray));
        contentBinding.tvOfferServePlace.setTextColor(ContextCompat.getColor(this,R.color.Gray));
        contentBinding.edtOfferServeDetailContent.setTextColor(ContextCompat.getColor(this,R.color.Gray));
        contentBinding.edtOfferServeDetailPrice.setTextColor(ContextCompat.getColor(this,R.color.Gray));
        contentBinding.edtOfferServeDetailPrice.setEnabled(false);
        contentBinding.edtOfferServeDetailContent.setEnabled(false);
        contentBinding.btnChangeConfirm.setVisibility(View.GONE);
        contentBinding.btnChange.setVisibility(View.VISIBLE);
        int state = offerServeEntity.getState();
        if(state == START_SERVE){
            contentBinding.tvOfferServeDetailState.setText("已上架");
            contentBinding.btnChange.setEnabled(false);
        }else{
            contentBinding.tvOfferServeDetailState.setText("已下架");
        }
        contentBinding.lyAddress.setOnClickListener(this);
        contentBinding.btnChange.setOnClickListener(this);
        contentBinding.btnChangeConfirm.setOnClickListener(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == CHOOSE_REQUEST_CODE){
            if(resultCode == CHOOSE_RESULT_CODE){
                long addressId = data.getExtras().getLong("addressId");
                addressEntity = AddressEntity.getAddressEntity(addressId);
                contentBinding.tvOfferServePlace.setText(addressEntity.getPlace());
                contentBinding.tvOfferServePhone.setText(addressEntity.getPhone());
                contentBinding.tvOfferServeUserName.setText(addressEntity.getUserName());
            }
        }
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ly_address:
                Intent intent = new Intent(this,ShowOrChooseAddressActivity.class);
                Bundle bundle = new Bundle();
                bundle.putBoolean("isChoosing",true);
                intent.putExtras(bundle);
                startActivityForResult(intent,CHOOSE_REQUEST_CODE);
                break;
            case R.id.btn_change:
                contentBinding.tvOfferServeUserName.setTextColor(ContextCompat.getColor(this,R.color.textColor));
                contentBinding.tvOfferServePhone.setTextColor(ContextCompat.getColor(this,R.color.textColor));
                contentBinding.tvOfferServePlace.setTextColor(ContextCompat.getColor(this,R.color.textColor));
                contentBinding.edtOfferServeDetailContent.setTextColor(ContextCompat.getColor(this,R.color.textColor));
                contentBinding.edtOfferServeDetailPrice.setTextColor(ContextCompat.getColor(this,R.color.textColor));
                contentBinding.edtOfferServeDetailPrice.setEnabled(true);
                contentBinding.edtOfferServeDetailContent.setEnabled(true);
                contentBinding.btnChangeConfirm.setVisibility(View.VISIBLE);
                contentBinding.btnChange.setVisibility(View.GONE);
                contentBinding.lyAddress.setClickable(true);
                break;
            case R.id.btn_change_confirm:
                popupwindowConfrimBinding.btnComfirm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        confrimPopupWindow.dismiss();
                        if(addressEntity == null)
                            presenter.updateMyOfferServe(offerServeEntity.getServeId(),-1,contentBinding.edtOfferServeDetailContent.getText()+"",Double.parseDouble(contentBinding.edtOfferServeDetailPrice.getText()+""));
                        else
                            presenter.updateMyOfferServe(offerServeEntity.getServeId(),addressEntity.getAddressId(),contentBinding.edtOfferServeDetailContent.getText()+"",Double.parseDouble(contentBinding.edtOfferServeDetailPrice.getText()+""));
                    }
                });
                CommonUtils.ShowPopupWindow(this,v,confrimPopupWindow);
                break;
        }
    }

    @Override
    public void showLoading() {
        showLoadingDialog(loadingDialog);
    }

    @Override
    public void dismissLoading() {
        dismissLoadingDialog(loadingDialog);
    }

    @Override
    public void showResult(final short result) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(result==0) {
                    showToast("修改成功");
                    contentBinding.lyAddress.setClickable(false);
                    contentBinding.tvOfferServeUserName.setTextColor(ContextCompat.getColor(MyOfferServeDetailActivity.this,R.color.Gray));
                    contentBinding.tvOfferServePhone.setTextColor(ContextCompat.getColor(MyOfferServeDetailActivity.this,R.color.Gray));
                    contentBinding.tvOfferServePlace.setTextColor(ContextCompat.getColor(MyOfferServeDetailActivity.this,R.color.Gray));
                    contentBinding.edtOfferServeDetailContent.setTextColor(ContextCompat.getColor(MyOfferServeDetailActivity.this,R.color.Gray));
                    contentBinding.edtOfferServeDetailPrice.setTextColor(ContextCompat.getColor(MyOfferServeDetailActivity.this,R.color.Gray));
                    contentBinding.edtOfferServeDetailPrice.setEnabled(false);
                    contentBinding.edtOfferServeDetailContent.setEnabled(false);
                    contentBinding.btnChangeConfirm.setVisibility(View.GONE);
                    contentBinding.btnChange.setVisibility(View.VISIBLE);
                }else{
                    showToast("修改失败，请重试");
                }
            }
        });
    }
}
