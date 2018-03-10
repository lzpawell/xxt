package bid.xiaocha.xxt.ui.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;

import com.android.tu.loadingdialog.LoadingDialog;

import java.util.ArrayList;
import java.util.List;

import bid.xiaocha.xxt.R;
import bid.xiaocha.xxt.adater.ServeCommentListAdater;
import bid.xiaocha.xxt.databinding.ActivityShowOfferServeDetailBinding;
import bid.xiaocha.xxt.databinding.ContentShowOfferServeDetailBinding;
import bid.xiaocha.xxt.iview.IShowOfferServeDetailView;
import bid.xiaocha.xxt.model.OfferServeEntity;
import bid.xiaocha.xxt.model.ServeCommentEntity;
import bid.xiaocha.xxt.model.UserEntity;
import bid.xiaocha.xxt.presenter.IShowOfferServeDetailPresenter;
import bid.xiaocha.xxt.presenter.ShowOfferServeDetailPresenter;
import bid.xiaocha.xxt.rongCloud.RongManager;
import bid.xiaocha.xxt.util.App;
import bid.xiaocha.xxt.util.UITool;

import static bid.xiaocha.xxt.util.CommonUtils.showToast;

public class ShowOfferServeDetailActivity extends AppCompatActivity implements IShowOfferServeDetailView, View.OnClickListener {

    private ActivityShowOfferServeDetailBinding activityBinding;
    private ContentShowOfferServeDetailBinding contentBinding;
    private ShowOfferServeDetailPresenter presenter;
    private LoadingDialog loadingDialog;
    private OfferServeEntity offerServeEntity;
    private ServeCommentListAdater adater;
    private boolean isHaveMore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityBinding = DataBindingUtil.setContentView(this,R.layout.activity_show_offer_serve_detail);
        contentBinding = activityBinding.content;
        setSupportActionBar(activityBinding.toolbar);
        initDatas();
        initPresenter();
        initView();
    }

    private void initDatas() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            offerServeEntity = OfferServeEntity.getOfferServeEntityFromJson(bundle.getString("serveJson"));
            getSupportActionBar().setTitle(offerServeEntity.getTitle());
        }else{
            offerServeEntity = null;
        }
    }

    private void initPresenter() {
        presenter = new ShowOfferServeDetailPresenter(this,offerServeEntity);
    }

    private void initView() {
        adater = new ServeCommentListAdater(new ArrayList<ServeCommentEntity>(),getLayoutInflater());
        contentBinding.lvCommentList.setAdapter(adater);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        loadingDialog = UITool.createLoadingDialog(this);
        contentBinding.btnTalk.setOnClickListener(this);
        contentBinding.btnRequest.setOnClickListener(this);
        SwipeRefreshLayout.OnRefreshListener onRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                contentBinding.lyRefreshOfferServeDetail.setRefreshing(true);
                adater.clearDataList();
                contentBinding.lyServeCommentFooter.setVisibility(View.VISIBLE);
                if (offerServeEntity != null){
                    presenter.getServeInfoAndComment(offerServeEntity.getServeId());
                }else {
                    showToast("获取信息失败，请重试");
                }
            }
        };
        contentBinding.lvCommentList.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (view.getChildAt(0) == null){
                    return;
                }
                if (firstVisibleItem == 0&&view.getChildAt(0).getTop()==0){
                    contentBinding.lyRefreshOfferServeDetail.setEnabled(true);
                }else{
                    contentBinding.lyRefreshOfferServeDetail.setEnabled(false);
                }
                if (isHaveMore){
                    if (firstVisibleItem + visibleItemCount == totalItemCount-5){
                        int nextPageNum = (totalItemCount-1)/ App.NUM_IN_A_PAGE+2;
                        presenter.getServeCommentByPages(offerServeEntity.getServeId(),nextPageNum);
                    }
                }else{
                    return;
                }
            }
        });
        contentBinding.lyRefreshOfferServeDetail.setOnRefreshListener(onRefreshListener);
        onRefreshListener.onRefresh();
    }

    @Override
    public void showLoading() {
        UITool.showLoadingDialog(loadingDialog);
    }


    @Override
    public void dismissLoading() {
        UITool.dismissLoadingDialog(loadingDialog);
    }

    @Override
    public void showRequestCreateOrderResult(final short result) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                switch (result){
                    case SUCCESS:
                        showToast("订单已生成并已通知对方");
                        RongManager.getInstance().talkWithOtherPeople(ShowOfferServeDetailActivity.this,offerServeEntity.getPublishUserId());
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
                        RongManager.getInstance().talkWithOtherPeople(ShowOfferServeDetailActivity.this,offerServeEntity.getPublishUserId());
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
    public void showNotEnoughMoney(final double money) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                showToast("你还缺少"+money+"元");
            }
        });
    }

    @Override
    public void showServeInfo(final OfferServeEntity offerServeEntity) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (offerServeEntity == null){
                    showToast("获取数据失败请重试");
                }else{
                    contentBinding.setOfferServe(offerServeEntity);
                }
                if (contentBinding.lyRefreshOfferServeDetail.isRefreshing())
                    contentBinding.lyRefreshOfferServeDetail.setRefreshing(false);
            }
        });
    }

    @Override
    public void showServeCommentListSuccess(List<ServeCommentEntity> serveCommentList, boolean isHaveMore) {
        if (serveCommentList == null){
            showToast("没有更多评论了");
        }else{
            this.isHaveMore = isHaveMore;
            adater.addDataList(serveCommentList);
        }
        contentBinding.lyServeCommentFooter.setVisibility(View.INVISIBLE);
        if (contentBinding.lyRefreshOfferServeDetail.isRefreshing())
            contentBinding.lyRefreshOfferServeDetail.setRefreshing(false);
    }

    @Override
    public void showServeCommentListFail() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                showToast("获取数据出错");
                contentBinding.lyServeCommentFooter.setVisibility(View.INVISIBLE);
                if (contentBinding.lyRefreshOfferServeDetail.isRefreshing())
                    contentBinding.lyRefreshOfferServeDetail.setRefreshing(false);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_talk:
                if (offerServeEntity == null){
                    return;
                }
                if (UserEntity.getCurrentUser().getUserId().equals(offerServeEntity.getPublishUserId())){
                    showToast("不能与自己聊天哦");
                    return;
                }
                RongManager.getInstance().talkWithOtherPeople(this,offerServeEntity.getPublishUserId());
                break;
            case R.id.btn_request:
                if (offerServeEntity == null){
                    return;
                }
                UserEntity currentUser = UserEntity.getCurrentUser();
                if (currentUser.getUserId().equals(offerServeEntity.getPublishUserId())){
                    showToast("不能请求自己的服务哦");
                    return;
                }
                presenter.requestCreateOrder();
                break;
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
