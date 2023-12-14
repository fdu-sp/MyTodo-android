package com.zmark.mytodo.network.api;

import com.zmark.mytodo.network.bo.group.req.TaskGroupCreateReq;
import com.zmark.mytodo.network.bo.group.resp.TaskGroupSimpleResp;
import com.zmark.mytodo.network.result.Result;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface TaskGroupService {
    @GET("task-group/simple/get-all")
    Call<Result<List<TaskGroupSimpleResp>>> getAllTaskGroup();

    @POST("task-group/create")
    Call<Result<TaskGroupSimpleResp>> createNew(@Body TaskGroupCreateReq createReq);
}
