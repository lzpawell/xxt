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
import bid.xiaocha.xxt.databinding.ActivityShowNeedServeBinding;
import bid.xiaocha.xxt.databinding.ContentShowNeedServeBinding;
import bid.xiaocha.xxt.iview.IShowServeView;
import bid.xiaocha.xxt.model.NeedServeEntity;
import bid.xiaocha.xxt.presenter.ShowNeedServePresenter;
import bid.xiaocha.xxt.util.App;

import static bid.xiaocha.xxt.util.CommonUtils.showToast;

public class ShowNeedServeActivity extends AppCompatActivity implements IShowServeView<NeedServeEntity> {
    private ActivityShowNeedServeBinding activityBinding;
    private ContentShowNeedServeBinding contentBinding;
    private ShowNeedServePresenter presenter;
    private boolean isHaveMore;
    private CommonListAdater<NeedServeEntity> adater;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityBinding = DataBindingUtil.setContentView(this,R.layout.activity_show_need_serve);
        contentBinding = activityBinding.content;
        setSupportActionBar(activityBinding.toolbar);
        initPresenter();
        initView();
    }

    private void initPresenter() {
        presenter = new ShowNeedServePresenter(this);
    }

    private void initView() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("他们需要你的帮助");
        adater = new CommonListAdater<NeedServeEntity>(new ArrayList<NeedServeEntity>(), BR.needServe, getLayoutInflater(), R.layout.list_item_show_need_serve_list, new CommonListAdater.OnItemClick<NeedServeEntity>() {
            @Override
            public void setItemClick(final NeedServeEntity needServe, View view) {
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(ShowNeedServeActivity.this,ShowNeedServeDetailActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("serveJson",needServe.toString());
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                });
            }
        });
        contentBinding.lvShowNeedServe.setAdapter(adater);
        SwipeRefreshLayout.OnRefreshListener onRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                isHaveMore = true;
                adater.clearDataList();
                contentBinding.lyFooter.getRoot().setVisibility(View.VISIBLE);
                presenter.getNeedServesByPage(0,"null");

            }
        };
        contentBinding.lyRefreshNeedServe.setOnRefreshListener(onRefreshListener);
        contentBinding.lvShowNeedServe.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (isHaveMore){
                    if (firstVisibleItem + visibleItemCount == totalItemCount-5){
                        int nextPageNum = (totalItemCount-1)/ App.NUM_IN_A_PAGE+2;
                        contentBinding.lyFooter.getRoot().setVisibility(View.VISIBLE);
                        presenter.getNeedServesByPage(nextPageNum,"null");
                    }
                }else{
                    return;
                }
            }
        });
        contentBinding.lyRefreshNeedServe.setRefreshing(true);
        onRefreshListener.onRefresh();
    }


    @Override
    public void showServeListSuccess(List<NeedServeEntity> needServeList, boolean isHaveMore) {
        if (needServeList == null){
            showToast("列表为空");
        }else {
            this.isHaveMore = isHaveMore;
            adater.addDataList(needServeList);
            contentBinding.lyFooter.getRoot().setVisibility(View.INVISIBLE);
        }
        if (contentBinding.lyRefreshNeedServe.isRefreshing()){
            contentBinding.lyRefreshNeedServe.setRefreshing(false);
        }
    }

    @Override
    public void showServeListFail() {
        showToast("获取数据出错");
        contentBinding.lyFooter.getRoot().setVisibility(View.INVISIBLE);
        if (contentBinding.lyRefreshNeedServe.isRefreshing()){
            contentBinding.lyRefreshNeedServe.setRefreshing(false);
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
