package com.zmark.mytodo;

import android.app.Application;
import android.util.Log;

import com.zmark.mytodo.api.HelloService;
import com.zmark.mytodo.api.TaskGroupService;
import com.zmark.mytodo.api.TaskService;
import com.zmark.mytodo.config.Config;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainApplication extends Application {
    private static final String TAG = "MainApplication";
    private static HelloService helloService;
    private static TaskService taskService;
    private static TaskGroupService taskGroupService;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate: Application started");
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Config.getRearBaseUrl())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        helloService = retrofit.create(HelloService.class);
        taskService = retrofit.create(TaskService.class);
        taskGroupService = retrofit.create(TaskGroupService.class);
    }

    public static HelloService getHelloService() {
        return helloService;
    }

    public static TaskService getTaskService() {
        return taskService;
    }

    public static TaskGroupService getTaskGroupService() {
        return taskGroupService;
    }
}
