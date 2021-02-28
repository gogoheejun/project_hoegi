package com.hjhj.daedan;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PartMap;

public interface RetrofitService {
    @Multipart
    @POST("FCMforProject/fcmPush_Chat.php")
    Call<String> postChat(@PartMap Map<String, String> dataForPush);
//    Call<String> postChat(@Field("name") String name, @Field("msg") String msg, @Field('aa') String token);

}
