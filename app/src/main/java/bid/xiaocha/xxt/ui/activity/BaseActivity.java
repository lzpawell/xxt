package bid.xiaocha.xxt.ui.activity;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.android.tu.loadingdialog.LoadingDialog;

import bid.xiaocha.xxt.iview.IBaseActivityView;
import bid.xiaocha.xxt.util.UITool;

/**
 * Created by 55039 on 2018/3/4.
 */

public class BaseActivity extends AppCompatActivity implements IBaseActivityView{
    private LoadingDialog loadingDialog;
    public void showLoading() {
        UITool.showLoadingDialog(loadingDialog);
    }
    public void dismissLoading() {
        UITool.dismissLoadingDialog(loadingDialog);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadingDialog = UITool.createLoadingDialog(this);
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
