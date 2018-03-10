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
import bid.xiaocha.xxt.rongCloud.Message.AcceptOrderMessage;
import bid.xiaocha.xxt.ui.activity.ShowOrderDetailActivity;
import io.rong.imkit.model.ProviderTag;
import io.rong.imkit.model.UIMessage;
import io.rong.imkit.widget.provider.IContainerItemProvider;
import io.rong.imlib.model.Message;

import static bid.xiaocha.xxt.model.ActiveOrderEntity.NEED_SERVE;

/**
 * Created by 55039 on 2017/11/13.
 */
@ProviderTag(messageContent = AcceptOrderMessage.class)
public class AcceptOrderMessageItemProvider extends IContainerItemProvider.MessageProvider<AcceptOrderMessage> {
    @Override
    public View newView(Context context, ViewGroup viewGroup) {
        ChatItemRequestCreateOrderMessageBinding dataBinding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.chat_item_request_create_order_message,viewGroup,false);
        return dataBinding.getRoot();
    }
    @Override
    public void bindView(View view, int i, AcceptOrderMessage acceptOrderMessage, UIMessage uiMessage) {
        ChatItemRequestCreateOrderMessageBinding dataBinding = DataBindingUtil.bind(view);
        String nickname = acceptOrderMessage.getSenderNickname();
        int serveType = acceptOrderMessage.getServeType();
        String text = "";
        if (uiMessage.getMessageDirection() == Message.MessageDirection.SEND) {//消息方向，自己发送的
            dataBinding.text.setBackgroundResource(io.rong.imkit.R.drawable.rc_ic_bubble_right);
            if (serveType == NEED_SERVE){
                text += "请耐心等待对方完成订单";
            }else{
                text += "你已成功接单，快去完成订单吧";
            }
        } else {
            if (serveType == NEED_SERVE){
                text += "对方已接单，快去完成订单吧";
            }else{
                text += "请耐心等待对方完成订单";
            }
            dataBinding.text.setBackgroundResource(io.rong.imkit.R.drawable.rc_ic_bubble_left);
        }
        dataBinding.text.setText(text);
    }

    @Override
    public Spannable getContentSummary(AcceptOrderMessage acceptOrderMessage) {
        int serveType = acceptOrderMessage.getServeType();
        if (acceptOrderMessage.getSenderId().equals(UserEntity.getCurrentUser().getUserId()))
            return new SpannableString("请耐心等待对方完成订单");
        else {
            if (serveType == NEED_SERVE){
                return new SpannableString("对方已接单");
            }else{
                return new SpannableString("你已成功接单");
            }
        }
    }

    @Override
    public void onItemClick(View view, int i, AcceptOrderMessage acceptOrderMessage, UIMessage uiMessage) {
        Intent intent = new Intent(view.getContext(), ShowOrderDetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("orderId",acceptOrderMessage.getOrderId());
        intent.putExtras(bundle);
        view.getContext().startActivity(intent);
    }

}
