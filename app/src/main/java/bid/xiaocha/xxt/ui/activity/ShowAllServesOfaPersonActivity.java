package bid.xiaocha.xxt.ui.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AbsListView;

import java.util.ArrayList;

import bid.xiaocha.xxt.R;
import bid.xiaocha.xxt.adater.CommentListAdater;
import bid.xiaocha.xxt.databinding.ActivityShowAllServesOfaPersonBinding;
import bid.xiaocha.xxt.databinding.ActivityShowPersonalCommentBinding;
import bid.xiaocha.xxt.databinding.ContentShowAllServesOfaPersonBinding;
import bid.xiaocha.xxt.databinding.ContentShowPersonalCommentBinding;
import bid.xiaocha.xxt.model.CommentEntity;
import bid.xiaocha.xxt.presenter.IShowPersonalCommentPersenter;
import bid.xiaocha.xxt.presenter.ShowPersonalCommentPresenter;
import bid.xiaocha.xxt.util.App;

public class ShowAllServesOfaPersonActivity extends BaseActivity {
    private ActivityShowAllServesOfaPersonBinding activityBinding;
    private ContentShowAllServesOfaPersonBinding contentBinding;
    private String userId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityBinding = DataBindingUtil.setContentView(this,R.layout.activity_show_all_serves_ofa_person);
        contentBinding = activityBinding.content;
        initPresenter();
        initView();

    }

    private void initPresenter() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            userId = bundle.getString("userId");
        }
        if (userId == null || userId.equals("")) {

        }else{

        }
    }

    private void initView() {
        setSupportActionBar(activityBinding.toolbar);
        getSupportActionBar().setTitle("他的服务");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

}
