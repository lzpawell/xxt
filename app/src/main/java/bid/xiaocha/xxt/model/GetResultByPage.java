package bid.xiaocha.xxt.model;

import java.util.List;

/**
 * Created by lenovo-pc on 2017/11/30.
 */

public class GetResultByPage<T> {
    private List<T> dataList;
    private boolean isHaveMore;
    public List<T> getDataList() {
        return dataList;
    }
    public void setDataList(List<T> dataList) {
        this.dataList = dataList;
    }
    public boolean isHaveMore() {
        return isHaveMore;
    }
    public void setHaveMore(boolean isHaveMore) {
        this.isHaveMore = isHaveMore;
    }
}
