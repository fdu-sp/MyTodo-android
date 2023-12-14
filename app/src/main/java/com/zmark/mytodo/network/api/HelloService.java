package com.zmark.mytodo.network.api;


import com.zmark.mytodo.network.result.Result;

import retrofit2.Call;
import retrofit2.http.GET;

public interface HelloService {
    @GET("hello")
    Call<Result<String>> hello();
}
