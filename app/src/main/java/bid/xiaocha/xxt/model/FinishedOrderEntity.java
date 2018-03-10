package bid.xiaocha.xxt.model;

import com.google.gson.Gson;

public class FinishedOrderEntity {

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
	private String refundResult;//退款理由
	private short serveType;//服务对象类型 例如发起者是服务还是被服务的
	private static Gson gson = new Gson();
	
	public static final short STATE_WAIT_FOR_APPRAISE = 0;//0.待评价
	public static final short STATE_PROVIDER_HAVE_APPRAISED = 1;//1.服务方已评
	public static final short STATE_DEMANDER_HAVE_APPRAISED = 2;//2.被服务方已评
	public static final short STATE_HAVE_FINISHED = 3;//3.已完成
	public static final short STATE_HAVE_CANCELED = 4;//4.已取消
	
	
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

	public static final int TYPE_TECHNOLOGY = 0;
    public static final int TYPE_LIFE = 1;
    public static final int TYPE_OTHERS = 2;

	@Override
	public String toString() {
		return gson.toJson(this);
	}
	public static FinishedOrderEntity getFinishedOrderEntityFromJson(String json){
		return gson.fromJson(json,FinishedOrderEntity.class);
	}
}
