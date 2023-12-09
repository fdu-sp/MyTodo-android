package com.zmark.mytodo.api;

import com.zmark.mytodo.api.bo.task.req.TaskCreatReq;
import com.zmark.mytodo.api.bo.task.resp.TaskSimpleResp;
import com.zmark.mytodo.api.result.Result;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface TaskService {
    @GET("task/simple/get-all-tasks")
    Call<Result<List<TaskSimpleResp>>> getAllTasksWithSimpleInfo();

    @GET("task/simple/get-all-tasks-by-list/{task-list-id}")
    Call<Result<List<TaskSimpleResp>>> getAllTasksWithSimpleInfoByList(@Path("task-list-id") Long taskListId);

    @POST("task/complete/{task-id}")
    Call<Result<String>> completeTask(@Path("task-id") Long taskId);

    @POST("task/un-complete/{task-id}")
    Call<Result<String>> unCompleteTask(@Path("task-id") Long taskId);

    @POST("task/create-new-task")
    Call<Result<Object>> createNewTask(@Body TaskCreatReq taskCreatReq);
}
