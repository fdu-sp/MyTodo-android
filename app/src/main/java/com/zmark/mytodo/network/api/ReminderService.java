package com.zmark.mytodo.network.api;

import com.zmark.mytodo.network.bo.reminder.TaskReminderInfoResp;
import com.zmark.mytodo.network.result.Result;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ReminderService {

    @GET("reminder/get-all")
    Call<Result<List<TaskReminderInfoResp>>> getAll();
}
