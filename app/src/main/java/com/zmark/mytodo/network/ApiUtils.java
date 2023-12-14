package com.zmark.mytodo.network;

import static com.zmark.mytodo.invariant.Msg.CLIENT_REQUEST_ERROR;
import static com.zmark.mytodo.invariant.Msg.SERVER_INTERNAL_ERROR;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.zmark.mytodo.network.result.Result;
import com.zmark.mytodo.network.result.ResultCode;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ApiUtils {

    public static void handleResponseError(String tag, Response<?> response) {
        try (ResponseBody errorBody = response.errorBody()) {
            Log.e(tag, "handleResponseError: " + errorBody);
        } catch (Exception e) {
            Log.e(tag, "handleResponseError: " + e.getMessage(), e);
        }
    }

    public interface Callbacks<T> {
        /**
         * 请求成功，且业务逻辑成功的回调
         */
        void onSuccess(T data);

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

    public interface SimpleCallbacks<T> {
        /**
         * 请求成功，且业务逻辑成功的回调
         */
        void onSuccess(T data);
    }

    public static <T> void doRequest(@NonNull Call<Result<T>> call, @NonNull Callbacks<T> callbacks) {
        call.enqueue(new Callback<Result<T>>() {
            @Override
            public void onResponse(@NonNull Call<Result<T>> call, @NonNull Response<Result<T>> response) {
                if (response.isSuccessful()) {
                    Result<T> result = response.body();
                    if (result == null) {
                        callbacks.onServerInternalError();
                        return;
                    }
                    if (result.getCode() != ResultCode.SUCCESS.getCode()) {
                        callbacks.onFailure(result.getCode(), result.getMsg());
                        return;
                    }
                    callbacks.onSuccess(result.getObject());
                }
            }

            @Override
            public void onFailure(@NonNull Call<Result<T>> call, @NonNull Throwable t) {
                callbacks.onClientRequestError(t);
            }
        });
    }

    public static <T> void doRequest(@NonNull Call<Result<T>> call, String TAG, Context context,
                                     @NonNull SimpleCallbacks<T> callbacks) {
        call.enqueue(new Callback<Result<T>>() {
            @Override
            public void onResponse(@NonNull Call<Result<T>> call, @NonNull Response<Result<T>> response) {
                if (response.isSuccessful()) {
                    Result<T> result = response.body();
                    if (result == null) {
                        Toast.makeText(context, SERVER_INTERNAL_ERROR, Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (result.getCode() != ResultCode.SUCCESS.getCode()) {
                        Integer code = result.getCode();
                        String msg = result.getMsg();
                        Log.w(TAG, "onFailure: " + code + " " + msg);
                        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
                        return;
                    }
                    callbacks.onSuccess(result.getObject());
                }
            }

            @Override
            public void onFailure(@NonNull Call<Result<T>> call, @NonNull Throwable t) {
                Log.e(TAG, "onClientRequestError: ", t);
                Toast.makeText(context, CLIENT_REQUEST_ERROR, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
