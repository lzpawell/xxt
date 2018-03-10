package bid.xiaocha.xxt.ui.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;

import com.amap.api.services.core.AMapException;
import com.amap.api.services.help.Inputtips;
import com.amap.api.services.help.InputtipsQuery;
import com.amap.api.services.help.Tip;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import bid.xiaocha.xxt.R;
import bid.xiaocha.xxt.databinding.ActivityAddressSearchBinding;
import bid.xiaocha.xxt.databinding.ContentAddressSearchBinding;

import static bid.xiaocha.xxt.util.CommonUtils.showToast;

public class AddressSearchActivity extends AppCompatActivity implements View.OnClickListener, TextWatcher, Inputtips.InputtipsListener {

    private ActivityAddressSearchBinding binding;
    private ContentAddressSearchBinding contentBinding;
    private String city;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_address_search);
        contentBinding = binding.content;
        setSupportActionBar(binding.toolbar);
        Intent intent = this.getIntent();
        city = intent.getStringExtra("city");
        initView();
    }

    private void initView() {
        Log.i("haha",city);
        if (city != null) {
            contentBinding.tvCity.setText(city);
        }
        contentBinding.edtSearch.addTextChangedListener(this);
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        String content = s.toString().trim();//获取自动提示输入框的内容
        InputtipsQuery inputtipsQuery = new InputtipsQuery(content, city);//初始化一个输入提示搜索对象，并传入参数
        inputtipsQuery.setCityLimit(true);//将获取到的结果进行城市限制筛选
        Inputtips inputtips = new Inputtips(this, inputtipsQuery);//定义一个输入提示对象，传入当前上下文和搜索对象
        inputtips.setInputtipsListener(this);//设置输入提示查询的监听，实现输入提示的监听方法onGetInputtips()
        inputtips.requestInputtipsAsyn();//输入查询提示的异步接口实现
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    @Override
    public void onGetInputtips(List<Tip> list, int returnCode) {
        if (returnCode == AMapException.CODE_AMAP_SUCCESS) {//如果输入提示搜索成功
            List<HashMap<String, String>> searchList = new ArrayList<HashMap<String, String>>();
            for (int i = 0; i < list.size(); i++) {
                HashMap<String, String> hashMap = new HashMap<String, String>();
                hashMap.put("name", list.get(i).getName());
                hashMap.put("Longitude",list.get(i).getPoint().getLongitude()+"");
                hashMap.put("Latitude",list.get(i).getPoint().getLatitude()+"");
                hashMap.put("address", list.get(i).getDistrict());//将地址信息取出放入HashMap中
                searchList.add(hashMap);//将HashMap放入表中
            }
//            AddressTipsAdater addressTipsAdater = new AddressTipsAdater(this,searchList, this.getLayoutInflater());
//            contentBinding.lvAddressTips.setAdapter(addressTipsAdater);
//            addressTipsAdater.notifyDataSetChanged();
        } else {
            showToast(returnCode + "");
        }
    }
}
