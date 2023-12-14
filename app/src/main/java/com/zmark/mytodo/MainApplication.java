package com.zmark.mytodo;

import static com.zmark.mytodo.model.task.PriorityTypeE.NOT_URGENCY_IMPORTANT;

import android.app.Application;
import android.content.res.ColorStateList;
import android.util.Log;

import androidx.core.content.ContextCompat;

import com.zmark.mytodo.config.Config;
import com.zmark.mytodo.model.task.PriorityTypeE;
import com.zmark.mytodo.network.api.FourQuadrantService;
import com.zmark.mytodo.network.api.HelloService;
import com.zmark.mytodo.network.api.MyDayTaskService;
import com.zmark.mytodo.network.api.TagService;
import com.zmark.mytodo.network.api.TaskGroupService;
import com.zmark.mytodo.network.api.TaskListService;
import com.zmark.mytodo.network.api.TaskService;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainApplication extends Application {
    public static final Long DEFAULT_LIST_ID = 1L;
    public static final Long DEFAULT_GROUP_ID = 1L;
    public static final String DEFAULT_LIST_NAME = "默认清单";
    public static final String DEFAULT_GROUP_NAME = "默认分组";
    private static final String TAG = "MainApplication";
    /**
     * 优先级相关的颜色
     */
    private static final Map<PriorityTypeE, ColorStateList> priorityTextColorMap = new HashMap<>();

    private static ColorStateList checkedColorStateList;
    private static ColorStateList unCheckedColorStateList;

    private static Retrofit retrofit;
    private static HelloService helloService;
    private static TaskService taskService;
    private static TaskGroupService taskGroupService;
    private static TaskListService taskListService;
    private static MyDayTaskService myDayTaskService;
    private static FourQuadrantService fourQuadrantService;
    private static TagService tagService;

    public static ColorStateList getPriorityTextColor(PriorityTypeE priorityTypeE) {
        return priorityTextColorMap.get(priorityTypeE);
    }

    public static ColorStateList getCheckedColorStateList() {
        return checkedColorStateList;
    }

    public static ColorStateList getUnCheckedColorStateList() {
        return unCheckedColorStateList;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate: Application started");

        ColorStateList color_prior_text_urgent_and_import = ContextCompat.getColorStateList(this, R.color.prior_text_urgent_and_import);
        ColorStateList color_prior_text_not_urgent_and_import = ContextCompat.getColorStateList(this, R.color.prior_text_not_urgent_and_import);
        ColorStateList color_prior_text_urgent_not_import = ContextCompat.getColorStateList(this, R.color.prior_text_urgent_not_import);
        ColorStateList color_prior_text_not_urgent_not_import = ContextCompat.getColorStateList(this, R.color.prior_text_not_urgent_not_import);

        priorityTextColorMap.put(PriorityTypeE.URGENCY_IMPORTANT, color_prior_text_urgent_and_import);
        priorityTextColorMap.put(NOT_URGENCY_IMPORTANT, color_prior_text_not_urgent_and_import);
        priorityTextColorMap.put(PriorityTypeE.URGENCY_NOT_IMPORTANT, color_prior_text_urgent_not_import);
        priorityTextColorMap.put(PriorityTypeE.NOT_URGENCY_NOT_IMPORTANT, color_prior_text_not_urgent_not_import);

        checkedColorStateList =
                ContextCompat.getColorStateList(this, R.color.cornflower_blue);
        unCheckedColorStateList =
                ContextCompat.getColorStateList(this, R.color.black);


        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(1, TimeUnit.SECONDS)
                .readTimeout(1, TimeUnit.SECONDS)
                .build();
        retrofit = new Retrofit.Builder()
                .baseUrl(Config.getRearBaseUrl())
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        helloService = retrofit.create(HelloService.class);
        taskService = retrofit.create(TaskService.class);
        taskGroupService = retrofit.create(TaskGroupService.class);
        taskListService = retrofit.create(TaskListService.class);
        myDayTaskService = retrofit.create(MyDayTaskService.class);
        fourQuadrantService = retrofit.create(FourQuadrantService.class);
        tagService = retrofit.create(TagService.class);
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

    public static TaskListService getTaskListService() {
        return taskListService;
    }

    public static MyDayTaskService getMyDayTaskService() {
        return myDayTaskService;
    }

    public static FourQuadrantService getFourQuadrantService() {
        return fourQuadrantService;
    }

    public static TagService getTagService() {
        return tagService;
    }
}
