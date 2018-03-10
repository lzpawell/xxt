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
import bid.xiaocha.xxt.rongCloud.Message.ConfirmOrderMessage;
import bid.xiaocha.xxt.rongCloud.Message.FinishOrderMessage;
import bid.xiaocha.xxt.ui.activity.ShowOrderDetailActivity;
import io.rong.imkit.model.ProviderTag;
import io.rong.imkit.model.UIMessage;
import io.rong.imkit.widget.provider.IContainerItemProvider;
import io.rong.imlib.model.Message;

/**
 * Created by 55039 on 2017/11/13.
 */
@ProviderTag(messageContent = ConfirmOrderMessage.class)
public class ConfirmOrderMessageItemProvider extends IContainerItemProvider.MessageProvider<ConfirmOrderMessage> {
    @Override
    public View newView(Context context, ViewGroup viewGroup) {
        ChatItemRequestCreateOrderMessageBinding dataBinding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.chat_item_request_create_order_message,viewGroup,false);
        return dataBinding.getRoot();
    }
    @Override
    public void bindView(View view, int i, ConfirmOrderMessage confirmOrderMessage, UIMessage uiMessage) {
        ChatItemRequestCreateOrderMessageBinding dataBinding = DataBindingUtil.bind(view);
        String nickname = confirmOrderMessage.getSenderNickname();
        int serveType = confirmOrderMessage.getServeType();
        String text = "";
        if (uiMessage.getMessageDirection() == Message.MessageDirection.SEND) {//消息方向，自己发送的
            dataBinding.text.setBackgroundResource(io.rong.imkit.R.drawable.rc_ic_bubble_right);
            text += "你已成功确认订单完成";
        } else {
            dataBinding.text.setBackgroundResource(io.rong.imkit.R.drawable.rc_ic_bubble_left);
            text += "对方已确认订单完成，钱已经放入你的余额";
        }
        dataBinding.text.setText(text);
    }

    @Override
    public Spannable getContentSummary(ConfirmOrderMessage confirmOrderMessage) {
        if (confirmOrderMessage.getSenderId().equals(UserEntity.getCurrentUser().getUserId()))
            return new SpannableString("你已成功确认订单完成");
        else
            return new SpannableString("对方已确认订单完成");
    }

    @Override
    public void onItemClick(View view, int i, ConfirmOrderMessage confirmOrderMessage, UIMessage uiMessage) {
        Intent intent = new Intent(view.getContext(), ShowOrderDetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("orderId",confirmOrderMessage.getOrderId());
        intent.putExtras(bundle);
        view.getContext().startActivity(intent);
    }

}
