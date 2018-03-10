package bid.xiaocha.xxt.presenter;

import android.app.Activity;
import android.graphics.Bitmap;

import bid.xiaocha.xxt.iview.IMyInfoView;
import bid.xiaocha.xxt.model.UserEntity;
import bid.xiaocha.xxt.service.NetService;
import bid.xiaocha.xxt.util.HeadProtraitUtil;
import bid.xiaocha.xxt.util.JwtUtil;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by 55039 on 2017/11/1.
 */

public class MyInfoPresenter implements IMyInfoPresenter {
    private IMyInfoView view;

    public MyInfoPresenter(IMyInfoView view) {
        this.view = view;
    }

    @Override
    public void updateMyInfo(final UserEntity userEntity) {
        view.showLoading();
        Call<Boolean> booleanCall = NetService.getInstance().updateUserInfo(JwtUtil.getJwt(),userEntity);
        booleanCall.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if(response.code() == 200) {
                    UserEntity.setCurrentUser(userEntity);
                    view.dismissLoading();
                    view.showUpdateResult(response.body());
                }else{
                    onFailure(call, new Throwable(response.code()+""));
                }

            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                view.dismissLoading();
                view.showUpdateResult(false);
                t.printStackTrace();
                //Log.i("balalabalala", );
            }
        });
    }

    @Override
    public void choosePic(Activity activity, int which) {
        view.showLoading();
        HeadProtraitUtil.UploadCallback callback = new HeadProtraitUtil.UploadCallback() {
            @Override
            public void getUploadPhoto(Bitmap bitmap) {
                view.dismissLoading();
                view.showHeadPic(bitmap);
            }
        };
        if (which == 0){
            HeadProtraitUtil.ChoosePic(activity,HeadProtraitUtil.CHOOSE_PICTURE,callback);
        }else{
            HeadProtraitUtil.ChoosePic(activity,HeadProtraitUtil.TAKE_PICTURE,callback);
        }
    }


}
