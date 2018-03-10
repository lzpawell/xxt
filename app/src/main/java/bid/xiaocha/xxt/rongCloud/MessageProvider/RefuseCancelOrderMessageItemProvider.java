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
import bid.xiaocha.xxt.rongCloud.Message.RefuseCancelOrderMessage;
import bid.xiaocha.xxt.ui.activity.ShowOrderDetailActivity;
import io.rong.imkit.model.ProviderTag;
import io.rong.imkit.model.UIMessage;
import io.rong.imkit.widget.provider.IContainerItemProvider;
import io.rong.imlib.model.Message;

/**
 * Created by 55039 on 2017/11/13.
 */
@ProviderTag(messageContent = RefuseCancelOrderMessage.class)
public class RefuseCancelOrderMessageItemProvider extends IContainerItemProvider.MessageProvider<RefuseCancelOrderMessage> {
    @Override
    public View newView(Context context, ViewGroup viewGroup) {
        ChatItemRequestCreateOrderMessageBinding dataBinding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.chat_item_request_create_order_message,viewGroup,false);
        return dataBinding.getRoot();
    }
    @Override
    public void bindView(View view, int i, RefuseCancelOrderMessage refuseCancelOrderMessage, UIMessage uiMessage) {
        ChatItemRequestCreateOrderMessageBinding dataBinding = DataBindingUtil.bind(view);
        String nickname = refuseCancelOrderMessage.getSenderNickname();
        int serveType = refuseCancelOrderMessage.getServeType();
        if (uiMessage.getMessageDirection() == Message.MessageDirection.SEND) {//消息方向，自己发送的
            dataBinding.text.setBackgroundResource(io.rong.imkit.R.drawable.rc_ic_bubble_right);
        } else {
            dataBinding.text.setBackgroundResource(io.rong.imkit.R.drawable.rc_ic_bubble_left);
        }
        String text = "订单已恢复，可点击查看详情";
        dataBinding.text.setText(text);
    }

    @Override
    public Spannable getContentSummary(RefuseCancelOrderMessage refuseCancelOrderMessage) {
        return new SpannableString("订单已恢复");
    }

    @Override
    public void onItemClick(View view, int i, RefuseCancelOrderMessage refuseCancelOrderMessage, UIMessage uiMessage) {
        Intent intent = new Intent(view.getContext(),ShowOrderDetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("orderId",refuseCancelOrderMessage.getOrderId());
        intent.putExtras(bundle);
        view.getContext().startActivity(intent);
    }

}
