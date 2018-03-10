package bid.xiaocha.xxt.model;

import android.util.Log;

import com.google.gson.Gson;

import java.util.List;

import bid.xiaocha.xxt.service.NetService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class NeedServeEntity {
    private String serveId;//服务号
    private String publishUserId;//发行者id
    private String content;//服务详情详细信息
    private String title;//服务标题
    private String place;//地点
    private String phone;//电话
    private Double price;//价格
    private long publishdate;//发布时间
    private int type;//类型 0.生活类型 1.技术类型 2.其他类型
    private int state;//状态
    private double longitude;//经度
    private double latitude;//纬度
    private String userName;//“收货人”姓名
    private static Gson gson = new Gson();


    public static final int TYPE_TECHNOLOGY = 0;
    public static final int TYPE_LIFE = 1;
    public static final int TYPE_OTHERS = 2;
	public static final int STOP_SERVE = 0;
	public static final int START_SERVE = 1;
	public static final int DELECT_SERVE = 2;

	public String getServeId() {
		return serveId;
	}
	public void setServeId(String serveId) {
		this.serveId = serveId;
	}
	public String getPublishUserId() {
		return publishUserId;
	}
	public void setPublishUserId(String publishUserId) {
		this.publishUserId = publishUserId;
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
	public long getPublishdate() {
		return publishdate;
	}
	public void setPublishdate(long publishdate) {
		this.publishdate = publishdate;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
	public double getLongitude() {
		return longitude;
	}
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	public double getLatitude() {
		return latitude;
	}
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public static void getNeedServesByPage(int pageNum , String whatSort , final OnGetNeedServesByPageResult onGetNeedServesByPageResult){
        Log.i("cccc",pageNum +"   "+whatSort+"    ");
        Call<GetResultByPage<NeedServeEntity>> call = NetService.getInstance().getNeedServesByPages(pageNum, whatSort);
        call.enqueue(new Callback<GetResultByPage<NeedServeEntity>>() {
            @Override
            public void onResponse(Call<GetResultByPage<NeedServeEntity>> call, Response<GetResultByPage<NeedServeEntity>> response) {
				if(response.code() == 200) {
					GetResultByPage<NeedServeEntity> result = response.body();
					Log.i("ccc", result + "");
					onGetNeedServesByPageResult.getNeedServesByPageSuccess(result.isHaveMore(), result.getDataList());
				}else{
					onFailure(call, new Throwable(response.code()+""));
				}
            }

            @Override
			public void onFailure(Call<GetResultByPage<NeedServeEntity>> call, Throwable t) {
				t.printStackTrace();
				onGetNeedServesByPageResult.getNeedServesByPageFail();
			}
        });
    }
	public static void getMyNeedServesByPage(String jwt,int pageNum , String whatSort , String userId, final OnGetNeedServesByPageResult onGetNeedServesByPageResult){
		Log.i("cccc",pageNum +"   "+whatSort+"    "+userId);
		Call<GetResultByPage<NeedServeEntity>> call = NetService.getInstance().getMyNeedServesByPages(jwt,pageNum, whatSort, userId);
		call.enqueue(new Callback<GetResultByPage<NeedServeEntity>>() {
			@Override
			public void onResponse(Call<GetResultByPage<NeedServeEntity>> call, Response<GetResultByPage<NeedServeEntity>> response) {
				if(response.code() == 200) {
					GetResultByPage<NeedServeEntity> result = response.body();
					Log.i("ccc", result + "");
					onGetNeedServesByPageResult.getNeedServesByPageSuccess(result.isHaveMore(), result.getDataList());
				}else{
					onFailure(call, new Throwable(response.code()+""));
				}
			}

			@Override
			public void onFailure(Call<GetResultByPage<NeedServeEntity>> call, Throwable t) {
				t.printStackTrace();
				onGetNeedServesByPageResult.getNeedServesByPageFail();
			}
		});
	}

    public interface OnGetNeedServesByPageResult{
        void getNeedServesByPageSuccess(Boolean isHaveMove, List<NeedServeEntity> needServeList);
        void getNeedServesByPageFail();
    }

    @Override
    public String toString() {
        return gson.toJson(this);
    }
    public static NeedServeEntity getNeedServeEntityFromJson(String json){
        return gson.fromJson(json, NeedServeEntity.class);
    }
}
