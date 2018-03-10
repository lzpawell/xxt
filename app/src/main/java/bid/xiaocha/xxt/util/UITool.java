package bid.xiaocha.xxt.util;

import android.app.Activity;
import android.os.Handler;
import android.os.Looper;

import com.android.tu.loadingdialog.LoadingDialog;

/**
 * Created by lzp on 2017/10/16.
 */

public class UITool {
    public static boolean isMainThread() {
        return Looper.getMainLooper().getThread() == Thread.currentThread();
    }
    public static LoadingDialog createLoadingDialog(Activity activity) {
        LoadingDialog.Builder  builder = new LoadingDialog.Builder(activity)
                .setMessage("加载中...")
                .setCancelable(false)
                .setCancelOutside(false)
                .setShowMessage(true);

        return builder.create();
    }

    public static void showLoadingDialog(final LoadingDialog dialog){
        Handler uiHandler = new Handler(Looper.getMainLooper());
        uiHandler.post(new Runnable() {
            @Override
            public void run() {
                dialog.show();
            }
        });
    }

    public static void dismissLoadingDialog(final LoadingDialog dialog){
        Handler uiHandler = new Handler(Looper.getMainLooper());
        uiHandler.post(new Runnable() {
            @Override
            public void run() {
                if (dialog.isShowing())
                    dialog.dismiss();
            }
        });
    }

}
