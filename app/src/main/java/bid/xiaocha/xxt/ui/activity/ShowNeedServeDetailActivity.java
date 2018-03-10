package bid.xiaocha.xxt.ui.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;

import com.android.tu.loadingdialog.LoadingDialog;

import bid.xiaocha.xxt.R;
import bid.xiaocha.xxt.databinding.ActivityShowNeedServeDetailBinding;
import bid.xiaocha.xxt.databinding.ContentShowNeedServeDetailBinding;
import bid.xiaocha.xxt.iview.IShowNeedServeDetailView;
import bid.xiaocha.xxt.model.NeedServeEntity;
import bid.xiaocha.xxt.model.UserEntity;
import bid.xiaocha.xxt.presenter.IShowNeedServeDetailPresenter;
import bid.xiaocha.xxt.presenter.ShowNeedServeDetailPresenter;
import bid.xiaocha.xxt.rongCloud.RongManager;
import bid.xiaocha.xxt.util.UITool;

import static bid.xiaocha.xxt.util.CommonUtils.showToast;

public class ShowNeedServeDetailActivity extends BaseActivity implements IShowNeedServeDetailView, View.OnClickListener {

    private ActivityShowNeedServeDetailBinding activityBinding;
    private ContentShowNeedServeDetailBinding contentBinding;
    private NeedServeEntity needServeEntity;
    private UserEntity publisherUserEntity;
    private IShowNeedServeDetailPresenter showNeedServeDetailPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityBinding = DataBindingUtil.setContentView(this,R.layout.activity_show_need_serve_detail);
        contentBinding = activityBinding.content;

        setSupportActionBar(activityBinding.toolbar);
        initPresenter();
        initView();
}

    private void initPresenter() {
        showNeedServeDetailPresenter = new ShowNeedServeDetailPresenter(this);
    }

    private void initView() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            needServeEntity = NeedServeEntity.getNeedServeEntityFromJson(bundle.getString("serveJson"));
            getSupportActionBar().setTitle(needServeEntity.getTitle());
            contentBinding.setNeedServe(needServeEntity);
            showNeedServeDetailPresenter.getUserEntity(needServeEntity.getPublishUserId());
        }else{
            needServeEntity = null;
        }

        contentBinding.btnTalk.setOnClickListener(this);
        contentBinding.btnRequest.setOnClickListener(this);
    }

    @Override
    public void showMarkResult(final boolean isSuccess, final UserEntity userEntity) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (isSuccess) {
                    publisherUserEntity = userEntity;
                    contentBinding.tvNeedServeDetailUsermark.setText(userEntity.getBeHelpedMark() + "");
                    contentBinding.tvNeedServeDetailUsercount.setText(userEntity.getBeHelpedNumber() + "");
                    contentBinding.setNeedServe(needServeEntity);
                } else {
                    showToast("获取失败");
                }
            }
        });
    }

    @Override
    public void showRequestCreateOrderResult(final short result) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                switch (result){
                    case SUCCESS:
                        showToast("订单已生成并已通知对方");
                        if (publisherUserEntity == null){
                            RongManager.getInstance().talkWithOtherPeople(ShowNeedServeDetailActivity.this,needServeEntity.getPublishUserId(),"");
                        }else{
                            RongManager.getInstance().talkWithOtherPeople(ShowNeedServeDetailActivity.this,needServeEntity.getPublishUserId(),publisherUserEntity.getNickName());
                        }
                        finish();
                        break;
                    case SERVE_UNUSUAL:
                        showToast("该服务异常（已被接或暂停）");
                        break;
                    case NET_ERROR:
                        showToast("网络错误");
                        break;
                    case SEND_MESSAGE_ERROR:
                        showToast("订单生成但消息发送异常");
                        if (publisherUserEntity == null){
                            RongManager.getInstance().talkWithOtherPeople(ShowNeedServeDetailActivity.this,needServeEntity.getPublishUserId(),"");
                        }else{
                            RongManager.getInstance().talkWithOtherPeople(ShowNeedServeDetailActivity.this,needServeEntity.getPublishUserId(),publisherUserEntity.getNickName());
                        }
                        finish();
                        break;
                    case SERVE_ERROR:
                        showToast("程序员太垃圾了，服务器都炸了");
                        break;
                }
            }
        });
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_talk:
                if (needServeEntity == null){
                    return;
                }
                if (UserEntity.getCurrentUser().getUserId().equals(needServeEntity.getPublishUserId())){
                    showToast("不能与自己聊天哦");
                    return;
                }
                if (publisherUserEntity == null){
                    RongManager.getInstance().talkWithOtherPeople(this,needServeEntity.getPublishUserId(),"");
                }else{
                    RongManager.getInstance().talkWithOtherPeople(this,needServeEntity.getPublishUserId(),publisherUserEntity.getNickName());
                }

                break;
            case R.id.btn_request:
                if (needServeEntity == null){
                    return;
                }
                UserEntity currentUser = UserEntity.getCurrentUser();
                if (currentUser.getUserId().equals(needServeEntity.getPublishUserId())){
                    showToast("不能接自己的单哦");
                    return;
                }

                showNeedServeDetailPresenter.requestCreateOrder(needServeEntity);

                break;
        }

    }
}
