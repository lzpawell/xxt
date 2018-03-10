package bid.xiaocha.xxt.ui.fragment;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import bid.xiaocha.xxt.R;
import bid.xiaocha.xxt.databinding.FragmentMainBinding;
import bid.xiaocha.xxt.ui.activity.ShowNeedServeActivity;
import bid.xiaocha.xxt.ui.activity.ShowOfferServeActivity;

public class MainFragment extends Fragment implements View.OnClickListener {


    private FragmentMainBinding binding;

    public static MainFragment getFragment(){
        MainFragment fragment = new MainFragment();
        return fragment;
    }

    public MainFragment(){

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(savedInstanceState != null){

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_main, container, false);
        initView();
        return binding.getRoot();
    }

    private void initView() {
        binding.btnShowNeedServe.setOnClickListener(this);
        binding.btnShowOfferServe.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_show_need_serve:
                startActivity(new Intent(getActivity(),ShowNeedServeActivity.class));
                break;
            case R.id.btn_show_offer_serve:
                startActivity(new Intent(getActivity(),ShowOfferServeActivity.class));
                break;
        }
    }
}
