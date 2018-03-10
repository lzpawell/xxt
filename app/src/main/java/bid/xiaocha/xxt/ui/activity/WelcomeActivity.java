package bid.xiaocha.xxt.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.util.List;

import bid.xiaocha.xxt.R;
import bid.xiaocha.xxt.model.AddressEntity;
import bid.xiaocha.xxt.model.UserEntity;
import bid.xiaocha.xxt.rongCloud.RongManager;

import static bid.xiaocha.xxt.util.CommonUtils.showToast;

public class WelcomeActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        UserEntity userEntity = UserEntity.getCurrentUser();
        if (userEntity != null){
            UserEntity.getUserByUserId(userEntity.getUserId(), new UserEntity.OnGetUserByUserIdResult() {
                @Override
                public void getUserByIdResult(boolean result,UserEntity userEntity) {
                    if (result == false){
                        showToast("获取用户信息失败");
                    }else{
                        UserEntity.setCurrentUser(userEntity);
                    }
                    RongManager.getInstance().connect(UserEntity.getCurrentUser().getToken());
                    startActivity(new Intent(WelcomeActivity.this,MainActivity.class));
                    finish();
                }
            });
            AddressEntity.getAllAddressFromServices(userEntity.getUserId(), new AddressEntity.OnGetAddressByUserIdResult() {
                @Override
                public void getAddressByIdResult(boolean result, List<AddressEntity> list) {
                    if (result == false){
                        showToast("获取地址信息失败");
                    }
                }
            });
        }else {
            startActivity(new Intent(this,LoginRegisterActivity.class));
            finish();
        }
    }
}