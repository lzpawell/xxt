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
import java.util.List;

import bid.xiaocha.xxt.R;
import bid.xiaocha.xxt.adater.CommentListAdater;
import bid.xiaocha.xxt.databinding.ActivityShowPersonalCommentBinding;
import bid.xiaocha.xxt.databinding.ContentShowPersonalCommentBinding;
import bid.xiaocha.xxt.iview.IshowPersonalCommentActivityView;
import bid.xiaocha.xxt.model.CommentEntity;
import bid.xiaocha.xxt.presenter.IShowPersonalCommentPersenter;
import bid.xiaocha.xxt.presenter.ShowPersonalCommentPresenter;
import bid.xiaocha.xxt.presenter.ShowUserInfoPrenster;
import bid.xiaocha.xxt.util.App;

import static bid.xiaocha.xxt.util.CommonUtils.showToast;

public class ShowPersonalCommentActivity extends BaseActivity implements IshowPersonalCommentActivityView{
    private ActivityShowPersonalCommentBinding activityBinding;
    private ContentShowPersonalCommentBinding contentBinding;
    private String userId;
    private boolean isHaveMore;
    private IShowPersonalCommentPersenter presenter;
    private CommentListAdater adater;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityBinding = DataBindingUtil.setContentView(this,R.layout.activity_show_personal_comment);
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
            presenter = new ShowPersonalCommentPresenter(this,null);
        }else{
            presenter = new ShowPersonalCommentPresenter(this,userId);
        }


    }

    private void initView() {
        setSupportActionBar(activityBinding.toolbar);
        getSupportActionBar().setTitle("他的评论");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        presenter.getPersonalCommentByPage(0,"null");
        contentBinding.lvShowPersonalComment.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (isHaveMore){
                    if (firstVisibleItem + visibleItemCount == totalItemCount-5){
                        int nextPageNum = (totalItemCount-1)/ App.NUM_IN_A_PAGE+2;
                        contentBinding.lyFooter.getRoot().setVisibility(View.VISIBLE);
                        presenter.getPersonalCommentByPage(nextPageNum,"null");
                    }
                }else{
                    return;
                }
            }
        });
        adater = new CommentListAdater(new ArrayList<CommentEntity>(),getLayoutInflater());
    }

    @Override
    public void showPersonalCommentSuccess(List<CommentEntity> commentList, boolean isHaveMore) {
        if (commentList == null){
            showToast("列表为空");
        }else {
            this.isHaveMore = isHaveMore;
            adater.addDataList(commentList);
        }
        contentBinding.lyFooter.getRoot().setVisibility(View.INVISIBLE);
    }

    @Override
    public void showPersonalCommentFail() {
        showToast("获取数据出错");
        contentBinding.lyFooter.getRoot().setVisibility(View.INVISIBLE);
    }
}
