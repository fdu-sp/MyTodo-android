package com.zmark.mytodo.service.api;


import com.zmark.mytodo.service.result.Result;

import retrofit2.Call;
import retrofit2.http.GET;

public interface HelloService {
    @GET("hello")
    Call<Result<String>> hello();
}
