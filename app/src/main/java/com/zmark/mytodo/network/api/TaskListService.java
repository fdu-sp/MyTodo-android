package com.zmark.mytodo.network.api;

import com.zmark.mytodo.network.bo.list.req.TaskListCreateReq;
import com.zmark.mytodo.network.bo.list.resp.TaskListDetailResp;
import com.zmark.mytodo.network.bo.list.resp.TaskListSimpleResp;
import com.zmark.mytodo.network.result.Result;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface TaskListService {
    @POST("task-list/create-new")
    Call<Result<TaskListDetailResp>> createNewTaskList(@Body TaskListCreateReq createReq);

    @GET("task-list/simple/my-day")
    Call<Result<TaskListSimpleResp>> getMyDayTaskList();
}
