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
import bid.xiaocha.xxt.adater.FinishOrderListAdater;
import bid.xiaocha.xxt.databinding.FragmentEndOrderBinding;
import bid.xiaocha.xxt.iview.IControlOrderView;
import bid.xiaocha.xxt.iview.IShowOrderView;
import bid.xiaocha.xxt.model.ActiveOrderEntity;
import bid.xiaocha.xxt.model.FinishedOrderEntity;
import bid.xiaocha.xxt.presenter.ControlOrderPresenter;
import bid.xiaocha.xxt.presenter.ShowFinishOrderPresenter;
import bid.xiaocha.xxt.util.App;

import static bid.xiaocha.xxt.util.CommonUtils.showToast;
import static bid.xiaocha.xxt.util.UITool.createLoadingDialog;
import static bid.xiaocha.xxt.util.UITool.dismissLoadingDialog;
import static bid.xiaocha.xxt.util.UITool.showLoadingDialog;


public class EndOrderFragment extends Fragment implements IShowOrderView<FinishedOrderEntity>,IControlOrderView {

    private FragmentEndOrderBinding binding;
    private ShowFinishOrderPresenter showFinishOrderPresenter;
    private ControlOrderPresenter controlOrderPresenter;
    private FinishOrderListAdater adater;
    private boolean isHaveMore;
    private LoadingDialog loadingDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_end_order,container,false);
        initPresenter();
        initView();
        return binding.getRoot();
    }

    private void initView() {
        loadingDialog = createLoadingDialog(getActivity());
        adater = new FinishOrderListAdater(getActivity(),new ArrayList<FinishedOrderEntity>(),controlOrderPresenter);
        SwipeRefreshLayout.OnRefreshListener onRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                isHaveMore = true;
                adater.clearDataList();
                showFinishOrderPresenter.getOrderByPage(0);
            }
        };
        adater.setOnRefreshListener(onRefreshListener);
        binding.lyRefreshEndOrder.setOnRefreshListener(onRefreshListener);
        binding.lvEndOrderList.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (view.getChildAt(0)==null)
                    return;
                if (firstVisibleItem == 0&&view.getChildAt(0).getTop()==0){
                    binding.lyRefreshEndOrder.setEnabled(true);
                }else{
                    binding.lyRefreshEndOrder.setEnabled(false);
                }
                if (isHaveMore){
                    if (firstVisibleItem + visibleItemCount == totalItemCount-5){
                        int nextPageNum = (totalItemCount-1)/ App.NUM_IN_A_PAGE+2;
                        showFinishOrderPresenter.getOrderByPage(nextPageNum);
                    }
                }else{
                    return;
                }
            }
        });
        binding.lvEndOrderList.setAdapter(adater);
        binding.lyRefreshEndOrder.setRefreshing(true);
        onRefreshListener.onRefresh();
    }

    private void initPresenter() {
        showFinishOrderPresenter = new ShowFinishOrderPresenter(this);
        controlOrderPresenter = new ControlOrderPresenter(this);
    }

    @Override
    public void showOrderListSuccess(List<FinishedOrderEntity> finishOrderList, boolean isHaveMore) {
        if (finishOrderList == null){
            showToast("列表为空");
        }else {
            this.isHaveMore = isHaveMore;
            adater.addDataList(finishOrderList);
        }
        binding.lyFooter.getRoot().setVisibility(View.INVISIBLE);
        if (binding.lyRefreshEndOrder.isRefreshing()){
            binding.lyRefreshEndOrder.setRefreshing(false);
        }
    }

    @Override
    public void showOrderListFail() {
        showToast("获取数据出错");
        binding.lyFooter.getRoot().setVisibility(View.INVISIBLE);
        if (binding.lyRefreshEndOrder.isRefreshing()){
            binding.lyRefreshEndOrder.setRefreshing(false);
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
                    case SUCCESS_RETURN_FINISHED_ORDER:
                        //刷新另一页，或把该数据丢进那页的列表中。
                        adater.updateListViewItem(binding.lvEndOrderList,position,(FinishedOrderEntity) result);
                        break;
                    case ERROR_SHOW_STRING:
                        showToast((String) result);
                        break;
                }
            }
        });
    }
}
