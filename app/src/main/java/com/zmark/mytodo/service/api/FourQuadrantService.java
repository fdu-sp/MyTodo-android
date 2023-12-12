package com.zmark.mytodo.service.api;

import com.zmark.mytodo.service.bo.quadrant.resp.FourQuadrantDetailResp;
import com.zmark.mytodo.service.result.Result;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface FourQuadrantService {
    @GET("/api/four-quadrant/get-by-list/{task-list-id}")
    Call<Result<FourQuadrantDetailResp>> getFourQuadrantDetailByList(@Path("task-list-id") Long taskListId);
    
    @GET("/api/four-quadrant/get-by-my-day")
    Call<Result<FourQuadrantDetailResp>> getFourQuadrantDetailByMyDay();
}
