package bid.xiaocha.xxt.model;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import bid.xiaocha.xxt.service.NetService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by 55039 on 2017/10/15.
 */

public class ActiveOrderEntity {

    private String orderId;//订单号

    private String serveId;

    private String servePublisherId;//发行者id

    private String serveReceiverId;

    private String content;//服务详情详细信息

    private String title;//服务标题

    private String place;//地点

    private String phone;//电话

    private Double price;//价格

    private long buildDate;//订单生成时间

    private long startDate;//订单开始时间

    private long finishDate;//订单结束时间

    private int type;//类型 0.生活类型 1.技术类型 2.其他类型

    private short state;//状态

    private short cancelState;//取消状态

    private String refundResult;//退款理由

    private short serveType;//服务对象类型 例如发起者是服务还是被服务的

    public static final short STATE_WAIT_AGREE_CREATE = 0;//待接单
    public static final short STATE_BEGIN_SERVE = 1;//已接单，服务可以开始了
    public static final short STATE_WAIT_FOR_CONFIRM = 2;//已完成，待确认完成
    public static final short CANCEL_STATE_NOT_CANCEL = 0;//未取消
    public static final short CANCEL_STATE_PUBLISHER_HAS_CANCEL = 1;//服务发布者已取消
    public static final short CANCEL_STATE_RECEIVER_HAS_CANCEL = 2;//服务接单者已取消


    public static final int TYPE_TECHNOLOGY = 0;
    public static final int TYPE_LIFE = 1;
    public static final int TYPE_OTHERS = 2;
    public static final short NEED_SERVE = 0;
    public static final short OFFER_SERVE = 1;

    public static final short STATE_WAIT_FOR_APPRAISE = 0;//0.待评价
    public static final short STATE_PROVIDER_HAVE_APPRAISED = 1;//1.服务方已评
    public static final short STATE_DEMANDER_HAVE_APPRAISED = 2;//2.被服务方已评
    public static final short STATE_HAVE_FINISHED = 3;//3.已完成
    public static final short STATE_HAVE_CANCELED = 4;//4.已取消
    private static Gson gson = new Gson();

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getServeId() {
        return serveId;
    }

    public void setServeId(String serveId) {
        this.serveId = serveId;
    }

    public String getServePublisherId() {
        return servePublisherId;
    }

    public void setServePublisherId(String servePublisherId) {
        this.servePublisherId = servePublisherId;
    }

    public String getServeReceiverId() {
        return serveReceiverId;
    }

    public void setServeReceiverId(String serveReceiverId) {
        this.serveReceiverId = serveReceiverId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public long getBuildDate() {
        return buildDate;
    }

    public void setBuildDate(long buildDate) {
        this.buildDate = buildDate;
    }

    public long getStartDate() {
        return startDate;
    }

    public void setStartDate(long startDate) {
        this.startDate = startDate;
    }

    public long getFinishDate() {
        return finishDate;
    }

    public void setFinishDate(long finishDate) {
        this.finishDate = finishDate;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public short getState() {
        return state;
    }

    public void setState(short state) {
        this.state = state;
    }

    public String getRefundResult() {
        return refundResult;
    }

    public void setRefundResult(String refundResult) {
        this.refundResult = refundResult;
    }

    public short getServeType() {
        return serveType;
    }

    public void setServeType(short serveType) {
        this.serveType = serveType;
    }

    public short getCancelState() {
        return cancelState;
    }
    public void setCancelState(short cancelState) {
        this.cancelState = cancelState;
    }


    public static void getActiveOrderByPage(String jwt, String userId, int pageNum ,final OnGetOrderByPageResult onGetOrderByPageResult){
        Call<GetResultByPage<ActiveOrderEntity>> call = NetService.getInstance().getMyActiveOrderByPage(jwt,userId,pageNum);
        call.enqueue(new Callback<GetResultByPage<ActiveOrderEntity>>() {
            @Override
            public void onResponse(Call<GetResultByPage<ActiveOrderEntity>> call, Response<GetResultByPage<ActiveOrderEntity>> response) {
                if(response.code() == 200){
                    GetResultByPage<ActiveOrderEntity> result = response.body();
                    onGetOrderByPageResult.getOrderByPage(result.isHaveMore(),result.getDataList());
                }else{
                    onFailure(call,new Throwable(response.code()+""));
                }
            }

            @Override
            public void onFailure(Call<GetResultByPage<ActiveOrderEntity>> call, Throwable t) {
                t.printStackTrace();
                onGetOrderByPageResult.getOrderByPageFail();
            }
        });
    }
    public static void getFinishOrderByPage(String jwt, String userId, int pageNum ,final OnGetOrderByPageResult onGetOrderByPageResult){
        Call<GetResultByPage<FinishedOrderEntity>> call = NetService.getInstance().getMyFinishOrderByPage(jwt, userId, pageNum);
        call.enqueue(new Callback<GetResultByPage<FinishedOrderEntity>>() {
            @Override
            public void onResponse(Call<GetResultByPage<FinishedOrderEntity>> call, Response<GetResultByPage<FinishedOrderEntity>> response) {
                if(response.code() == 200){
                    GetResultByPage<FinishedOrderEntity> result = response.body();
                    onGetOrderByPageResult.getOrderByPage(result.isHaveMore(),result.getDataList());
                }else{
                    onFailure(call,new Throwable(response.code()+""));
                }
            }

            @Override
            public void onFailure(Call<GetResultByPage<FinishedOrderEntity>> call, Throwable t) {
                t.printStackTrace();
                onGetOrderByPageResult.getOrderByPageFail();
            }
        });
    }

    public static interface OnGetOrderByPageResult<T>{
        void getOrderByPage(Boolean isHaveMove, List<T> orderServeList);
        void getOrderByPageFail();
    }

    @Override
    public String toString() {
        return gson.toJson(this);
    }
    public static ActiveOrderEntity getActiveOrderEntityFromJson(String jsonString){
        return gson.fromJson(jsonString,ActiveOrderEntity.class);
    }




    //    public static ActiveOrderEntity getActiveOrderEntityFromJson(String jsonString) throws JSONException {
//        ActiveOrderEntity orderEntity = new ActiveOrderEntity();
//        JSONObject json = new JSONObject(jsonString);
//        long buildDate = 0;
//        long startDate = 0;
//        long finishDate = 0;
//        if (json.has("buildDate")){
//            buildDate = json.getLong("buildDate");
//            json.remove("buildDate");
//        }
//        if (json.has("startDate")){
//            buildDate = json.getLong("startDate");
//            json.remove("startDate");
//        }
//        if (json.has("finishDate")){
//            buildDate = json.getLong("finishDate");
//            json.remove("finishDate");
//        }
//        Gson gson = new Gson();
//        orderEntity = gson.fromJson(json.toString(),ActiveOrderEntity.class);
//        if (buildDate != 0){
//            orderEntity.setBuildDate();
//        }
//        return orderEntity;
//    }
}
