package bid.xiaocha.xxt.model;


import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;

import bid.xiaocha.xxt.service.NetService;
import bid.xiaocha.xxt.util.App;
import bid.xiaocha.xxt.rongCloud.RongManager;
import bid.xiaocha.xxt.util.HeadProtraitUtil;
import bid.xiaocha.xxt.util.JwtUtil;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserEntity {

	private String userId;
	private String nickName;
	
	
	public static final short SEX_UNDEFINED = 0;
	public static final short SEX_MAN = 1;
	public static final short SEX_WOMAN = 2;
	private short sex;
	

	private double money;
	
	
	//userId + _0 + 文件后缀
	private String picPath;
	private String token;

	private int beHelpedNumber;//被服务时，评论人数
	private int helpNumber;//服务他人时，评论人数
	private double beHelpedMark;//被服务时，评论分数
	private double helpMark;//服务他人时，评论分数

	public int getBeHelpedNumber() {
		return beHelpedNumber;
	}

	public void setBeHelpedNumber(int beHelpedNumber) {
		this.beHelpedNumber = beHelpedNumber;
	}

	public int getHelpNumber() {
		return helpNumber;
	}

	public void setHelpNumber(int helpNumber) {
		this.helpNumber = helpNumber;
	}

	public double getBeHelpedMark() {
		return beHelpedMark;
	}

	public void setBeHelpedMark(double beHelpedMark) {
		this.beHelpedMark = beHelpedMark;
	}

	public double getHelpMark() {
		return helpMark;
	}

	public void setHelpMark(double helpMark) {
		this.helpMark = helpMark;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}


	public double getMoney() {
		return money;
	}

	public void setMoney(double money) {
		this.money = money;
	}

	public String getPicPath() {
		return picPath;
	}

	public short getSex() {
		return sex;
	}

	public void setSex(short sex) {
		this.sex = sex;
	}

	public void setPicPath(String picPath) {
		this.picPath = picPath;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	private static SharedPreferences sharedPreferences;
	private static Gson gson = new Gson();

	public static UserEntity getCurrentUser(){
		if (sharedPreferences == null){
			sharedPreferences = App.getSharedPreferences();
		}
		String currentUser = sharedPreferences.getString("currentUser","");
		if ("".equals(currentUser)){
			return null;
		}
		return gson.fromJson(currentUser,UserEntity.class);
	}

	public static void setCurrentUser(UserEntity userEntity) {
		if (sharedPreferences == null){
			sharedPreferences = App.getSharedPreferences();
		}
		SharedPreferences.Editor editor = sharedPreferences.edit();
		if (userEntity == null){
			editor.putString("currentUser","");
		}else {
			HeadProtraitUtil.updateHeadPic(userEntity,null);
			editor.putString("currentUser", gson.toJson(userEntity));
		}
		editor.commit();
	}
	public static void logout(){
		if (sharedPreferences == null){
			sharedPreferences = App.getSharedPreferences();
		}
		SharedPreferences.Editor editor = sharedPreferences.edit();
		editor.putString("currentUser","");
		editor.putString("jwt","");
		editor.commit();
		RongManager.getInstance().logout();
	}
	public static void getUserByUserId(String userId, final OnGetUserByUserIdResult onGetUserByUserIdResult){
		Call<UserEntity> call = NetService.getInstance().getUserByUserId(JwtUtil.getJwt(),userId);
		call.enqueue(new Callback<UserEntity>() {
			@Override
			public void onResponse(Call<UserEntity> call, Response<UserEntity> response) {
				if (response.code() == 200) {
					UserEntity userEntity = response.body();
					onGetUserByUserIdResult.getUserByIdResult(true, userEntity);
				}else {
					onFailure(call, new Throwable(""));
				}
			}

			@Override
			public void onFailure(Call<UserEntity> call, Throwable t) {
				Log.i("getUserFailure",t.toString());
				onGetUserByUserIdResult.getUserByIdResult(false,null);
			}
		});
	}

	public static void getMoneyByUserId(String userId , final OnGetMoneyByUserIdResult onGetMoneyByUserIdResult){
		Call<Double> call = NetService.getInstance().getMoneyByUserId(JwtUtil.getJwt(),userId);
		call.enqueue(new Callback<Double>() {
			@Override
			public void onResponse(Call<Double> call, Response<Double> response) {
				if(response.code() == 200) {
					onGetMoneyByUserIdResult.getMoneyByUserIdResult(true, response.body());
				}else {
					onFailure(call , new Throwable(response.code()+""));
				}
			}

			@Override
			public void onFailure(Call<Double> call, Throwable t) {
				t.printStackTrace();
				onGetMoneyByUserIdResult.getMoneyByUserIdResult(false,0);
			}
		});
	}

	public interface OnGetUserByUserIdResult{
		void getUserByIdResult(boolean result,UserEntity userEntity);
	}
	public interface OnGetMoneyByUserIdResult{
		void getMoneyByUserIdResult(boolean isSuccess,double money);
	}

}