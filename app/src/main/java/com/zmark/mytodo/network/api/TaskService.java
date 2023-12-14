package com.zmark.mytodo.network.api;

import com.zmark.mytodo.network.bo.task.req.TaskCreateReq;
import com.zmark.mytodo.network.bo.task.req.TaskUpdateReq;
import com.zmark.mytodo.network.bo.task.resp.TaskDetailResp;
import com.zmark.mytodo.network.bo.task.resp.TaskSimpleResp;
import com.zmark.mytodo.network.result.Result;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface TaskService {
    @GET("task/detail/get-info/{task-id}")
    Call<Result<TaskDetailResp>> getDetailInfoById(@Path("task-id") Long taskId);

    @GET("task/simple/get-all-tasks")
    Call<Result<List<TaskSimpleResp>>> getAllTasksWithSimpleInfo();

    @GET("task/simple/get-all-tasks-by-list/{task-list-id}")
    Call<Result<List<TaskSimpleResp>>> getAllTasksWithSimpleInfoByList(@Path("task-list-id") Long taskListId);

    @POST("task/complete/{task-id}")
    Call<Result<String>> completeTask(@Path("task-id") Long taskId);

    @POST("task/un-complete/{task-id}")
    Call<Result<String>> unCompleteTask(@Path("task-id") Long taskId);

    @POST("task/create-new-task")
    Call<Result<Object>> createNewTask(@Body TaskCreateReq taskCreateReq);

    @POST("task/update")
    Call<Result<TaskDetailResp>> updateTask(@Body TaskUpdateReq taskUpdateReq);

    @POST("task/delete/{task-id}")
    Call<Result<String>> deleteTaskById(@Path("task-id") Long taskId);
}
