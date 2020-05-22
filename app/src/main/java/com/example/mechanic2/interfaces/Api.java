package com.example.mechanic2.interfaces;

import android.widget.TextView;

import androidx.annotation.Nullable;

import com.example.mechanic2.models.AdminMedia;
import com.example.mechanic2.models.Ads;
import com.example.mechanic2.models.CountriesAndWarranties;
import com.example.mechanic2.models.Good;
import com.example.mechanic2.models.Job;
import com.example.mechanic2.models.Question;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;


public interface Api {

    @POST(".")
    Call<String> getDataInString(@QueryMap Map<String, String> map);

    @GET(".")
    Call<List<AdminMedia>> getAdminMediaInList(@QueryMap Map<String, String> map);

    @GET(".")
    Call<List<Good>> getGoodList(@QueryMap Map<String, String> map);

    @GET(".")
    Call<List<Ads>> getAdsList(@Query("route") String ads);

    @GET(".")
    Call<List<Question>> getQuestionList(@QueryMap Map<String, String> map);

    @GET(".")
    Call<String> sendImage(@QueryMap Map<String, String> map);


    @GET(".")
    Call<String> sendQuestion(@QueryMap Map<String, String> map);


    @Multipart
    @POST(".")
    Call<String> uploadMultipleFilesDynamic(
            @QueryMap Map<String, String> map,
            @Part List<MultipartBody.Part> files);

    @Multipart
    @POST(".")
    Call<String> uploadAudioFile(
            @QueryMap Map<String, String> map,
            @Nullable @Part MultipartBody.Part file);


    @GET(".")
    Call<String> sendPhone(@QueryMap Map<String, String> map);

    @GET(".")
    Call<List<Job>> getJobs(@QueryMap Map<String, String> map);

    @GET(".")
    Call<CountriesAndWarranties> getCountriesAndWarranties(@QueryMap Map<String, String> map);


}
