package com.zmark.mytodo.api;

import com.zmark.mytodo.api.bo.group.req.TaskGroupCreateReq;
import com.zmark.mytodo.api.bo.group.resp.TaskGroupSimpleResp;
import com.zmark.mytodo.api.result.Result;

import java.util.List;

import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface TaskGroupService {
    @GET("/api/task-group/simple/get-all")
    public Result<List<TaskGroupSimpleResp>> getAllTaskGroup();

    @POST("/api/task-group/create")
    public Result<TaskGroupSimpleResp> createNew(@Body TaskGroupCreateReq createReq);
}
