package com.jacob.www.easycar.net;

import com.jacob.www.easycar.data.Data;
import com.jacob.www.easycar.data.GarageBean;
import com.jacob.www.easycar.data.GarageLotBean;
import com.jacob.www.easycar.data.UserBean;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 *
 * @author ASUS-NB
 * @date 2017/11/12
 */

public interface Api {
    @FormUrlEncoded
    @POST("login")
    Observable<Data<UserBean>> logIn(@Field("phone") String phoneNum, @Field("password") String pas);
    
    @FormUrlEncoded
    @POST("user")
    Observable<Data<UserBean>> signIn(@Field("phone") String phoneNum,@Field("password") String password);

    @GET("garage/near")
    Observable<GarageBean> getNearGarage(@Query("longitude") double longitude, @Query("latitude") double latitude, @Query("distance") double distance);

    @GET("garage")
    Observable<GarageLotBean> getGarageLot(@Query("garageId")String garageId);
    
    @FormUrlEncoded
    @PUT("user/{uid}")
    Observable<Data<UserBean>> changUserInfo(@Path("uid") String uId,@Field("type") String type,@Field("value") String value);
}
