package bid.xiaocha.xxt.ui.fragment;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import bid.xiaocha.xxt.R;
import bid.xiaocha.xxt.adater.FragmentAdater;
import bid.xiaocha.xxt.databinding.FragmentOrderBinding;

public class OrderFragment extends Fragment {



    private FragmentOrderBinding binding;
    private ActiveOrderFragment activeOrderFragment= new ActiveOrderFragment();
    private EndOrderFragment endOrderFragment= new EndOrderFragment();
    private WaitCommentOrderFragment waitCommentOrderFragment = new WaitCommentOrderFragment();
    private FragmentAdater adater;
//
//    private OrderPresenter orderPresenter;
//
//    private double balala = Math.random();


//    private boolean isHaveMore;

//    private OrderListAdater orderListAdater;
//    private SwipeRefreshLayout.OnRefreshListener onRefreshListener;

    public static OrderFragment getFragment(){
        OrderFragment fragment = new OrderFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
//    @Override
//    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
//        super.onActivityCreated(savedInstanceState);
//
//        if(savedInstanceState != null){
//            balala = savedInstanceState.getDouble("balala");
//            Log.i("balala", "resume " +  balala);
//        }else{
//            Log.i("balala", "create " +  balala);
//        }
//    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_order, container, false);
        initView();

        return binding.getRoot();
    }

    private void initView() {
        setupViewPager();
    }

    private void setupViewPager() {
        TabLayout tabs = binding.orderTabs;
        ViewPager viewPager = binding.orderViewpager;
        List<String> titles = new ArrayList<>();
        titles.add("进行中");
        titles.add("已完成");
        List<Fragment> fragmentList = new ArrayList<>();
        fragmentList.add(activeOrderFragment);
        fragmentList.add(endOrderFragment);
        tabs.addTab(tabs.newTab().setText(titles.get(0)));
        tabs.addTab(tabs.newTab().setText(titles.get(1)));
        adater = new FragmentAdater(getFragmentManager(),fragmentList,titles);
        viewPager.setAdapter(adater);
        tabs.setupWithViewPager(viewPager);
        tabs.setTabsFromPagerAdapter(adater);
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);


    }

//    private void initView() {
//        orderListAdater = new OrderListAdater(getActivity());
//        binding.demoListView.setAdapter(orderListAdater);
//        onRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                Toast.makeText(App.getAppContext(), "onRefresh", Toast.LENGTH_SHORT).show();
//                orderPresenter.getOrdersByPages(1,true);
//            }
//        };
//
//        binding.demoSwiperefreshlayout.setOnRefreshListener(onRefreshListener);
//
//
//        binding.demoListView.setOnScrollListener(new AbsListView.OnScrollListener() {
//            @Override
//            public void onScrollStateChanged(AbsListView view, int scrollState) {
//            }
//            @Override
//            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
//                if (firstVisibleItem + visibleItemCount == totalItemCount){
//                    if (orderPresenter.numInAPage == 0){
//                        showToast("一页的数量不能为0");
//                        return;
//                    }
//                    if (!isHaveMore){
//                        Log.i("scroll","到底了");
//
//                        return;
//                    }
//                    int nextPageNum = (totalItemCount-1)/orderPresenter.numInAPage+2;
//                    orderPresenter.getOrdersByPages(nextPageNum,false);
//                }
//            }
//        });
//
//        binding.demoSwiperefreshlayout.setRefreshing(true);
//        onRefreshListener.onRefresh();
//    }
//
//    private void initPresenter(){
//        orderPresenter = new OrderPresenter(this);
//    }
//
//    @Override
//    public void showOrders(final List<ActiveOrderEntity> dataList, final boolean isRefresh, final boolean more) {
//
//        Runnable task = new Runnable() {
//            @Override
//            public void run() {
//                isHaveMore = more;
//                if (!isHaveMore){
//                    showToast("到底了");
//                }
//                if (isRefresh){
//                    orderListAdater.setDatalist(dataList);
//                }else{
//                    orderListAdater.addData(dataList);
//                }
//                binding.demoSwiperefreshlayout.setRefreshing(false);
//            }
//        };
//
//        if(UITool.isMainThread()){
//            task.run();
//        }else{
//            getActivity().runOnUiThread(task);
//        }
//    }
}
