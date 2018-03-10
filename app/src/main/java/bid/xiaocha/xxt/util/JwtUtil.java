package bid.xiaocha.xxt.util;

import android.content.SharedPreferences;

/**
 * Created by 55039 on 2017/11/28.
 */

public class JwtUtil {
    public static void saveJwt(String jwt){
        SharedPreferences.Editor edit = App.getSharedPreferences().edit();
        edit.putString("jwt",jwt);
        edit.commit();
    }
    public static String getJwt(){
        return App.getSharedPreferences().getString("jwt","");
    }
}
