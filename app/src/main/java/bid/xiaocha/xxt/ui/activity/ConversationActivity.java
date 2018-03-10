package bid.xiaocha.xxt.ui.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import java.util.Locale;

import bid.xiaocha.xxt.R;
import bid.xiaocha.xxt.databinding.ActivityConversationBinding;
import bid.xiaocha.xxt.model.UserEntity;
import io.rong.imkit.fragment.ConversationFragment;
import io.rong.imlib.model.Conversation;

public class ConversationActivity extends FragmentActivity {

    /*目标 Id*/
    private String targetUserId;
    /*会话类型*/
    private Conversation.ConversationType mConversationType;
    private ActivityConversationBinding activityBinding;
    private String title;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityBinding = DataBindingUtil.setContentView(this,R.layout.activity_conversation);

        Uri data = getIntent().getData();

        getIntentDate(data);
        initView();
    }

    private void initView() {
        activityBinding.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        if (title.equals("")) {
            activityBinding.tvTitle.setText("nickName");
            UserEntity.getUserByUserId(targetUserId, new UserEntity.OnGetUserByUserIdResult() {
                @Override
                public void getUserByIdResult(boolean result, UserEntity userEntity) {
                    if (result){
                        activityBinding.tvTitle.setText(userEntity.getNickName());
                    }
                }
            });
        }else{
            activityBinding.tvTitle.setText(title);
        }
    }


    /**
     * 展示如何从 Intent 中得到 融云会话页面传递的 Uri
     */
    private void getIntentDate(Uri data) {

        targetUserId = data.getQueryParameter("targetId");
        title = data.getQueryParameter("title");
        mConversationType = Conversation.ConversationType.valueOf(data.getLastPathSegment().toUpperCase(Locale.getDefault()));
        enterFragment(mConversationType, targetUserId);
    }

    /**
     * 加载会话页面 ConversationFragment
     *
     * @param mConversationType 会话类型
     * @param targetUserId 目标 Id
     */
    private void enterFragment(Conversation.ConversationType mConversationType, String targetUserId) {

        ConversationFragment fragment = (ConversationFragment) getSupportFragmentManager().findFragmentById(R.id.conversation);

        Uri uri = Uri.parse("rong://" + getApplicationInfo().packageName).buildUpon()
                .appendPath("conversation").appendPath(mConversationType.getName().toLowerCase())
                .appendQueryParameter("targetId", targetUserId).build();

        fragment.setUri(uri);
    }


}
