package bid.xiaocha.xxt.ui.fragment;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import bid.xiaocha.xxt.R;
import bid.xiaocha.xxt.databinding.ActivityShowNeedServeRequestBinding;
import bid.xiaocha.xxt.databinding.ContentShowNeedServeRequestBinding;
import bid.xiaocha.xxt.iview.IShowNeedServeRequestView;
import bid.xiaocha.xxt.presenter.ShowNeedServeRequestPresenter;

import static bid.xiaocha.xxt.presenter.ShowNeedServeRequestPresenter.HAVE_NO_SERVE;
import static bid.xiaocha.xxt.presenter.ShowNeedServeRequestPresenter.SEND_MESSAGE_FAILURE;
import static bid.xiaocha.xxt.presenter.ShowNeedServeRequestPresenter.SUCCESS;
import static bid.xiaocha.xxt.util.CommonUtils.showToast;

public class ShowNeedServeRequestView extends AppCompatActivity implements View.OnClickListener,IShowNeedServeRequestView {

    private ActivityShowNeedServeRequestBinding activityBinding;
    private ContentShowNeedServeRequestBinding contentBinding;
    private String requesterId;
    private String serveId;
    private ShowNeedServeRequestPresenter presenter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityBinding = DataBindingUtil.setContentView(this,R.layout.activity_show_need_serve_request);
        contentBinding = activityBinding.content;
        setSupportActionBar(activityBinding.toolbar);
        initDatas();
        initPresenter();
        initView();
    }



    private void initDatas() {
        Bundle bundle = getIntent().getExtras();
        requesterId = bundle.getString("requesterId");
        serveId = bundle.getString("serveId");
    }
    private void initPresenter() {
        presenter = new ShowNeedServeRequestPresenter(this,serveId,requesterId);
    }
    private void initView() {
        contentBinding.btnAgree.setOnClickListener(this);
        contentBinding.btnRefuse.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_agree:
                presenter.agreeRequest();
                break;
            case R.id.btn_refuse:
                presenter.refuseRequest();
                break;
        }
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void dismissLoading() {

    }

    @Override
    public void showSendRefuseRequestResult(final int result) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                switch (result){
                    case SUCCESS:
                        showToast("发送成功");
                        finish();
                        break;
                    case HAVE_NO_SERVE:
                        showToast("不可以重复拒绝哦");
                        break;
                    case SEND_MESSAGE_FAILURE:
                        showToast("发送失败");
                        break;
                }
            }
        });
    }

    @Override
    public void showCreateOrderResult(int result) {

    }


}
