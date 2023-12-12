package com.zmark.mytodo.service.impl;

import android.util.Log;

import androidx.annotation.NonNull;

import com.zmark.mytodo.MainApplication;
import com.zmark.mytodo.service.api.TaskService;
import com.zmark.mytodo.service.result.Result;
import com.zmark.mytodo.service.result.ResultCode;

public class TaskServiceImpl {
    private static final String TAG = "TaskServiceImpl";

    public static void completeTask(Long taskId) {
        TaskService taskService = MainApplication.getTaskService();
        taskService.completeTask(taskId).enqueue(new retrofit2.Callback<Result<String>>() {
            @Override
            public void onResponse(@NonNull retrofit2.Call<Result<String>> call, @NonNull retrofit2.Response<Result<String>> response) {
                if (response.isSuccessful()) {
                    Result<String> result = response.body();
                    if (result == null) {
                        Log.w(TAG, "result is null");
                        return;
                    }
                    if (result.getCode() == ResultCode.SUCCESS.getCode()) {
                        Log.i(TAG, "onResponse: 任务完成");
                    } else {
                        Log.w(TAG, "code:" + result.getCode() + " onResponse: " + result.getMsg());
                    }
                }
            }

            @Override
            public void onFailure(@NonNull retrofit2.Call<Result<String>> call, @NonNull Throwable t) {
                Log.e(TAG, "onFailure: ", t);
            }
        });
    }

    public static void unCompleteTask(Long taskId) {
        TaskService taskService = MainApplication.getTaskService();
        taskService.unCompleteTask(taskId).enqueue(new retrofit2.Callback<Result<String>>() {
            @Override
            public void onResponse(@NonNull retrofit2.Call<Result<String>> call, @NonNull retrofit2.Response<Result<String>> response) {
                if (response.isSuccessful()) {
                    Result<String> result = response.body();
                    if (result == null) {
                        Log.w(TAG, "result is null");
                        return;
                    }
                    if (result.getCode() == ResultCode.SUCCESS.getCode()) {
                        Log.i(TAG, "onResponse: 任务取消完成");
                    } else {
                        Log.w(TAG, "code:" + result.getCode() + " onResponse: " + result.getMsg());
                    }
                }
            }

            @Override
            public void onFailure(@NonNull retrofit2.Call<Result<String>> call, @NonNull Throwable t) {
                Log.e(TAG, "onFailure: ", t);
            }
        });
    }

}
