package bid.xiaocha.xxt.ui.fragment;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import bid.xiaocha.xxt.R;
import bid.xiaocha.xxt.databinding.FragmentMyBinding;
import bid.xiaocha.xxt.model.UserEntity;
import bid.xiaocha.xxt.ui.activity.LoginRegisterActivity;
import bid.xiaocha.xxt.ui.activity.MyInfoActivity;
import bid.xiaocha.xxt.ui.activity.ShowOrChooseAddressActivity;
import bid.xiaocha.xxt.util.HeadProtraitUtil;

public class MyFragment extends Fragment implements View.OnClickListener{

    private FragmentMyBinding binding;
    private UserEntity currentUser;


    public static MyFragment getFragment(){
        MyFragment fragment = new MyFragment();
        return fragment;
    }




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_my, container, false);

        binding.lyCollect.setOnClickListener(this);
        binding.lyInformation.setOnClickListener(this);
        binding.lyShowAddress.setOnClickListener(this);
        binding.lyWallet.setOnClickListener(this);
        binding.lySetting.setOnClickListener(this);

        return binding.getRoot();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ly_collect:

                break;
            case R.id.ly_information:
                if (currentUser != null)
                    startActivity(new Intent(getActivity(),MyInfoActivity.class));
                else
                    startActivity(new Intent(getActivity(),LoginRegisterActivity.class));
                break;
            case R.id.ly_wallet:
                break;
            case R.id.ly_show_address:
                Intent intent= new Intent(getActivity(),ShowOrChooseAddressActivity.class);
                Bundle bundle = new Bundle();
                bundle.putBoolean("isChoosing",false);
                intent.putExtras(bundle);
                startActivity(intent);
                break;
            case R.id.ly_setting:
                break;

        }
    }

    @Override
    public void onResume() {
        super.onResume();
        currentUser = UserEntity.getCurrentUser();
        if (currentUser == null){
            binding.imgHead.setImageDrawable(ContextCompat.getDrawable(getActivity(),R.mipmap.head2));
            binding.tvNickName.setText("请登录");
            binding.tvPhone.setText("");
        }else {
            HeadProtraitUtil.getHeadPic(currentUser.getUserId(), new HeadProtraitUtil.GetHeadCallback() {
                @Override
                public void getHeadPhoto(Bitmap bitmap) {
                    if (bitmap == null) {
                        binding.imgHead.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.mipmap.head2));
                    } else {
                        binding.imgHead.setImageBitmap(bitmap);
                    }
                }
            });
            binding.tvNickName.setText(currentUser.getNickName());
            binding.tvPhone.setText(currentUser.getUserId());
        }
    }
}
