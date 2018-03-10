package bid.xiaocha.xxt.adater;

import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;

import java.util.List;

import bid.xiaocha.xxt.BR;
import bid.xiaocha.xxt.R;
import bid.xiaocha.xxt.databinding.ListItemNeedServeManagerListBinding;
import bid.xiaocha.xxt.databinding.PopupwindowConfrimBinding;
import bid.xiaocha.xxt.model.NeedServeEntity;
import bid.xiaocha.xxt.model.UserEntity;
import bid.xiaocha.xxt.presenter.IControlServePresenter;
import bid.xiaocha.xxt.ui.activity.MyNeedServeDetailActivity;
import bid.xiaocha.xxt.util.CommonUtils;

import static bid.xiaocha.xxt.model.NeedServeEntity.START_SERVE;
import static bid.xiaocha.xxt.model.NeedServeEntity.STOP_SERVE;

/**
 * Created by lenovo-pc on 2017/12/7.
 */

public class NeedServeManagerListAdater extends BaseAdapter  {

    private List<NeedServeEntity> needServeList;
    private LayoutInflater layoutInflater;
    private Activity activity;
    private PopupwindowConfrimBinding popupwindowConfirmBinding;
    private ListView listview;
    private PopupWindow confirmPopupWindow;
    private IControlServePresenter presenter;


    public NeedServeManagerListAdater(ListView listview,List<NeedServeEntity> needServeList,Activity activity,IControlServePresenter presenter){
        this.activity = activity;
        this.needServeList = needServeList;
        layoutInflater = LayoutInflater.from(activity);
        this.listview = listview;
        this.presenter = presenter;
        popupwindowConfirmBinding = DataBindingUtil.inflate(layoutInflater,R.layout.popupwindow_confrim,null,false);
        confirmPopupWindow = new PopupWindow(popupwindowConfirmBinding.getRoot(),ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT,true);
        popupwindowConfirmBinding.tvTitle.setText("提示");
        popupwindowConfirmBinding.btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmPopupWindow.dismiss();
            }
        });
    }

    public void updateList(List<NeedServeEntity> needServeList){
        this.needServeList = needServeList;
        notifyDataSetChanged();
    }
    public void addDataList(List<NeedServeEntity> dataList){
        for (NeedServeEntity data:dataList){
            this.needServeList.add(data);
        }
        notifyDataSetChanged();
    }
    public void clearDataList(){
        needServeList.clear();
        notifyDataSetChanged();
    }
    public void deleteView(int position){
        needServeList.remove(position);
        notifyDataSetChanged();
    }

    public void updateView(int position,NeedServeEntity needServeEntity){
        int firstVisiblePosition = listview.getFirstVisiblePosition();
        int lastVisiblePosition = listview.getLastVisiblePosition();
        if (position - firstVisiblePosition >= 0 && position <= lastVisiblePosition) {
            View view = listview.getChildAt(position - firstVisiblePosition);
            needServeList.set(position,needServeEntity);
            getView(position,view,null);
        }
    }


    @Override
    public int getCount() {
        return needServeList.size();
    }

    @Override
    public Object getItem(int position) {
        return needServeList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ListItemNeedServeManagerListBinding binding;
        if (convertView == null) {
            binding = DataBindingUtil.inflate(layoutInflater, R.layout.list_item_need_serve_manager_list, parent, false);
        } else {
            binding = DataBindingUtil.getBinding(convertView);
        }
        binding.setVariable(BR.needServeManager, needServeList.get(position));
        View.OnClickListener listener = initOnclick(position);
        int state = needServeList.get(position).getState();
        String userId = UserEntity.getCurrentUser().getUserId();

        if(state == STOP_SERVE){
            binding.state.setText("已下架");
            binding.btnDelete.setVisibility(View.VISIBLE);
            binding.btnStart.setVisibility(View.VISIBLE);
            binding.btnStop.setVisibility(View.GONE);

        }
        else
            if(state == START_SERVE){
                binding.state.setText("已上架");
                binding.btnDelete.setVisibility(View.VISIBLE);
                binding.btnStart.setVisibility(View.GONE);
                binding.btnStop.setVisibility(View.VISIBLE);
            }
            binding.getRoot().setOnClickListener(listener);
            binding.btnStop.setOnClickListener(listener);
            binding.btnStart.setOnClickListener(listener);
            binding.btnDelete.setOnClickListener(listener);
        return binding.getRoot();
    }

    private View.OnClickListener initOnclick(final int position){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.btn_start:
                        popupwindowConfirmBinding.tvContent.setText("服务确定上架吗");
                        CommonUtils.ShowPopupWindow(activity,v,confirmPopupWindow);
                        popupwindowConfirmBinding.btnComfirm.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                confirmPopupWindow.dismiss();
                                presenter.startNeedServe(needServeList.get(position).getServeId(),position);
                            }
                        });
                        break;
                    case R.id.btn_stop:
                        popupwindowConfirmBinding.tvContent.setText("服务确定下架吗");
                        popupwindowConfirmBinding.btnComfirm.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                confirmPopupWindow.dismiss();
                                presenter.stopNeedServe(needServeList.get(position).getServeId(),position);
                            }
                        });
                        CommonUtils.ShowPopupWindow(activity,v,confirmPopupWindow);

                        break;
                    case R.id.btn_delete:
                        popupwindowConfirmBinding.tvContent.setText("确定删除吗");
                        CommonUtils.ShowPopupWindow(activity,v,confirmPopupWindow);
                        popupwindowConfirmBinding.btnComfirm.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                confirmPopupWindow.dismiss();
                                presenter.deleteNeedServe(needServeList.get(position).getServeId(),position);
                            }
                        });
                        CommonUtils.ShowPopupWindow(activity,v,confirmPopupWindow);

                        break;
                    case R.id.list_item:
                        Intent intent = new Intent(activity, MyNeedServeDetailActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("NeedServe",needServeList.get(position).toString());
                        intent.putExtras(bundle);
                        activity.startActivity(intent);
                        break;
                    default:
                        break;
                }
            }
        };
    }
}
