package bid.xiaocha.xxt.util;

import android.os.Environment;
import android.util.Log;

import java.io.File;

/**
 * Created by lzp on 2016/12/6.
 */

public class DataStore {
    public static String APP_PATH;
    public static String USER_PATH;
    static{
        APP_PATH = Environment.getExternalStorageDirectory().getPath() + File.separator + "xxt";
        File file = new File(APP_PATH);
        if(file.exists() == false){
            file.mkdir();
        }
        USER_PATH=APP_PATH + File.separator + "user";
        file = new File(APP_PATH + File.separator + "user");
        if(file.exists() == false){
            file.mkdir();
        }
        Log.i("aaaaa","ccccccc");
    }
}
