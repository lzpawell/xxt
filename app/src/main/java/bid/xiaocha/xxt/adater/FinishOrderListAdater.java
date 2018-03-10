package bid.xiaocha.xxt.adater;


import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
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
import bid.xiaocha.xxt.databinding.ListItemFinishOrderBinding;
import bid.xiaocha.xxt.databinding.PopupwindowAppraiseBinding;
import bid.xiaocha.xxt.model.ActiveOrderEntity;
import bid.xiaocha.xxt.model.FinishedOrderEntity;
import bid.xiaocha.xxt.model.UserEntity;
import bid.xiaocha.xxt.presenter.ControlOrderPresenter;
import bid.xiaocha.xxt.rongCloud.RongManager;
import bid.xiaocha.xxt.ui.activity.ShowOrderDetailActivity;
import bid.xiaocha.xxt.ui.views.StarAppraiseView;
import bid.xiaocha.xxt.util.CommonUtils;

import static bid.xiaocha.xxt.model.ActiveOrderEntity.NEED_SERVE;
import static bid.xiaocha.xxt.model.ActiveOrderEntity.OFFER_SERVE;
import static bid.xiaocha.xxt.model.ActiveOrderEntity.STATE_DEMANDER_HAVE_APPRAISED;
import static bid.xiaocha.xxt.model.ActiveOrderEntity.STATE_HAVE_CANCELED;
import static bid.xiaocha.xxt.model.ActiveOrderEntity.STATE_HAVE_FINISHED;
import static bid.xiaocha.xxt.model.ActiveOrderEntity.STATE_PROVIDER_HAVE_APPRAISED;
import static bid.xiaocha.xxt.model.ActiveOrderEntity.STATE_WAIT_FOR_APPRAISE;
import static bid.xiaocha.xxt.util.CommonUtils.showToast;

/**
 * Created by lenovo-pc on 2017/12/3.
 */

public class FinishOrderListAdater extends BaseAdapter{

    private List<FinishedOrderEntity> finishOrderList;
    private Activity activity;
    private LayoutInflater layoutInflater;
    public SwipeRefreshLayout.OnRefreshListener onRefreshListener;
    private PopupwindowAppraiseBinding appraiseBinding;
    private PopupWindow appraisePopupWindow;
    private ControlOrderPresenter controlOrderPresenter;
    public FinishOrderListAdater(Activity activity, List<FinishedOrderEntity> finishOrderList, ControlOrderPresenter controlOrderPresenter){
        this.finishOrderList = finishOrderList;
        this.activity = activity;
        this.layoutInflater = activity.getLayoutInflater();
        this.controlOrderPresenter = controlOrderPresenter;
        initAppraisePopupUpwindow();
    }

    private void initAppraisePopupUpwindow() {
        appraiseBinding = DataBindingUtil.inflate(layoutInflater,R.layout.popupwindow_appraise,null,false);
        appraiseBinding.starAppraise.setOnRateChangeListener(new StarAppraiseView.OnRateChangeListener() {
            @Override
            public void onRateChange(int rate) {
                appraiseBinding.tvAppraisePoint.setText(rate+"分");
            }
        });
        appraisePopupWindow = new PopupWindow(appraiseBinding.getRoot(), ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT,true);
    }

    public void setOnRefreshListener(SwipeRefreshLayout.OnRefreshListener onRefreshListener){
        this.onRefreshListener = onRefreshListener;
    }

    public void updateList(List<FinishedOrderEntity> finishOrderList){
        this.finishOrderList = finishOrderList;
        notifyDataSetChanged();
    }
    @Override
    public int getCount() {
        return finishOrderList.size();
    }

    public void addDataList(List<FinishedOrderEntity> dataList){
        for (FinishedOrderEntity data:dataList){
            this.finishOrderList.add(data);
        }
        notifyDataSetChanged();
    }

    public void clearDataList(){
        finishOrderList.clear();
        notifyDataSetChanged();
    }
    public void updateListViewItem(ListView listView, int position, FinishedOrderEntity finishedOrderEntity){
        if (position<0)
            return;
        int firstVisiblePosition = listView.getFirstVisiblePosition();
        int lastVisiblePosition = listView.getLastVisiblePosition();
        finishOrderList.set(position,finishedOrderEntity);
        if (position >= firstVisiblePosition && position <= lastVisiblePosition){
            View view = listView.getChildAt(position-firstVisiblePosition);
            getView(position,view,null);
        }
    }
    @Override
    public Object getItem(int position) {
        return finishOrderList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ListItemFinishOrderBinding binding;
        if (convertView == null) {
            binding = DataBindingUtil.inflate(layoutInflater, R.layout.list_item_finish_order, parent, false);
        } else {
            binding = DataBindingUtil.getBinding(convertView);
        }
        View.OnClickListener listener = initOnClickListener(finishOrderList.get(position),position);
        binding.btnAppraise.setOnClickListener(listener);
        binding.btnContact.setOnClickListener(listener);
        binding.lyListItem.setOnClickListener(listener);
        binding.setVariable(BR.finishorder, finishOrderList.get(position));
        short state = finishOrderList.get(position).getState();
        String servePublisherId = finishOrderList.get(position).getServePublisherId();
        String userId = UserEntity.getCurrentUser().getUserId();
        short serveType = finishOrderList.get(position).getServeType();
        if(state == STATE_HAVE_FINISHED || state == STATE_HAVE_CANCELED){
            if(state == STATE_HAVE_FINISHED){
                binding.state.setText("订单已完成");
            }
            else{
                binding.state.setText("订单已取消");
            }
            binding.btnAppraise.setVisibility(View.GONE);
        }else
        if (userId.equals(servePublisherId) && serveType == NEED_SERVE){
            if(state == STATE_WAIT_FOR_APPRAISE || state == STATE_PROVIDER_HAVE_APPRAISED ){
                binding.state.setText("等待评价");
                binding.btnAppraise.setVisibility(View.VISIBLE);
            }else
                if(state == STATE_DEMANDER_HAVE_APPRAISED){
                    binding.state.setText("评价完成");
                binding.btnAppraise.setVisibility(View.GONE);
                }
        }else
            if(userId.equals(servePublisherId) && serveType == OFFER_SERVE){
                if(state == STATE_WAIT_FOR_APPRAISE || state == STATE_DEMANDER_HAVE_APPRAISED ){
                    binding.state.setText("等待评价");
                    binding.btnAppraise.setVisibility(View.VISIBLE);
                }else
                if(state == STATE_PROVIDER_HAVE_APPRAISED){
                    binding.state.setText("评价完成");
                    binding.btnAppraise.setVisibility(View.GONE);
                }
            }else
            if (!userId.equals(servePublisherId) && serveType == NEED_SERVE){
                if(state == STATE_WAIT_FOR_APPRAISE || state == STATE_DEMANDER_HAVE_APPRAISED ){
                    binding.state.setText("等待评价");
                    binding.btnAppraise.setVisibility(View.VISIBLE);
                }else
                if(state == STATE_PROVIDER_HAVE_APPRAISED){
                    binding.btnAppraise.setVisibility(View.GONE);
                    binding.state.setText("评价完成");
                }
            }else
            if(!userId.equals(servePublisherId) && serveType == OFFER_SERVE){
                if(state == STATE_WAIT_FOR_APPRAISE || state == STATE_PROVIDER_HAVE_APPRAISED ){
                    binding.state.setText("等待评价");
                    binding.btnAppraise.setVisibility(View.VISIBLE);
                }else
                if(state == STATE_DEMANDER_HAVE_APPRAISED){
                    binding.state.setText("评价完成");
                    binding.btnAppraise.setVisibility(View.GONE);
                }
            }
            return binding.getRoot();
    }
    private View.OnClickListener initOnClickListener(final FinishedOrderEntity finishedOrder, final int position){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.ly_list_item:
                        Intent intent = new Intent(activity, ShowOrderDetailActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("finishedOrder", finishedOrder.toString());
                        intent.putExtras(bundle);
                        activity.startActivity(intent);
                        break;
                    case R.id.btn_appraise:
                        resetApprisePopupWindow(appraiseBinding,finishedOrder,position);
                        CommonUtils.ShowPopupWindow(activity,v,appraisePopupWindow);
                        break;
                    case R.id.btn_contact:
                        if (finishedOrder.getServePublisherId().equals(UserEntity.getCurrentUser().getUserId())){
                            RongManager.getInstance().talkWithOtherPeople(activity,finishedOrder.getServeReceiverId());
                        }else{
                            RongManager.getInstance().talkWithOtherPeople(activity,finishedOrder.getServePublisherId());
                        }
                        break;
                }
            }
        };
    }

    private void resetApprisePopupWindow(final PopupwindowAppraiseBinding appraiseBinding, final FinishedOrderEntity finishedOrder, final int position) {
        appraiseBinding.etAppraise.setText("");
        appraiseBinding.starAppraise.setRate(0);
        appraiseBinding.tvAppraisePoint.setText("请评分");
        appraiseBinding.btnConfirmAppraise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String comment = appraiseBinding.etAppraise.getText().toString();
                if (comment == null||comment.equals("")||comment.trim().equals("")){
                    showToast("评论不能为空哦");
                    return;
                }
                String strMarks = appraiseBinding.tvAppraisePoint.getText().toString();
                if (strMarks == null || strMarks.equals("请评分")){
                    showToast("请给予评分");
                    return;
                }
                strMarks = strMarks.substring(0,strMarks.length()-1);
                int mark = Integer.parseInt(strMarks);
                appraisePopupWindow.dismiss();
                controlOrderPresenter.saveComment(finishedOrder.getOrderId(),mark,comment,position);

            }
        });
    }
}
