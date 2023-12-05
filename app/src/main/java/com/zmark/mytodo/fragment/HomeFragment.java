package com.zmark.mytodo.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.zmark.mytodo.R;
import com.zmark.mytodo.api.HelloService;
import com.zmark.mytodo.api.vo.Result;
import com.zmark.mytodo.model.group.TaskGroup;
import com.zmark.mytodo.model.group.TaskGroupAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HomeFragment extends Fragment {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Assuming you have a RecyclerView for task groups
        RecyclerView taskGroupRecyclerView = view.findViewById(R.id.taskGroupRecyclerView);

        // Assuming you have a list of TaskGroup objects
        List<TaskGroup> taskGroups = getTaskGroups(); // Your method to get task groups

        // Create a TaskGroupAdapter
        TaskGroupAdapter taskGroupAdapter = new TaskGroupAdapter(taskGroups);

        // Set the adapter to the RecyclerView
        taskGroupRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        taskGroupRecyclerView.setAdapter(taskGroupAdapter);

        HelloService helloService;
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.31.61:8787/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        helloService = retrofit.create(HelloService.class);

        Call<Result<String>> call = helloService.hello();
        call.enqueue(new Callback<Result<String>>() {
            @Override
            public void onResponse(@NonNull Call<Result<String>> call, @NonNull Response<Result<String>> response) {
                if (response.isSuccessful()) {
                    Result<String> result = response.body();
                    assert result != null;
                    Toast.makeText(getActivity(), result.getObject(), Toast.LENGTH_SHORT).show();
                    Log.i("HelloService", "onResponse: " + result.getMsg());
                    Log.i("HelloService", "onResponse: " + result.getCode());
                    Log.i("HelloService", "onResponse: " + result.getObject());
                } else {
                    Log.w("HelloService", "onResponse: " + response.errorBody());
                }
            }

            @Override
            public void onFailure(@NonNull Call<Result<String>> call, @NonNull Throwable t) {
                Log.e("HelloService", "onFailure: " + t.getMessage());
            }
        });
    }

    private List<TaskGroup> getTaskGroups() {
        List<TaskGroup> taskGroups = new ArrayList<>();
        taskGroups.add(new TaskGroup("专业课程", Arrays.asList("智能移动平台应用开发", "软件设计", "操作系统", "计算机网络", "机器学习", "概率论与数理统计", "数据库设计")));
        taskGroups.add(new TaskGroup("专业实践", Arrays.asList("软件工程实践", "软件测试", "软件项目管理", "软件需求工程", "软件体系结构设计", "软件工程经济学")));
        taskGroups.add(new TaskGroup("Group 2", Arrays.asList("List 4", "List 5", "List 6")));
        taskGroups.add(new TaskGroup("Group 3", Arrays.asList("List 7", "List 8", "List 9")));
        taskGroups.add(new TaskGroup("Group 4", Arrays.asList("List 10", "List 11", "List 12")));
        taskGroups.add(new TaskGroup("Group 5", Arrays.asList("List 13", "List 14", "List 15")));
        taskGroups.add(new TaskGroup("Group 6", Arrays.asList("List 16", "List 17", "List 18")));
        taskGroups.add(new TaskGroup("Group 7", Arrays.asList("List 19", "List 20", "List 21")));
        return taskGroups;
    }
}
