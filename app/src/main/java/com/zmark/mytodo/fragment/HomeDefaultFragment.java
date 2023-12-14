package com.zmark.mytodo.fragment;

import android.app.Activity;
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
import com.zmark.mytodo.fragment.common.AddGroupDialogFragment;
import com.zmark.mytodo.fragment.list.ListDetailFragment;
import com.zmark.mytodo.handler.MenuItemHandler;
import com.zmark.mytodo.invariant.Msg;
import com.zmark.mytodo.model.group.TaskGroup;
import com.zmark.mytodo.model.group.TaskGroupAdapter;
import com.zmark.mytodo.model.group.TaskListSimple;
import com.zmark.mytodo.network.ApiUtils;
import com.zmark.mytodo.network.api.TaskGroupService;
import com.zmark.mytodo.network.bo.group.req.TaskGroupCreateReq;
import com.zmark.mytodo.network.bo.group.resp.TaskGroupSimpleResp;
import com.zmark.mytodo.network.result.Result;
import com.zmark.mytodo.network.result.ResultCode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeDefaultFragment extends Fragment {
    private final static String TAG = "HomeDefaultFragment";
    private static final String NAV_TOP_TITLE = "主页";
    private RecyclerView containerView;
    private Map<Integer, MenuItemHandler> menuHandlerMap;
    private List<TaskGroup> taskGroups;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.taskGroups = new ArrayList<>();
        this.menuHandlerMap = new HashMap<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_default, container, false);
        this.findView(view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // 注册顶部菜单
        this.registerTopMenu();
        // 注册上部视图的点击事件
        this.registerTopContentClickListener(view);
        // 注册任务创建事件
        this.registerOnTaskCreateListener();
        // 获取分组数据并更新UI
        this.fetchDataAndUpdateUI();
    }

    private void registerTopMenu() {
        menuHandlerMap.put(R.id.addGroup, item -> this.handleAddGroup());
        menuHandlerMap.put(R.id.addList, item -> this.handleAddList());
        MainActivity mainActivity = (MainActivity) getActivity();
        if (mainActivity != null) {
            mainActivity.setNavTopTitleView(NAV_TOP_TITLE);
            mainActivity.setOnRightIconClickListener(this::initPopupMenu);
        }
    }

    private void handleAddGroup() {
        AddGroupDialogFragment dialogFragment = new AddGroupDialogFragment();
        dialogFragment.setOnGroupCreatedListener(this::createGroup);
        dialogFragment.show(getChildFragmentManager(), dialogFragment.getTag());
    }


    private void createGroup(String groupName, String description) {
        TaskGroupCreateReq taskGroupCreateReq = new TaskGroupCreateReq(groupName, description);
        Call<Result<TaskGroupSimpleResp>> call = MainApplication.getTaskGroupService().createNew(taskGroupCreateReq);
        ApiUtils.doRequest(call, TAG, requireContext(),
                data -> {
                    TaskGroup taskGroup = TaskGroup.from(data);
                    taskGroups.add(taskGroup);
                    updateUI();
                });
    }

    private void handleAddList() {

    }

    private void initPopupMenu(View view) {
        PopupMenu popupMenu = new PopupMenu(requireContext(), view);
        // 替换为自定义的菜单资源
        popupMenu.inflate(R.menu.menu_home_default);
        // 设置菜单项的点击事件
        popupMenu.setOnMenuItemClickListener(item -> {
            MenuItemHandler menuItemHandler = menuHandlerMap.get(item.getItemId());
            if (menuItemHandler != null) {
                menuItemHandler.handle(item);
            }
            return true;
        });
        popupMenu.show();
    }

    private void findView(View view) {
        containerView = view.findViewById(R.id.taskGroupRecyclerView);
    }

    private void registerTopContentClickListener(View view) {
        // 注册上部视图的点击事件
        view.findViewById(R.id.myDayItem).setOnClickListener(v -> navigateToFragment(ListDetailFragment.MyDayInstance()));
        view.findViewById(R.id.fourQuadrantsItem).setOnClickListener(v -> navigateToFragment(new QuadrantViewFragment()));
        // todo 后续的日历视图、倒计时视图
        view.findViewById(R.id.calendarViewItem).setVisibility(View.GONE);
        view.findViewById(R.id.countdownItem).setVisibility(View.GONE);
    }

    private void registerOnTaskCreateListener() {
        Activity activity = getActivity();
        if (activity == null) {
            Log.e(TAG, "registerTaskCreateListener: activity is null");
            return;
        }
        MainActivity mainActivity = (MainActivity) requireActivity();
        mainActivity.setOnTaskCreateListener(taskDetail -> {
            for (TaskGroup taskGroup : taskGroups) {
                List<TaskListSimple> taskListSimples = taskGroup.getTaskListSimpleList();
                for (TaskListSimple taskListSimple : taskListSimples) {
                    if (taskListSimple.getId().equals(taskDetail.getTaskListId())) {
                        taskListSimple.setCount(taskListSimple.getCount() + 1);
                        updateUI();
                        return;
                    }
                }
            }
        });
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
                taskGroupAdapter.setOnListClickListener(taskListSimple -> {
                    ListDetailFragment listDetailFragment = ListDetailFragment.NewListDetailFragmentInstance(taskListSimple);
                    navigateToFragment(listDetailFragment);
                });
            } catch (Exception e) {
                Log.e(TAG, "updateUI: " + e.getMessage(), e);
                Toast.makeText(requireContext(), Msg.CLIENT_INTERNAL_ERROR, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void navigateToFragment(Integer id) {
        Fragment practiceFragment = getParentFragment();
        if (!(practiceFragment instanceof HomeFragment)) {
            Log.e(TAG, "navigateToFragment: practiceFragment is null");
            Toast.makeText(requireContext(), Msg.CLIENT_INTERNAL_ERROR, Toast.LENGTH_SHORT).show();
            return;
        }
        HomeFragment homeFragment = (HomeFragment) practiceFragment;
        homeFragment.navigateToChildFragment(id);
    }

    private void navigateToFragment(Fragment fragment) {
        Fragment practiceFragment = getParentFragment();
        if (!(practiceFragment instanceof HomeFragment)) {
            Log.e(TAG, "navigateToFragment: practiceFragment is null");
            Toast.makeText(requireContext(), Msg.CLIENT_INTERNAL_ERROR, Toast.LENGTH_SHORT).show();
            return;
        }
        HomeFragment homeFragment = (HomeFragment) practiceFragment;
        homeFragment.navigateToChildFragment(fragment);
    }
}
