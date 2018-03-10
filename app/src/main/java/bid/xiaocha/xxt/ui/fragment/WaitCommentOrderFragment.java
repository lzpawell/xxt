package bid.xiaocha.xxt.ui.fragment;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import bid.xiaocha.xxt.R;
import bid.xiaocha.xxt.databinding.FragmentWaitCommentOrderBinding;


public class WaitCommentOrderFragment extends Fragment {

    private FragmentWaitCommentOrderBinding binding;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_wait_comment_order,container,false);
        initPresenter();
        initView();
        return binding.getRoot();
    }

    private void initView() {

    }

    private void initPresenter() {

    }


}
