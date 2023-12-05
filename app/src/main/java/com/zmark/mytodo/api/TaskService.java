package com.zmark.mytodo.api;

import com.zmark.mytodo.api.result.Result;
import com.zmark.mytodo.api.vo.task.resp.TaskSimpleResp;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface TaskService {
    @GET("task/simple/get-all-tasks")
    Call<Result<List<TaskSimpleResp>>> getAllTasksWithSimpleInfo();
}
