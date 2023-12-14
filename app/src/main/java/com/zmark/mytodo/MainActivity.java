package com.zmark.mytodo;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.zmark.mytodo.fragment.AddTaskBottomSheetFragment;
import com.zmark.mytodo.fragment.HomeFragment;
import com.zmark.mytodo.fragment.QuadrantViewFragment;
import com.zmark.mytodo.fragment.factory.NavFragmentFactory;
import com.zmark.mytodo.fragment.list.ListDetailFragment;
import com.zmark.mytodo.handler.ClickListener;
import com.zmark.mytodo.invariant.Msg;
import com.zmark.mytodo.model.group.TaskGroup;
import com.zmark.mytodo.model.group.TaskGroupAdapter;
import com.zmark.mytodo.network.ApiUtils;
import com.zmark.mytodo.network.api.HelloService;
import com.zmark.mytodo.network.api.TaskGroupService;
import com.zmark.mytodo.network.bo.group.resp.TaskGroupSimpleResp;
import com.zmark.mytodo.network.result.Result;
import com.zmark.mytodo.network.result.ResultCode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private final Map<Integer, NavFragmentFactory> navFragmentFactoryMap = new HashMap<>();

    private AddTaskBottomSheetFragment.OnTaskCreateListener onTaskCreateListener;
    private ClickListener onRightIconClickListener;

    private ImageView leftIcon;
    private TextView navTopTitleView;
    private ImageView rightIcon;
    private BottomNavigationView bottomNavigation;

    private final List<TaskGroup> taskGroups = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.findViews();
        // 注册顶部导航栏
        this.registerTopNavigations();
        // 注册底部导航栏
        this.registerBottomNavigations();
        // 注册 FloatingActionButton
        this.registerFloatingActionButton();
        // 加载默认的Fragment
        this.changeNavFragmentTo(R.id.navigation_home);
        // 获取欢迎信息
        this.fetchHelloMsg();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }


    /**
     * 导航至某个页面
     */
    public void navigateToFragment(Fragment fragment) {
        // 获取FragmentManager
        FragmentManager fragmentManager = getSupportFragmentManager();
        // 开启事务
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        // 添加自定义过渡动画
        fragmentTransaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, R.anim.slide_in_left, R.anim.slide_out_right);
        // 替换Fragment
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        // 添加到回退栈
        fragmentTransaction.addToBackStack(null);
//        this.registerTopLeftAsBackToHome();
        // 提交事务
        fragmentTransaction.commit();
    }

    /**
     * 设置顶部导航栏的标题
     */
    public void setNavTopTitleView(String title) {
        this.navTopTitleView.setText(title);
    }

    /**
     * 设置右侧图标菜单的的点击事件
     */
    public void setOnRightIconClickListener(ClickListener listener) {
        this.onRightIconClickListener = listener;
    }

    /**
     * 设置任务创建的监听器
     */
    public void setOnTaskCreateListener(AddTaskBottomSheetFragment.OnTaskCreateListener listener) {
        this.onTaskCreateListener = listener;
    }

    private void findViews() {
        this.leftIcon = findViewById(R.id.icon_left);
        this.navTopTitleView = findViewById(R.id.nav_top_title);
        this.rightIcon = findViewById(R.id.icon_right);
        this.bottomNavigation = findViewById(R.id.bottom_navigation);
    }

    private void registerTopNavigations() {
        // 隐藏默认的ActionBar
        Objects.requireNonNull(getSupportActionBar()).hide();
//        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
//        Objects.requireNonNull(getSupportActionBar()).setHomeButtonEnabled(true);

        // 注册左侧图标的点击事件 -- 打开左侧抽屉菜单
        this.registerTopLeftAsMenu();

        // 设置右侧图标的点击事件
        rightIcon.setOnClickListener(view -> {
            if (this.onRightIconClickListener != null) {
                this.onRightIconClickListener.onRightIconClick(rightIcon);
            } else {
                openPopupMenu();
            }
        });
    }

    /**
     * 设置左侧图标的点击事件 -- 打开左侧抽屉菜单设置左侧图标的点击事件 -- 打开左侧抽屉菜单
     */
    private void registerTopLeftAsMenu() {
        leftIcon = findViewById(R.id.icon_left);
        leftIcon.setImageResource(R.drawable.ic_top_menu_left);
        // 设置左侧图标的点击事件 -- 打开左侧抽屉菜单
        leftIcon.setOnClickListener(view -> openLeftDrawer());

        // 设置抽屉菜单的最小宽度为屏幕宽度的85%
        DisplayMetrics dm = getResources().getDisplayMetrics();
        int screenWidth = dm.widthPixels;
        NavigationView navigationView = findViewById(R.id.top_left_nav_view);
        DrawerLayout.LayoutParams params = (DrawerLayout.LayoutParams) navigationView.getLayoutParams();
        params.width = (int) (screenWidth * 0.85);
        navigationView.setLayoutParams(params);
    }

    /**
     * 设置左侧图标的点击事件 -- 返回上一个Fragment
     *  todo 这个方案也有问题
     */
    private void registerTopLeftAsBackToHome() {
        // 设置左侧图标为返回
        leftIcon.setImageResource(R.drawable.ic_arrow_left);
        leftIcon.setOnClickListener(view -> {
            this.changeNavFragmentTo(R.id.navigation_home);
            // 重新设置左侧图标的点击事件 -- 打开左侧抽屉菜单
            this.registerTopLeftAsMenu();
        });
    }

    private void registerBottomNavigations() {
        navFragmentFactoryMap.put(R.id.navigation_home, HomeFragment::new);
        navFragmentFactoryMap.put(R.id.navigation_my_day, ListDetailFragment::MyDayInstance);
//        navFragmentFactoryMap.put(R.id.navigation_calendar_view, CalendarViewFragment::new);
        navFragmentFactoryMap.put(R.id.navigation_four_quadrants_view, QuadrantViewFragment::new);
        // 设置底部导航的点击事件
        bottomNavigation = findViewById(R.id.bottom_navigation);
        bottomNavigation.setOnItemSelectedListener(item -> {
            int menuItemId = item.getItemId();
            changeNavFragmentTo(menuItemId);
            return true;
        });
    }

    /**
     * 通过底部导航栏加载Fragment
     */
    private void changeNavFragmentTo(Integer id) {
        NavFragmentFactory fragmentFactory = navFragmentFactoryMap.get(id);
        if (fragmentFactory != null) {
            Fragment fragment = fragmentFactory.createFragment();
            // 获取FragmentManager
            FragmentManager fragmentManager = getSupportFragmentManager();
            // 开启事务
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            // 替换Fragment
            fragmentTransaction.replace(R.id.fragment_container, fragment);
            // 提交事务
            fragmentTransaction.commit();
            // 清空回退栈
            fragmentManager.popBackStackImmediate();
        } else {
            Log.e(TAG, "changeNavFragmentTo: fragmentFactory is null");
            Toast.makeText(MainActivity.this, Msg.CLIENT_INTERNAL_ERROR, Toast.LENGTH_SHORT).show();
        }
    }

    public Fragment getFragmentById(int id) {
        return Objects.requireNonNull(navFragmentFactoryMap.get(id)).createFragment();
    }

    private void registerFloatingActionButton() {
        // 设置 FloatingActionButton 点击事件
        FloatingActionButton fabAddTodo = findViewById(R.id.fab_add_todo);
        // 设置点击监听器
        fabAddTodo.setOnClickListener(view -> {
            AddTaskBottomSheetFragment addTaskBottomSheetFragment = new AddTaskBottomSheetFragment();
            addTaskBottomSheetFragment.setOnTaskCreateListener(onTaskCreateListener);
            addTaskBottomSheetFragment.show(getSupportFragmentManager(), addTaskBottomSheetFragment.getTag());
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
        this.fetchDataAndUpdateUI();
        ImageView iconLeft = findViewById(R.id.icon_left);
        this.animateIcon(iconLeft);
        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        // 延迟（ms）打开抽屉菜单
        drawerLayout.postDelayed(() -> drawerLayout.openDrawer(GravityCompat.START), 250);
    }

    private void fetchDataAndUpdateUI() {
        TaskGroupService taskGroupService = MainApplication.getTaskGroupService();
        Call<Result<List<TaskGroupSimpleResp>>> call = taskGroupService.getAllTaskGroup();
        call.enqueue(new Callback<Result<List<TaskGroupSimpleResp>>>() {
            @Override
            public void onResponse(@NonNull Call<Result<List<TaskGroupSimpleResp>>> call,
                                   @NonNull Response<Result<List<TaskGroupSimpleResp>>> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(MainActivity.this, Msg.CLIENT_REQUEST_ERROR, Toast.LENGTH_SHORT).show();
                    ApiUtils.handleResponseError(TAG, response);
                    return;
                }
                Result<List<TaskGroupSimpleResp>> result = response.body();
                if (result == null) {
                    Log.e(TAG, "onResponse: result is null");
                    Toast.makeText(MainActivity.this, Msg.SERVER_INTERNAL_ERROR, Toast.LENGTH_SHORT).show();
                    return;
                }
                if (result.getCode() == ResultCode.SUCCESS.getCode()) {
                    List<TaskGroupSimpleResp> simpleRespList = result.getObject();
                    if (simpleRespList == null) {
                        Log.e(TAG, "onResponse: simpleRespList is null");
                        Toast.makeText(MainActivity.this, Msg.SERVER_INTERNAL_ERROR, Toast.LENGTH_SHORT).show();
                        return;
                    }
                    taskGroups.clear();
                    taskGroups.addAll(TaskGroup.from(simpleRespList));
                    updateDrawerGroupUI();
                } else {
                    Log.w(TAG, "code:" + result.getCode() + " onResponse: " + result.getMsg());
                    Toast.makeText(MainActivity.this, result.getMsg(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Result<List<TaskGroupSimpleResp>>> call, @NonNull Throwable t) {
                Log.e(TAG, "onFailure: " + t.getMessage());
                Toast.makeText(MainActivity.this, Msg.CLIENT_REQUEST_ERROR, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateDrawerGroupUI() {
        runOnUiThread(() -> {
            try {
                NavigationView navigationView = findViewById(R.id.top_left_nav_view);
                RecyclerView containerView = navigationView.findViewById(R.id.taskGroupRecyclerView);
                TaskGroupAdapter taskGroupAdapter = new TaskGroupAdapter(taskGroups);
                containerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                containerView.setAdapter(taskGroupAdapter);
                taskGroupAdapter.setOnListClickListener(taskListSimple -> {
                    this.navigateToFragment(ListDetailFragment.NewListDetailFragmentInstance(taskListSimple));
                    DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
                    drawerLayout.closeDrawer(GravityCompat.START);
                });
            } catch (Exception e) {
                Log.e(TAG, "updateUI: " + e.getMessage(), e);
                Toast.makeText(MainActivity.this, Msg.CLIENT_INTERNAL_ERROR, Toast.LENGTH_SHORT).show();
            }
        });
    }

    // 打开普通菜单栏的方法
    private void openPopupMenu() {
        // TODO: 2021/4/25 打开设置菜单？
        Toast.makeText(MainActivity.this, "右侧图标被点击", Toast.LENGTH_SHORT).show();
    }

    private void fetchHelloMsg() {
        HelloService helloService = MainApplication.getHelloService();

        Call<Result<String>> call = helloService.hello();
        call.enqueue(new Callback<Result<String>>() {
            @Override
            public void onResponse(@NonNull Call<Result<String>> call, @NonNull Response<Result<String>> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(MainActivity.this, Msg.CLIENT_REQUEST_ERROR, Toast.LENGTH_SHORT).show();
                    ApiUtils.handleResponseError(TAG, response);
                    return;
                }
                Result<String> result = response.body();
                assert result != null;
                Toast.makeText(MainActivity.this, result.getObject(), Toast.LENGTH_SHORT).show();
                Log.i(TAG, "onResponse: " + result.getMsg());
                Log.i(TAG, "onResponse: " + result.getCode());
                Log.i(TAG, "onResponse: " + result.getObject());
            }

            @Override
            public void onFailure(@NonNull Call<Result<String>> call, @NonNull Throwable t) {
                Log.e(TAG, "onFailure: " + t.getMessage());
                Toast.makeText(MainActivity.this, Msg.CLIENT_REQUEST_ERROR, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
