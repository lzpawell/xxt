package bid.xiaocha.xxt.presenter;

import bid.xiaocha.xxt.iview.IVerificationCodeView;
import bid.xiaocha.xxt.model.UserEntity;
import bid.xiaocha.xxt.util.VerificationCodeUtil;

import static bid.xiaocha.xxt.util.CommonUtils.showToast;

/**
 * Created by 55039 on 2017/10/13.
 */

public class VerificationPresenter implements IVerificationPresenter {

    private IVerificationCodeView view;

    public VerificationPresenter(IVerificationCodeView view){
        this.view = view;
    }

    @Override
    public void requestVerificationCode(final String userId) {
        view.onRequestVerification();
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (VerificationCodeUtil.requestVerificationCode(userId)){
                    UserEntity.getUserByUserId(userId, new UserEntity.OnGetUserByUserIdResult() {
                        @Override
                        public void getUserByIdResult(boolean result,UserEntity userEntity) {
                            if (result == false){
                                showToast("获取用户信息失败");
                            }
                            view.requestVerificationSucceeded();
                        }
                    });
                }else{
                    view.requestVerificationFailed();
                }
            }
        }).start();
    }
}
