package bid.xiaocha.xxt.adater;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import bid.xiaocha.xxt.R;
import bid.xiaocha.xxt.model.ActiveOrderEntity;

/**
 * Created by 55039 on 2017/10/14.
 */

public class OrderListAdater extends BaseAdapter {
    private List<ActiveOrderEntity> datalist = new ArrayList<>();
    private LayoutInflater layoutInflater;
    public OrderListAdater(Context context){
        this.layoutInflater = LayoutInflater.from(context);
    }
    public OrderListAdater(Context context, List<ActiveOrderEntity> datalist){
        this.datalist = datalist;
        this.layoutInflater = LayoutInflater.from(context);
    }

    public void setDatalist(List<ActiveOrderEntity> datalist) {
        this.datalist = datalist;
        notifyDataSetChanged();
    }
    public void addData(List<ActiveOrderEntity> datalist){
        for (ActiveOrderEntity order : datalist){
            this.datalist.add(order);
        }
        notifyDataSetChanged();
    }


    public List<ActiveOrderEntity> getDatalist() {
        return datalist;
    }

    @Override
    public int getCount() {
        return datalist.size();
    }

    @Override
    public Object getItem(int position) {
        return datalist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null){
            holder = new ViewHolder();
            convertView = layoutInflater.inflate(R.layout.demo_list_item,null);
            holder.tv = (TextView) convertView.findViewById(R.id.demo_tv);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
//        holder.tv.setText(datalist.get(position).getAaa()+"");
        return convertView;
    }
    class ViewHolder{
        public TextView tv;
    }
}
