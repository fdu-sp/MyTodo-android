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
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.zmark.mytodo.R;
import com.zmark.mytodo.fragment.factory.NavFragmentFactory;
import com.zmark.mytodo.fragment.list.ListDetailFragment;
import com.zmark.mytodo.network.invariant.Msg;

import java.util.HashMap;
import java.util.Map;

public class HomeFragment extends Fragment {
    private final static String TAG = "HomeFragment";
    private final Map<Integer, NavFragmentFactory> childFragmentFactoryMap = new HashMap<>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.registerChildFragmentFactoryMap();
        // 加载默认的childFragment --> HomeDefaultFragment
        this.navigateToChildFragment(R.id.homeDefaultLayout);
    }

    private void registerChildFragmentFactoryMap() {
        // 注册childFragmentFactoryMap
        childFragmentFactoryMap.put(R.id.myDayItem, ListDetailFragment::MyDayInstance);
        childFragmentFactoryMap.put(R.id.fourQuadrantsItem, QuadrantViewFragment::new);
        childFragmentFactoryMap.put(R.id.homeDefaultLayout, HomeDefaultFragment::new);
        // todo 后续的日历视图、倒计时视图
//        childFragmentFactoryMap.put(R.id.calendarViewItem,CalendarViewFragment::new);
//        childFragmentFactoryMap.put(R.id.countdownItem, CountdownFragment::new);
    }

    public void navigateToChildFragment(Integer id) {
        NavFragmentFactory childFragmentFactory = childFragmentFactoryMap.get(id);
        if (childFragmentFactory == null) {
            Log.e(TAG, "navigateToChildFragment: childFragmentFactory is null");
            Toast.makeText(requireContext(), Msg.CLIENT_INTERNAL_ERROR, Toast.LENGTH_SHORT).show();
            return;
        }
        Fragment childFragment = childFragmentFactory.createFragment();
        this.navigateToChildFragment(childFragment);
    }

    public void navigateToChildFragment(Fragment childFragment) {
        // 获取 ChildFragmentManager
        FragmentManager childFragmentManager = getChildFragmentManager();
        // 开启新的事务
        FragmentTransaction fragmentTransaction = childFragmentManager.beginTransaction();
        // 添加自定义过渡动画
        fragmentTransaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, R.anim.slide_in_left, R.anim.slide_out_right);
        // 替换当前 Fragment 为新的 ChildFragment
        fragmentTransaction.replace(R.id.childFragmentContainer, childFragment);
        // 将事务添加到回退栈
        // todo 无效
        fragmentTransaction.addToBackStack(null);
        // 提交事务
        fragmentTransaction.commit();
    }
}
