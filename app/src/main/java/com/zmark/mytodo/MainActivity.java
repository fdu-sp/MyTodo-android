package com.zmark.mytodo;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.zmark.mytodo.fragment.HomeFragment;
import com.zmark.mytodo.fragment.MyDayFragment;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private final Map<Integer, Fragment> navigationMap = new HashMap<>();

    protected void registerNavigations() {
        navigationMap.put(R.id.navigation_home, new HomeFragment());
        navigationMap.put(R.id.navigation_my_day, new MyDayFragment());
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
        // 加载默认的Fragment
        loadFragment(new HomeFragment());
    }

    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }
}
