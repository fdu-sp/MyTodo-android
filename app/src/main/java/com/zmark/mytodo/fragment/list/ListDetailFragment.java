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
import com.zmark.mytodo.api.ApiUtils;
import com.zmark.mytodo.api.bo.task.resp.TaskSimpleResp;
import com.zmark.mytodo.api.invariant.Msg;
import com.zmark.mytodo.api.result.Result;
import com.zmark.mytodo.api.result.ResultCode;
import com.zmark.mytodo.comparator.task.SortTypeE;
import com.zmark.mytodo.comparator.task.TodoItemComparators;
import com.zmark.mytodo.fragment.list.inner.BottomGroupAndSortSheetFragment;
import com.zmark.mytodo.handler.MenuItemHandler;
import com.zmark.mytodo.model.TodoItem;
import com.zmark.mytodo.model.TodoListAdapter;
import com.zmark.mytodo.model.group.TaskListSimple;

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
    private static final String KEY_GROUP_BY = "group_by";
    private static final String KEY_SORT_BY = "sort_by";
    private View containerView;
    private RecyclerView todoRecyclerView;
    private final TaskListSimple taskListSimple;
    private final boolean isMyDay;
    private final String perfName;
    private final Call<Result<List<TaskSimpleResp>>> call;
    private List<TodoItem> todoList;
    private Map<Integer, MenuItemHandler> menuHandlerMap;
    private boolean detailsVisible;
    private BottomGroupAndSortSheetFragment.GroupTypeE groupType;
    private SortTypeE sortType;

    public ListDetailFragment(TaskListSimple taskListSimple) {
        this.taskListSimple = taskListSimple;
        this.perfName = "TASK_LIST_PREF" + taskListSimple.getId();
        this.call = MainApplication.getTaskService().getAllTasksWithSimpleInfoByList(taskListSimple.getId());
        this.isMyDay = false;
    }

    private ListDetailFragment(TaskListSimple taskListSimple, Call<Result<List<TaskSimpleResp>>> call) {
        this.taskListSimple = taskListSimple;
        this.perfName = "TASK_LIST_PREF" + taskListSimple.getId();
        this.call = call;
        this.isMyDay = true;
    }

    public static ListDetailFragment MyDayInstance() {
        Call<Result<List<TaskSimpleResp>>> myDayCall = MainApplication.getMyDayTaskService().getMyDayListWithSimpleInfo();
        TaskListSimple myDay = new TaskListSimple();
        // 设置必要的字段
        myDay.setName("我的一天");
        return new ListDetailFragment(myDay, myDayCall);
    }

    // todo: 切换细节的显示和隐藏
    // todo: 排序待办事项
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.todoList = new ArrayList<>();
        this.menuHandlerMap = new HashMap<>();
        this.detailsVisible = true;
        this.groupType = getSavedGroupBy();
        this.sortType = getSavedSortBy();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        containerView = inflater.inflate(R.layout.fragment_myday, container, false);
        this.findView(containerView);
        // 注册顶部菜单
        this.registerTopMenu();
        // 注册底部图标
        this.registerBottomIcon();
        // 获取数据并更新UI
        this.fetchDataAndUpdateUI();
        return containerView;
    }

    private void registerBottomIcon() {
        CardView cardView = containerView.findViewById(R.id.fab_recommend_button);
        if (isMyDay) {
            cardView.setOnClickListener(v -> {
                // todo
                Toast.makeText(getContext(), "推荐", Toast.LENGTH_SHORT).show();
            });
        } else {
            cardView.setVisibility(View.GONE);
        }
    }

    private void findView(View view) {
        this.todoRecyclerView = view.findViewById(R.id.todoRecyclerView);
    }

    private void initPopupMenu(View view) {
        PopupMenu popupMenu = new PopupMenu(requireContext(), view);
        // 替换为自定义的菜单资源
        popupMenu.inflate(R.menu.menu_myday);
        // 设置菜单项-显示or隐藏细节
        this.setDetailShowMenuItem(popupMenu.getMenu().findItem(R.id.hide_or_show_details));
        popupMenu.setOnMenuItemClickListener(item -> {
            MenuItemHandler menuItemHandler = menuHandlerMap.get(item.getItemId());
            if (menuItemHandler != null) {
                menuItemHandler.handle(item);
            }
            return true;
        });
        popupMenu.show();
    }

    private void registerTopMenu() {
        this.menuHandlerMap.put(R.id.menu_task_sort, item -> showGroupAndSortDialog());
        this.menuHandlerMap.put(R.id.hide_or_show_details, item -> {
            // 切换状态
            detailsVisible = !detailsVisible;
            setDetailShowMenuItem(item);
            // todo
            Toast.makeText(getContext(), "显示or隐藏细节", Toast.LENGTH_SHORT).show();
        });
        MainActivity mainActivity = (MainActivity) getActivity();
        if (mainActivity != null) {
            mainActivity.setOnRightIconClickListener(this::initPopupMenu);
            mainActivity.setNavTopTitleView(taskListSimple.getName());
        }
    }

    private void sortData(Comparator<TodoItem> comparator) {
        todoList.sort(comparator);
    }

    private void setDetailShowMenuItem(MenuItem item) {
        // 根据当前状态进行设置
        if (detailsVisible) {
            // 显示细节
            item.setTitle("隐藏细节"); // 设置菜单项的 title
//            item.setIcon(R.drawable.ic_show_details); // 设置菜单项的图标
        } else {
            // 隐藏细节
            item.setTitle("显示细节");
//            item.setIcon(R.drawable.ic_hide_details); // 设置菜单项的图标
        }
    }

    private void showGroupAndSortDialog() {
        BottomGroupAndSortSheetFragment bottomGroupAndSortSheetFragment =
                new BottomGroupAndSortSheetFragment(this.groupType, this.sortType);
        bottomGroupAndSortSheetFragment.setSortListener((sortTypeE) -> {
            // 根据用户选择的分组方式和排序方式对todoList进行排序
            this.sortType = sortTypeE;
            this.saveSelectedSortType(sortTypeE);
            this.sortData(TodoItemComparators.getComparator(this.sortType));
            // 更新UI
            this.updateUI();
        });
        bottomGroupAndSortSheetFragment.setGroupListener((groupTypeE) -> {
            // 根据用户选择的分组方式和排序方式对todoList进行排序
            this.groupType = groupTypeE;
            this.saveSelectGroupType(groupTypeE);
            //  todo setGroupListener
//            this.sortData(TodoItemComparators.getComparator(this.sortType));
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
            Log.e(TAG, "fetchDataAndUpdateUI: call is executed");
            Toast.makeText(getContext(), Msg.CLIENT_INTERNAL_ERROR + "is " + this.todoList.size(), Toast.LENGTH_SHORT).show();
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
                    if (taskList == null || taskList.isEmpty()) {
                        Log.w(TAG, "taskSimpleResp is null");
                        View noTaskMsgView = containerView.findViewById(R.id.noTaskMsgView);
                        noTaskMsgView.setVisibility(View.VISIBLE);
                        return;
                    }
                    todoList.clear();
                    todoList.addAll(TodoItem.from(taskList));
                    // 根据用户选择的排序方式对todoList进行排序
                    sortData(TodoItemComparators.getComparator(sortType));
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
}
