package com.zmark.mytodo.api;

import com.zmark.mytodo.config.Config;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class APIUtils {
    private static final Retrofit retrofit;

    static {
        retrofit = new Retrofit.Builder()
                .baseUrl(Config.getRearBaseUrl())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static Retrofit getRetrofit() {
        return new Retrofit.Builder()
                .baseUrl(Config.getRearBaseUrl())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }
}
