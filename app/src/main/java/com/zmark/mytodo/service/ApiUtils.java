package com.zmark.mytodo.service;

import android.util.Log;

import okhttp3.ResponseBody;
import retrofit2.Response;

public class ApiUtils {

    public static void handleResponseError(String tag, Response<?> response) {
        try (ResponseBody errorBody = response.errorBody()) {
            Log.e(tag, "handleResponseError: " + errorBody);
        } catch (Exception e) {
            Log.e(tag, "handleResponseError: " + e.getMessage(), e);
        }
    }
}
