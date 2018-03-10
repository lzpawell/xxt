package bid.xiaocha.xxt.util;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import java.util.List;

import bid.xiaocha.xxt.model.UserEntity;
import cn.bmob.newsmssdk.BmobSMS;

import static bid.xiaocha.xxt.util.CommonUtils.showToast;

/**
 * Created by ssx on 2017/10/11.
 */

public class App extends Application{
    private static Context appContext;
    public final static int NUM_IN_A_PAGE = 10;
    public static Context getAppContext() {
        return appContext;
    }
    private static SharedPreferences sharedPreferences;
    public static SharedPreferences getSharedPreferences(){
        return sharedPreferences;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        appContext = this.getApplicationContext();
        sharedPreferences = getSharedPreferences("settings",MODE_PRIVATE);

//        UserEntity userEntity = new UserEntity();
//        userEntity.setUserId("12345678910");
//        userEntity.setToken("VxLD1RoUAjrAM6cajC4i8wC7qhz5RaJHWHrTMMSXZ75QD0y8lm8hKKBGae/GGX7hOX7wRmv8slgQNhNm6RbEmKJrach+8iHy");
//        UserEntity.setUserEntity(userEntity);
        if(shouldInit()){
            try{
                Class.forName("bid.xiaocha.xxt.util.DataStore");
                BmobUtils.init();
            }catch (Exception e){
                e.printStackTrace();

            }

        }
    }



    private boolean shouldInit() {
        ActivityManager am = ((ActivityManager) getSystemService(Context.ACTIVITY_SERVICE));
        List<ActivityManager.RunningAppProcessInfo> processInfos = am.getRunningAppProcesses();
        String mainProcessName = getPackageName();
        int myPid = android.os.Process.myPid();
        for (ActivityManager.RunningAppProcessInfo info : processInfos) {
            if (info.pid == myPid && mainProcessName.equals(info.processName)) {
                return true;
            }
        }
        return false;
    }
}
