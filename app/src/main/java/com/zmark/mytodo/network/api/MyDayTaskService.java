package com.zmark.mytodo.network.api;

import com.zmark.mytodo.network.bo.list.resp.RecommendMyDayResp;
import com.zmark.mytodo.network.bo.task.resp.TaskSimpleResp;
import com.zmark.mytodo.network.result.Result;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface MyDayTaskService {

    @POST("task/my-day/add/{task-id}")
    Call<Result<Object>> addTaskToMyDay(@Path("task-id") Long taskId);

    @POST("task/my-day/remove/{task-id}")
    Call<Result<Object>> removeFromMyDayList(@Path("task-id") Long taskId);

    @POST("task/my-day/clear")
    Call<Result<Object>> clearMyDayList();

    @GET("task/my-day/simple/list")
    Call<Result<List<TaskSimpleResp>>> getMyDayListWithSimpleInfo();

    @GET("task/my-day/detail/list")
    Call<Result<List<TaskSimpleResp>>> getMyDayListWithDetailInfo();

    @GET("task/my-day/recommend")
    Call<Result<RecommendMyDayResp>> getRecommendTaskList();
}
