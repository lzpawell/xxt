package bid.xiaocha.xxt.presenter;

import android.accounts.NetworkErrorException;
import android.util.Log;

import bid.xiaocha.xxt.iview.ILoginView;
import bid.xiaocha.xxt.model.LoginState;
import bid.xiaocha.xxt.model.UserEntity;
import bid.xiaocha.xxt.service.NetService;
import bid.xiaocha.xxt.util.App;
import bid.xiaocha.xxt.rongCloud.RongManager;
import bid.xiaocha.xxt.util.JwtUtil;
import cn.bmob.newsmssdk.BmobSMS;
import cn.bmob.newsmssdk.exception.BmobException;
import cn.bmob.newsmssdk.listener.VerifySMSCodeListener;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by 55039 on 2017/10/12.
 */

public class LoginPresenter implements ILoginPresenter {

    private ILoginView view;

    public LoginPresenter(ILoginView loginView){
        this.view = loginView;
    }

    @Override
    public void loginByPassword(String userId, String password) {
        view.showLoading();


        LoginResult result = LoginResult.FAILED_NETWORK_ERROR;

        view.dismissLoading();
        view.showLoginResult(result);
    }

    @Override
    public void loginByVerificationCode(final String userId, String verificationCode) {
        view.showLoading();
        BmobSMS.verifySmsCode(App.getAppContext(), userId, verificationCode, new VerifySMSCodeListener() {
            @Override
            public void done(BmobException e) {
                if(e == null){
                    Call<LoginState> call =  NetService.getInstance().login(userId);
                    call.enqueue(new Callback<LoginState>() {
                        @Override
                        public void onResponse(Call<LoginState> call, Response<LoginState> response) {
                            if (response.code() == 200) {
                                LoginResult result;
                                LoginState loginState = response.body();
                                Log.i("loginState", loginState.toString());
                                if (loginState.getState() == bid.xiaocha.xxt.model.LoginState.State.success) {
                                    result = LoginResult.SUCCESS;
                                    UserEntity.getUserByUserId(userId, new UserEntity.OnGetUserByUserIdResult() {
                                        @Override
                                        public void getUserByIdResult(boolean result, UserEntity userEntity) {
                                            if (result) {
                                                UserEntity.setCurrentUser(userEntity);
                                            }
                                        }
                                    });
                                    UserEntity userEntity = new UserEntity();
                                    userEntity.setUserId(userId);
                                    userEntity.setNickName("nickName");
                                    userEntity.setToken(loginState.getRongCloudToken());
                                    UserEntity.setCurrentUser(userEntity);
                                    RongManager.getInstance().connect(loginState.getRongCloudToken());
                                    JwtUtil.saveJwt(loginState.getJwt());
                                } else if (loginState.getState() == bid.xiaocha.xxt.model.LoginState.State.serverError) {
                                    result = LoginResult.FAILED_UNKNOWN_ERROR;
                                } else {
                                    result = LoginResult.FAILED_NETWORK_ERROR;
                                }
                                view.dismissLoading();
                                view.showLoginResult(result);
                            }else {
                                Log.i("aaa",response.code()+"");
                                onFailure(call,new Throwable(response.code()+""));
                            }
                        }

                        @Override
                        public void onFailure(Call<LoginState> call, Throwable t) {
                            view.dismissLoading();
                            t.printStackTrace();
                            Log.i("loginState","failure"+t.getMessage());
                            if(t instanceof NetworkErrorException){
                                view.showLoginResult(LoginResult.FAILED_NETWORK_ERROR);
                            }else{
                                view.showLoginResult(LoginResult.FAILED_UNKNOWN_ERROR);
                            }
                        }
                    });
                }else{
                    view.dismissLoading();
                    view.showLoginResult(LoginResult.FAILED_CHECK_OUT_ERROR);
                }
            }
        });







    }

}
