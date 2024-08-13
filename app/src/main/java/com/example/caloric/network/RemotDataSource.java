package com.example.caloric.network;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import hu.akarnokd.rxjava3.retrofit.RxJava3CallAdapterFactory;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RemotDataSource {
    private static RemotDataSource instance = null;
    private API myApi;
    private static final String TAG = "API_Client";


    public RemotDataSource() {
        Gson gson = new GsonBuilder().create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(API.BASE_URL_DAILY_INSPIRATIONS)
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())          //rxjava retrofit
                .build();

        myApi = retrofit.create(API.class);
    }

    public static synchronized RemotDataSource getInstance() {
        if (instance == null) {
            instance = new RemotDataSource();
        }
        return instance;
    }

    public API getMyApi() {
        return myApi;
    }

}
