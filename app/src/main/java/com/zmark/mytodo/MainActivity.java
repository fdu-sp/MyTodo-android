package com.zmark.mytodo;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
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
import com.zmark.mytodo.handler.ClickListener;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private final Map<Integer, Fragment> navigationMap = new HashMap<>();
    private ClickListener onRightIconClickListener;
    private ImageView rightIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 隐藏默认的ActionBar
        Objects.requireNonNull(getSupportActionBar()).hide();
        setContentView(R.layout.activity_main);
        // 注册顶部导航栏
        this.registerTopNavigations();
        // 注册底部导航栏
        this.registerBottomNavigations();
        // 注册 FloatingActionButton
        this.registerFloatingActionButton();
        // 加载默认的Fragment
        this.loadFragment(this.navigationMap.get(R.id.navigation_home));
        // 获取欢迎信息
        this.fetchHelloMsg();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    /**
     * 设置右侧图标菜单的的点击事件
     */
    public void setOnRightIconClickListener(ClickListener listener) {
        this.onRightIconClickListener = listener;
    }

    private void registerTopNavigations() {
        ImageView leftIcon = findViewById(R.id.icon_left);
        rightIcon = findViewById(R.id.icon_right);

        // 设置左侧图标的点击事件 -- 打开左侧抽屉菜单
        leftIcon.setOnClickListener(view -> openLeftDrawer());

        // 设置抽屉菜单的最小宽度为屏幕宽度的85%
        DisplayMetrics dm = getResources().getDisplayMetrics();
        int screenWidth = dm.widthPixels;
        NavigationView navigationView = findViewById(R.id.top_left_nav_view);
        DrawerLayout.LayoutParams params = (DrawerLayout.LayoutParams) navigationView.getLayoutParams();
        params.width = (int) (screenWidth * 0.85);
        navigationView.setLayoutParams(params);

        // 设置右侧图标的点击事件
        rightIcon.setOnClickListener(view -> {
            if (this.onRightIconClickListener != null) {
                this.onRightIconClickListener.onRightIconClick(rightIcon);
            } else {
                openPopupMenu();
            }
        });
    }

    private void registerBottomNavigations() {
        navigationMap.put(R.id.navigation_home, new HomeFragment());
        navigationMap.put(R.id.navigation_my_day, new MyDayFragment());
        navigationMap.put(R.id.navigation_calendar_view, new CalendarViewFragment());
        navigationMap.put(R.id.navigation_four_quadrants_view, new QuadrantViewFragment());
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
    }

    private void registerFloatingActionButton() {
        // 设置 FloatingActionButton 点击事件
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
    }

    private void animateIcon(ImageView icon) {
        // 通过属性动画实现旋转效果
        ObjectAnimator rotation = ObjectAnimator.ofFloat(icon, "rotation", 0f, 180f);

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(rotation);
        animatorSet.setDuration(500);  // 设置动画时长
        animatorSet.start();
    }

    // 打开左侧抽屉菜单的方法
    private void openLeftDrawer() {
        ImageView iconLeft = findViewById(R.id.icon_left);
        this.animateIcon(iconLeft);
        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        // 延迟（ms）打开抽屉菜单
        drawerLayout.postDelayed(() -> drawerLayout.openDrawer(GravityCompat.START), 250);
    }

    // 打开普通菜单栏的方法
    private void openPopupMenu() {
        // TODO: 2021/4/25 打开设置菜单？
        Toast.makeText(MainActivity.this, "右侧图标被点击", Toast.LENGTH_SHORT).show();
    }

    private void loadFragment(Fragment fragment) {
        this.setOnRightIconClickListener(null);
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
