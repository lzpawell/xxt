package bid.xiaocha.xxt.iview;

import java.util.List;

import bid.xiaocha.xxt.model.CommentEntity;
import bid.xiaocha.xxt.model.ServeCommentEntity;

/**
 * Created by 55039 on 2018/3/4.
 */

public interface IshowPersonalCommentActivityView extends IBaseActivityView{
    void showPersonalCommentSuccess(List<CommentEntity> commentList, boolean isHaveMore);
    void showPersonalCommentFail();
}
