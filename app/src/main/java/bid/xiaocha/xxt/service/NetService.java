package bid.xiaocha.xxt.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.TimeUnit;

import bid.xiaocha.xxt.util.JwtUtil;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.GetJwtCallback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * Created by 55039 on 2017/10/14.
 */

public class NetService{
    private static INetService iNetService;
    public static final String BASE_URL = "http://192.168.31.150:8080/";
    public static INetService getInstance(){
        if (iNetService == null){
            synchronized (NetService.class){
                if(iNetService == null){
                    init();
                }
            }
        }
        return iNetService;
    }

    private static void init(){
        OkHttpClient client = new OkHttpClient().newBuilder()
                .connectTimeout(4, TimeUnit.SECONDS)
                .readTimeout(6,TimeUnit.SECONDS)
                .writeTimeout(6,TimeUnit.SECONDS)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .client(client)
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addConverterFactory(ScalarsConverterFactory.create()).build();

        iNetService = retrofit.create(INetService.class);
    }


    public static boolean saveFile(ResponseBody body,File file) {
        InputStream inputStream = body.byteStream();
        OutputStream outputStream = null;
        byte[] reader = new byte[4096];
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
            outputStream = new FileOutputStream(file);
            while (true) {
                int end = inputStream.read(reader);
                if (end == -1) {
                    outputStream.flush();
                    break;
                }
                outputStream.write(reader, 0, end);

            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (inputStream != null)
                    inputStream.close();
                if (outputStream != null)
                    outputStream.close();
                return true;
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }
    }
}
