package bid.xiaocha.xxt.util;

import android.util.Log;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import bid.xiaocha.xxt.model.LoginState;
import bid.xiaocha.xxt.service.INetService;
import bid.xiaocha.xxt.service.NetService;
import cn.bmob.newsmssdk.BmobSMS;
import cn.bmob.newsmssdk.exception.BmobException;
import cn.bmob.newsmssdk.listener.RequestSMSCodeListener;
import io.rong.imkit.RongIM;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by 55039 on 2017/10/13.
 */

public class VerificationCodeUtil {
    public static boolean requestVerificationCode(String userId){
        final Lock lock = new ReentrantLock();
        final Condition condition = lock.newCondition();
        final AtomicBoolean result = new AtomicBoolean(false);
        //调用bmob方法申请验证码， 在callback里面调用lock.unlock();
        BmobSMS.requestSMSCode(App.getAppContext(), userId, "注册与登陆", new RequestSMSCodeListener() {
            @Override
            public void done(Integer integer, BmobException e) {
                if (e == null) {
                    result.set(true);
                }else{
                    e.printStackTrace();
                    Log.i("BmobSMSsend",e.toString());
                }
                Log.i("testLock","111");
                lock.lock();
                Log.i("testLock","222");
                condition.signal();
                Log.i("testLock","333");
                lock.unlock();
                Log.i("testLock","444");
            }
        });
        Log.i("testLock","555");
        lock.lock();
        Log.i("testLock","666");
        condition.awaitUninterruptibly();
        Log.i("testLock","777");
        lock.unlock();
        Log.i("testLock","888");
        return result.get();
    }


    public static boolean checkOutVerificationCode(String userId, String verificationCode){
        final Boolean result = new Boolean(false);
        Call<LoginState> call =  NetService.getInstance().login(userId);
        try {
            Response<LoginState> response = call.execute();
            LoginState LoginState = response.body();
            if (LoginState.getState()== bid.xiaocha.xxt.model.LoginState.State.success){
                return true;
            }else{
                return false;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

}
