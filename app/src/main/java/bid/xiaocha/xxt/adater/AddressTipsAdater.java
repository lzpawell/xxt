package bid.xiaocha.xxt.adater;

import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.PopupWindow;

import java.util.HashMap;
import java.util.List;

import bid.xiaocha.xxt.R;
import bid.xiaocha.xxt.databinding.ListItemAddressTipsBinding;

import static bid.xiaocha.xxt.ui.activity.MapChooseAddressActivity.RESULT_FINISH_CHOOSE_CODE;

/**
 * Created by lenovo-pc on 2018/2/2.
 */

public class AddressTipsAdater extends BaseAdapter {
    private List<HashMap<String,String>> addressData;
    private LayoutInflater layoutInflater;
    private Activity activity;
    private PopupWindow addressSearchPopupWindow;

    public AddressTipsAdater(Activity activity, List<HashMap<String,String>> addressData, LayoutInflater layoutInflater, PopupWindow addressSearchPopupWindow) {
        this.addressData = addressData;
        this.layoutInflater = layoutInflater;
        this.addressSearchPopupWindow = addressSearchPopupWindow;
        this.activity =activity;
    }

    @Override
    public int getCount() {
        return addressData.size();
    }

    @Override
    public Object getItem(int position) {
        return addressData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ListItemAddressTipsBinding binding;
        if (convertView == null) {
            binding = DataBindingUtil.inflate(layoutInflater, R.layout.list_item_address_tips, parent, false);
        } else {
            binding = DataBindingUtil.getBinding(convertView);
        }
        binding.tvTitle.setText(addressData.get(position).get("name"));
        binding.tvText.setText(addressData.get(position).get("address"));

        binding.listItemTips.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putDouble("longtitude",Double.parseDouble(addressData.get(position).get("Longitude")));
                bundle.putDouble("latitude",Double.parseDouble(addressData.get(position).get("Latitude")));
                bundle.putString("gaoDePlace",addressData.get(position).get("name"));
                intent.putExtras(bundle);
                activity.setResult(RESULT_FINISH_CHOOSE_CODE,intent);
                addressSearchPopupWindow.dismiss();
                activity.finish();

            }
        });
        return binding.getRoot();
    }
}
