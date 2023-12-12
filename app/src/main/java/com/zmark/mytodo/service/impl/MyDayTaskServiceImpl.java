package com.zmark.mytodo.service.impl;

import androidx.annotation.NonNull;

import com.zmark.mytodo.MainApplication;
import com.zmark.mytodo.service.result.Result;
import com.zmark.mytodo.service.result.ResultCode;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyDayTaskServiceImpl {
    public interface Callbacks {
        /**
         * 请求成功，且业务逻辑成功的回调
         */
        void onSuccess();

        /**
         * 请求成功的，但是业务逻辑失败的回调
         */
        void onFailure(Integer code, String msg);

        /**
         * 客户端请求失败的回调
         */
        void onClientRequestError(Throwable t);

        /**
         * 服务器内部错误的回调
         */
        void onServerInternalError();
    }

    private static void doRequest(@NonNull Call<Result<Object>> call, @NonNull Callbacks callbacks) {
        call.enqueue(new Callback<Result<Object>>() {
            @Override
            public void onResponse(@NonNull Call<Result<Object>> call, @NonNull Response<Result<Object>> response) {
                if (response.isSuccessful()) {
                    Result<Object> result = response.body();
                    if (result == null) {
                        callbacks.onServerInternalError();
                        return;
                    }
                    if (result.getCode() == null || result.getCode() != ResultCode.SUCCESS.getCode()) {
                        callbacks.onFailure(result.getCode(), result.getMsg());
                        return;
                    }
                    callbacks.onSuccess();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Result<Object>> call, @NonNull Throwable t) {
                callbacks.onClientRequestError(t);
            }
        });
    }

    public static void addTaskToMyDay(Long taskId, Callbacks callbacks) {
        Call<Result<Object>> call = MainApplication.getMyDayTaskService().addTaskToMyDay(taskId);
        doRequest(call, callbacks);
    }

    public static void removeFromMyDayList(Long taskId, Callbacks callbacks) {
        Call<Result<Object>> call = MainApplication.getMyDayTaskService().removeFromMyDayList(taskId);
        doRequest(call, callbacks);
    }
}
