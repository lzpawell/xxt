package bid.xiaocha.xxt.ui.fragment;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;

import com.android.tu.loadingdialog.LoadingDialog;

import java.util.ArrayList;
import java.util.List;

import bid.xiaocha.xxt.R;
import bid.xiaocha.xxt.adater.ActiveOrderListAdater;
import bid.xiaocha.xxt.databinding.FragmentActiveOrderBinding;
import bid.xiaocha.xxt.iview.IControlOrderView;
import bid.xiaocha.xxt.iview.IShowOrderView;
import bid.xiaocha.xxt.model.ActiveOrderEntity;
import bid.xiaocha.xxt.presenter.ControlOrderPresenter;
import bid.xiaocha.xxt.presenter.IControlOrderPresenter;
import bid.xiaocha.xxt.presenter.ShowActiveOrderPresenter;
import bid.xiaocha.xxt.util.App;

import static bid.xiaocha.xxt.util.CommonUtils.showToast;
import static bid.xiaocha.xxt.util.UITool.createLoadingDialog;
import static bid.xiaocha.xxt.util.UITool.dismissLoadingDialog;
import static bid.xiaocha.xxt.util.UITool.showLoadingDialog;


public class ActiveOrderFragment extends Fragment implements IShowOrderView<ActiveOrderEntity>,IControlOrderView {

    private FragmentActiveOrderBinding binding ;
    private ShowActiveOrderPresenter showActiveOrderPresenter;
    private IControlOrderPresenter controlOrderPresenter;
    private ActiveOrderListAdater adater;
    private boolean isHaveMore;
    private LoadingDialog loadingDialog;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_active_order,container,false);
        initPresenter();
        initView();
        return binding.getRoot();
    }
    private void initPresenter() {
        showActiveOrderPresenter = new ShowActiveOrderPresenter(this);
        controlOrderPresenter = new ControlOrderPresenter(this);
    }

    private void initView(){
        adater = new ActiveOrderListAdater(getActivity(),new ArrayList<ActiveOrderEntity>(),controlOrderPresenter);
        SwipeRefreshLayout.OnRefreshListener onRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                isHaveMore = true;
                adater.clearDataList();
                showActiveOrderPresenter.getOrderByPage(0);
            }
        };
         binding.lyRefreshActiveOrder.setOnRefreshListener(onRefreshListener);
         binding.lvActiveOrderList.setAdapter(adater);

         binding.lvActiveOrderList.setOnScrollListener(new AbsListView.OnScrollListener() {
             @Override
             public void onScrollStateChanged(AbsListView view, int scrollState) {

             }

             @Override
             public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                 if (view.getChildAt(0) == null){
                     return;
                 }
                 if (firstVisibleItem == 0&&view.getChildAt(0).getTop()==0){
                     binding.lyRefreshActiveOrder.setEnabled(true);
                 }else{
                     binding.lyRefreshActiveOrder.setEnabled(false);
                 }
                 if (isHaveMore){
                     if (firstVisibleItem + visibleItemCount == totalItemCount-5){
                         int nextPageNum = (totalItemCount-1)/ App.NUM_IN_A_PAGE+2;
                         showActiveOrderPresenter.getOrderByPage(nextPageNum);
                     }
                 }else{
                     return;
                 }
             }
        });
        loadingDialog = createLoadingDialog(getActivity());
        binding.lyRefreshActiveOrder.setRefreshing(true);
        onRefreshListener.onRefresh();
    }


    @Override
    public void showOrderListSuccess(List<ActiveOrderEntity> activeOrderList, boolean isHaveMore) {
        if (activeOrderList == null){
            showToast("列表为空");
        }else {
            this.isHaveMore = isHaveMore;
            adater.addDataList(activeOrderList);
        }
        binding.lyFooter.getRoot().setVisibility(View.INVISIBLE);
        if (binding.lyRefreshActiveOrder.isRefreshing()){
            binding.lyRefreshActiveOrder.setRefreshing(false);
        }
    }

    @Override
    public void showOrderListFail() {
        showToast("获取数据出错");
        binding.lyFooter.getRoot().setVisibility(View.INVISIBLE);
        if (binding.lyRefreshActiveOrder.isRefreshing()){
            binding.lyRefreshActiveOrder.setRefreshing(false);
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
    public void showResult(final short resultCode, final Object result, final int position) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                switch (resultCode){
                    case SUCCESS_RETURN_ACTIVE_ORDER:
                        adater.updateListViewItem(binding.lvActiveOrderList,position,(ActiveOrderEntity) result);
                        break;
                    case SUCCESS_RETURN_FINISHED_ORDER:
                        //刷新另一页，或把该数据丢进那页的列表中。
                        adater.removeFromDataList(position);
                        break;
                    case ERROR_SHOW_STRING:
                        showToast((String) result);
                        break;
                }
            }
        });
    }
}
