package com.zmark.mytodo.api;

import com.zmark.mytodo.api.bo.list.req.TaskListCreateReq;
import com.zmark.mytodo.api.bo.list.resp.TaskListDetailResp;
import com.zmark.mytodo.api.result.Result;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface TaskListService {
    @POST("task-list/create-new")
    Call<Result<TaskListDetailResp>> createNewTaskList(@Body TaskListCreateReq createReq);
}
