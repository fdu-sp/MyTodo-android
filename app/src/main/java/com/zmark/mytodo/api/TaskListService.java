package com.zmark.mytodo.api;

import com.zmark.mytodo.api.bo.task.resp.TaskSimpleResp;
import com.zmark.mytodo.api.result.Result;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface TaskListService {
    @GET("task-list/simple/get-all-tasks/{task-list-id}")
    Call<Result<List<TaskSimpleResp>>> getAllTasksWithSimpleInfo(@Path("task-list-id") Long taskListId);
}
