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

import com.zmark.mytodo.MainActivity;
import com.zmark.mytodo.MainApplication;
import com.zmark.mytodo.R;
import com.zmark.mytodo.fragment.list.ListDetailFragment;
import com.zmark.mytodo.fragment.quadrant.QuadrantViewFragment;
import com.zmark.mytodo.model.group.TaskGroup;
import com.zmark.mytodo.model.group.TaskGroupAdapter;
import com.zmark.mytodo.model.group.TaskListSimple;
import com.zmark.mytodo.service.ApiUtils;
import com.zmark.mytodo.service.api.TaskGroupService;
import com.zmark.mytodo.service.bo.group.resp.TaskGroupSimpleResp;
import com.zmark.mytodo.service.invariant.Msg;
import com.zmark.mytodo.service.result.Result;
import com.zmark.mytodo.service.result.ResultCode;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {
    private final static String TAG = "HomeFragment";
    private static final String NAV_TOP_TITLE = "主页";
    private RecyclerView containerView;
    private List<TaskGroup> taskGroups;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.taskGroups = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        this.findView(view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // 注册顶部菜单
        this.registerTopMenu();
        // 注册上部视图的点击事件
        this.registerClickListener(view);
        // 获取分组数据并更新UI
        this.fetchDataAndUpdateUI();
    }

    private void registerTopMenu() {
        MainActivity mainActivity = (MainActivity) requireActivity();
        mainActivity.setNavTopTitleView(NAV_TOP_TITLE);
        // todo  注册右侧菜单的点击事件
    }

    private void findView(View view) {
        containerView = view.findViewById(R.id.taskGroupRecyclerView);
    }

    private void registerClickListener(View view) {
        // 注册上部视图的点击事件
        view.findViewById(R.id.myDayItem).setOnClickListener(v -> navigateToFragment(ListDetailFragment.MyDayInstance()));
        // todo more
        view.findViewById(R.id.calendarViewItem).setOnClickListener(v -> navigateToFragment(new CalendarViewFragment()));
        view.findViewById(R.id.fourQuadrantsItem).setOnClickListener(v -> navigateToFragment(new QuadrantViewFragment()));
//        view.findViewById(R.id.countdownItem).setOnClickListener(v -> navigateToFragment(new CountdownFragment()));
    }

    private void fetchDataAndUpdateUI() {
        TaskGroupService taskGroupService = MainApplication.getTaskGroupService();
        Call<Result<List<TaskGroupSimpleResp>>> call = taskGroupService.getAllTaskGroup();
        call.enqueue(new Callback<Result<List<TaskGroupSimpleResp>>>() {
            @Override
            public void onResponse(@NonNull Call<Result<List<TaskGroupSimpleResp>>> call,
                                   @NonNull Response<Result<List<TaskGroupSimpleResp>>> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(getContext(), Msg.CLIENT_REQUEST_ERROR, Toast.LENGTH_SHORT).show();
                    ApiUtils.handleResponseError(TAG, response);
                    return;
                }
                Result<List<TaskGroupSimpleResp>> result = response.body();
                if (result == null) {
                    Log.e(TAG, "onResponse: result is null");
                    Toast.makeText(requireContext(), Msg.SERVER_INTERNAL_ERROR, Toast.LENGTH_SHORT).show();
                    return;
                }
                if (result.getCode() == ResultCode.SUCCESS.getCode()) {
                    List<TaskGroupSimpleResp> simpleRespList = result.getObject();
                    if (simpleRespList == null) {
                        Log.e(TAG, "onResponse: simpleRespList is null");
                        Toast.makeText(requireContext(), Msg.SERVER_INTERNAL_ERROR, Toast.LENGTH_SHORT).show();
                        return;
                    }
                    taskGroups.clear();
                    taskGroups.addAll(TaskGroup.from(simpleRespList));
                    updateUI();
                } else {
                    Log.w(TAG, "code:" + result.getCode() + " onResponse: " + result.getMsg());
                    Toast.makeText(requireContext(), result.getMsg(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Result<List<TaskGroupSimpleResp>>> call, @NonNull Throwable t) {
                Log.e(TAG, "onFailure: " + t.getMessage());
                Toast.makeText(requireContext(), Msg.CLIENT_REQUEST_ERROR, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateUI() {
        requireActivity().runOnUiThread(() -> {
            try {
                TaskGroupAdapter taskGroupAdapter = new TaskGroupAdapter(taskGroups);
                containerView.setLayoutManager(new LinearLayoutManager(requireContext()));
                containerView.setAdapter(taskGroupAdapter);
                taskGroupAdapter.setOnItemClickListener(this::navigateToListDetailFragment);
            } catch (Exception e) {
                Log.e(TAG, "updateUI: " + e.getMessage(), e);
                Toast.makeText(requireContext(), Msg.CLIENT_INTERNAL_ERROR, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void navigateToListDetailFragment(TaskListSimple taskListSimple) {
        ListDetailFragment listDetailFragment = ListDetailFragment.NewListDetailFragmentInstance(taskListSimple);
        this.navigateToFragment(listDetailFragment);
    }

    private void navigateToFragment(Fragment fragment) {
        MainActivity mainActivity = (MainActivity) requireActivity();
        mainActivity.navigateToFragment(fragment);
    }
}
