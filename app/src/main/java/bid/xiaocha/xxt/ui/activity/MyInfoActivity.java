package bid.xiaocha.xxt.ui.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupWindow;


import com.android.tu.loadingdialog.LoadingDialog;

import bid.xiaocha.xxt.R;
import bid.xiaocha.xxt.databinding.ActivityMyInfoBinding;
import bid.xiaocha.xxt.databinding.ContentMyInfoBinding;
import bid.xiaocha.xxt.iview.IMyInfoView;
import bid.xiaocha.xxt.model.UserEntity;
import bid.xiaocha.xxt.presenter.MyInfoPresenter;
import bid.xiaocha.xxt.util.CommonUtils;
import bid.xiaocha.xxt.util.HeadProtraitUtil;

import static bid.xiaocha.xxt.util.CommonUtils.showToast;
import static bid.xiaocha.xxt.util.UITool.createLoadingDialog;
import static bid.xiaocha.xxt.util.UITool.dismissLoadingDialog;
import static bid.xiaocha.xxt.util.UITool.showLoadingDialog;

public class MyInfoActivity extends AppCompatActivity implements View.OnClickListener,IMyInfoView{

    private MyInfoPresenter presenter;
    private ActivityMyInfoBinding activityBinding;
    private ContentMyInfoBinding contentBinding;
    private UserEntity userEntity;
    private PopupWindow updateNameWindow;
    private PopupWindow updateSexWindow;
    private ImageButton chooseBoyBtn;
    private ImageButton chooseGirlBtn;
    private Button nameConfirmBtn;
    private Button nameCancelBtn;
    private EditText updateNameEdt;
    private LoadingDialog loadingDialog;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityBinding = DataBindingUtil.setContentView(this,R.layout.activity_my_info);
        contentBinding = activityBinding.content;

        setSupportActionBar(activityBinding.toolbar);
        initView();
        initPresenter();

    }

    private void initPresenter() {
        presenter = new MyInfoPresenter(this);
    }

    private void initData() {
        userEntity = UserEntity.getCurrentUser();
    }

    private void initView() {
        getSupportActionBar().setTitle("个人信息");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        updateSexWindow = new PopupWindow(LayoutInflater.from(this).inflate(R.layout.popupwindow_sex,null), ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT,true);
        updateNameWindow= new PopupWindow(LayoutInflater.from(this).inflate(R.layout.popupwindow_name,null), ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT,true);
        chooseBoyBtn = (ImageButton) updateSexWindow.getContentView().findViewById(R.id.btn_choose_boy);
        chooseGirlBtn = (ImageButton)  updateSexWindow.getContentView().findViewById(R.id.btn_choose_girl);
        nameConfirmBtn = (Button)  updateNameWindow.getContentView().findViewById(R.id.btn_name_comfirm);
        nameCancelBtn = (Button)  updateNameWindow.getContentView().findViewById(R.id.btn_name_cancel);
        updateNameEdt = (EditText)  updateNameWindow.getContentView().findViewById(R.id.edt_update_name);


        resetButtonAndTextView();

        HeadProtraitUtil.getHeadPic(userEntity.getUserId(), new HeadProtraitUtil.GetHeadCallback() {
            @Override
            public void getHeadPhoto(Bitmap bitmap) {
                if (bitmap != null){
                    contentBinding.imgHead.setImageBitmap(bitmap);
                }else {
                    contentBinding.imgHead.setImageDrawable(ContextCompat.getDrawable(MyInfoActivity.this,R.mipmap.head2));
                }
            }
        });

        contentBinding.tvSex.setOnClickListener(this);
        contentBinding.tvNickName.setOnClickListener(this);
        chooseBoyBtn.setOnClickListener(this);
        chooseGirlBtn.setOnClickListener(this);
        nameCancelBtn.setOnClickListener(this);
        nameConfirmBtn.setOnClickListener(this);
        contentBinding.lyHead.setOnClickListener(this);
        contentBinding.lyNickName.setOnClickListener(this);
        contentBinding.lySex.setOnClickListener(this);
        contentBinding.btnLogout.setOnClickListener(this);
        loadingDialog = createLoadingDialog(this);
    }

    private void resetButtonAndTextView() {
        initData();
        String nickName = userEntity.getNickName();
        updateNameEdt.setText(nickName);
        contentBinding.tvNickName.setText(nickName);
        String sex = "";
        switch (userEntity.getSex()){
            case UserEntity.SEX_MAN:
                sex = "男";
                break;
            case UserEntity.SEX_WOMAN:
                sex = "女";
                break;
            case UserEntity.SEX_UNDEFINED:
                sex = "未指定";
                break;
        }

        contentBinding.tvSex.setText(sex);
        if(userEntity.getSex()==UserEntity.SEX_MAN){
            chooseGirlBtn.setEnabled(true);
            chooseBoyBtn.setEnabled(false);
        }
        else if(userEntity.getSex()==UserEntity.SEX_WOMAN){
            chooseGirlBtn.setEnabled(false);
            chooseBoyBtn.setEnabled(true);
        }


    }

    @Override
    public void onClick(View v) {
         switch(v.getId()){
             case R.id.ly_head:
                    showChoosePicDialog();
                 break;
             case R.id.ly_sex:
                 CommonUtils.ShowPopupWindow(this,v,updateSexWindow);
                 break;
             case R.id.ly_nick_name:
                 CommonUtils.ShowPopupWindow(this,v,updateNameWindow);
                 break;
             case R.id.btn_choose_boy:
                 userEntity.setSex(UserEntity.SEX_MAN);
                 presenter.updateMyInfo(userEntity);
                 updateSexWindow.dismiss();
                 break;
             case R.id.btn_choose_girl:
                 userEntity.setSex(UserEntity.SEX_WOMAN);
                 presenter.updateMyInfo(userEntity);
                 updateSexWindow.dismiss();
                 break;
             case R.id.btn_name_comfirm:
                 userEntity.setNickName(updateNameEdt.getText().toString().trim());
                 presenter.updateMyInfo(userEntity);
                 updateNameWindow.dismiss();
                 break;
             case R.id.btn_name_cancel:
                 updateNameWindow.dismiss();
                 break;
             case R.id.btn_logout:
                 UserEntity.logout();
                 finish();
                 break;
         }
    }
    protected void showChoosePicDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("设置头像");
        String[] items = {"选择本地照片", "拍照"};
        builder.setNegativeButton("取消", null);
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0 || which == 1){
                    presenter.choosePic(MyInfoActivity.this,which);
                }
            }
        });
        builder.create().show();
    }

    @Override
    public void showLoading() {
         showLoadingDialog(loadingDialog);
    }

    @Override
    public void showUpdateResult(final boolean result) {
           runOnUiThread(new Runnable() {
               @Override
               public void run() {
                   if(result) {
                       showToast("修改成功");
                       resetButtonAndTextView();
                   }else{
                       showToast("修改失败，请重试");
                   }
               }
           });
    }

    @Override
    public void dismissLoading() {
        dismissLoadingDialog(loadingDialog);
    }

    @Override
    public void showHeadPic(final Bitmap bitmap) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (bitmap == null){
                    showToast("修改失败");
                }else{
                    showToast("修改成功");
                    contentBinding.imgHead.setImageBitmap(bitmap);
                }
            }
        });
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        HeadProtraitUtil.onActivityResult(requestCode,resultCode,data);
    }
}
