package bid.xiaocha.xxt.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

import bid.xiaocha.xxt.R;
import bid.xiaocha.xxt.model.UserEntity;

/**
 * Created by 55039 on 2017/10/15.
 */

public class CommonUtils {
    public static void showToast(String text){
        Toast.makeText(App.getAppContext(), text, Toast.LENGTH_SHORT).show();
    }
    public static String timeFormat(Date date){
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm");
        return df.format(date);
    }

    public static int dp2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

//    public static String getOddNum(){
//        final BmobUser user = BmobUser.getCurrentUser();
//        final SimpleDateFormat df = new SimpleDateFormat("yyyyMMddhhmmss");
//        return df.format(new Date()) + user.getUsername().substring(7, 11);
//    }
    public static String formatTime(long time){
        int second = (int) (time/1000);
        if (second == 0)
            return "1秒";
        int min = second/60;
        if(min == 0)
            return second+"秒";

        if(min < 60)
            return min + "分钟";

        if((min / 60) >= 24){
            int h = min / 60;
            int d = h / 24;
            return d + "天" + h +"小时";
        }else
            return min / 60 + "小时" + min % 60 + "分";
    }

    public static void phonecall(Context context, String mobile){
        Intent intent=new Intent();
        //激活源代码,添加intent对象
        intent.setAction("android.intent.action.CALL");
        //intent.addCategory("android.intent.category.DEFAULT");内部会自动添加类别，
        intent.setData(Uri.parse("tel:"+mobile));
        //激活Intent
        context.startActivity(intent);
    }
    public static double fotmatNumber(int digit,double number){
        BigDecimal b = new BigDecimal(number);
        return b.setScale(digit,BigDecimal.ROUND_HALF_UP).doubleValue();
    }
    public static String getTextFromView(TextView textView){
        return textView.getText().toString().trim();
    }
    public static String getTextFromViewCheckNotNull(TextView textView) throws CanNotNullExcetion {
        String text = textView.getText().toString().trim();
        if ("".equals(text))
            throw new CanNotNullExcetion();
        return textView.getText().toString().trim();
    }
    public static String getOddNum(){
        final UserEntity user = UserEntity.getCurrentUser();
        final SimpleDateFormat df = new SimpleDateFormat("yyyyMMddhhmmss");
        return df.format(new Date()) + user.getUserId().substring(7, 11);
    }
    public static class CanNotNullExcetion extends Exception{

    }

    public static void ShowPopupWindow(final Activity activity, View view, PopupWindow popupWindow) {

        // 一个自定义的布局，作为显示的内容


        popupWindow.setTouchable(true);

        popupWindow.setTouchInterceptor(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                Log.i("mengdd", "onTouch : ");

                return false;
                // 这里如果返回true的话，touch事件将被拦截
                // 拦截后 PopupWindow的onTouchEvent不被调用，这样点击外部区域无法dismiss
            }
        });

        // 如果不设置PopupWindow的背景，无论是点击外部区域还是Back键都无法dismiss弹框
        // 我觉得这里是API的一个bug


        // 设置好参数之后再show
        backgroundAlpha(0.6f,activity);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha(1f,activity);
            }
        });
        popupWindow.setAnimationStyle(R.style.mypopwindow_anim_style);
        popupWindow.showAtLocation(view, Gravity.CENTER,0,0);

    }

    public static void backgroundAlpha(float bgAlpha,Activity activity)
    {
        WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        activity.getWindow().setAttributes(lp);
    }

}
