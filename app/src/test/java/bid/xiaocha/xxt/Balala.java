package bid.xiaocha.xxt;

import org.junit.Test;

import java.io.IOException;

import bid.xiaocha.xxt.iview.ILoginView;
import bid.xiaocha.xxt.presenter.ILoginPresenter;
import bid.xiaocha.xxt.presenter.LoginPresenter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.GetJwtCallback;
import retrofit2.Jwt;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Headers;

/**
 * Created by 55039 on 2017/11/5.
 */

public class Balala {

    interface aowu{
        @GET("/aaa")
        @Jwt
        Call<String> balala();
    }


    @Test
    public void testBalala(){
         Retrofit retrofit = new Retrofit.Builder().baseUrl("http://127.0.0.1:8080").addConverterFactory(GsonConverterFactory.create())
                 .addConverterFactory(ScalarsConverterFactory.create()).enableJwt(new GetJwtCallback() {
                     @Override
                     public String getJwt() {
                         return "ccc";
                     }
                 }).build();


         aowu ao = retrofit.create(aowu.class);

        try {
            int code = ao.balala().execute().code();
            System.out.println(code);
        } catch (IOException e) {
            e.printStackTrace();
        }

        LoginPresenter presenter = new LoginPresenter(new ILoginView() {
            @Override
            public void showLoading() {

            }

            @Override
            public void showLoginResult(ILoginPresenter.LoginResult result) {

            }

            @Override
            public void dismissLoading() {

            }
        });

//        presenter.loginByPassword();
    }
}
