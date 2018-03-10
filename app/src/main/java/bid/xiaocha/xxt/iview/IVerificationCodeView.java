package bid.xiaocha.xxt.iview;

/**
 * Created by 55039 on 2017/10/13.
 */

public interface IVerificationCodeView {
    void onRequestVerification();

    void requestVerificationSucceeded();

    void requestVerificationFailed();
}
