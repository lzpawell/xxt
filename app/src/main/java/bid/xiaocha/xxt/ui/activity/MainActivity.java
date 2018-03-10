package bid.xiaocha.xxt.ui.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;


import bid.xiaocha.xxt.R;
import bid.xiaocha.xxt.databinding.ContentMainBinding;
import bid.xiaocha.xxt.model.UserEntity;
import bid.xiaocha.xxt.ui.fragment.ChatFragment;
import bid.xiaocha.xxt.ui.fragment.MainFragment;
import bid.xiaocha.xxt.ui.fragment.MyFragment;
import bid.xiaocha.xxt.ui.fragment.OrderFragment;
import bid.xiaocha.xxt.ui.fragment.ServiceManagerFragment;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{


    private MainFragment mainFragment;
    private OrderFragment orderFragment;
    private ChatFragment chatFragment;
    private ServiceManagerFragment serviceManagerFragment;
    private MyFragment myFragment;
    private ContentMainBinding contentMainBinding;



    private static final String FRAGMENT_TAG = "fragmentTag";
    private static final String MAIN_FRAGMENT_TAG = "mainFragment";
    private static final String ORDER_FRAGMENT_TAG = "orderFragment";
    private static final String CHAT_FRAGMENT_TAG = "chatFragment";
    private static final String SERVICE_MANAGER_FRAGMENT_TAG = "serviceManagerFragment";
    private static final String MY_FRAGMENT_TAG = "myFragment";



    private android.os.Handler uiHandler = new android.os.Handler();


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        Log.i("balala", "id   " + this.toString());
        setContentView(R.layout.activity_main);
        contentMainBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.content_main, (ViewGroup) this.findViewById(R.id.main_content), true);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().hide();
        initView();
        initCurrentFragment();
    }

    /*
    * 从bundle中恢复当前fragment*/
    private void initCurrentFragment() {
        if(currentFragmentTag == null)
            currentFragmentTag = MAIN_FRAGMENT_TAG;

        switch (currentFragmentTag){
            case MAIN_FRAGMENT_TAG:
                currentFragment = mainFragment;
                break;
            case ORDER_FRAGMENT_TAG:
                currentFragment = orderFragment;
                break;
            case CHAT_FRAGMENT_TAG:
                currentFragment = chatFragment;
                break;
            case MY_FRAGMENT_TAG:
                currentFragment = myFragment;
                break;
            case SERVICE_MANAGER_FRAGMENT_TAG:
                currentFragment = serviceManagerFragment;
                break;
            default:break;
        }

        Log.i("balala", "resume " + currentFragmentTag);

        setFragment(currentFragment, currentFragmentTag);
    }

    private void initView() {
        contentMainBinding.lyMain.setOnClickListener(this);
        contentMainBinding.lyChat.setOnClickListener(this);
        contentMainBinding.lyMy.setOnClickListener(this);
        contentMainBinding.lyOrder.setOnClickListener(this);
        contentMainBinding.lyServiceManager.setOnClickListener(this);


        initFragment();
    }

    private void initFragment() {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();

        mainFragment = (MainFragment) getSupportFragmentManager().findFragmentByTag(MAIN_FRAGMENT_TAG);
        if(mainFragment == null){
            mainFragment = MainFragment.getFragment();
            transaction.add(R.id.v_content, mainFragment, MAIN_FRAGMENT_TAG);
        }

        orderFragment = (OrderFragment) getSupportFragmentManager().findFragmentByTag(ORDER_FRAGMENT_TAG);
        if(orderFragment == null){
            orderFragment = OrderFragment.getFragment();
            transaction.add(R.id.v_content, orderFragment, ORDER_FRAGMENT_TAG);
        }

        chatFragment = (ChatFragment) getSupportFragmentManager().findFragmentByTag(CHAT_FRAGMENT_TAG);
        if(chatFragment == null){
            chatFragment = ChatFragment.getFragment();
            transaction.add(R.id.v_content, chatFragment, CHAT_FRAGMENT_TAG);
        }

        serviceManagerFragment = (ServiceManagerFragment) getSupportFragmentManager().findFragmentByTag(SERVICE_MANAGER_FRAGMENT_TAG);
        if(serviceManagerFragment == null){
            serviceManagerFragment = ServiceManagerFragment.getFragment();
            transaction.add(R.id.v_content, serviceManagerFragment, SERVICE_MANAGER_FRAGMENT_TAG);
        }

        myFragment = (MyFragment) getSupportFragmentManager().findFragmentByTag(MY_FRAGMENT_TAG);
        if(myFragment == null){
            myFragment = MyFragment.getFragment();
            transaction.add(R.id.v_content, myFragment, MY_FRAGMENT_TAG);
        }

        //把所有的fragment都托管给fragmentManager， 让fragmentManager帮我们管理这些fragment的状态
        transaction.hide(mainFragment);
        transaction.hide(orderFragment);
        transaction.hide(chatFragment);
        transaction.hide(serviceManagerFragment);
        transaction.hide(myFragment);
        transaction.commit();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ly_main:
                reset();
                contentMainBinding.tvHome.setTextColor(ContextCompat.getColor(this,R.color.colorPrimary));
                contentMainBinding.imgHome.setBackgroundResource(R.mipmap.home_choose);
                //set ly_main style
                setFragment(mainFragment, MAIN_FRAGMENT_TAG);
                break;
            case R.id.ly_order:
                reset();
                contentMainBinding.tvOrder.setTextColor(ContextCompat.getColor(this,R.color.colorPrimary));;
                contentMainBinding.imgOrder.setBackgroundResource(R.mipmap.order_choose);
                //set ly_order style
                setFragment(orderFragment, ORDER_FRAGMENT_TAG);
                break;
            case R.id.ly_chat:
                if(UserEntity.getCurrentUser()==null){
                    startActivity(new Intent(MainActivity.this,LoginRegisterActivity.class));
                    return;
                }
                reset();
                contentMainBinding.tvChat.setTextColor(ContextCompat.getColor(this,R.color.colorPrimary));;
                contentMainBinding.imgChat.setBackgroundResource(R.mipmap.chat_choose);
                //set ly_chat style
                setFragment(chatFragment, CHAT_FRAGMENT_TAG);
                break;
            case R.id.ly_service_manager:
                if(UserEntity.getCurrentUser()==null){
                    startActivity(new Intent(MainActivity.this,LoginRegisterActivity.class));
                    return;
                }
                reset();
                contentMainBinding.tvManager.setTextColor(ContextCompat.getColor(this,R.color.colorPrimary));;
                contentMainBinding.imgManager.setBackgroundResource(R.mipmap.manager_choose);
                //set ly_service_manager style
                setFragment(serviceManagerFragment, SERVICE_MANAGER_FRAGMENT_TAG);
                break;
            case R.id.ly_my:
                if(UserEntity.getCurrentUser()==null){
                    startActivity(new Intent(MainActivity.this,LoginRegisterActivity.class));
                    return;
                }
                reset();
                contentMainBinding.tvMy.setTextColor(ContextCompat.getColor(this,R.color.colorPrimary));
                contentMainBinding.imgMy.setBackgroundResource(R.mipmap.my_choose);
                //set ly_my style
                setFragment(myFragment, MY_FRAGMENT_TAG);
                break;
            default:
                break;
        }
    }


    private Fragment currentFragment;


    private void reset(){
        //TODO:重置按钮样式为初始状态
        contentMainBinding.imgChat.setBackgroundResource(R.mipmap.chat_not_choose);
        contentMainBinding.imgHome.setBackgroundResource(R.mipmap.home_not_choose);
        contentMainBinding.imgManager.setBackgroundResource(R.mipmap.manager_not_choose);
        contentMainBinding.imgOrder.setBackgroundResource(R.mipmap.order_not_choose);
        contentMainBinding.imgMy.setBackgroundResource(R.mipmap.my_not_choose);
        contentMainBinding.tvChat.setTextColor(ContextCompat.getColor(this,R.color.textColor));
        contentMainBinding.tvHome.setTextColor(ContextCompat.getColor(this,R.color.textColor));
        contentMainBinding.tvManager.setTextColor(ContextCompat.getColor(this,R.color.textColor));;
        contentMainBinding.tvOrder.setTextColor(ContextCompat.getColor(this,R.color.textColor));;
        contentMainBinding.tvMy.setTextColor(ContextCompat.getColor(this,R.color.textColor));;

    }
    private static String currentFragmentTag;

    private void setFragment(Fragment fragment, String tag){
        FragmentManager fragmentManager = getSupportFragmentManager();

        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.hide(mainFragment);
        transaction.hide(orderFragment);
        transaction.hide(myFragment);
        transaction.hide(chatFragment);
        transaction.hide(serviceManagerFragment);
        transaction.show(fragment);
        transaction.commit();
        currentFragmentTag = tag;

        Log.i("balala", tag);
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
