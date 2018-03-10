package bid.xiaocha.xxt.ui.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import com.android.tu.loadingdialog.LoadingDialog;

import bid.xiaocha.xxt.R;
import bid.xiaocha.xxt.databinding.ActivityShowOrderDetailBinding;
import bid.xiaocha.xxt.databinding.ContentShowOrderDetailBinding;
import bid.xiaocha.xxt.databinding.PopupwindowAppraiseBinding;
import bid.xiaocha.xxt.databinding.PopupwindowConfrimBinding;
import bid.xiaocha.xxt.iview.IControlOrderView;
import bid.xiaocha.xxt.iview.IShowOrderDetailView;
import bid.xiaocha.xxt.model.ActiveOrderEntity;
import bid.xiaocha.xxt.model.FinishedOrderEntity;
import bid.xiaocha.xxt.model.UserEntity;
import bid.xiaocha.xxt.presenter.ControlOrderPresenter;
import bid.xiaocha.xxt.presenter.IControlOrderPresenter;
import bid.xiaocha.xxt.presenter.IShowOrderDetailPresenter;
import bid.xiaocha.xxt.presenter.ShowOrderDetailPresenter;
import bid.xiaocha.xxt.ui.views.StarAppraiseView;
import bid.xiaocha.xxt.util.CommonUtils;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;
import static bid.xiaocha.xxt.model.ActiveOrderEntity.CANCEL_STATE_NOT_CANCEL;
import static bid.xiaocha.xxt.model.ActiveOrderEntity.CANCEL_STATE_PUBLISHER_HAS_CANCEL;
import static bid.xiaocha.xxt.model.ActiveOrderEntity.CANCEL_STATE_RECEIVER_HAS_CANCEL;
import static bid.xiaocha.xxt.model.ActiveOrderEntity.NEED_SERVE;
import static bid.xiaocha.xxt.model.ActiveOrderEntity.OFFER_SERVE;
import static bid.xiaocha.xxt.model.ActiveOrderEntity.STATE_BEGIN_SERVE;
import static bid.xiaocha.xxt.model.ActiveOrderEntity.STATE_DEMANDER_HAVE_APPRAISED;
import static bid.xiaocha.xxt.model.ActiveOrderEntity.STATE_HAVE_CANCELED;
import static bid.xiaocha.xxt.model.ActiveOrderEntity.STATE_HAVE_FINISHED;
import static bid.xiaocha.xxt.model.ActiveOrderEntity.STATE_PROVIDER_HAVE_APPRAISED;
import static bid.xiaocha.xxt.model.ActiveOrderEntity.STATE_WAIT_AGREE_CREATE;
import static bid.xiaocha.xxt.model.ActiveOrderEntity.STATE_WAIT_FOR_APPRAISE;
import static bid.xiaocha.xxt.model.ActiveOrderEntity.STATE_WAIT_FOR_CONFIRM;
import static bid.xiaocha.xxt.presenter.IShowOrderDetailPresenter.ACTIVE_ORDER;
import static bid.xiaocha.xxt.presenter.IShowOrderDetailPresenter.FINISHED_ORDER;
import static bid.xiaocha.xxt.util.CommonUtils.showToast;
import static bid.xiaocha.xxt.util.UITool.createLoadingDialog;
import static bid.xiaocha.xxt.util.UITool.dismissLoadingDialog;
import static bid.xiaocha.xxt.util.UITool.showLoadingDialog;

public class ShowOrderDetailActivity extends AppCompatActivity implements IControlOrderView, IShowOrderDetailView,View.OnClickListener {
    private ActivityShowOrderDetailBinding activityBinding;
    private ContentShowOrderDetailBinding contentBinding;
    private ActiveOrderEntity activeOrder = null;
    private FinishedOrderEntity finishedOrder = null;
    private IControlOrderPresenter controlOrderPresenter;
    private IShowOrderDetailPresenter showOrderDetailPresenter;
    private PopupWindow confrimPopupWindow;
    private PopupwindowConfrimBinding confrimPopupwindowBinding;
    private PopupwindowAppraiseBinding appraiseBinding;
    private PopupWindow appraisePopupWindow;
    private LoadingDialog loadingDailog;
    private short orderType;
    private String orderId ="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityBinding = DataBindingUtil.setContentView(this, R.layout.activity_show_order_detail);
        contentBinding = activityBinding.content;
        setSupportActionBar(activityBinding.toolbar);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            String activeOrderJson = bundle.getString("activeOrder","");
            if (!activeOrderJson.equals("")){
                activeOrder = ActiveOrderEntity.getActiveOrderEntityFromJson(activeOrderJson);
                orderType = ACTIVE_ORDER;
                orderId = activeOrder.getOrderId();
            }
            String finishedOrderJson = bundle.getString("finishedOrder","");
            if (!finishedOrderJson.equals("")){
                finishedOrder = FinishedOrderEntity.getFinishedOrderEntityFromJson(finishedOrderJson);
                orderType = FINISHED_ORDER;
                orderId = finishedOrder.getOrderId();
            }
            if (orderId.equals("")){
                orderId = bundle.getString("orderId","");
                orderType = -1;
            }
        }
        initPresenter();
        initView();
    }

    private void initPresenter() {
        controlOrderPresenter = new ControlOrderPresenter(this);
        showOrderDetailPresenter = new ShowOrderDetailPresenter(this);
    }

    private void initView() {
        confrimPopupwindowBinding = DataBindingUtil.inflate(getLayoutInflater(),R.layout.popupwindow_confrim,null,false);
        confrimPopupWindow = new PopupWindow(confrimPopupwindowBinding.getRoot(),WRAP_CONTENT, WRAP_CONTENT,true);
        confrimPopupwindowBinding.tvTitle.setText("提示");
        confrimPopupwindowBinding.btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confrimPopupWindow.dismiss();
            }
        });

        appraiseBinding = DataBindingUtil.inflate(getLayoutInflater(),R.layout.popupwindow_appraise,null,false);
        appraiseBinding.starAppraise.setOnRateChangeListener(new StarAppraiseView.OnRateChangeListener() {
            @Override
            public void onRateChange(int rate) {
                appraiseBinding.tvAppraisePoint.setText(rate+"分");
            }
        });
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
                controlOrderPresenter.saveComment(finishedOrder.getOrderId(),mark,comment,0);
            }
        });

        appraisePopupWindow = new PopupWindow(appraiseBinding.getRoot(), ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT,true);
        loadingDailog = createLoadingDialog(this);
        contentBinding.btnAccept.setOnClickListener(this);
        contentBinding.btnCancel.setOnClickListener(this);
        contentBinding.btnConfirm.setOnClickListener(this);
        contentBinding.btnConfirmCancel.setOnClickListener(this);
        contentBinding.btnRefuseCancel.setOnClickListener(this);
        contentBinding.btnAppraise.setOnClickListener(this);
        contentBinding.btnComplain.setOnClickListener(this);
        SwipeRefreshLayout.OnRefreshListener onRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                contentBinding.lyRefreshOrderDetail.setRefreshing(true);
                showOrderDetailPresenter.getOrderDetail(orderId,orderType);
            }
        };
        contentBinding.lyRefreshOrderDetail.setOnRefreshListener(onRefreshListener);
        onRefreshListener.onRefresh();
        resetView();
    }

    private void resetView() {
        String userId = UserEntity.getCurrentUser().getUserId();
        Log.i("orderType",orderType+"");
        if (orderType == ACTIVE_ORDER){
            short state = activeOrder.getState();
            short cancelState = activeOrder.getCancelState();
            String servePublisherId = activeOrder.getServePublisherId();
            short serveType = activeOrder.getServeType();
            contentBinding.btnAppraise.setVisibility(View.INVISIBLE);
            contentBinding.btnConfirm.setEnabled(true);
            if (cancelState == CANCEL_STATE_PUBLISHER_HAS_CANCEL ) {
                if (userId.equals(servePublisherId)){
                    contentBinding.tvOrderState.setText("等待对方确认取消");
                    contentBinding.btnAccept.setVisibility(View.GONE);
                    contentBinding.btnCancel.setVisibility(View.GONE);
                    contentBinding.btnConfirm.setVisibility(View.GONE);
                    contentBinding.btnConfirmCancel.setVisibility(View.GONE);
                    contentBinding.btnRefuseCancel.setVisibility(View.VISIBLE);
                }else{
                    contentBinding.tvOrderState.setText("待你取消");
                    contentBinding.btnAccept.setVisibility(View.GONE);
                    contentBinding.btnCancel.setVisibility(View.GONE);
                    contentBinding.btnConfirm.setVisibility(View.GONE);
                    contentBinding.btnConfirmCancel.setVisibility(View.VISIBLE);
                    contentBinding.btnRefuseCancel.setVisibility(View.VISIBLE);
                }
            }

            if (cancelState == CANCEL_STATE_RECEIVER_HAS_CANCEL ) {
                if (!userId.equals(servePublisherId)){
                    contentBinding.tvOrderState.setText("等待对方确认取消");
                    contentBinding.btnAccept.setVisibility(View.GONE);
                    contentBinding.btnCancel.setVisibility(View.GONE);
                    contentBinding.btnConfirm.setVisibility(View.GONE);
                    contentBinding.btnConfirmCancel.setVisibility(View.GONE);
                    contentBinding.btnRefuseCancel.setVisibility(View.VISIBLE);
                }else{
                    contentBinding.tvOrderState.setText("待你取消");
                    contentBinding.btnAccept.setVisibility(View.GONE);
                    contentBinding.btnCancel.setVisibility(View.GONE);
                    contentBinding.btnConfirm.setVisibility(View.GONE);
                    contentBinding.btnConfirmCancel.setVisibility(View.VISIBLE);
                    contentBinding.btnRefuseCancel.setVisibility(View.VISIBLE);
                }
            }

            if (cancelState == CANCEL_STATE_NOT_CANCEL) {
                contentBinding.btnCancel.setVisibility(View.VISIBLE);
                contentBinding.btnConfirmCancel.setVisibility(View.GONE);
                contentBinding.btnRefuseCancel.setVisibility(View.GONE);
                if (userId.equals(servePublisherId) && serveType == NEED_SERVE) {
                    if (state == STATE_WAIT_AGREE_CREATE) {
                        contentBinding.tvOrderState.setText("待接受");
                        contentBinding.btnAccept.setVisibility(View.VISIBLE);
                        contentBinding.btnCancel.setVisibility(View.VISIBLE);
                        contentBinding.btnConfirm.setVisibility(View.GONE);
                    } else if (state == STATE_BEGIN_SERVE) {
                        contentBinding.tvOrderState.setText("等待对方完成");
                        contentBinding.btnConfirm.setVisibility(View.VISIBLE);
                        contentBinding.btnAccept.setVisibility(View.GONE);
                        contentBinding.btnConfirm.setEnabled(false);
                        contentBinding.btnCancel.setVisibility(View.VISIBLE);
                    } else if (state == STATE_WAIT_FOR_CONFIRM) {
                        contentBinding.tvOrderState.setText("等待您的确认");
                        contentBinding.btnConfirm.setVisibility(View.VISIBLE);
                        contentBinding.btnAccept.setVisibility(View.GONE);
                    }

                }
                if (userId.equals(servePublisherId) && serveType == OFFER_SERVE) {
                    if (state == STATE_WAIT_AGREE_CREATE) {
                        contentBinding.tvOrderState.setText("待接受");
                        contentBinding.btnAccept.setVisibility(View.VISIBLE);
                        contentBinding.btnConfirm.setVisibility(View.GONE);
                    } else if (state == STATE_BEGIN_SERVE) {
                        contentBinding.tvOrderState.setText("等待您完成");
                        contentBinding.btnConfirm.setVisibility(View.GONE);
                        contentBinding.btnAccept.setVisibility(View.VISIBLE);
                        contentBinding.btnConfirm.setEnabled(true);
                    } else if (state == STATE_WAIT_FOR_CONFIRM) {
                        contentBinding.tvOrderState.setText("等待对方确认");
                        contentBinding.btnConfirm.setVisibility(View.GONE);
                        contentBinding.btnAccept.setVisibility(View.GONE);
                    }
                }
                if (!userId.equals(servePublisherId) && serveType == NEED_SERVE) {
                    if (state == STATE_WAIT_AGREE_CREATE) {
                        contentBinding.tvOrderState.setText("等待对方接受");
                        contentBinding.btnAccept.setVisibility(View.GONE);
                        contentBinding.btnConfirm.setVisibility(View.GONE);
                    } else if (state == STATE_BEGIN_SERVE) {
                        contentBinding.tvOrderState.setText("等待您完成");
                        contentBinding.btnConfirm.setVisibility(View.VISIBLE);
                        contentBinding.btnAccept.setVisibility(View.GONE);
                        contentBinding.btnConfirm.setEnabled(true);
                    } else if (state == STATE_WAIT_FOR_CONFIRM) {
                        contentBinding.tvOrderState.setText("等待对方确认");
                        contentBinding.btnConfirm.setVisibility(View.GONE);
                        contentBinding.btnAccept.setVisibility(View.GONE);
                    }
                }
                if (!userId.equals(servePublisherId) && serveType == OFFER_SERVE) {
                    if (state == STATE_WAIT_AGREE_CREATE) {
                        contentBinding.tvOrderState.setText("等待对方接受");
                        contentBinding.btnAccept.setVisibility(View.GONE);
                        contentBinding.btnConfirm.setVisibility(View.GONE);
                    } else if (state == STATE_BEGIN_SERVE) {
                        contentBinding.tvOrderState.setText("等待对方完成");
                        contentBinding.btnConfirm.setVisibility(View.VISIBLE);
                        contentBinding.btnAccept.setVisibility(View.GONE);
                        contentBinding.btnConfirm.setEnabled(false);
                    } else if (state == STATE_WAIT_FOR_CONFIRM) {
                        contentBinding.tvOrderState.setText("等待您确认");
                        contentBinding.btnConfirm.setVisibility(View.VISIBLE);
                        contentBinding.btnConfirm.setEnabled(true);
                        contentBinding.btnAccept.setVisibility(View.GONE);
                    }
                }
            }
        }else if(orderType == FINISHED_ORDER){
            contentBinding.btnAccept.setVisibility(View.GONE);
            contentBinding.btnCancel.setVisibility(View.GONE);
            contentBinding.btnConfirm.setVisibility(View.GONE);
            contentBinding.btnConfirmCancel.setVisibility(View.GONE);
            contentBinding.btnRefuseCancel.setVisibility(View.GONE);
            short state = finishedOrder.getState();
            String servePublisherId = finishedOrder.getServePublisherId();
            short serveType = finishedOrder.getServeType();
            if(state == STATE_HAVE_FINISHED || state == STATE_HAVE_CANCELED){
                if(state == STATE_HAVE_FINISHED){
                    contentBinding.tvOrderState.setText("订单已完成");
                }
                else{
                    contentBinding.tvOrderState.setText("订单已取消");
                }
                contentBinding.btnAppraise.setVisibility(View.GONE);
            }else
            if (userId.equals(servePublisherId) && serveType == NEED_SERVE){
                if(state == STATE_WAIT_FOR_APPRAISE || state == STATE_PROVIDER_HAVE_APPRAISED ){
                    contentBinding.tvOrderState.setText("等待评价");
                    contentBinding.btnAppraise.setVisibility(View.VISIBLE);
                }else
                if(state == STATE_DEMANDER_HAVE_APPRAISED){
                    contentBinding.tvOrderState.setText("评价完成");
                    contentBinding.btnAppraise.setVisibility(View.GONE);
                }
            }else
            if(userId.equals(servePublisherId) && serveType == OFFER_SERVE){
                if(state == STATE_WAIT_FOR_APPRAISE || state == STATE_DEMANDER_HAVE_APPRAISED ){
                    contentBinding.tvOrderState.setText("等待评价");
                    contentBinding.btnAppraise.setVisibility(View.VISIBLE);
                }else
                if(state == STATE_PROVIDER_HAVE_APPRAISED){
                    contentBinding.tvOrderState.setText("评价完成");
                    contentBinding.btnAppraise.setVisibility(View.GONE);
                }
            }else
            if (!userId.equals(servePublisherId) && serveType == NEED_SERVE){
                if(state == STATE_WAIT_FOR_APPRAISE || state == STATE_DEMANDER_HAVE_APPRAISED ){
                    contentBinding.tvOrderState.setText("等待评价");
                    contentBinding.btnAppraise.setVisibility(View.VISIBLE);
                }else
                if(state == STATE_PROVIDER_HAVE_APPRAISED){
                    contentBinding.btnAppraise.setVisibility(View.GONE);
                    contentBinding.tvOrderState.setText("评价完成");
                }
            }else
            if(!userId.equals(servePublisherId) && serveType == OFFER_SERVE){
                if(state == STATE_WAIT_FOR_APPRAISE || state == STATE_PROVIDER_HAVE_APPRAISED ){
                    contentBinding.tvOrderState.setText("等待评价");
                    contentBinding.btnAppraise.setVisibility(View.VISIBLE);
                }else
                if(state == STATE_DEMANDER_HAVE_APPRAISED){
                    contentBinding.tvOrderState.setText("评价完成");
                    contentBinding.btnAppraise.setVisibility(View.GONE);
                }
            }
        }
    }


    @Override
    public void showLoading() {
        showLoadingDialog(loadingDailog);
    }

    @Override
    public void dismissLoading() {
        dismissLoadingDialog(loadingDailog);
    }

    @Override
    public void showResult(final short resultCode, final Object result, final int position) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                switch (resultCode){
                    case IControlOrderView.SUCCESS_RETURN_ACTIVE_ORDER:
                        activeOrder = (ActiveOrderEntity) result;
                        finishedOrder = null;
                        resetView();
                        break;
                    case IControlOrderView.SUCCESS_RETURN_FINISHED_ORDER:
                        activeOrder = null;
                        finishedOrder = (FinishedOrderEntity) result;
                        resetView();
                        break;
                    case IControlOrderView.ERROR_SHOW_STRING:
                        showToast((String) result);
                        break;
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        final String userId = UserEntity.getCurrentUser().getUserId();
        switch (v.getId()) {
            case R.id.btn_accept:
                confrimPopupwindowBinding.tvContent.setText("你确定要接单吗");
                confrimPopupwindowBinding.btnComfirm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        confrimPopupWindow.dismiss();
                        controlOrderPresenter.acceptOrder(activeOrder.getOrderId(),0);
                    }
                });
                CommonUtils.ShowPopupWindow(ShowOrderDetailActivity.this,v,confrimPopupWindow);
                break;
            case R.id.btn_confirm:
                confrimPopupwindowBinding.tvContent.setText("你确定完成了吗");
                confrimPopupwindowBinding.btnComfirm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        confrimPopupWindow.dismiss();
                        if ((activeOrder.getServeType() == OFFER_SERVE && userId.equals(activeOrder.getServePublisherId()))
                                ||(activeOrder.getServeType() == NEED_SERVE && userId.equals(activeOrder.getServeReceiverId())))
                            controlOrderPresenter.finishOrder(activeOrder.getOrderId(),0);
                        if ((activeOrder.getServeType() == NEED_SERVE && userId.equals(activeOrder.getServePublisherId()))
                                ||(activeOrder.getServeType() == OFFER_SERVE && userId.equals(activeOrder.getServeReceiverId())))
                            controlOrderPresenter.confrimOrder(activeOrder.getOrderId(),0);
                    }
                });
                CommonUtils.ShowPopupWindow(ShowOrderDetailActivity.this,v,confrimPopupWindow);
                break;
            case R.id.btn_cancel:
                confrimPopupwindowBinding.tvContent.setText("你确定要取消订单吗");
                confrimPopupwindowBinding.btnComfirm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        confrimPopupWindow.dismiss();
                        controlOrderPresenter.cancelOrder(activeOrder.getOrderId(),0);
                    }
                });
                CommonUtils.ShowPopupWindow(ShowOrderDetailActivity.this,v,confrimPopupWindow);
                break;
            case R.id.btn_refuse_cancel:
                confrimPopupwindowBinding.tvContent.setText("你确定要拒绝取消订单吗");
                confrimPopupwindowBinding.btnComfirm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        confrimPopupWindow.dismiss();
                        controlOrderPresenter.refuseCancelOrder(activeOrder.getOrderId(),0);
                    }
                });
                CommonUtils.ShowPopupWindow(ShowOrderDetailActivity.this,v,confrimPopupWindow);
                break;
            case R.id.btn_confirm_cancel:
                confrimPopupwindowBinding.tvContent.setText("你确定要取消订单吗");
                confrimPopupwindowBinding.btnComfirm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        confrimPopupWindow.dismiss();
                        controlOrderPresenter.cancelOrder(activeOrder.getOrderId(),0);
                    }
                });
                CommonUtils.ShowPopupWindow(ShowOrderDetailActivity.this,v,confrimPopupWindow);
                break;
            case R.id.btn_appraise:
                CommonUtils.ShowPopupWindow(ShowOrderDetailActivity.this,v,appraisePopupWindow);
                break;
        }
    }

    @Override
    public void showOrderDetailResult(final short resultCode, final Object result) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                switch (resultCode){
                    case IShowOrderDetailView.SUCCESS_RETURN_ACTIVE_ORDER:
                        activeOrder = (ActiveOrderEntity) result;
                        finishedOrder = null;
                        orderType = ACTIVE_ORDER;
                        resetView();
                        break;
                    case IShowOrderDetailView.SUCCESS_RETURN_FINISHED_ORDER:
                        activeOrder = null;
                        finishedOrder = (FinishedOrderEntity) result;
                        orderType = FINISHED_ORDER;
                        resetView();
                        break;
                    case IShowOrderDetailView.ERROR_SHOW_STRING:
                        showToast((String) result);
                        break;
                }
                contentBinding.lyRefreshOrderDetail.setRefreshing(false);
            }
        });
    }
}
