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
import bid.xiaocha.xxt.adater.NeedServeManagerListAdater;
import bid.xiaocha.xxt.databinding.FragmentNeedServeManagerBinding;
import bid.xiaocha.xxt.iview.IControlServeView;
import bid.xiaocha.xxt.iview.IShowServeView;
import bid.xiaocha.xxt.model.NeedServeEntity;
import bid.xiaocha.xxt.model.UserEntity;
import bid.xiaocha.xxt.presenter.ControlServePresenter;
import bid.xiaocha.xxt.presenter.ShowNeedServePresenter;
import bid.xiaocha.xxt.util.App;

import static bid.xiaocha.xxt.util.CommonUtils.showToast;
import static bid.xiaocha.xxt.util.UITool.createLoadingDialog;
import static bid.xiaocha.xxt.util.UITool.dismissLoadingDialog;
import static bid.xiaocha.xxt.util.UITool.showLoadingDialog;

public class NeedServeManagerFragment extends Fragment implements IShowServeView<NeedServeEntity>,IControlServeView{
    private FragmentNeedServeManagerBinding binding;
    private ShowNeedServePresenter presenter;
    private NeedServeManagerListAdater adater;
    private ControlServePresenter controlPresenter;
    private LoadingDialog loadingDialog;
    private boolean isHaveMore;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_need_serve_manager,container,false);
        initPresenter();
        initView();
        return binding.getRoot();
    }

    private void initPresenter() {
        presenter = new ShowNeedServePresenter(this);
        controlPresenter = new ControlServePresenter(this);
    }

    private void initView() {
        adater = new NeedServeManagerListAdater(binding.lvServiceManagerList,new ArrayList<NeedServeEntity>(),getActivity(),controlPresenter);
        SwipeRefreshLayout.OnRefreshListener onRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                isHaveMore = true;
                adater.clearDataList();
                presenter.getMyNeedServesByPage(0,"null", UserEntity.getCurrentUser().getUserId());
            }
        };
        binding.lyRefreshServiceManager.setOnRefreshListener(onRefreshListener);
        binding.lvServiceManagerList.setAdapter(adater);
        binding.lvServiceManagerList.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (isHaveMore){
                    if (firstVisibleItem + visibleItemCount == totalItemCount-5){
                        int nextPageNum = (totalItemCount-1)/ App.NUM_IN_A_PAGE+2;
                        presenter.getMyNeedServesByPage(0,"null", UserEntity.getCurrentUser().getUserId());
                    }
                }else{
                    return;
                }
            }
        });
        loadingDialog = createLoadingDialog(getActivity());

        binding.lyRefreshServiceManager.setRefreshing(true);
        onRefreshListener.onRefresh();
    }





    @Override
    public void showServeListSuccess(List<NeedServeEntity> ServeList, boolean isHaveMore) {
        if (ServeList == null){
            showToast("列表为空");
        }else {
            this.isHaveMore = isHaveMore;
            adater.addDataList(ServeList);
        }
        binding.lyFooter.getRoot().setVisibility(View.INVISIBLE);
        if (binding.lyRefreshServiceManager.isRefreshing()){
            binding.lyRefreshServiceManager.setRefreshing(false);
        }
    }

    @Override
    public void showServeListFail() {
        showToast("获取数据出错");
        binding.lyFooter.getRoot().setVisibility(View.INVISIBLE);
        if (binding.lyRefreshServiceManager.isRefreshing()){
            binding.lyRefreshServiceManager.setRefreshing(false);
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
    public void showResult(final short resultCode,final Object result,final int position) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                switch (resultCode){
                    case CHANGE_SUCCESS:
                        adater.updateView(position,(NeedServeEntity) result);
                        break;
                    case ERROR_SHOW_STRING:
                        showToast((String) result);
                        break;
                    case DELETE_SUCCESS:
                        adater.deleteView(position);
                        showToast((String) result);
                        break;
                }
            }
        });
    }
}
