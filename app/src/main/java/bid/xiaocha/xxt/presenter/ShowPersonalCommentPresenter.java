package bid.xiaocha.xxt.presenter;

import bid.xiaocha.xxt.iview.IshowPersonalCommentActivityView;
import bid.xiaocha.xxt.model.CommentEntity;
import bid.xiaocha.xxt.model.GetResultByPage;
import bid.xiaocha.xxt.service.NetService;
import bid.xiaocha.xxt.util.JwtUtil;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by 55039 on 2018/3/4.
 */

public class ShowPersonalCommentPresenter implements IShowPersonalCommentPersenter {
    private String userId;
    private IshowPersonalCommentActivityView view;

    public ShowPersonalCommentPresenter(IshowPersonalCommentActivityView view ,String userId) {
        this.userId = userId;
        this.view = view;
    }

    @Override
    public void getPersonalCommentByPage(int pageNum, String whatSort) {
        if (userId == null || userId.equals("")){
            view.showPersonalCommentFail();
            return;
        }
        Call<GetResultByPage<CommentEntity>> call = NetService.getInstance().getCommentByPage(JwtUtil.getJwt(), pageNum, whatSort, userId, (short) 0);
        call.enqueue(new Callback<GetResultByPage<CommentEntity>>() {
            @Override
            public void onResponse(Call<GetResultByPage<CommentEntity>> call, Response<GetResultByPage<CommentEntity>> response) {
                if (response.code() == 200){
                    view.showPersonalCommentSuccess(response.body().getDataList(),response.body().isHaveMore());
                }else{
                    onFailure(call,new Throwable(response.code()+""));
                }
            }

            @Override
            public void onFailure(Call<GetResultByPage<CommentEntity>> call, Throwable t) {
                t.printStackTrace();
                view.showPersonalCommentFail();
            }
        });
    }
}