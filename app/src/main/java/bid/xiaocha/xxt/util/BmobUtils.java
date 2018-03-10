package bid.xiaocha.xxt.util;

import android.util.Log;

import cn.bmob.newsmssdk.BmobSMS;

/**
 * Created by 55039 on 2017/10/30.
 */

public class BmobUtils {
    public static void init(){
        Log.i("aowu","balalbalala");
        BmobSMS.initialize(App.getAppContext(),"fe0e98fd5c52e0e6089e20a259cf2d87");
    }
    //老司机备用 fe0e98fd5c52e0e6089e20a259cf2d87
    //校校通备用 fcca7ea738dc1ddd892f5fb7ee5e848a

}
