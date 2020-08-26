package com.example.mechanic2.app;

import android.content.Context;
import android.os.StrictMode;
import android.preference.PreferenceManager;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.downloader.PRDownloader;
import com.downloader.PRDownloaderConfig;
import com.example.mechanic2.interfaces.Api;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.osmdroid.config.Configuration;

import java.util.concurrent.TimeUnit;

import io.alterac.blurkit.BlurKit;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class Application extends android.app.Application {

    private static Context context;



    private static Api api;



    public static Context getContext() {
        return context;
    }

    @Override
    public void onCreate() {

        super.onCreate();
        context = this;
        app.l("tttApppp");

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        BlurKit.init(this);
        String iran_yekan = "fonts/b.ttf";
        FontOverride.setDefaultFont(this, "MONOSPACE", iran_yekan);

        Gson gson = new GsonBuilder().setLenient().create();

        OkHttpClient client = new OkHttpClient();

        final OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .readTimeout(60, TimeUnit.SECONDS)
                .connectTimeout(20, TimeUnit.SECONDS)
                .build();
        /**/
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(app.main.URL)
                // .baseUrl("http://google.com/")
                .client(okHttpClient)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        api = retrofit.create(Api.class);


        // Enabling database for resume support even after the application is killed:
        PRDownloaderConfig config = PRDownloaderConfig.newBuilder()
                .setDatabaseEnabled(true)
                .setReadTimeout(30_000)
                .setConnectTimeout(30_000)
                .build();
        PRDownloader.initialize(this, config);

        Configuration.getInstance().load(getContext(), PreferenceManager.getDefaultSharedPreferences(getContext()));
    }

    public static RequestQueue getRequest() {
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.getCache().clear();
        return requestQueue;
    }

    public static Api getApi() {
        return api;
    }



}
