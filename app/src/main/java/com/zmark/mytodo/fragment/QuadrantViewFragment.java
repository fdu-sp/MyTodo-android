package com.zmark.mytodo.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.zmark.mytodo.MainActivity;
import com.zmark.mytodo.MainApplication;
import com.zmark.mytodo.R;
import com.zmark.mytodo.comparator.task.SortTypeE;
import com.zmark.mytodo.comparator.task.TaskSimpleComparators;
import com.zmark.mytodo.fragment.common.TaskListSelectBottomSheetFragment;
import com.zmark.mytodo.fragment.list.inner.BottomGroupAndSortSheetFragment;
import com.zmark.mytodo.handler.MenuItemHandler;
import com.zmark.mytodo.invariant.Msg;
import com.zmark.mytodo.model.quadrant.FourQuadrant;
import com.zmark.mytodo.model.task.PriorityTypeE;
import com.zmark.mytodo.model.task.QuadrantTaskItemAdapter;
import com.zmark.mytodo.model.task.TaskDetail;
import com.zmark.mytodo.model.task.TaskSimple;
import com.zmark.mytodo.network.ApiUtils;
import com.zmark.mytodo.network.api.TaskService;
import com.zmark.mytodo.network.bo.quadrant.resp.FourQuadrantDetailResp;
import com.zmark.mytodo.network.bo.task.resp.TaskDetailResp;
import com.zmark.mytodo.network.result.Result;
import com.zmark.mytodo.network.result.ResultCode;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import retrofit2.Call;

public class QuadrantViewFragment extends Fragment {
    private final static String TAG = "QuadrantViewFragment";
    private final static String NAV_TOP_TITLE = "四象限视图";
    private final static Long DEFAULT_TASK_LIST_ID = 0L;
    private final static String DEFAULT_TASK_LIST_TITLE = "我的一天";
    /**
     * 用户偏好设置的名称
     */
    private final static String perfName = "QuadrantViewFragment";
    private final static String KEY_SORT_BY = "sort_by";
    private final static String KEY_TASK_LIST_ID = "task_list_id";
    private final static String KEY_TASK_LIST_TITLE = "task_list_title";
    /**
     * 排序方式
     */
    private SortTypeE sortType;

    private Map<Integer, MenuItemHandler> menuHandlerMap;

    private Long taskListId = DEFAULT_TASK_LIST_ID;
    private String taskListTitle = DEFAULT_TASK_LIST_TITLE;
    private FourQuadrant fourQuadrant;
    private View view;

    private TaskListSelectBottomSheetFragment taskListSelectBottomSheetFragment;

    private RecyclerView urgentImportantRecyclerView;
    private RecyclerView notUrgentImportantRecyclerView;
    private RecyclerView urgentNotImportantRecyclerView;
    private RecyclerView notUrgentNotImportantRecyclerView;

    List<TaskSimple> urgentImportantTasks = new ArrayList<>();
    List<TaskSimple> notUrgentImportantTasks = new ArrayList<>();
    List<TaskSimple> urgentNotImportantTasks = new ArrayList<>();
    List<TaskSimple> notUrgentNotImportantTasks = new ArrayList<>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.menuHandlerMap = new HashMap<>();
        this.sortType = getSavedSortBy();
        this.taskListId = getTaskListId();
        this.taskListTitle = getTaskListTitle();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.view = inflater.inflate(R.layout.fragment_quadrant_view, container, false);
        urgentImportantRecyclerView = view.findViewById(R.id.urgentImportantRecyclerView);
        notUrgentImportantRecyclerView = view.findViewById(R.id.notUrgentImportantRecyclerView);
        urgentNotImportantRecyclerView = view.findViewById(R.id.urgentNotImportantRecyclerView);
        notUrgentNotImportantRecyclerView = view.findViewById(R.id.notUrgentNotImportantRecyclerView);
        return this.view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // 注册顶部菜单
        this.registerTopMenu();
        // 注册任务创建事件
        this.registerOnTaskCreateListener();
        // 获取数据并更新UI
        this.fetchDataAndUpdateUI();
    }

    protected void registerTopMenu() {
        // 注册右侧菜单的点击事件 --> 选择清单，和排序方式
        this.menuHandlerMap.put(R.id.menuSelectList, item -> showListSelectFragment());
        this.menuHandlerMap.put(R.id.menuSelectSortType, item -> showSortDialog());
        MainActivity mainActivity = (MainActivity) requireActivity();
        mainActivity.setNavTopTitleView(NAV_TOP_TITLE);
        mainActivity.setOnRightIconClickListener(this::initPopupMenu);
    }

    private void registerOnTaskCreateListener() {
        Activity activity = getActivity();
        if (activity != null) {
            MainActivity mainActivity = (MainActivity) activity;
            mainActivity.setOnTaskCreateListener(taskDetail -> {
                this.fetchDataAndUpdateUI();
                // todo maybe优化
            });
        }
    }

    private void showListSelectFragment() {
        taskListSelectBottomSheetFragment = new TaskListSelectBottomSheetFragment();
        taskListSelectBottomSheetFragment.show(requireActivity().getSupportFragmentManager(), taskListSelectBottomSheetFragment.getTag());
        taskListSelectBottomSheetFragment.setOnListClickListener(taskListSimple -> {
            this.taskListId = taskListSimple.getId();
            this.taskListTitle = taskListSimple.getName();
            this.saveSelectedTaskListId(this.taskListId);
            this.saveSelectedTaskListTitle(this.taskListTitle);
            this.sortData();
            fetchDataAndUpdateUI();
        });
    }

    private void initPopupMenu(View view) {
        PopupMenu popupMenu = new PopupMenu(requireContext(), view);
        // 替换为自定义的菜单资源
        popupMenu.inflate(R.menu.menu_quadrant_view);
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

    private void showSortDialog() {
        BottomGroupAndSortSheetFragment bottomGroupAndSortSheetFragment =
                new BottomGroupAndSortSheetFragment(null, this.sortType);
        bottomGroupAndSortSheetFragment.setSortListener((sortTypeE) -> {
            // 根据用户选择的排序方式对进行排序
            this.sortType = sortTypeE;
            this.saveSelectedSortType(sortTypeE);
            this.sortData();
            // 更新UI
            this.updateUI();
        });
        bottomGroupAndSortSheetFragment.show(requireActivity().getSupportFragmentManager(), bottomGroupAndSortSheetFragment.getTag());
    }

    private void fetchDataAndUpdateUI() {
        if (taskListId == null) {
            taskListId = DEFAULT_TASK_LIST_ID;
        }
        Call<Result<FourQuadrantDetailResp>> call;
        if (Objects.equals(taskListId, DEFAULT_TASK_LIST_ID)) {
            call = MainApplication.getFourQuadrantService().getFourQuadrantDetailByMyDay();
        } else {
            call = MainApplication.getFourQuadrantService().getFourQuadrantDetailByList(taskListId);
        }
        ApiUtils.doRequest(call, new ApiUtils.Callbacks<FourQuadrantDetailResp>() {
            @Override
            public void onSuccess(FourQuadrantDetailResp data) {
                Log.i(TAG, "onSuccess: " + data);
                fourQuadrant = new FourQuadrant(data);
                sortData();
                updateUI();
            }

            @Override
            public void onFailure(Integer code, String msg) {
                Log.w(TAG, "onFailure: " + code + " " + msg);
                Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onClientRequestError(Throwable t) {
                Log.e(TAG, "onFailure: ", t);
                Toast.makeText(requireContext(), Msg.CLIENT_REQUEST_ERROR, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onServerInternalError() {
                Toast.makeText(requireContext(), Msg.SERVER_INTERNAL_ERROR, Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void sortData() {
        // 根据用户选择的排序方式对todoList进行排序
        Comparator<TaskSimple> comparator = TaskSimpleComparators.getComparator(sortType);
        this.fourQuadrant.getUrgentAndImportant().getTasks().sort(comparator);
        this.fourQuadrant.getNotUrgentAndImportant().getTasks().sort(comparator);
        this.fourQuadrant.getUrgentAndNotImportant().getTasks().sort(comparator);
        this.fourQuadrant.getNotUrgentAndNotImportant().getTasks().sort(comparator);
        // 处理数据
        urgentImportantTasks = this.fourQuadrant.getUrgentAndImportant().getTasks();
        notUrgentImportantTasks = this.fourQuadrant.getNotUrgentAndImportant().getTasks();
        urgentNotImportantTasks = this.fourQuadrant.getUrgentAndNotImportant().getTasks();
        notUrgentNotImportantTasks = this.fourQuadrant.getNotUrgentAndNotImportant().getTasks();
    }

    private void updateUI() {
        requireActivity().runOnUiThread(() -> {
            if (taskListSelectBottomSheetFragment != null) {
                taskListSelectBottomSheetFragment.dismiss();
            }
            // 设置导航栏标题
            if (taskListTitle != null) {
                MainActivity mainActivity = (MainActivity) requireActivity();
                mainActivity.setNavTopTitleView(NAV_TOP_TITLE + "-" + taskListTitle);
            }
            setupQuadrantRecyclerView(urgentImportantRecyclerView);
            setupQuadrantRecyclerView(notUrgentImportantRecyclerView);
            setupQuadrantRecyclerView(urgentNotImportantRecyclerView);
            setupQuadrantRecyclerView(notUrgentNotImportantRecyclerView);
            // 为每个象限设置数据
            setQuadrantAdapter(view, urgentImportantRecyclerView,
                    fourQuadrant.getUrgentAndImportant().getTitle(),
                    urgentImportantTasks,
                    R.id.urgentImportantTitleTextView, R.id.urgentImportantEmptyTextView,
                    MainApplication.getPriorityTextColor(PriorityTypeE.URGENCY_IMPORTANT));
            setQuadrantAdapter(view, notUrgentImportantRecyclerView,
                    fourQuadrant.getNotUrgentAndImportant().getTitle(),
                    notUrgentImportantTasks,
                    R.id.notUrgentImportantTitleTextView, R.id.notUrgentImportantEmptyTextView,
                    MainApplication.getPriorityTextColor(PriorityTypeE.NOT_URGENCY_IMPORTANT));
            setQuadrantAdapter(view, urgentNotImportantRecyclerView,
                    fourQuadrant.getUrgentAndNotImportant().getTitle(),
                    urgentNotImportantTasks,
                    R.id.urgentNotImportantTitleTextView, R.id.urgentNotImportantEmptyTextView,
                    MainApplication.getPriorityTextColor(PriorityTypeE.URGENCY_NOT_IMPORTANT));
            setQuadrantAdapter(view, notUrgentNotImportantRecyclerView,
                    fourQuadrant.getNotUrgentAndNotImportant().getTitle(),
                    notUrgentNotImportantTasks,
                    R.id.notUrgentNotImportantTitleTextView, R.id.notUrgentNotImportantEmptyTextView
                    , MainApplication.getPriorityTextColor(PriorityTypeE.NOT_URGENCY_NOT_IMPORTANT));
        });
    }

    private void setupQuadrantRecyclerView(RecyclerView recyclerView) {
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        // 添加任何其他的 RecyclerView 配置
    }

    private void setQuadrantAdapter(@NonNull View view, RecyclerView recyclerView,
                                    String title, List<TaskSimple> tasks,
                                    int titleTextViewId, int emptyTaskTextId,
                                    ColorStateList colorStateList) {
        TextView titleTextView = view.findViewById(titleTextViewId);
        titleTextView.setText(title);
        if (tasks.isEmpty()) {
            recyclerView.setVisibility(View.GONE);
            TextView textView = view.findViewById(emptyTaskTextId);
            textView.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            TextView textView = view.findViewById(emptyTaskTextId);
            textView.setVisibility(View.GONE);
            Activity activity = requireActivity();
            QuadrantTaskItemAdapter quadrantTaskItemAdapter = new QuadrantTaskItemAdapter(activity, tasks, colorStateList);
            quadrantTaskItemAdapter.setOnTaskContentClickListener(this::openTaskDetail);
            recyclerView.setAdapter(quadrantTaskItemAdapter);
        }
    }

    private void openTaskDetail(TaskSimple taskSimple) {
        TaskService taskService = MainApplication.getTaskService();
        taskService.getDetailInfoById(taskSimple.getId()).enqueue(new retrofit2.Callback<Result<TaskDetailResp>>() {
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
                        fetchDataAndUpdateUI();
                    });
                    taskDetailBottomSheetFragment.setOnTaskUpdateListener(taskDetail -> {
                        fetchDataAndUpdateUI();
                    });
                    taskDetailBottomSheetFragment.setOnTaskDeleteListener(taskDetail -> {
                        fetchDataAndUpdateUI();
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

    private void saveSelectedSortType(SortTypeE sortType) {
        Activity activity = getActivity();
        if (activity != null) {
            SharedPreferences preferences = activity.getSharedPreferences(perfName, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString(KEY_SORT_BY, sortType.getDesc());
            editor.apply();
        }
    }

    private Long getTaskListId() {
        Activity activity = getActivity();
        if (activity != null) {
            SharedPreferences preferences = activity.getSharedPreferences(perfName, Context.MODE_PRIVATE);
            return preferences.getLong(KEY_TASK_LIST_ID, DEFAULT_TASK_LIST_ID);
        }
        return DEFAULT_TASK_LIST_ID;
    }

    private void saveSelectedTaskListId(Long taskListId) {
        Activity activity = getActivity();
        if (activity != null) {
            SharedPreferences preferences = activity.getSharedPreferences(perfName, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putLong(KEY_TASK_LIST_ID, taskListId);
            editor.apply();
        }
    }

    private String getTaskListTitle() {
        Activity activity = getActivity();
        if (activity != null) {
            SharedPreferences preferences = activity.getSharedPreferences(perfName, Context.MODE_PRIVATE);
            return preferences.getString(KEY_TASK_LIST_TITLE, DEFAULT_TASK_LIST_TITLE);
        }
        return DEFAULT_TASK_LIST_TITLE;
    }

    private void saveSelectedTaskListTitle(String taskListTitle) {
        Activity activity = getActivity();
        if (activity != null) {
            SharedPreferences preferences = activity.getSharedPreferences(perfName, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString(KEY_TASK_LIST_TITLE, taskListTitle);
            editor.apply();
        }
    }
}
