package com.zmark.mytodo.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.zmark.mytodo.comparator.task.TodoItemComparators;
import com.zmark.mytodo.model.QuadrantTaskItemAdapter;
import com.zmark.mytodo.model.TaskSimple;
import com.zmark.mytodo.model.quadrant.FourQuadrant;
import com.zmark.mytodo.service.ApiUtils;
import com.zmark.mytodo.service.bo.quadrant.resp.FourQuadrantDetailResp;
import com.zmark.mytodo.service.invariant.Msg;
import com.zmark.mytodo.service.result.Result;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import retrofit2.Call;

public class QuadrantViewFragment extends Fragment {
    private final static String TAG = "QuadrantViewFragment";
    private static final String NAV_TOP_TITLE = "四象限视图";
    /**
     * 用户偏好设置的名称
     */
    private static final String perfName = "QuadrantViewFragment";
    private static final String KEY_SORT_BY = "sort_by";
    /**
     * 排序方式
     */
    private SortTypeE sortType;
    private FourQuadrant fourQuadrant;
    private View view;
    private RecyclerView urgentImportantRecyclerView;
    private RecyclerView notUrgentImportantRecyclerView;
    private RecyclerView urgentNotImportantRecyclerView;
    private RecyclerView notUrgentNotImportantRecyclerView;

    List<TaskSimple> urgentImportantTasks = new ArrayList<>();
    List<TaskSimple> notUrgentImportantTasks = new ArrayList<>();
    List<TaskSimple> urgentNotImportantTasks = new ArrayList<>();
    List<TaskSimple> notUrgentNotImportantTasks = new ArrayList<>();

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
        // 获取数据并更新UI
        this.fetchDataAndUpdateUI();
    }

    private void registerTopMenu() {
        MainActivity mainActivity = (MainActivity) requireActivity();
        mainActivity.setNavTopTitleView(NAV_TOP_TITLE);
        // todo  注册右侧菜单的点击事件 --> 选择清单，和排序方式
    }

    private void fetchDataAndUpdateUI() {
        Call<Result<FourQuadrantDetailResp>> call
                = MainApplication.getFourQuadrantService().getFourQuadrantDetailByMyDay();
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
        Comparator<TaskSimple> comparator = TodoItemComparators.getComparator(sortType);
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
            setupQuadrantRecyclerView(urgentImportantRecyclerView);
            setupQuadrantRecyclerView(notUrgentImportantRecyclerView);
            setupQuadrantRecyclerView(urgentNotImportantRecyclerView);
            setupQuadrantRecyclerView(notUrgentNotImportantRecyclerView);
            // 为每个象限设置数据
            setQuadrantAdapter(view, urgentImportantRecyclerView,
                    fourQuadrant.getUrgentAndImportant().getTitle(),
                    urgentImportantTasks,
                    R.id.urgentImportantTitleTextView, R.id.urgentImportantEmptyTextView);
            setQuadrantAdapter(view, notUrgentImportantRecyclerView,
                    fourQuadrant.getNotUrgentAndImportant().getTitle(),
                    notUrgentImportantTasks,
                    R.id.notUrgentImportantTitleTextView, R.id.notUrgentImportantEmptyTextView);
            setQuadrantAdapter(view, urgentNotImportantRecyclerView,
                    fourQuadrant.getUrgentAndNotImportant().getTitle(),
                    urgentNotImportantTasks,
                    R.id.urgentNotImportantTitleTextView, R.id.urgentNotImportantEmptyTextView);
            setQuadrantAdapter(view, notUrgentNotImportantRecyclerView,
                    fourQuadrant.getNotUrgentAndNotImportant().getTitle(),
                    notUrgentNotImportantTasks,
                    R.id.notUrgentNotImportantTitleTextView, R.id.notUrgentNotImportantEmptyTextView);
        });
    }

    private void setupQuadrantRecyclerView(RecyclerView recyclerView) {
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        // 添加任何其他的 RecyclerView 配置
    }

    private void setQuadrantAdapter(@NonNull View view, RecyclerView recyclerView,
                                    String title, List<TaskSimple> tasks,
                                    int titleTextViewId, int emptyTaskTextId) {
        // todo 设置象限名称
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
            QuadrantTaskItemAdapter quadrantTaskItemAdapter = new QuadrantTaskItemAdapter(activity, tasks);
            recyclerView.setAdapter(quadrantTaskItemAdapter);
        }
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

    private List<String> getSampleTasks(int taskNumber) {
        // 返回一些示例任务数据
        List<String> sampleTasks = new ArrayList<>();
        for (int i = 0; i < taskNumber; i++) {
            sampleTasks.add("Task " + i);
        }
        return sampleTasks;
    }
}
