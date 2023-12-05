package com.zmark.mytodo;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.zmark.mytodo.api.HelloService;
import com.zmark.mytodo.api.TaskService;
import com.zmark.mytodo.api.invariant.Msg;
import com.zmark.mytodo.api.result.Result;
import com.zmark.mytodo.api.result.ResultCode;
import com.zmark.mytodo.api.vo.task.req.TaskCreatReq;
import com.zmark.mytodo.fragment.CalendarViewFragment;
import com.zmark.mytodo.fragment.HomeFragment;
import com.zmark.mytodo.fragment.MyDayFragment;
import com.zmark.mytodo.fragment.QuadrantViewFragment;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private final Map<Integer, Fragment> navigationMap = new HashMap<>();

    protected void registerNavigations() {
        navigationMap.put(R.id.navigation_home, new HomeFragment());
        navigationMap.put(R.id.navigation_my_day, new MyDayFragment());
        navigationMap.put(R.id.navigation_calendar_view, new CalendarViewFragment());
        navigationMap.put(R.id.navigation_four_quadrants_view, new QuadrantViewFragment());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // 注册导航
        this.registerNavigations();
        // 设置底部导航的点击事件
        BottomNavigationView bottomNavigation = findViewById(R.id.bottom_navigation);
        bottomNavigation.setOnItemSelectedListener(item -> {
            int menuItemId = item.getItemId();
            if (navigationMap.containsKey(menuItemId)) {
                loadFragment(navigationMap.get(menuItemId));
                return true;
            } else {
                return false;
            }
        });
        // 获取 FloatingActionButton
        FloatingActionButton fabAddTodo = findViewById(R.id.fab_add_todo);

        // 设置点击监听器
        fabAddTodo.setOnClickListener(view -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("新建待办事项");

            // 设置对话框布局
            View dialogView = getLayoutInflater().inflate(R.layout.dialog_add_todo, null);
            builder.setView(dialogView);

            // 获取对话框中的 EditText
            EditText editTextTodo = dialogView.findViewById(R.id.editTextTodo);

            // 设置对话框按钮
            builder.setPositiveButton("确定", (dialog, which) -> {
                // 处理用户点击确定按钮的逻辑
                if (editTextTodo.getText().toString().isEmpty()) {
                    // 如果待办事项为空，则不执行添加操作
                    dialog.cancel();
                }
                String todoText = editTextTodo.getText().toString();
                // 执行添加待办事项的操作
                createNewTask(new TaskCreatReq(todoText));
                // 关闭对话框
                dialog.dismiss();
            });

            builder.setNegativeButton("取消", (dialog, which) -> {
                // 处理用户点击取消按钮的逻辑
                dialog.cancel();
            });

            builder.show();
        });

        // 加载默认的Fragment
        loadFragment(new HomeFragment());

        this.fetchHelloMsg();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }

    private void fetchHelloMsg() {
        HelloService helloService = MainApplication.getHelloService();

        Call<Result<String>> call = helloService.hello();
        call.enqueue(new Callback<Result<String>>() {
            @Override
            public void onResponse(@NonNull Call<Result<String>> call, @NonNull Response<Result<String>> response) {
                if (response.isSuccessful()) {
                    Result<String> result = response.body();
                    assert result != null;
                    Toast.makeText(MainActivity.this, result.getObject(), Toast.LENGTH_SHORT).show();
                    Log.i(TAG, "onResponse: " + result.getMsg());
                    Log.i(TAG, "onResponse: " + result.getCode());
                    Log.i(TAG, "onResponse: " + result.getObject());
                } else {
                    Log.w(TAG, "onResponse: " + response.errorBody());
                }
            }

            @Override
            public void onFailure(@NonNull Call<Result<String>> call, @NonNull Throwable t) {
                Log.e(TAG, "onFailure: " + t.getMessage());
                Toast.makeText(MainActivity.this, Msg.CLIENT_REQUEST_ERROR, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void createNewTask(TaskCreatReq taskCreatReq) {
        TaskService taskService = MainApplication.getTaskService();
        Call<Result<Object>> call = taskService.createNewTask(taskCreatReq);
        call.enqueue(new Callback<Result<Object>>() {
            @Override
            public void onResponse(@NonNull Call<Result<Object>> call, @NonNull Response<Result<Object>> response) {
                if (response.isSuccessful()) {
                    Result<Object> result = response.body();
                    assert result != null;
                    if (result.getCode() == ResultCode.SUCCESS.getCode()) {
                        Log.i(TAG, "onResponse: 任务创建成功");
                        Toast.makeText(MainActivity.this, "任务创建成功", Toast.LENGTH_SHORT).show();
                    } else {
                        Log.w(TAG, "code:" + result.getCode() + " onResponse: " + result.getMsg());
                        Toast.makeText(MainActivity.this, result.getMsg(), Toast.LENGTH_LONG).show();
                    }
                } else {
                    Log.w(TAG, "onResponse: " + response.errorBody());
                }
            }

            @Override
            public void onFailure(@NonNull Call<Result<Object>> call, @NonNull Throwable t) {
                Log.e(TAG, "onFailure: " + t.getMessage());
                Toast.makeText(MainActivity.this, Msg.CLIENT_REQUEST_ERROR, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
