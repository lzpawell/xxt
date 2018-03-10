package bid.xiaocha.xxt.ui.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.android.tu.loadingdialog.LoadingDialog;

import bid.xiaocha.xxt.R;
import bid.xiaocha.xxt.databinding.ActivityShowUserInfoBinding;
import bid.xiaocha.xxt.databinding.ContentShowUserInfoBinding;
import bid.xiaocha.xxt.iview.IShowUserInfoView;
import bid.xiaocha.xxt.model.UserEntity;
import bid.xiaocha.xxt.presenter.IShowUserInfoPresenter;
import bid.xiaocha.xxt.presenter.ShowUserInfoPrenster;
import bid.xiaocha.xxt.util.HeadProtraitUtil;
import bid.xiaocha.xxt.util.UITool;
import io.rong.imlib.model.UserInfo;

import static bid.xiaocha.xxt.util.CommonUtils.showToast;

public class ShowUserInfoActivity extends BaseActivity implements IShowUserInfoView,View.OnClickListener {
    private ActivityShowUserInfoBinding activityBinding;
    private ContentShowUserInfoBinding contentBinding;
    private IShowUserInfoPresenter presenter;
    private String userId = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityBinding = DataBindingUtil.setContentView(this, R.layout.activity_show_user_info);
        contentBinding = activityBinding.content;
        setSupportActionBar(activityBinding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");
        initDatas();
        initView();
    }
    private void initDatas(){
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            userId = bundle.getString("userId");
        }
        if (userId == null || userId.equals("")) {
            presenter = new ShowUserInfoPrenster(this,null);
        }else{
            presenter = new ShowUserInfoPrenster(this,userId);
        }
        presenter.getUserInfo();
    }
    private void initView() {
        contentBinding.btnOfferServes.setOnClickListener(this);
        contentBinding.btnPersonalComment.setOnClickListener(this);
    }

    @Override
    public void showUserInfo(UserEntity userEntity) {
        if (userEntity == null){
            showToast("信息加载失败");
        }else{
            HeadProtraitUtil.getHeadPic(userEntity.getUserId(), new HeadProtraitUtil.GetHeadCallback() {
                @Override
                public void getHeadPhoto(Bitmap bitmap) {
                    if (bitmap != null){
                        contentBinding.imgHead.setImageBitmap(bitmap);
                    }else {
                        contentBinding.imgHead.setImageDrawable(ContextCompat.getDrawable(ShowUserInfoActivity.this,R.mipmap.head2));
                    }
                }
            });
            contentBinding.tvNickName.setText(userEntity.getNickName());
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
            contentBinding.tvDemenderMark.setText(userEntity.getBeHelpedMark()+"");
            contentBinding.tvDemenderNum.setText("("+userEntity.getBeHelpedNumber()+"人已评)");
            contentBinding.tvProviderMark.setText(userEntity.getHelpMark()+"");
            contentBinding.tvProviderNum.setText("("+userEntity.getHelpNumber()+"人已评)");
        }
    }


    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()){
            case R.id.btn_personal_comment:
                intent = new Intent(ShowUserInfoActivity.this,ShowPersonalCommentActivity.class);
                break;
            case R.id.btn_offer_serves:
                intent = new Intent(ShowUserInfoActivity.this,ShowAllServesOfaPersonActivity.class);
                break;
        }
        if (intent != null && userId != null) {
            Bundle bundle = new Bundle();
            bundle.putString("userId",userId);
            intent.putExtras(bundle);
            startActivity(intent);
        }else{
            showToast("用户信息出错");
        }
    }
}
