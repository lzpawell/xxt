package bid.xiaocha.xxt.util;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import bid.xiaocha.xxt.model.UserEntity;
import bid.xiaocha.xxt.service.NetService;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import okio.BufferedSink;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Multipart;

import static bid.xiaocha.xxt.service.NetService.BASE_URL;

/**
 * Created by 苏颂贤 on 2017/2/13.
 */

public class HeadProtraitUtil {
    public static final int CHOOSE_PICTURE = 0;
    public static final int TAKE_PICTURE = 1;
    private static final int CROP_SMALL_PICTURE = 2;
    protected static Uri tempUri;
    private static UploadCallback uploadCallback = null;
    private static Activity activity;
    private static String LOCAL_HEAD_PATH = DataStore.APP_PATH + File.separator + "head" + File.separator;
    public static String WEB_HEAD_PATH = BASE_URL+"headImg/";
    private static Map<String,Bitmap> picMap = new HashMap<>();
    static{
        File file = new File(DataStore.APP_PATH + File.separator + "head");
        if (!file.exists()){
            file.mkdir();
        }
    }

    public static void updateHeadPic(final UserEntity userEntityWithPicPath, final UpdateHeadCallback callback){
        final String picPath = userEntityWithPicPath.getPicPath();
        final String userId = userEntityWithPicPath.getUserId();
        if (picPath == null || picPath.equals("")){
            return;
        }else{
            final String localPicPath = LOCAL_HEAD_PATH + picPath;
            final File picFile = new File(localPicPath);
            if (picFile.exists()){
                Bitmap pic = BitmapFactory.decodeFile(localPicPath);
                if (picMap.containsKey(userId)){
                    picMap.remove(userId);
                }
                picMap.put(userId,pic);
                if (callback !=  null)
                    callback.updateHeadPhoto(pic);
                return;
            }else{
                Call<ResponseBody> call = NetService.getInstance().downloadFile(WEB_HEAD_PATH + picPath);
                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.body() == null){
                            if (callback !=  null)
                                callback.updateHeadPhoto(null);
                            return;
                        }
                        if(NetService.saveFile(response.body(),picFile)){
                            Bitmap pic = BitmapFactory.decodeFile(localPicPath);
                            if (picMap.containsKey(userId)){
                                picMap.remove(userId);
                            }
                            picMap.put(userId,pic);
                            if (callback !=  null)
                                callback.updateHeadPhoto(pic);
                        }else{
                            Log.i("updateHeadPic","下载头像失败");
                            if (callback !=  null)
                                callback.updateHeadPhoto(null);
                        }
                    }
                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        t.printStackTrace();
                        Log.i("updateHeadPic","下载头像失败");
                        if (callback !=  null)
                            callback.updateHeadPhoto(null);
                    }
                });
            }
        }

    }

    public static void getHeadPic(String userId, final GetHeadCallback getHeadCallback) {
        if (picMap.containsKey(userId)){
            getHeadCallback.getHeadPhoto(picMap.get(userId));
            return;
        }
        getHeadCallback.getHeadPhoto(null);
        UserEntity.getUserByUserId(userId, new UserEntity.OnGetUserByUserIdResult() {
            @Override
            public void getUserByIdResult(boolean result, UserEntity userEntity) {
                if (result){
                    updateHeadPic(userEntity, new UpdateHeadCallback() {
                        @Override
                        public void updateHeadPhoto(Bitmap bitmap) {
                            if (getHeadCallback != null)
                                getHeadCallback.getHeadPhoto(bitmap);
                        }
                    });
                }else{
                    Log.i("getHeadPic","获取头像失败");
                    if (getHeadCallback != null)
                        getHeadCallback.getHeadPhoto(null);
                }
            }
        });

    }

    public static void ChoosePic(final Activity Activity, int which, UploadCallback UploadCallback) {
        activity = Activity;
        uploadCallback = UploadCallback;
        switch (which) {
            case CHOOSE_PICTURE: // 选择本地照片
                Intent openAlbumIntent = new Intent(
                        Intent.ACTION_GET_CONTENT);
                openAlbumIntent.setType("image/*");
                activity.startActivityForResult(openAlbumIntent, CHOOSE_PICTURE);
                break;
            case TAKE_PICTURE: // 拍照
                Intent openCameraIntent = new Intent(
                        MediaStore.ACTION_IMAGE_CAPTURE);
                tempUri = Uri.fromFile(new File(LOCAL_HEAD_PATH + "image.png"));
                // 指定照片保存路径（SD卡），image.png为一个临时文件，每次拍照后这个图片都会被替换
                openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, tempUri);
                activity.startActivityForResult(openCameraIntent, TAKE_PICTURE);
                break;
        }
    }

    public static void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) { // 如果返回码是可以用的
            switch (requestCode) {
                case TAKE_PICTURE:
                    startPhotoZoom(tempUri); // 开始对图片进行裁剪处理
                    break;
                case CHOOSE_PICTURE:
                    startPhotoZoom(data.getData()); // 开始对图片进行裁剪处理
                    break;
                case CROP_SMALL_PICTURE:
                    if (data != null) {
                        try {
                            savePhoto(data); // 让刚才选择裁剪得到的图片显示在界面上
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
            }
        }else{
            uploadCallback.getUploadPhoto(null);

        }
    }

    /**
     * 裁剪图片方法实现
     */
    protected static void startPhotoZoom(Uri uri) {
        if (uri == null) {
            Log.i("tag", "The uri is not exist.");
        }
        tempUri = uri;
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        // 设置裁剪
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 200);
        intent.putExtra("outputY", 200);
        intent.putExtra("return-data", true);
        activity.startActivityForResult(intent, CROP_SMALL_PICTURE);
    }

    /**
     * 保存裁剪之后的图片数据
     */
    protected static void savePhoto(Intent data) throws IOException {
        Bundle extras = data.getExtras();
        if (extras != null) {
            Bitmap photo = extras.getParcelable("data");
//            photo = toRoundBitmap(photo); // 这个时候的图片已经被处理成圆形的了
            uploadCurrentUserPic(photo);
        }
    }


    private static void uploadCurrentUserPic(final Bitmap bitmap) throws IOException {
        final String userId = UserEntity.getCurrentUser().getUserId();
        String headName = userId + ".png";
        final File file = new File(LOCAL_HEAD_PATH,headName);
        if (!file.exists())
            file.createNewFile();
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.flush();
        } catch (IOException e) {
            throw e;
        } finally {
            if (fos != null) fos.close();
        }
        RequestBody userIdBody = RequestBody.create(MediaType.parse("multipart/form-data"),userId);
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part fileBody = MultipartBody.Part.createFormData("headPic",file.getName(),requestFile);
        Call<String> callback = NetService.getInstance().uploadUserPic(JwtUtil.getJwt(),userIdBody, fileBody);
        callback.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String version = response.body();
                if (version == null||version.equals("")){
                    uploadCallback.getUploadPhoto(null);
                    return;
                }
                Log.i("aaaccc","sdad");
                String picName = userId + "_" + response.body() + "_.png";
                File newFile = new File(LOCAL_HEAD_PATH,picName);
                file.renameTo(newFile);
                if (picMap.containsKey(userId)){
                    picMap.remove(userId);
                }
                picMap.put(userId,bitmap);
                UserEntity newUser = UserEntity.getCurrentUser();
                newUser.setPicPath(picName);
                UserEntity.setCurrentUser(newUser);
                uploadCallback.getUploadPhoto(bitmap);
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                t.printStackTrace();
                uploadCallback.getUploadPhoto(null);
            }
        });



    }

    public static Bitmap getDiskBitmap(String pathString) {
        Bitmap bitmap = null;
        try {
            File file = new File(pathString);
            if (file.exists()) {
                bitmap = BitmapFactory.decodeFile(pathString);
            }
        } catch (Exception e) {
            // TODO: handle exception
        }
        return bitmap;
    }

    /**
     * 转换图片成圆形
     *
     * @param bitmap 传入Bitmap对象
     * @return
     */
    public static Bitmap toRoundBitmap(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        float roundPx;
        float left, top, right, bottom, dst_left, dst_top, dst_right, dst_bottom;
        if (width <= height) {
            roundPx = width / 2 - 5;
            top = 0;
            bottom = width;
            left = 0;
            right = width;
            height = width;
            dst_left = 0;
            dst_top = 0;
            dst_right = width;
            dst_bottom = width;
        } else {
            roundPx = height / 2 - 5;
            float clip = (width - height) / 2;
            left = clip;
            right = width - clip;
            top = 0;
            bottom = height;
            width = height;
            dst_left = 0;
            dst_top = 0;
            dst_right = height;
            dst_bottom = height;
        }

        Bitmap output = Bitmap.createBitmap(width,
                height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect src = new Rect((int) left, (int) top, (int) right, (int) bottom);
        final Rect dst = new Rect((int) dst_left, (int) dst_top, (int) dst_right, (int) dst_bottom);
        final RectF rectF = new RectF(dst_left + 15, dst_top + 15, dst_right - 20, dst_bottom - 20);

        paint.setAntiAlias(true);

        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);

        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, src, dst, paint);
        return output;
    }
    public interface UpdateHeadCallback{
        void updateHeadPhoto(Bitmap bitmap);
    }

    public interface UploadCallback {
        void getUploadPhoto(Bitmap bitmap);
    }
    public interface GetHeadCallback{
        void getHeadPhoto(Bitmap bitmap);
    }
}
