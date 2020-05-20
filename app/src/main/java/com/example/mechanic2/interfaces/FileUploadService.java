package com.example.mechanic2.interfaces;

import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.QueryMap;

public interface FileUploadService {
    @Multipart
    @POST(".")
    Call<String> upload(
            @QueryMap Map<String,String> data,/*
            @Part("description") RequestBody description,*/
            @Part MultipartBody.Part file
    );/*
    @Multipart
    @POST(".")
    Call<ResponseBody> upload(
            @QueryMap Map<String,String> data,
            @Part("description") RequestBody description,
            @Part MultipartBody.Part file
    );*/
}
