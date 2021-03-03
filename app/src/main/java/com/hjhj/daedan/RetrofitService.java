package com.hjhj.daedan;

import java.util.ArrayList;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PartMap;
import retrofit2.http.Query;

public interface RetrofitService {
    @Multipart
    @POST("FCMforProject/fcmPush_Chat.php")
    Call<String> postChat(@PartMap Map<String, String> dataForPush);
//    Call<String> postChat(@Field("name") String name, @Field("msg") String msg, @Field('aa') String token);

    //"좋아요" 클릭으로 데이터의 변경을 시키는 작업을 해주는 php를 실행시키기
    @Multipart
    @POST("FCMforProject/updateFav.php")
    Call<String> updateData(@PartMap Map<String, String> datas);

    @Multipart
    @POST("FCMforProject/checkFav.php")
    Call<String> checkFavor(@PartMap Map<String, String> datas);


    //현재가 유저가 누른 fav의 id가져오기
    @GET("FCMforProject/loadFavList.php")
    Call<ArrayList<FavItem>> loadFavList(@Query("userID") String userID);





}
