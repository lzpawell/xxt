package bid.xiaocha.xxt.adater;

import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;

import java.util.List;

import bid.xiaocha.xxt.BR;
import bid.xiaocha.xxt.R;
import bid.xiaocha.xxt.databinding.ListItemActiveOrderBinding;
import bid.xiaocha.xxt.databinding.PopupwindowConfrimBinding;
import bid.xiaocha.xxt.model.ActiveOrderEntity;
import bid.xiaocha.xxt.model.UserEntity;
import bid.xiaocha.xxt.presenter.IControlOrderPresenter;
import bid.xiaocha.xxt.ui.activity.ShowOrderDetailActivity;
import bid.xiaocha.xxt.util.CommonUtils;

import static bid.xiaocha.xxt.model.ActiveOrderEntity.CANCEL_STATE_NOT_CANCEL;
import static bid.xiaocha.xxt.model.ActiveOrderEntity.CANCEL_STATE_PUBLISHER_HAS_CANCEL;
import static bid.xiaocha.xxt.model.ActiveOrderEntity.CANCEL_STATE_RECEIVER_HAS_CANCEL;
import static bid.xiaocha.xxt.model.ActiveOrderEntity.NEED_SERVE;
import static bid.xiaocha.xxt.model.ActiveOrderEntity.OFFER_SERVE;
import static bid.xiaocha.xxt.model.ActiveOrderEntity.STATE_BEGIN_SERVE;
import static bid.xiaocha.xxt.model.ActiveOrderEntity.STATE_WAIT_AGREE_CREATE;
import static bid.xiaocha.xxt.model.ActiveOrderEntity.STATE_WAIT_FOR_CONFIRM;

/**
 * Created by lenovo-pc on 2017/12/1.
 */

public class ActiveOrderListAdater extends BaseAdapter{

    private List<ActiveOrderEntity> activeOrderList;
    private LayoutInflater layoutInflater;
    private Activity activity;
    private PopupWindow confrimPopupWindow;
    private PopupwindowConfrimBinding popupwindowBinding;
    private IControlOrderPresenter presenter;



    public ActiveOrderListAdater(Activity activity, List<ActiveOrderEntity> activeOrderList, IControlOrderPresenter presenter){
        this.activity = activity;
        this.activeOrderList = activeOrderList;
        this.layoutInflater = activity.getLayoutInflater();
        this.presenter = presenter;
        popupwindowBinding = DataBindingUtil.inflate(layoutInflater,R.layout.popupwindow_confrim,null,false);
        confrimPopupWindow = new PopupWindow(popupwindowBinding.getRoot(), ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT,true);
        popupwindowBinding.tvTitle.setText("提示");
        popupwindowBinding.btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confrimPopupWindow.dismiss();
            }
        });
    }
    public void updateList(List<ActiveOrderEntity> activeOrderList){
        this.activeOrderList = activeOrderList;
        notifyDataSetChanged();
    }
    @Override
    public int getCount() {
        return activeOrderList.size();
    }

    public void addDataList(List<ActiveOrderEntity> dataList){
        for (ActiveOrderEntity data:dataList){
            this.activeOrderList.add(data);
        }
        notifyDataSetChanged();
    }

    public void clearDataList(){
        activeOrderList.clear();
        notifyDataSetChanged();
    }
    public void removeFromDataList(int position){
        activeOrderList.remove(position);
        notifyDataSetChanged();
    }

    @Override
    public Object getItem(int position) {
        return activeOrderList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ListItemActiveOrderBinding binding;
        if (convertView == null) {
            binding = DataBindingUtil.inflate(layoutInflater, R.layout.list_item_active_order, parent, false);
        } else {
            binding = DataBindingUtil.getBinding(convertView);
        }

        View.OnClickListener listener = initOnClickListener(activeOrderList.get(position),position);
        binding.setVariable(BR.activeorder, activeOrderList.get(position));
        short state = activeOrderList.get(position).getState();
        short cancelState = activeOrderList.get(position).getCancelState();
        String servePublisherId = activeOrderList.get(position).getServePublisherId();
        String userId = UserEntity.getCurrentUser().getUserId();
        short serveType = activeOrderList.get(position).getServeType();

        binding.lyListItem.setOnClickListener(listener);
        binding.btnAccept.setOnClickListener(listener);
        binding.btnCancel.setOnClickListener(listener);
        binding.btnConfirm.setOnClickListener(listener);
        binding.btnConfirmCancel.setOnClickListener(listener);
        binding.btnRefuseCancel.setOnClickListener(listener);
        binding.btnConfirm.setEnabled(true);

        if (cancelState == CANCEL_STATE_PUBLISHER_HAS_CANCEL ) {
            if (userId.equals(servePublisherId)){
                binding.state.setText("等待对方确认取消");
                binding.btnAccept.setVisibility(View.GONE);
                binding.btnCancel.setVisibility(View.GONE);
                binding.btnConfirm.setVisibility(View.GONE);
                binding.btnConfirmCancel.setVisibility(View.GONE);
                binding.btnRefuseCancel.setVisibility(View.VISIBLE);
            }else{
                binding.state.setText("待你取消");
                binding.btnAccept.setVisibility(View.GONE);
                binding.btnCancel.setVisibility(View.GONE);
                binding.btnConfirm.setVisibility(View.GONE);
                binding.btnConfirmCancel.setVisibility(View.VISIBLE);
                binding.btnRefuseCancel.setVisibility(View.VISIBLE);
            }
        }

        if (cancelState == CANCEL_STATE_RECEIVER_HAS_CANCEL ) {
            if (!userId.equals(servePublisherId)){
                binding.state.setText("等待对方确认取消");
                binding.btnAccept.setVisibility(View.GONE);
                binding.btnCancel.setVisibility(View.GONE);
                binding.btnConfirm.setVisibility(View.GONE);
                binding.btnConfirmCancel.setVisibility(View.GONE);
                binding.btnRefuseCancel.setVisibility(View.VISIBLE);
            }else{
                binding.state.setText("待你取消");
                binding.btnAccept.setVisibility(View.GONE);
                binding.btnCancel.setVisibility(View.GONE);
                binding.btnConfirm.setVisibility(View.GONE);
                binding.btnConfirmCancel.setVisibility(View.VISIBLE);
                binding.btnRefuseCancel.setVisibility(View.VISIBLE);
            }
        }

        if (cancelState == CANCEL_STATE_NOT_CANCEL) {
            binding.btnCancel.setVisibility(View.VISIBLE);
            binding.btnConfirmCancel.setVisibility(View.GONE);
            binding.btnRefuseCancel.setVisibility(View.GONE);
            if (userId.equals(servePublisherId) && serveType == NEED_SERVE) {
                if (state == STATE_WAIT_AGREE_CREATE) {
                    binding.state.setText("待接受");
                    binding.btnAccept.setVisibility(View.VISIBLE);
                    binding.btnCancel.setVisibility(View.VISIBLE);
                    binding.btnConfirm.setVisibility(View.GONE);
                } else if (state == STATE_BEGIN_SERVE) {
                    binding.state.setText("等待对方完成");
                    binding.btnConfirm.setVisibility(View.VISIBLE);
                    binding.btnAccept.setVisibility(View.GONE);
                    binding.btnConfirm.setEnabled(false);
                    binding.btnCancel.setVisibility(View.VISIBLE);
                } else if (state == STATE_WAIT_FOR_CONFIRM) {
                    binding.state.setText("等待您的确认");
                    binding.btnConfirm.setVisibility(View.VISIBLE);
                    binding.btnAccept.setVisibility(View.GONE);
                }

            }
            if (userId.equals(servePublisherId) && serveType == OFFER_SERVE) {
                if (state == STATE_WAIT_AGREE_CREATE) {
                    binding.state.setText("待接受");
                    binding.btnAccept.setVisibility(View.VISIBLE);
                    binding.btnConfirm.setVisibility(View.GONE);
                } else if (state == STATE_BEGIN_SERVE) {
                    binding.state.setText("等待您完成");
                    binding.btnConfirm.setVisibility(View.GONE);
                    binding.btnAccept.setVisibility(View.VISIBLE);
                    binding.btnConfirm.setEnabled(true);
                } else if (state == STATE_WAIT_FOR_CONFIRM) {
                    binding.state.setText("等待对方确认");
                    binding.btnConfirm.setVisibility(View.GONE);
                    binding.btnAccept.setVisibility(View.GONE);
                }
            }
            if (!userId.equals(servePublisherId) && serveType == NEED_SERVE) {
                if (state == STATE_WAIT_AGREE_CREATE) {
                    binding.state.setText("等待对方接受");
                    binding.btnAccept.setVisibility(View.GONE);
                    binding.btnConfirm.setVisibility(View.GONE);
                } else if (state == STATE_BEGIN_SERVE) {
                    binding.state.setText("等待您完成");
                    binding.btnConfirm.setVisibility(View.VISIBLE);
                    binding.btnAccept.setVisibility(View.GONE);
                    binding.btnConfirm.setEnabled(true);
                } else if (state == STATE_WAIT_FOR_CONFIRM) {
                    binding.state.setText("等待对方确认");
                    binding.btnConfirm.setVisibility(View.GONE);
                    binding.btnAccept.setVisibility(View.GONE);
                }
            }
            if (!userId.equals(servePublisherId) && serveType == OFFER_SERVE) {
                if (state == STATE_WAIT_AGREE_CREATE) {
                    binding.state.setText("等待对方接受");
                    binding.btnAccept.setVisibility(View.GONE);
                    binding.btnConfirm.setVisibility(View.GONE);
                } else if (state == STATE_BEGIN_SERVE) {
                    binding.state.setText("等待对方完成");
                    binding.btnConfirm.setVisibility(View.VISIBLE);
                    binding.btnAccept.setVisibility(View.GONE);
                    binding.btnConfirm.setEnabled(false);
                } else if (state == STATE_WAIT_FOR_CONFIRM) {
                    binding.state.setText("等待您确认");
                    binding.btnConfirm.setVisibility(View.VISIBLE);
                    binding.btnConfirm.setEnabled(true);
                    binding.btnAccept.setVisibility(View.GONE);
                }
            }
        }
        return binding.getRoot();
    }

    public void updateListViewItem(ListView listView,int position,ActiveOrderEntity activeOrder){
        if (position<0)
            return;
        int firstVisiblePosition = listView.getFirstVisiblePosition();
        int lastVisiblePosition = listView.getLastVisiblePosition();
        Log.i("updateListViewItem",position+"  "+firstVisiblePosition+"   "+lastVisiblePosition);
        activeOrderList.set(position,activeOrder);
        if (position >= firstVisiblePosition && position <= lastVisiblePosition){
            View view = listView.getChildAt(position-firstVisiblePosition);
            getView(position,view,null);
        }
    }

    private View.OnClickListener initOnClickListener(final ActiveOrderEntity order, final int position) {
        final String orderId = order.getOrderId();
        final String userId = UserEntity.getCurrentUser().getUserId();
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.ly_list_item:
                        Intent intent = new Intent(activity, ShowOrderDetailActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("activeOrder",order.toString());
                        intent.putExtras(bundle);
                        activity.startActivity(intent);
                        break;
                    case R.id.btn_accept:
                        popupwindowBinding.tvContent.setText("你确定要接单吗");
                        popupwindowBinding.btnComfirm.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                confrimPopupWindow.dismiss();
                                presenter.acceptOrder(orderId,position);
                            }
                        });
                        CommonUtils.ShowPopupWindow(activity,v,confrimPopupWindow);
                        break;
                    case R.id.btn_confirm:
                        popupwindowBinding.tvContent.setText("你确定完成了吗");
                        popupwindowBinding.btnComfirm.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                confrimPopupWindow.dismiss();
                                if ((order.getServeType() == OFFER_SERVE && userId.equals(order.getServePublisherId()))
                                        ||(order.getServeType() == NEED_SERVE && userId.equals(order.getServeReceiverId())))
                                    presenter.finishOrder(orderId,position);
                                if ((order.getServeType() == NEED_SERVE && userId.equals(order.getServePublisherId()))
                                        ||(order.getServeType() == OFFER_SERVE && userId.equals(order.getServeReceiverId())))
                                    presenter.confrimOrder(orderId,position);
                            }
                        });
                        CommonUtils.ShowPopupWindow(activity,v,confrimPopupWindow);
                        break;
                    case R.id.btn_cancel:
                        popupwindowBinding.tvContent.setText("你确定要取消订单吗");
                        popupwindowBinding.btnComfirm.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                confrimPopupWindow.dismiss();
                                presenter.cancelOrder(orderId,position);
                            }
                        });
                        CommonUtils.ShowPopupWindow(activity,v,confrimPopupWindow);
                        break;
                    case R.id.btn_refuse_cancel:
                        popupwindowBinding.tvContent.setText("你确定要拒绝取消订单吗");
                        popupwindowBinding.btnComfirm.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                confrimPopupWindow.dismiss();
                                presenter.refuseCancelOrder(orderId,position);
                            }
                        });
                        CommonUtils.ShowPopupWindow(activity,v,confrimPopupWindow);
                        break;
                    case R.id.btn_confirm_cancel:
                        popupwindowBinding.tvContent.setText("你确定要取消订单吗");
                        popupwindowBinding.btnComfirm.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                confrimPopupWindow.dismiss();
                                presenter.cancelOrder(orderId,position);
                            }
                        });
                        CommonUtils.ShowPopupWindow(activity,v,confrimPopupWindow);
                        break;
                }
            }
        };
    }



}
