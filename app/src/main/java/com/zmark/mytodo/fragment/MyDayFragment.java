package com.zmark.mytodo.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.zmark.mytodo.MainActivity;
import com.zmark.mytodo.MainApplication;
import com.zmark.mytodo.R;
import com.zmark.mytodo.api.TaskService;
import com.zmark.mytodo.api.invariant.Msg;
import com.zmark.mytodo.api.result.Result;
import com.zmark.mytodo.api.result.ResultCode;
import com.zmark.mytodo.api.vo.task.resp.TaskSimpleResp;
import com.zmark.mytodo.handler.ClickListener;
import com.zmark.mytodo.model.TodoItem;
import com.zmark.mytodo.model.TodoListAdapter;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyDayFragment extends Fragment implements ClickListener {
    private static final String TAG = "MyDayFragment";

    private RecyclerView todoRecyclerView;

    private List<TodoItem> todoList;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        todoList = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_myday, container, false);
        this.findViewAndInit(view);
        // 注册顶部菜单的点击事件
        this.registerTopMenu();
        this.fetchData();
        return view;
    }

    private void findViewAndInit(View view) {
        this.todoRecyclerView = view.findViewById(R.id.todoRecyclerView);
    }

    private void registerTopMenu() {
        MainActivity mainActivity = (MainActivity) getActivity();
        if (mainActivity != null) {
            mainActivity.setOnRightIconClickListener(this);
        }
    }

    private void fetchData() {
        TaskService taskService = MainApplication.getTaskService();
        // 获取待办事项列表数据
        Call<Result<List<TaskSimpleResp>>> call = taskService.getAllTasksWithSimpleInfo();
        call.enqueue(new Callback<Result<List<TaskSimpleResp>>>() {
            @Override
            public void onResponse(@NonNull Call<Result<List<TaskSimpleResp>>> call, @NonNull Response<Result<List<TaskSimpleResp>>> response) {
                if (response.isSuccessful()) {
                    Result<List<TaskSimpleResp>> result = response.body();
                    if (result == null) {
                        Log.w(TAG, "result is null");
                        Toast.makeText(getContext(), Msg.SERVER_INTERNAL_ERROR, Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (result.getCode() == ResultCode.SUCCESS.getCode()) {
                        List<TaskSimpleResp> taskList = result.getObject();
                        if (taskList == null || taskList.isEmpty()) {
                            Log.w(TAG, "taskSimpleResp is null");
                            Toast.makeText(getContext(), Msg.NO_TASKS_TODAY, Toast.LENGTH_SHORT).show();
                            return;
                        }
                        todoList.clear();
                        todoList.addAll(TodoItem.from(taskList));
                        updateUI();
                    } else {
                        Log.w(TAG, "code:" + result.getCode() + " onResponse: " + result.getMsg());
                        Toast.makeText(getContext(), result.getMsg(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<Result<List<TaskSimpleResp>>> call, @NonNull Throwable t) {
                Log.e(TAG, "onFailure: " + t.getMessage());
                Toast.makeText(getContext(), Msg.CLIENT_REQUEST_ERROR, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateUI() {
        requireActivity().runOnUiThread(() -> {
            try {
                // 创建RecyclerView的Adapter
                TodoListAdapter todoListAdapter = new TodoListAdapter(todoList);
                // 设置RecyclerView的LayoutManager
                todoRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
                // 将Adapter设置给RecyclerView
                todoRecyclerView.setAdapter(todoListAdapter);
            } catch (Exception e) {
                Log.e(TAG, "updateUI: " + e.getMessage(), e);
                Toast.makeText(getContext(), Msg.CLIENT_INTERNAL_ERROR, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onRightIconClick(View view) {
        PopupMenu popupMenu = new PopupMenu(requireContext(), view);
        // 替换为自定义的菜单资源
        popupMenu.inflate(R.menu.menu_myday);
//        popupMenu.getMenuInflater().inflate(R.menu.menu_myday, popupMenu.getMenu());
        popupMenu.show();
    }
}
