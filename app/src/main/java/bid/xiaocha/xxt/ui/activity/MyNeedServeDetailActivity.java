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
import bid.xiaocha.xxt.databinding.ActivityMyNeedServeDetailBinding;
import bid.xiaocha.xxt.databinding.ContentMyNeedServeDetailBinding;
import bid.xiaocha.xxt.databinding.PopupwindowConfrimBinding;
import bid.xiaocha.xxt.iview.IMyServeDetailView;
import bid.xiaocha.xxt.model.AddressEntity;
import bid.xiaocha.xxt.model.NeedServeEntity;
import bid.xiaocha.xxt.presenter.IMyServePresenter;
import bid.xiaocha.xxt.presenter.MyServePresenter;
import bid.xiaocha.xxt.util.CommonUtils;

import static bid.xiaocha.xxt.model.NeedServeEntity.START_SERVE;
import static bid.xiaocha.xxt.ui.activity.ShowOrChooseAddressActivity.CHOOSE_RESULT_CODE;
import static bid.xiaocha.xxt.util.CommonUtils.showToast;
import static bid.xiaocha.xxt.util.UITool.createLoadingDialog;
import static bid.xiaocha.xxt.util.UITool.dismissLoadingDialog;
import static bid.xiaocha.xxt.util.UITool.showLoadingDialog;

public class MyNeedServeDetailActivity extends AppCompatActivity implements View.OnClickListener,IMyServeDetailView{

    private ActivityMyNeedServeDetailBinding binding;
    private ContentMyNeedServeDetailBinding contentBinding;
    private PopupWindow confrimPopupWindow;
    private PopupwindowConfrimBinding popupwindowConfrimBinding;
    private LoadingDialog loadingDialog;
    private IMyServePresenter presenter;
    private NeedServeEntity needServeEntity;
    private AddressEntity addressEntity;
    private final static int CHOOSE_REQUEST_CODE = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_my_need_serve_detail);
        contentBinding = binding.content;
        popupwindowConfrimBinding = DataBindingUtil.inflate(this.getLayoutInflater(),R.layout.popupwindow_confrim,null,false);
        confrimPopupWindow = new PopupWindow(popupwindowConfrimBinding.getRoot(), ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT,true);
        popupwindowConfrimBinding.btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confrimPopupWindow.dismiss();
            }
        });
        popupwindowConfrimBinding.btnComfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confrimPopupWindow.dismiss();
                if(addressEntity == null)
                presenter.updateMyNeedServe(needServeEntity.getServeId(),-1,contentBinding.edtNeedServeDetailContent.getText()+"",Double.parseDouble(contentBinding.edtNeedServeDetailPrice.getText()+""));
                else
                    presenter.updateMyNeedServe(needServeEntity.getServeId(),addressEntity.getAddressId(),contentBinding.edtNeedServeDetailContent.getText()+"",Double.parseDouble(contentBinding.edtNeedServeDetailPrice.getText()+""));
            }
        });
        setSupportActionBar(binding.toolbar);
        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            String json = bundle.getString("NeedServe","");
            if(!json.equals("")){
                needServeEntity = NeedServeEntity.getNeedServeEntityFromJson(json);

            }
        }

        initPresenter();
        initView();

    }

    private void initView() {
        popupwindowConfrimBinding.tvTitle.setText("提示");
        popupwindowConfrimBinding.tvContent.setText("确定修改吗？");

        loadingDialog = createLoadingDialog(this);
        contentBinding.setVariable(BR.needServeDetail,needServeEntity);
        contentBinding.lyAddress.setClickable(false);
        contentBinding.tvNeedServeUserName.setTextColor(ContextCompat.getColor(this,R.color.Gray));
        contentBinding.tvNeedServePhone.setTextColor(ContextCompat.getColor(this,R.color.Gray));
        contentBinding.tvNeedServePlace.setTextColor(ContextCompat.getColor(this,R.color.Gray));
        contentBinding.edtNeedServeDetailContent.setTextColor(ContextCompat.getColor(this,R.color.Gray));
        contentBinding.edtNeedServeDetailPrice.setTextColor(ContextCompat.getColor(this,R.color.Gray));
        contentBinding.edtNeedServeDetailPrice.setEnabled(false);
        contentBinding.edtNeedServeDetailContent.setEnabled(false);
        contentBinding.btnChangeConfirm.setVisibility(View.GONE);
        contentBinding.btnChange.setVisibility(View.VISIBLE);
        int state = needServeEntity.getState();
        if(state == START_SERVE){
            contentBinding.tvNeedServeDetailState.setText("已上架");
            contentBinding.btnChange.setEnabled(false);
        }else{
            contentBinding.tvNeedServeDetailState.setText("已下架");
        }
        contentBinding.lyAddress.setOnClickListener(this);
        contentBinding.btnChange.setOnClickListener(this);
        contentBinding.btnChangeConfirm.setOnClickListener(this);

    }

    private void initPresenter() {
        presenter = new MyServePresenter(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == CHOOSE_REQUEST_CODE){
            if(resultCode == CHOOSE_RESULT_CODE){
                long addressId = data.getExtras().getLong("addressId");
                addressEntity = AddressEntity.getAddressEntity(addressId);
                contentBinding.tvNeedServePlace.setText(addressEntity.getPlace());
                contentBinding.tvNeedServePhone.setText(addressEntity.getPhone());
                contentBinding.tvNeedServeUserName.setText(addressEntity.getUserName());
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
                contentBinding.tvNeedServeUserName.setTextColor(ContextCompat.getColor(this,R.color.textColor));
                contentBinding.tvNeedServePhone.setTextColor(ContextCompat.getColor(this,R.color.textColor));
                contentBinding.tvNeedServePlace.setTextColor(ContextCompat.getColor(this,R.color.textColor));
                contentBinding.edtNeedServeDetailContent.setTextColor(ContextCompat.getColor(this,R.color.textColor));
                contentBinding.edtNeedServeDetailPrice.setTextColor(ContextCompat.getColor(this,R.color.textColor));
                contentBinding.edtNeedServeDetailPrice.setEnabled(true);
                contentBinding.edtNeedServeDetailContent.setEnabled(true);
                contentBinding.btnChangeConfirm.setVisibility(View.VISIBLE);
                contentBinding.btnChange.setVisibility(View.GONE);
                contentBinding.lyAddress.setClickable(true);
                break;
            case R.id.btn_change_confirm:
                if(Double.parseDouble(contentBinding.edtNeedServeDetailPrice.getText()+"") > needServeEntity.getPrice()){
                    //充值支付
                }else {

                    CommonUtils.ShowPopupWindow(this,v,confrimPopupWindow);
                }
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
                    contentBinding.tvNeedServeUserName.setTextColor(ContextCompat.getColor(MyNeedServeDetailActivity.this,R.color.Gray));
                    contentBinding.tvNeedServePhone.setTextColor(ContextCompat.getColor(MyNeedServeDetailActivity.this,R.color.Gray));
                    contentBinding.tvNeedServePlace.setTextColor(ContextCompat.getColor(MyNeedServeDetailActivity.this,R.color.Gray));
                    contentBinding.edtNeedServeDetailContent.setTextColor(ContextCompat.getColor(MyNeedServeDetailActivity.this,R.color.Gray));
                    contentBinding.edtNeedServeDetailPrice.setTextColor(ContextCompat.getColor(MyNeedServeDetailActivity.this,R.color.Gray));
                    contentBinding.edtNeedServeDetailPrice.setEnabled(false);
                    contentBinding.edtNeedServeDetailContent.setEnabled(false);
                    contentBinding.btnChangeConfirm.setVisibility(View.GONE);
                    contentBinding.btnChange.setVisibility(View.VISIBLE);

                }else{
                    showToast("修改失败，请重试");
                }
            }
        });
    }
}
