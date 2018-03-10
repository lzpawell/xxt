package bid.xiaocha.xxt.util;

/**
 * Created by 55039 on 2017/12/12.
 */

public class PayUtil {
    public static void requestServePay(String orderId,OnPayResultListener listener){
        double money = 0;
        //上网查服务价格
        requestPay(money,listener);
    }
    public static void requestPay(double needMoney,OnPayResultListener listener){
        //TODO:getmoney判断
        listener.onPayResultSuccess();
    }
    public interface OnPayResultListener{
        void onPayResultSuccess();
        void onPayResultFailed(short result);
    }
}
