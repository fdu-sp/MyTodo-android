package com.zmark.mytodo.api;


import com.zmark.mytodo.api.vo.Result;

import retrofit2.Call;
import retrofit2.http.GET;

public interface HelloService {
    @GET("hello")
    Call<Result<String>> hello();
}
