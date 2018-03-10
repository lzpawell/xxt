package bid.xiaocha.xxt.ui.fragment;

import android.content.Intent;
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
import bid.xiaocha.xxt.databinding.FragmentServiceManagerBinding;
import bid.xiaocha.xxt.ui.activity.CreateNeedServeActivity;
import bid.xiaocha.xxt.ui.activity.CreateOfferServeActivity;

public class ServiceManagerFragment extends Fragment implements View.OnClickListener {

    private FragmentServiceManagerBinding binding;
    private NeedServeManagerFragment needServeManagerFragment = new NeedServeManagerFragment();
    private OfferServeManagerFragment offerServeManagerFragment = new OfferServeManagerFragment();
    private FragmentAdater adater;
    private TabLayout tabs;

    public static ServiceManagerFragment getFragment(){
        ServiceManagerFragment fragment = new ServiceManagerFragment();
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_service_manager, container, false);
        initView();
        return binding.getRoot();
    }
    private void initView() {
        setupViewPager();
        binding.lyCreateServe.setOnClickListener(this);
    }

    private void setupViewPager() {
        tabs = binding.tabs;
        ViewPager viewPager = binding.viewpager;
        List<String> titles = new ArrayList<>();
        titles.add("我需要服务");
        titles.add("我能提供服务");
        List<Fragment> fragmentList = new ArrayList<>();
        fragmentList.add(needServeManagerFragment);
        fragmentList.add(offerServeManagerFragment);
        tabs.addTab(tabs.newTab().setText(titles.get(0)));
        tabs.addTab(tabs.newTab().setText(titles.get(1)));
        adater = new FragmentAdater(getFragmentManager(),fragmentList,titles);
        viewPager.setAdapter(adater);
        tabs.setupWithViewPager(viewPager);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ly_create_serve:
                if (tabs.getSelectedTabPosition() == 0) {
                    startActivity(new Intent(getActivity(), CreateNeedServeActivity.class));
                }else if (tabs.getSelectedTabPosition() ==1){
                    startActivity(new Intent(getActivity(), CreateOfferServeActivity.class));
                }
                break;
        }
    }
}
