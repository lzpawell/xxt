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

import java.util.List;

import bid.xiaocha.xxt.R;
import bid.xiaocha.xxt.databinding.ListItemAddressListBinding;
import bid.xiaocha.xxt.model.AddressEntity;
import bid.xiaocha.xxt.ui.activity.CreateOrUpdateAddressActivity;

import static bid.xiaocha.xxt.ui.activity.ShowOrChooseAddressActivity.CHOOSE_RESULT_CODE;

/**
 * Created by 55039 on 2017/11/4.
 */

public class AddressListAdater extends BaseAdapter {
    private Activity activity;
    private List<AddressEntity> addressList;
    private boolean isChoosing;
    private LayoutInflater layoutInflater;

    public AddressListAdater(Activity activity, List<AddressEntity> addressList, boolean isChoosing) {
        this.activity = activity;
        this.addressList = addressList;
        this.isChoosing = isChoosing;
        layoutInflater = LayoutInflater.from(activity);
    }
    public void updateList(List<AddressEntity> addressList){
        this.addressList = addressList;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return addressList.size();
    }

    @Override
    public Object getItem(int position) {
        return addressList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return addressList.get(position).getAddressId();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ListItemAddressListBinding binding;
        Log.i("bbb",position+"   "+getItemId(position));
        if (convertView == null){
            binding = DataBindingUtil.inflate(layoutInflater, R.layout.list_item_address_list,parent,false);
        }else {
            binding = DataBindingUtil.getBinding(convertView);
        }
//        binding.setVariable(BR.address,addressList.get(position));这两种方法都行
        binding.setAddress(addressList.get(position));
        binding.lyAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long addressId = getItemId(position);
                Bundle bundle = new Bundle();
                bundle.putLong("addressId",addressId);
                if (isChoosing){
                    Intent intent = new Intent();
                    intent.putExtras(bundle);
                    activity.setResult(CHOOSE_RESULT_CODE,intent);
                    activity.finish();
                }else{
                    Intent intent = new Intent(activity, CreateOrUpdateAddressActivity.class);
                    intent.putExtras(bundle);
                    activity.startActivity(intent);
                }
            }
        });

        return binding.getRoot();
    }
}
