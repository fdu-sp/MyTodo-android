package com.zmark.mytodo;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.zmark.mytodo.fragment.CalendarViewFragment;
import com.zmark.mytodo.fragment.HomeFragment;
import com.zmark.mytodo.fragment.MyDayFragment;
import com.zmark.mytodo.fragment.QuadrantViewFragment;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

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
                String todoText = editTextTodo.getText().toString();
                // 执行添加待办事项的操作
                // 可以将任务添加到数据库或其他数据源
                // 更新 UI 或重新加载数据
            });

            builder.setNegativeButton("取消", (dialog, which) -> {
                // 处理用户点击取消按钮的逻辑
                dialog.cancel();
            });

            builder.show();
        });
        
        // 加载默认的Fragment
        loadFragment(new HomeFragment());
    }

    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }
}
