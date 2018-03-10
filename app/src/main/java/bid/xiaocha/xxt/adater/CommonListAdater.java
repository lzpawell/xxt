package bid.xiaocha.xxt.adater;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

import bid.xiaocha.xxt.R;

/**
 * Created by 55039 on 2017/11/5.
 */

public class CommonListAdater<T> extends BaseAdapter {
    protected List<T> dataList;
    private int variable;
    protected LayoutInflater layoutInflater;
    private int layoutId;
    private OnItemClick onItemClick;

    public CommonListAdater(List<T> dataList, LayoutInflater layoutInflater) {
        this.dataList = dataList;
        this.layoutInflater = layoutInflater;
    }

    public CommonListAdater(List<T> dataList, int variable, LayoutInflater layoutInflater, int layoutId, OnItemClick onItemClick) {
        this.dataList = dataList;
        this.variable = variable;
        this.layoutInflater = layoutInflater;
        this.layoutId = layoutId;
        this.onItemClick = onItemClick;
    }
    public void setDataList(List<T> dataList){
        this.dataList = dataList;
        notifyDataSetChanged();
    }
    public void clearDataList(){
        dataList.clear();
        notifyDataSetChanged();
    }
    public void addDataList(List<T> dataList){
        for (T data:dataList){
            this.dataList.add(data);
        }
        notifyDataSetChanged();
    }
    public void addData(T data){
        dataList.add(data);
        notifyDataSetChanged();
    }

    public List<T> getDataList() {
        return dataList;
    }

    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public Object getItem(int position) {
        return dataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewDataBinding binding;
        if (convertView == null){
            binding = DataBindingUtil.inflate(layoutInflater,layoutId,parent,false);
        }else{
            binding = DataBindingUtil.bind(convertView);
        }
        binding.setVariable(variable,dataList.get(position));
        onItemClick.setItemClick(dataList.get(position),binding.getRoot());
        return binding.getRoot();
    }

    public interface OnItemClick<T>{
        void setItemClick(T data,View view);
    }
}
