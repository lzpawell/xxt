package bid.xiaocha.xxt.presenter;

import bid.xiaocha.xxt.iview.IShowUserInfoView;
import bid.xiaocha.xxt.model.UserEntity;

/**
 * Created by lenovo-pc on 2018/1/13.
 */

public class ShowUserInfoPrenster implements IShowUserInfoPresenter {
    private IShowUserInfoView view;
    private String userId;

    public ShowUserInfoPrenster(IShowUserInfoView view, String userId) {
        this.view = view;
        this.userId = userId;
    }

    @Override
    public void getUserInfo(){
        view.showLoading();
        if (userId != null)
            UserEntity.getUserByUserId(userId, new UserEntity.OnGetUserByUserIdResult() {
                @Override
                public void getUserByIdResult(boolean result, UserEntity userEntity) {
                    if (result){
                        view.showUserInfo(userEntity);
                        view.dismissLoading();
                    }else{
                        view.showUserInfo(null);
                        view.dismissLoading();
                    }
                }
            });
        else{
            view.showUserInfo(null);
            view.dismissLoading();
        }
    }

//    @Override
//    public void getUserOfferServeBypage(String whatSort, int pageNum) {
//        if (userId == null ||userId.equals("")){
//            view.showOfferServeCommentListFail();
//            view.dismissLoading();
//            return;
//        }
//        Call<GetResultByPage<OfferServeEntity>> call = NetService.getInstance().getOfferServeByUserIdAndPage(JwtUtil.getJwt(), pageNum, whatSort, userId);
//        call.enqueue(new Callback<GetResultByPage<OfferServeEntity>>() {
//            @Override
//            public void onResponse(Call<GetResultByPage<OfferServeEntity>> call, Response<GetResultByPage<OfferServeEntity>> response) {
//                if (response.code() == 200){
//                    view.showOfferServeSuccess(response.body().getDataList(),response.body().isHaveMore());
//                    view.dismissLoading();
//                }else{
//                    onFailure(call,new Throwable(response.code() + ""));
//                }
//            }
//
//            @Override
//            public void onFailure(Call<GetResultByPage<OfferServeEntity>> call, Throwable t) {
//                t.printStackTrace();
//                view.showOfferServeCommentListFail();
//                view.dismissLoading();
//            }
//        });
//    }
}
