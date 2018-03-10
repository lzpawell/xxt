package bid.xiaocha.xxt.ui.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import bid.xiaocha.xxt.R;
import bid.xiaocha.xxt.adater.AddressListAdater;
import bid.xiaocha.xxt.databinding.ActivityShowOrChooseAddressBinding;
import bid.xiaocha.xxt.databinding.ContentShowOrChooseAddressBinding;
import bid.xiaocha.xxt.model.AddressEntity;

public class ShowOrChooseAddressActivity extends AppCompatActivity implements View.OnClickListener {
    private ActivityShowOrChooseAddressBinding activityBinding;
    private ContentShowOrChooseAddressBinding contentBinding;
    private AddressListAdater adater;
    private boolean isChoosing;
    public final static int CHOOSE_RESULT_CODE = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityBinding = DataBindingUtil.setContentView(this,R.layout.activity_show_or_choose_address);
        contentBinding = activityBinding.content;

        setSupportActionBar(activityBinding.toolbar);

        initView();

    }

    private void initView() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            isChoosing = bundle.getBoolean("isChoosing", false);
            Log.i("aaaabba",isChoosing+"");
        }else{
            isChoosing = false;
        }
        if (isChoosing){
            getSupportActionBar().setTitle("请选择地址");
        }else{
            getSupportActionBar().setTitle("你的地址");
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Log.i("aaaaa",isChoosing+"");
        adater = new AddressListAdater(this, new ArrayList<AddressEntity>(), isChoosing);
        contentBinding.lvAddress.setAdapter(adater);
        contentBinding.lyAddAddress.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ly_add_address:
                startActivity(new Intent(this,CreateOrUpdateAddressActivity.class));
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        List<AddressEntity> addressEntityList = AddressEntity.getAllAddress(new AddressEntity.OnGetAllAddressResult() {
            @Override
            public void getAllAddressResult(final List<AddressEntity> list) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (list != null)
                            adater.updateList(list);
                    }
                });
            }
        });
        if (addressEntityList != null)
            adater.updateList(addressEntityList);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
