package com.zmark.mytodo.api;

import com.zmark.mytodo.api.bo.list.resp.RecommendMyDayResp;
import com.zmark.mytodo.api.bo.task.resp.TaskSimpleResp;
import com.zmark.mytodo.api.result.Result;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface MyDayTaskService {

    @POST("/api/task/my-day/add/{task-id}")
    Call<Result<Object>> addTaskToMyDay(@Path("task-id") Long taskId);

    @POST("/api/task/my-day/remove/{task-id}")
    Call<Result<Object>> removeFromMyDayList(@Path("task-id") Long taskId);

    @POST("/api/task/my-day/clear")
    Call<Result<Object>> clearMyDayList();

    @GET("/api/task/my-day/simple/list")
    Call<Result<List<TaskSimpleResp>>> getMyDayListWithSimpleInfo();

    @GET("/api/task/my-day/detail/list")
    Call<Result<List<TaskSimpleResp>>> getMyDayListWithDetailInfo();

    @GET("/api/task/my-day/recommend")
    Call<Result<RecommendMyDayResp>> getRecommendTaskList();
}
