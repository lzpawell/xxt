package bid.xiaocha.xxt.rongCloud.MessageProvider;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import bid.xiaocha.xxt.R;
import bid.xiaocha.xxt.databinding.ChatItemRequestCreateOrderMessageBinding;
import bid.xiaocha.xxt.model.UserEntity;
import bid.xiaocha.xxt.rongCloud.Message.CancelOrderMessage;
import bid.xiaocha.xxt.rongCloud.Message.FinishOrderMessage;
import bid.xiaocha.xxt.ui.activity.ShowOrderDetailActivity;
import io.rong.imkit.model.ProviderTag;
import io.rong.imkit.model.UIMessage;
import io.rong.imkit.widget.provider.IContainerItemProvider;
import io.rong.imlib.model.Message;

/**
 * Created by 55039 on 2017/11/13.
 */
@ProviderTag(messageContent = CancelOrderMessage.class)
public class CancelOrderMessageItemProvider extends IContainerItemProvider.MessageProvider<CancelOrderMessage> {
    @Override
    public View newView(Context context, ViewGroup viewGroup) {
        ChatItemRequestCreateOrderMessageBinding dataBinding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.chat_item_request_create_order_message,viewGroup,false);
        return dataBinding.getRoot();
    }
    @Override
    public void bindView(View view, int i, CancelOrderMessage cancelOrderMessage, UIMessage uiMessage) {
        ChatItemRequestCreateOrderMessageBinding dataBinding = DataBindingUtil.bind(view);
        String nickname = cancelOrderMessage.getSenderNickname();
        int serveType = cancelOrderMessage.getServeType();
        String text = "";
        if (uiMessage.getMessageDirection() == Message.MessageDirection.SEND) {//消息方向，自己发送的
            dataBinding.text.setBackgroundResource(io.rong.imkit.R.drawable.rc_ic_bubble_right);
            if (cancelOrderMessage.getIsReallyCancel() == CancelOrderMessage.REALLY_CANCEL)
                text += "你已确认取消订单";
            else
                text += "订单处于待取消状态，待对方确认取消";
        } else {
            dataBinding.text.setBackgroundResource(io.rong.imkit.R.drawable.rc_ic_bubble_left);
            if (cancelOrderMessage.getIsReallyCancel() == CancelOrderMessage.REALLY_CANCEL)
                text += "对方已确认取消订单";
            else
                text += "订单处于待取消状态，请确认是否取消订单，点击查看详情";
        }
        dataBinding.text.setText(text);
    }

    @Override
    public Spannable getContentSummary(CancelOrderMessage cancelOrderMessage) {
        if (cancelOrderMessage.getIsReallyCancel() == CancelOrderMessage.REALLY_CANCEL)
            return new SpannableString("订单已取消");
        else
            return new SpannableString("订单处于待取消状态");

    }

    @Override
    public void onItemClick(View view, int i, CancelOrderMessage cancelOrderMessage, UIMessage uiMessage) {
        Intent intent = new Intent(view.getContext(), ShowOrderDetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("orderId",cancelOrderMessage.getOrderId());
        intent.putExtras(bundle);
        view.getContext().startActivity(intent);
    }
}
