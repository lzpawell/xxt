package bid.xiaocha.xxt.presenter;

import android.util.Log;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import bid.xiaocha.xxt.iview.IControlServeView;
import bid.xiaocha.xxt.model.NeedServeEntity;
import bid.xiaocha.xxt.model.OfferServeEntity;
import bid.xiaocha.xxt.model.UserEntity;
import bid.xiaocha.xxt.service.NetService;
import bid.xiaocha.xxt.util.JwtUtil;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static bid.xiaocha.xxt.iview.IControlServeView.CHANGE_SUCCESS;
import static bid.xiaocha.xxt.iview.IControlServeView.DELETE_SUCCESS;
import static bid.xiaocha.xxt.iview.IControlServeView.ERROR_SHOW_STRING;

/**
 * Created by lenovo-pc on 2017/12/9.
 */

public class ControlServePresenter implements  IControlServePresenter {

    IControlServeView view;
    private static Gson gson = new Gson();

    public ControlServePresenter(IControlServeView view){
        this.view = view;
    }

    @Override
    public void startOfferServe(String orderId, final int position) {
        Log.i("ljf","222");
        view. showLoading();
        String userId = UserEntity.getCurrentUser().getUserId();
        Call<Object> call = NetService.getInstance().startOfferServe(JwtUtil.getJwt(),userId,orderId);
        call.enqueue(new Callback<Object>() {

            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                if(response.code() == 200){
                    Object object = response.body();
                    JSONObject json;
                    OfferServeEntity offerServeEntity = null;
                    int result = -1;
                    try {
                        json = new JSONObject(object.toString());
                        if (json.has("result"))
                            result = json.getInt("result");
                        if (json.has("serveEntity")) {
                            offerServeEntity = gson.fromJson(json.getString("serveEntity"),OfferServeEntity.class);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        view.dismissLoading();
                        view.showResult(ERROR_SHOW_STRING,"解析数据失败请重试",position);
                    }
                    switch (result){
                        case -1:
                            view.dismissLoading();
                            view.showResult(ERROR_SHOW_STRING,"获取数据，失败请重试",position);
                            break;
                        case 0:
                            view.dismissLoading();
                            view.showResult(CHANGE_SUCCESS,offerServeEntity,position);
                            break;
                        case 1:
                            view.dismissLoading();
                            view.showResult(ERROR_SHOW_STRING,"非法操作",position);
                            break;
                        case 2:
                            view.dismissLoading();
                            view.showResult(ERROR_SHOW_STRING,"服务器错误",position);
                            break;

                    }
                }
                else{
                    onFailure(call,new Throwable(""+response.code()));
                }

            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                t.printStackTrace();
                view.dismissLoading();
                view.showResult(ERROR_SHOW_STRING,"网络错误",position);
            }
        });
    }

    @Override
    public void stopOfferServe(String orderId,final int position) {
        view. showLoading();
        String userId = UserEntity.getCurrentUser().getUserId();
        Call<Object> call = NetService.getInstance().stopOfferServe(JwtUtil.getJwt(),userId, orderId);
        call.enqueue(new Callback<Object>() {

            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                if(response.code() == 200){
                    Object object = response.body();
                    JSONObject json;
                    OfferServeEntity offerServeEntity = null;
                    int result = -1;
                    try {
                        Log.i("ljf",object.toString());
                        json = new JSONObject(object.toString());
                        if (json.has("result"))
                            result = json.getInt("result");
                        if (json.has("serveEntity")) {
                            offerServeEntity = gson.fromJson(json.getString("serveEntity"),OfferServeEntity.class);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        view.dismissLoading();
                        view.showResult(ERROR_SHOW_STRING,"解析数据失败请重试",position);
                    }
                    switch (result){
                        case -1:
                            view.dismissLoading();
                            view.showResult(ERROR_SHOW_STRING,"获取数据，失败请重试",position);
                            break;
                        case 0:
                            view.dismissLoading();
                            view.showResult(CHANGE_SUCCESS,offerServeEntity,position);
                            break;
                        case 1:
                            view.dismissLoading();
                            view.showResult(ERROR_SHOW_STRING,"非法操作",position);
                            break;
                        case 2:
                            view.dismissLoading();
                            view.showResult(ERROR_SHOW_STRING,"服务器错误",position);
                            break;

                    }
                }
                else{
                    onFailure(call,new Throwable(""+response.code()));
                }

            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                t.printStackTrace();
                view.dismissLoading();
                view.showResult(ERROR_SHOW_STRING,"网络错误",position);
            }
        });
    }

    @Override
    public void deleteOfferServe(String orderId,final int position) {
        view. showLoading();
        String userId = UserEntity.getCurrentUser().getUserId();
        Call<Object> call = NetService.getInstance().deleteOfferServe(JwtUtil.getJwt(),userId, orderId);
        call.enqueue(new Callback<Object>() {

            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                if(response.code() == 200){
                    Object object = response.body();
                    JSONObject json;
                    OfferServeEntity offerServeEntity = null;
                    int result = -1;
                    try {
                        json = new JSONObject(object.toString());
                        if (json.has("result"))
                            result = json.getInt("result");

                    } catch (JSONException e) {
                        e.printStackTrace();
                        view.dismissLoading();
                        view.showResult(ERROR_SHOW_STRING,"解析数据失败请重试",position);
                    }
                    switch (result){
                        case -1:
                            view.dismissLoading();
                            view.showResult(ERROR_SHOW_STRING,"获取数据，失败请重试",position);
                            break;
                        case 0:
                            view.dismissLoading();
                            view.showResult(DELETE_SUCCESS,"删除成功",position);
                            break;
                        case 1:
                            view.dismissLoading();
                            view.showResult(ERROR_SHOW_STRING,"非法操作",position);
                            break;
                        case 2:
                            view.dismissLoading();
                            view.showResult(ERROR_SHOW_STRING,"服务器错误",position);
                            break;

                    }
                }
                else{
                    onFailure(call,new Throwable(""+response.code()));
                }

            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                t.printStackTrace();
                view.dismissLoading();
                view.showResult(ERROR_SHOW_STRING,"网络错误",position);
            }
        });
    }

    @Override
    public void startNeedServe(String orderId,final int position) {
        view. showLoading();
        String userId = UserEntity.getCurrentUser().getUserId();
        Call<Object> call = NetService.getInstance().startNeedServe(JwtUtil.getJwt(),userId, orderId);
        call.enqueue(new Callback<Object>() {

            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                if(response.code() == 200){
                    Object object = response.body();
                    JSONObject json;
                    NeedServeEntity needServeEntity = null;
                    int result = -1;
                    try {
                        json = new JSONObject(object.toString());
                        if (json.has("result"))
                            result = json.getInt("result");
                        if (json.has("serveEntity")) {
                            needServeEntity = gson.fromJson(json.getString("serveEntity"),NeedServeEntity.class);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        view.dismissLoading();
                        view.showResult(ERROR_SHOW_STRING,"解析数据失败请重试",position);
                    }
                    switch (result){
                        case -1:
                            view.dismissLoading();
                            view.showResult(ERROR_SHOW_STRING,"获取数据，失败请重试",position);
                            break;
                        case 0:
                            view.dismissLoading();
                            view.showResult(CHANGE_SUCCESS,needServeEntity,position);
                            break;
                        case 1:
                            view.dismissLoading();
                            view.showResult(ERROR_SHOW_STRING,"非法操作",position);
                            break;
                        case 2:
                            view.dismissLoading();
                            view.showResult(ERROR_SHOW_STRING,"服务器错误",position);
                            break;

                    }
                }
                else{
                    onFailure(call,new Throwable(""+response.code()));
                }

            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                t.printStackTrace();
                view.dismissLoading();
                view.showResult(ERROR_SHOW_STRING,"网络错误",position);
            }
        });
    }

    @Override
    public void stopNeedServe(String orderId,final int position) {
        view. showLoading();
        String userId = UserEntity.getCurrentUser().getUserId();
        Call<Object> call = NetService.getInstance().stopNeedServe(JwtUtil.getJwt(),userId,orderId);
        call.enqueue(new Callback<Object>() {

            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                if(response.code() == 200){
                    Object object = response.body();
                    JSONObject json;
                    NeedServeEntity needServeEntity = null;
                    int result = -1;
                    try {
                        json = new JSONObject(object.toString());
                        if (json.has("result"))
                            result = json.getInt("result");
                        if (json.has("serveEntity")) {
                            needServeEntity = gson.fromJson(json.getString("serveEntity"),NeedServeEntity.class);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        view.dismissLoading();
                        view.showResult(ERROR_SHOW_STRING,"解析数据失败请重试",position);
                    }
                    switch (result){
                        case -1:
                            view.dismissLoading();
                            view.showResult(ERROR_SHOW_STRING,"获取数据，失败请重试",position);
                            break;
                        case 0:
                            view.dismissLoading();
                            view.showResult(CHANGE_SUCCESS,needServeEntity,position);
                            break;
                        case 1:
                            view.dismissLoading();
                            view.showResult(ERROR_SHOW_STRING,"非法操作",position);
                            break;
                        case 2:
                            view.dismissLoading();
                            view.showResult(ERROR_SHOW_STRING,"服务器错误",position);
                            break;

                    }
                }
                else{
                    onFailure(call,new Throwable(""+response.code()));
                }

            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                t.printStackTrace();
                view.dismissLoading();
                view.showResult(ERROR_SHOW_STRING,"网络错误",position);
            }
        });
    }

    @Override
    public void deleteNeedServe(String orderId,final int position) {
        view. showLoading();
        String userId = UserEntity.getCurrentUser().getUserId();
        Call<Object> call = NetService.getInstance().deleteNeedServe(JwtUtil.getJwt(),userId, orderId);
        call.enqueue(new Callback<Object>() {

            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                if(response.code() == 200){
                    Object object = response.body();
                    JSONObject json;
                    int result = -1;
                    try {
                        json = new JSONObject(object.toString());
                        if (json.has("result"))
                            result = json.getInt("result");

                    } catch (JSONException e) {
                        e.printStackTrace();
                        view.dismissLoading();
                        view.showResult(ERROR_SHOW_STRING,"解析数据失败请重试",position);
                    }
                    switch (result){
                        case -1:
                            view.dismissLoading();
                            view.showResult(ERROR_SHOW_STRING,"获取数据，失败请重试",position);
                            break;
                        case 0:
                            view.dismissLoading();
                            view.showResult(DELETE_SUCCESS,"删除成功",position);
                            break;
                        case 1:
                            view.dismissLoading();
                            view.showResult(ERROR_SHOW_STRING,"非法操作",position);
                            break;
                        case 2:
                            view.dismissLoading();
                            view.showResult(ERROR_SHOW_STRING,"服务器错误",position);
                            break;

                    }
                }
                else{
                    onFailure(call,new Throwable(""+response.code()));
                }

            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                t.printStackTrace();
                view.dismissLoading();
                view.showResult(ERROR_SHOW_STRING,"网络错误",position);
            }
        });
    }
}
