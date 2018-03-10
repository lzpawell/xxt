package bid.xiaocha.xxt.ui.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;

import java.util.ArrayList;
import java.util.List;

import bid.xiaocha.xxt.BR;
import bid.xiaocha.xxt.R;
import bid.xiaocha.xxt.adater.CommonListAdater;
import bid.xiaocha.xxt.databinding.ActivityShowOfferServeBinding;
import bid.xiaocha.xxt.databinding.ContentShowOfferServeBinding;
import bid.xiaocha.xxt.iview.IShowServeView;
import bid.xiaocha.xxt.model.OfferServeEntity;
import bid.xiaocha.xxt.presenter.ShowOfferServePresenter;
import bid.xiaocha.xxt.util.App;

import static bid.xiaocha.xxt.util.CommonUtils.showToast;

public class ShowOfferServeActivity extends AppCompatActivity implements IShowServeView<OfferServeEntity> {
    private ActivityShowOfferServeBinding activityBinding;
    private ContentShowOfferServeBinding contentBinding;
    private ShowOfferServePresenter presenter;
    private boolean isHaveMore;
    private CommonListAdater<OfferServeEntity> adater;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityBinding = DataBindingUtil.setContentView(this,R.layout.activity_show_offer_serve);
        contentBinding = activityBinding.content;
        setSupportActionBar(activityBinding.toolbar);
        initPresenter();
        initView();
    }

    private void initPresenter() {
        presenter = new ShowOfferServePresenter(this);
    }

    private void initView() {
        adater = new CommonListAdater<OfferServeEntity>(new ArrayList<OfferServeEntity>(), BR.offerServe, getLayoutInflater(), R.layout.list_item_show_offer_serve_list, new CommonListAdater.OnItemClick<OfferServeEntity>() {
            @Override
            public void setItemClick(final OfferServeEntity offerServe, View view) {
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(ShowOfferServeActivity.this,ShowOfferServeDetailActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("serveJson",offerServe.toString());
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                });
            }
        });
        contentBinding.lvShowOfferServe.setAdapter(adater);
        SwipeRefreshLayout.OnRefreshListener onRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                isHaveMore = true;
                adater.clearDataList();
                contentBinding.lyFooter.getRoot().setVisibility(View.VISIBLE);
                presenter.getOfferServesByPage(0,"null");

            }
        };
        contentBinding.lyRefreshOfferServe.setOnRefreshListener(onRefreshListener);
        contentBinding.lvShowOfferServe.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (isHaveMore){
                    if (firstVisibleItem + visibleItemCount == totalItemCount-5){
                        int nextPageNum = (totalItemCount-1)/ App.NUM_IN_A_PAGE+2;
                        contentBinding.lyFooter.getRoot().setVisibility(View.VISIBLE);
                        presenter.getOfferServesByPage(nextPageNum,"null");
                    }
                }else{
                    return;
                }
            }
        });
        contentBinding.lyRefreshOfferServe.setRefreshing(true);
        onRefreshListener.onRefresh();
    }


    @Override
    public void showServeListSuccess(List<OfferServeEntity> needServeList, boolean isHaveMore) {
        if (needServeList == null){
            showToast("列表为空");
        }else {
            this.isHaveMore = isHaveMore;
            adater.addDataList(needServeList);
        }
        contentBinding.lyFooter.getRoot().setVisibility(View.INVISIBLE);
        if (contentBinding.lyRefreshOfferServe.isRefreshing()){
           contentBinding.lyRefreshOfferServe.setRefreshing(false);
        }
    }

    @Override
    public void showServeListFail() {
        showToast("获取数据出错");
        contentBinding.lyFooter.getRoot().setVisibility(View.INVISIBLE);
        if (contentBinding.lyRefreshOfferServe.isRefreshing()){
            contentBinding.lyRefreshOfferServe.setRefreshing(false);
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
