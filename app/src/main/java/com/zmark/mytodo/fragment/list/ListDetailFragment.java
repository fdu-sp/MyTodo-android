package com.zmark.mytodo.fragment.list;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.zmark.mytodo.MainActivity;
import com.zmark.mytodo.MainApplication;
import com.zmark.mytodo.R;
import com.zmark.mytodo.anim.AnimUtils;
import com.zmark.mytodo.comparator.task.SortTypeE;
import com.zmark.mytodo.comparator.task.TaskSimpleComparators;
import com.zmark.mytodo.fragment.TaskDetailBottomSheetFragment;
import com.zmark.mytodo.fragment.list.inner.BottomGroupAndSortSheetFragment;
import com.zmark.mytodo.fragment.list.inner.RecommendMyDayBottomSheetFragment;
import com.zmark.mytodo.handler.MenuItemHandler;
import com.zmark.mytodo.invariant.Msg;
import com.zmark.mytodo.model.group.TaskListSimple;
import com.zmark.mytodo.model.task.TaskDetail;
import com.zmark.mytodo.model.task.TaskSimple;
import com.zmark.mytodo.model.task.TaskSimpleAdapter;
import com.zmark.mytodo.network.ApiUtils;
import com.zmark.mytodo.network.api.TaskService;
import com.zmark.mytodo.network.bo.task.resp.TaskDetailResp;
import com.zmark.mytodo.network.bo.task.resp.TaskSimpleResp;
import com.zmark.mytodo.network.result.Result;
import com.zmark.mytodo.network.result.ResultCode;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListDetailFragment extends Fragment {
    private static final String TAG = "ListDetailFragment";
    private static final String HIDE_OR_SHOW_COMPLETED_TASKS = "hide_or_show_completed_tasks";
    private static final String HIDE_OR_SHOW_DETAILS = "hide_or_show_details";
    private static final String KEY_GROUP_BY = "group_by";
    private static final String KEY_SORT_BY = "sort_by";
    /**
     * 用户偏好设置的名称
     */
    private final String perfName;
    private View containerView;
    private RecyclerView todoRecyclerView;
    private View noTaskMsgView;
    private final TaskListSimple taskListSimple;
    private final boolean isMyDay;
    private final Call<Result<List<TaskSimpleResp>>> call;
    private List<TaskSimple> todoList;
    private Map<Integer, MenuItemHandler> menuHandlerMap;
    private boolean completedTasksVisible;
    private boolean detailsVisible;
    private BottomGroupAndSortSheetFragment.GroupTypeE groupType;
    private SortTypeE sortType;

    private ListDetailFragment(TaskListSimple taskListSimple) {
        this.taskListSimple = taskListSimple;
        this.perfName = "TASK_LIST_PREF" + taskListSimple.getId();
        this.call = MainApplication.getTaskService().getAllTasksWithSimpleInfoByList(taskListSimple.getId());
        this.isMyDay = false;
    }

    /**
     * for my day
     */
    private ListDetailFragment(TaskListSimple taskListSimple, Call<Result<List<TaskSimpleResp>>> call) {
        this.taskListSimple = taskListSimple;
        this.perfName = "MyDay" + taskListSimple.getId();
        this.call = call;
        this.isMyDay = true;
    }

    public static ListDetailFragment NewListDetailFragmentInstance(TaskListSimple taskListSimple) {
        return new ListDetailFragment(taskListSimple);
    }

    public static ListDetailFragment MyDayInstance() {
        Call<Result<List<TaskSimpleResp>>> myDayCall = MainApplication.getMyDayTaskService().getMyDayListWithSimpleInfo();
        TaskListSimple myDay = new TaskListSimple();
        // 设置必要的字段
        myDay.setName("我的一天");
        return new ListDetailFragment(myDay, myDayCall);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.todoList = new ArrayList<>();
        this.menuHandlerMap = new HashMap<>();
        // 获取用户偏好设置
        this.completedTasksVisible = getHideOrShowCompletedTasks();
        this.detailsVisible = getHideOrShowDetails();
        this.groupType = getSavedGroupBy();
        this.sortType = getSavedSortBy();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        containerView = inflater.inflate(R.layout.fragment_list_detail, container, false);
        this.findView(containerView);
        // 注册顶部菜单
        this.registerTopMenu();
        // 注册任务创建事件
        this.registerOnTaskCreateListener();
        // 注册底部图标
        this.registerBottomIcon();
        // 获取数据并更新UI
        this.fetchDataAndUpdateUI();
        return containerView;
    }

    private void findView(View view) {
        this.todoRecyclerView = view.findViewById(R.id.todoRecyclerView);
        this.noTaskMsgView = containerView.findViewById(R.id.noTaskMsgView);
    }

    private void registerTopMenu() {
        // 注册菜单项的点击事件
        this.menuHandlerMap.put(R.id.menu_task_sort, item -> showGroupAndSortDialog());
        if (!isMyDay) {
            this.menuHandlerMap.put(R.id.edit_list_info, this::handleListEdit);
        }
//        this.menuHandlerMap.put(R.id.view_switching, this::handleViewSwitching);
        this.menuHandlerMap.put(R.id.hide_or_show_completed_tasks, this::handleCompletedTasksVisible);
//        this.menuHandlerMap.put(R.id.hide_or_show_details, this::handleDetailsVisible);


        // 设置顶部菜单
        MainActivity mainActivity = (MainActivity) getActivity();
        if (mainActivity != null) {
            mainActivity.setOnRightIconClickListener(this::initPopupMenu);
            mainActivity.setNavTopTitleView(taskListSimple.getName());
        }
    }

    private void initPopupMenu(View view) {
        PopupMenu popupMenu = new PopupMenu(requireContext(), view);
        // 替换为自定义的菜单资源
        if (isMyDay) {
            popupMenu.inflate(R.menu.menu_my_day_detail);
        } else {
            popupMenu.inflate(R.menu.menu_list_detail);
        }
        // 根据用户偏好设置菜单项的 title
//        this.setDetailShowMenuItem(popupMenu.getMenu().findItem(R.id.hide_or_show_details));
        this.setCompletedTasksVisibleMenuItem(popupMenu.getMenu().findItem(R.id.hide_or_show_completed_tasks));
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

    private void handleListEdit(MenuItem item) {
        // todo
        Toast.makeText(getContext(), "编辑清单", Toast.LENGTH_SHORT).show();
    }

    private void handleViewSwitching(MenuItem item) {
        // todo 切换视图
        Toast.makeText(getContext(), "切换视图", Toast.LENGTH_SHORT).show();
    }

    private void handleCompletedTasksVisible(MenuItem item) {
        // 切换状态
        completedTasksVisible = !completedTasksVisible;
        // 保存状态
        this.saveHideOrShowCompletedTasks(completedTasksVisible);
        // 更新菜单项相关的UI
        this.setCompletedTasksVisibleMenuItem(item);
        this.updateUI();
    }

    private void handleDetailsVisible(MenuItem item) {
        // 切换状态
        detailsVisible = !detailsVisible;
        // 设置菜单项的 title
        this.setDetailShowMenuItem(item);
        // 保存状态
        this.saveHideOrShowDetails(detailsVisible);
        // todo 切换细节的显示和隐藏
        Toast.makeText(getContext(), "显示or隐藏细节", Toast.LENGTH_SHORT).show();
    }

    private void registerOnTaskCreateListener() {
        Activity activity = getActivity();
        if (activity == null) {
            Log.e(TAG, "registerTaskCreateListener: activity is null");
            return;
        }
        MainActivity mainActivity = (MainActivity) requireActivity();
        mainActivity.setOnTaskCreateListener(taskDetail -> {
            if (isMyDay) {
                if (taskDetail.getInMyDay()) {
                    todoList.add(new TaskSimple(taskDetail));
                    updateUI();
                }
            } else {
                if (taskListSimple.getId().equals(taskDetail.getTaskListId())) {
                    todoList.add(new TaskSimple(taskDetail));
                    updateUI();
                }
            }
        });
    }

    private void registerBottomIcon() {
        CardView cardView = containerView.findViewById(R.id.fab_recommend_button);
        if (isMyDay) {
            cardView.setOnClickListener(v -> {
                AnimUtils.applyClickScalingAnimation(cardView);
                this.showMyDayRecommendBottomSheet();
            });
        } else {
            cardView.setVisibility(View.GONE);
        }
    }

    private List<TaskSimple> getShowData() {
        Comparator<TaskSimple> comparator = TaskSimpleComparators.getComparator(sortType);
        todoList.sort(comparator);
        // 根据当前状态进行设置
        if (completedTasksVisible) {
            // 当前需要显示已完成的任务
            return todoList;
        } else {
            // 当前需要隐藏已完成的任务
            List<TaskSimple> todoListTemp = new ArrayList<>();
            for (TaskSimple todo : todoList) {
                if (!todo.getCompleted()) {
                    todoListTemp.add(todo);
                }
            }
            return todoListTemp;
        }
    }

    private void setCompletedTasksVisibleMenuItem(MenuItem item) {
        // 根据当前状态进行设置
        if (completedTasksVisible) {
            item.setTitle(R.string.hide_completed_tasks);
        } else {
            item.setTitle(R.string.show_completed_tasks);
        }
    }

    private void setDetailShowMenuItem(MenuItem item) {
        // 根据当前状态进行设置
        if (detailsVisible) {
            // 显示细节
            item.setTitle(R.string.show_details);
        } else {
            // 隐藏细节
            item.setTitle(R.string.hide_details);
        }
    }

    private void showMyDayRecommendBottomSheet() {
        RecommendMyDayBottomSheetFragment fragment = new RecommendMyDayBottomSheetFragment();
        fragment.setOnTaskAddedListener(taskSimple -> {
            this.todoList.add(taskSimple);
            this.updateUI();
        });
        fragment.show(requireActivity().getSupportFragmentManager(), fragment.getTag());
    }

    private void showGroupAndSortDialog() {
        // todo 分组方式选择
//        BottomGroupAndSortSheetFragment bottomGroupAndSortSheetFragment =
//                new BottomGroupAndSortSheetFragment(this.groupType, this.sortType);
        BottomGroupAndSortSheetFragment bottomGroupAndSortSheetFragment =
                new BottomGroupAndSortSheetFragment(null, this.sortType);
        bottomGroupAndSortSheetFragment.setSortListener((sortTypeE) -> {
            // 更新排序方式
            this.sortType = sortTypeE;
            this.saveSelectedSortType(sortTypeE);
            // 更新UI
            this.updateUI();
        });
        bottomGroupAndSortSheetFragment.setGroupListener((groupTypeE) -> {
            // 更新分组方式
            this.groupType = groupTypeE;
            this.saveSelectGroupType(groupTypeE);
            //  todo setGroupListener
            // 更新UI
//            this.updateUI();
        });
        bottomGroupAndSortSheetFragment.show(requireActivity().getSupportFragmentManager(), bottomGroupAndSortSheetFragment.getTag());
    }

    private void fetchDataAndUpdateUI() {
        if (call == null) {
            Log.e(TAG, "fetchDataAndUpdateUI: call is null");
            Toast.makeText(getContext(), Msg.CLIENT_INTERNAL_ERROR, Toast.LENGTH_SHORT).show();
            return;
        }
        if (call.isExecuted()) {
            Log.e(TAG, "fetchDataAndUpdateUI: call is executed, taskListSimple:" + taskListSimple.getId());
//            Toast.makeText(getContext(), Msg.CLIENT_INTERNAL_ERROR, Toast.LENGTH_SHORT).show();
            return;
        }
        call.enqueue(new Callback<Result<List<TaskSimpleResp>>>() {
            @Override
            public void onResponse(@NonNull Call<Result<List<TaskSimpleResp>>> call, @NonNull Response<Result<List<TaskSimpleResp>>> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(getContext(), Msg.CLIENT_REQUEST_ERROR, Toast.LENGTH_SHORT).show();
                    ApiUtils.handleResponseError(TAG, response);
                    return;
                }
                Result<List<TaskSimpleResp>> result = response.body();
                if (result == null) {
                    Log.w(TAG, "result is null");
                    Toast.makeText(getContext(), Msg.SERVER_INTERNAL_ERROR, Toast.LENGTH_SHORT).show();
                    return;
                }
                if (result.getCode() == ResultCode.SUCCESS.getCode()) {
                    List<TaskSimpleResp> taskList = result.getObject();
                    if (taskList == null) {
                        Toast.makeText(getContext(), Msg.SERVER_INTERNAL_ERROR, Toast.LENGTH_SHORT).show();
                        return;
                    }
                    todoList.clear();
                    todoList.addAll(TaskSimple.from(taskList));
                    updateUI();
                } else {
                    Log.w(TAG, "code:" + result.getCode() + " onResponse: " + result.getMsg());
                    Toast.makeText(getContext(), result.getMsg(), Toast.LENGTH_SHORT).show();
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
        List<TaskSimple> todoListTemp = getShowData();
        requireActivity().runOnUiThread(() -> {
            try {
                Log.d(TAG, "updateUI: " + todoListTemp.size());
                if (this.todoList == null || this.todoList.isEmpty()) {
                    noTaskMsgView.setVisibility(View.VISIBLE);
                    todoRecyclerView.setVisibility(View.GONE);
                    return;
                }
                noTaskMsgView.setVisibility(View.GONE);
                todoRecyclerView.setVisibility(View.VISIBLE);
                // 创建RecyclerView的Adapter
                TaskSimpleAdapter taskSimpleAdapter = new TaskSimpleAdapter(todoListTemp);
                taskSimpleAdapter.setOnTaskContentClickListener(this::openTaskDetail);
                // 设置RecyclerView的LayoutManager
                todoRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
                // 将Adapter设置给RecyclerView
                todoRecyclerView.setAdapter(taskSimpleAdapter);
            } catch (Exception e) {
                Log.e(TAG, "updateUI: " + e.getMessage(), e);
                Toast.makeText(getContext(), Msg.CLIENT_INTERNAL_ERROR, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void openTaskDetail(TaskSimple todoItem) {
        TaskService taskService = MainApplication.getTaskService();
        taskService.getDetailInfoById(todoItem.getId()).enqueue(new retrofit2.Callback<Result<TaskDetailResp>>() {
            @Override
            public void onResponse(@NonNull retrofit2.Call<Result<TaskDetailResp>> call,
                                   @NonNull retrofit2.Response<Result<TaskDetailResp>> response) {
                if (response.isSuccessful()) {
                    Result<TaskDetailResp> result = response.body();
                    if (result == null) {
                        Log.w(TAG, "result is null");
                        Toast.makeText(requireContext(), Msg.SERVER_INTERNAL_ERROR, Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (result.getCode() != ResultCode.SUCCESS.getCode()) {
                        Log.w(TAG, "code:" + result.getCode() + " onResponse: " + result.getMsg());
                        Toast.makeText(requireContext(), result.getMsg(), Toast.LENGTH_SHORT).show();
                        return;
                    }
                    TaskDetailResp taskDetailResp = result.getObject();
                    if (taskDetailResp == null) {
                        Log.w(TAG, "taskDetail is null");
                        Toast.makeText(requireContext(), Msg.SERVER_INTERNAL_ERROR, Toast.LENGTH_SHORT).show();
                        return;
                    }
                    TaskDetailBottomSheetFragment taskDetailBottomSheetFragment = new TaskDetailBottomSheetFragment(new TaskDetail(taskDetailResp));
                    taskDetailBottomSheetFragment.setOnTaskCompleteStateListener(taskDetail -> {
                        todoList.forEach(todo -> {
                            if (todo.getId().equals(taskDetail.getId())) {
                                todo.setCompleted(taskDetail.getCompleted());
                            }
                        });
                        updateUI();
                    });
                    taskDetailBottomSheetFragment.setOnTaskUpdateListener(taskDetail -> {
                        TaskSimple todo = null;
                        for (int i = 0; i < todoList.size(); i++) {
                            if (todoList.get(i).getId().equals(taskDetail.getId())) {
                                todo = todoList.get(i);
                                break;
                            }
                        }
                        if (todo == null) {
                            todoList.add(new TaskSimple(taskDetail));
                        } else if (todo.getId().equals(taskDetail.getId())) {
                            todo.updateByTaskDetail(taskDetail);
                            // 如果不在清单中，需要移除
                            if (isMyDay) {
                                if (!taskDetail.isInMyDay()) {
                                    todoList.remove(todo);
                                }
                            } else {
                                if (!taskDetail.getTaskListId().equals(taskListSimple.getId())) {
                                    todoList.remove(todo);
                                }
                            }
                        }
                        updateUI();
                    });
                    taskDetailBottomSheetFragment.setOnTaskDeleteListener(taskDetail -> {
                        for (int i = 0; i < todoList.size(); i++) {
                            if (todoList.get(i).getId().equals(taskDetail.getId())) {
                                todoList.remove(i);
                                break;
                            }
                        }
                        updateUI();
                    });
                    taskDetailBottomSheetFragment.show(requireActivity().getSupportFragmentManager(), taskDetailBottomSheetFragment.getTag());
                }
            }

            @Override
            public void onFailure(@NonNull retrofit2.Call<Result<TaskDetailResp>> call, @NonNull Throwable t) {
                Log.e(TAG, "onFailure: ", t);
                Toast.makeText(requireContext(), Msg.CLIENT_REQUEST_ERROR, Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 从SharedPreferences获取保存的 GroupBy 状态
     */
    private BottomGroupAndSortSheetFragment.GroupTypeE getSavedGroupBy() {
        Activity activity = getActivity();
        if (activity != null) {
            SharedPreferences preferences = activity.getSharedPreferences(perfName, Context.MODE_PRIVATE);
            // 从SharedPreferences获取保存的状态，默认为LIST
            String groupBy = preferences.getString(KEY_GROUP_BY, BottomGroupAndSortSheetFragment.GroupTypeE.LIST.getDesc());
            return BottomGroupAndSortSheetFragment.GroupTypeE.getByDesc(groupBy);
        }
        // 默认为LIST
        return BottomGroupAndSortSheetFragment.GroupTypeE.LIST;
    }

    /**
     * 从SharedPreferences获取保存的 SortBy 状态
     */
    private SortTypeE getSavedSortBy() {
        Activity activity = getActivity();
        if (activity != null) {
            SharedPreferences preferences = activity.getSharedPreferences(perfName, Context.MODE_PRIVATE);
            // 从SharedPreferences获取保存的状态，默认为DUE_DATE_FIRST
            String sortBy = preferences.getString(KEY_SORT_BY, SortTypeE.DUE_DATE_FIRST.getDesc());
            return SortTypeE.getByDesc(sortBy);
        }
        // 默认为DUE_DATE_FIRST
        return SortTypeE.DUE_DATE_FIRST;
    }

    private void saveSelectGroupType(BottomGroupAndSortSheetFragment.GroupTypeE groupType) {
        Activity activity = getActivity();
        if (activity != null) {
            SharedPreferences preferences = activity.getSharedPreferences(perfName, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString(KEY_GROUP_BY, groupType.getDesc());
            editor.apply();
        }
    }

    private void saveSelectedSortType(SortTypeE sortType) {
        Activity activity = getActivity();
        if (activity != null) {
            SharedPreferences preferences = activity.getSharedPreferences(perfName, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString(KEY_SORT_BY, sortType.getDesc());
            editor.apply();
        }
    }

    private void saveHideOrShowCompletedTasks(boolean hideOrShow) {
        Activity activity = getActivity();
        if (activity != null) {
            SharedPreferences preferences = activity.getSharedPreferences(perfName, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean(HIDE_OR_SHOW_COMPLETED_TASKS, hideOrShow);
            editor.apply();
        }
    }

    private boolean getHideOrShowCompletedTasks() {
        Activity activity = getActivity();
        if (activity != null) {
            SharedPreferences preferences = activity.getSharedPreferences(perfName, Context.MODE_PRIVATE);
            return preferences.getBoolean(HIDE_OR_SHOW_COMPLETED_TASKS, false);
        }
        return false;
    }

    private void saveHideOrShowDetails(boolean hideOrShow) {
        Activity activity = getActivity();
        if (activity != null) {
            SharedPreferences preferences = activity.getSharedPreferences(perfName, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean(HIDE_OR_SHOW_DETAILS, hideOrShow);
            editor.apply();
        }
    }

    private boolean getHideOrShowDetails() {
        Activity activity = getActivity();
        if (activity != null) {
            SharedPreferences preferences = activity.getSharedPreferences(perfName, Context.MODE_PRIVATE);
            return preferences.getBoolean(HIDE_OR_SHOW_DETAILS, false);
        }
        return false;
    }
}
