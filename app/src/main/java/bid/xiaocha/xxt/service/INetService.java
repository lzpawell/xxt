package bid.xiaocha.xxt.service;

import java.util.List;

import bid.xiaocha.xxt.model.ActiveOrderEntity;
import bid.xiaocha.xxt.model.AddressEntity;
import bid.xiaocha.xxt.model.CommentEntity;
import bid.xiaocha.xxt.model.FinishedOrderEntity;
import bid.xiaocha.xxt.model.GetResultByPage;
import bid.xiaocha.xxt.model.LoginState;
import bid.xiaocha.xxt.model.NeedServeEntity;
import bid.xiaocha.xxt.model.OfferServeEntity;
import bid.xiaocha.xxt.model.ServeCommentEntity;
import bid.xiaocha.xxt.model.UserEntity;
import bid.xiaocha.xxt.presenter.ICreateNeedServePresenter;
import bid.xiaocha.xxt.presenter.ICreateOfferServePresenter;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Jwt;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Url;

/**
 * Created by 55039 on 2017/10/14.
 */

public interface INetService {
    @GET("/login")
    Call<LoginState> login(@Query("userId") String userId);
    @POST("/createNeedServe")
    Call<ICreateNeedServePresenter.CreateNeedServeResult> createNeedServe(@Header("jwt") String jwt,@Body NeedServeEntity needServeEntity);
    @POST("/createOfferServe")
    Call<ICreateOfferServePresenter.CreateOfferServeResult> createOfferServe(@Header("jwt") String jwt,@Body OfferServeEntity offerServeEntity);
    @GET("/getNeedServesByPage")
    Call<GetResultByPage<NeedServeEntity>> getNeedServesByPages(@Query("pageNum") int pageNum , @Query("whatSort")String whatSort);
    @GET("/getOfferServesByPage")
    Call<GetResultByPage<OfferServeEntity>> getOfferServesByPages(@Query("pageNum") int pageNum ,@Query("whatSort")String whatSort);
    @GET("/getOfferServeByServeId")
    Call<OfferServeEntity> getOfferServeByServeId(@Header("jwt") String jwt,@Query("serveId") String serveId);
    @GET("/getMyNeedServesByPage/{pageNum}/{whatSort}/{userId}")
    Call<GetResultByPage<NeedServeEntity>> getMyNeedServesByPages(@Header("jwt") String jwt,@Path("pageNum") int pageNum ,@Path("whatSort")String whatSort ,@Path("userId")String userId);
    @GET("/getMyOfferServesByPage/{pageNum}/{whatSort}/{userId}")
    Call<GetResultByPage<OfferServeEntity>> getMyOfferServesByPages(@Header("jwt") String jwt,@Path("pageNum") int pageNum , @Path("whatSort")String whatSort , @Path("userId")String userId);
    @GET("/getMyActiveOrderByPage/{userId}/{pageNum}")
    Call<GetResultByPage<ActiveOrderEntity>> getMyActiveOrderByPage(@Header("jwt") String jwt, @Path("userId") String userId, @Path("pageNum") int pageNum);
    @GET("/getMyFinishedOrderByPage/{userId}/{pageNum}")
    Call<GetResultByPage<FinishedOrderEntity>> getMyFinishOrderByPage(@Header("jwt") String jwt, @Path("userId") String userId, @Path("pageNum") int pageNum);
    @GET("/getUserByUserId/{userId}")
    Call<UserEntity> getUserByUserId(@Header("jwt") String jwt,@Path("userId")String userId);
    @POST("/updateUserInfo")
    Call<Boolean> updateUserInfo(@Header("jwt") String jwt,@Body UserEntity userEntity);
    @POST("/getMoneyByUserId")
    Call<Double> getMoneyByUserId(@Header("jwt") String jwt,@Body String userId);
    @GET("/getAddressList/{userId}")
    Call<List<AddressEntity>> getAddressList(@Header("jwt") String jwt,@Path("userId")String userId);
    @POST("/deleteAddress")
    Call<Boolean> deleteAddress(@Header("jwt") String jwt,@Body Long addressId);
    @POST("/createOrUpdateAddress")
    Call<AddressEntity> createOrUpdateAddress(@Header("jwt") String jwt,@Body AddressEntity addressEntity);
    @GET("/startOfferServe")
    Call<Object> startOfferServe(@Header("jwt") String jwt,@Query("userId")String userId,@Query("serveId") String serveId);
    @GET("/stopOfferServe")
    Call<Object> stopOfferServe(@Header("jwt") String jwt,@Query("userId")String userId,@Query("serveId") String serveId);
    @GET("/deleteOfferServe")
    Call<Object> deleteOfferServe(@Header("jwt") String jwt,@Query("userId")String userId,@Query("serveId") String serveId);
    @GET("/startNeedServe")
    Call<Object> startNeedServe(@Header("jwt") String jwt,@Query("userId")String userId,@Query("serveId") String serveId);
    @GET("/stopNeedServe")
    Call<Object> stopNeedServe(@Header("jwt") String jwt,@Query("userId")String userId,@Query("serveId") String serveId);
    @GET("/deleteNeedServe")
    Call<Object> deleteNeedServe(@Header("jwt") String jwt,@Query("userId")String userId,@Query("serveId") String serveId);
    @GET("/createNeedOrder")
    Call<Object> createNeedOrder(@Header("jwt") String jwt, @Query("serveId") String serveId, @Query("userId") String userId);
    @GET("/createOfferOrder")
    Call<Object> createOfferOrder(@Header("jwt") String jwt,@Query("serveId") String serveId,@Query("userId") String userId);
    @GET("/agreeCreate")
    Call<Object> acceptOrder(@Header("jwt") String jwt,@Query("orderId")String orderId,@Query("userId") String userId);
    @GET("/finishOrder")
    Call<Object> finishOrder(@Header("jwt") String jwt,@Query("orderId")String orderId,@Query("userId") String userId);
    @GET("/confrimOrder")
    Call<Object> confrimOrder(@Header("jwt") String jwt,@Query("orderId")String orderId,@Query("userId") String userId);
    @GET("/refuseCancelOrder")
    Call<Object> refuseCancelOrder(@Header("jwt") String jwt,@Query("orderId")String orderId,@Query("userId") String userId);
    @GET("/cancelOrder")
    Call<Object> cancelOrder(@Header("jwt") String jwt,@Query("orderId")String orderId,@Query("userId") String userId);
    @GET("/updateOfferServe")
    Call<Short> updateOfferServe(@Header("jwt") String jwt,@Query("userId") String userId,@Query("serveId")String serveId,@Query("addressId")long addressId, @Query("content") String content, @Query("price") double price);
    @GET("/updateNeedServe")
    Call<Short> updateNeedServe(@Header("jwt") String jwt,@Query("userId") String userId,@Query("serveId") String serveId,@Query("addressId") long addressId,@Query("content") String content, @Query("price") double price);
    @GET("/getOrderByOrderId")
    Call<Object> getOrderByOrderId(@Header("jwt") String jwt,@Query("orderId")String orderId,@Query("userId") String userId,@Query("orderType") short orderType);
    @GET("/saveComment")
    Call<Object> saveComment(@Header("jwt") String jwt,@Query("orderId") String orderId,@Query("userId") String userId,@Query("commentContent") String commentContent,@Query("commentScore") int commentScore);
    @GET("/getServeCommentByPage")
    Call<GetResultByPage<ServeCommentEntity>> getServeCommentByPage(@Header("jwt") String jwt,@Query("pageNum") int pageNum,@Query("whatSort") String whatSort,@Query("serveId") String serveId);
    @GET("/getCommentByPage")
    Call<GetResultByPage<CommentEntity>> getCommentByPage(@Header("jwt") String jwt, @Query("pageNum") int pageNum, @Query("whatSort") String whatSort,@Query("commentedId") String commentedId, @Query("type") short type);
    @GET("/getOfferServeByUserIdAndPage")
    Call<GetResultByPage<OfferServeEntity>> getOfferServeByUserIdAndPage(@Header("jwt") String jwt,@Query("pageNum") int pageNum,@Query("whatSort") String whatSort,@Query("userId") String userId);
    @GET("/getNeedServeByUserIdAndPage")
    Call<GetResultByPage<NeedServeEntity>> getNeedServeByUserIdAndPage(@Header("jwt") String jwt,@Query("pageNum") int pageNum,@Query("whatSort") String whatSort,@Query("userId") String userId);
    @Multipart
    @POST("/uploadUserPic")
    Call<String> uploadUserPic(@Header("jwt") String jwt,@Part("userId")RequestBody description,@Part MultipartBody.Part headPic);
    @GET
    Call<ResponseBody> downloadFile(@Url String fileUrl);
}
