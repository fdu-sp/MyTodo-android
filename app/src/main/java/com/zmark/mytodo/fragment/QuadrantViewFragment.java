package com.zmark.mytodo.fragment;

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
import com.zmark.mytodo.model.QuadrantAdapter;
import com.zmark.mytodo.model.TaskSimple;
import com.zmark.mytodo.model.quadrant.FourQuadrant;
import com.zmark.mytodo.service.ApiUtils;
import com.zmark.mytodo.service.bo.quadrant.resp.FourQuadrantDetailResp;
import com.zmark.mytodo.service.invariant.Msg;
import com.zmark.mytodo.service.result.Result;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import retrofit2.Call;

public class QuadrantViewFragment extends Fragment {
    private final static String TAG = "QuadrantViewFragment";
    private static final String NAV_TOP_TITLE = "四象限视图";
    private FourQuadrant fourQuadrant;
    private View view;
    private RecyclerView urgentImportantRecyclerView;
    private RecyclerView notUrgentImportantRecyclerView;
    private RecyclerView urgentNotImportantRecyclerView;
    private RecyclerView notUrgentNotImportantRecyclerView;

    List<String> urgentImportantTasks = new ArrayList<>();
    List<String> notUrgentImportantTasks = new ArrayList<>();
    List<String> urgentNotImportantTasks = new ArrayList<>();
    List<String> notUrgentNotImportantTasks = new ArrayList<>();

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
        // todo  排序数据

        // 处理数据
        urgentImportantTasks = this.fourQuadrant.getUrgentAndImportant().getTasks()
                .stream().map(TaskSimple::getTitle).collect(Collectors.toList());
        notUrgentImportantTasks = this.fourQuadrant.getNotUrgentAndImportant().getTasks()
                .stream().map(TaskSimple::getTitle).collect(Collectors.toList());
        urgentNotImportantTasks = this.fourQuadrant.getUrgentAndNotImportant().getTasks()
                .stream().map(TaskSimple::getTitle).collect(Collectors.toList());
        notUrgentNotImportantTasks = this.fourQuadrant.getNotUrgentAndNotImportant().getTasks()
                .stream().map(TaskSimple::getTitle).collect(Collectors.toList());
    }

    private void updateUI() {
        requireActivity().runOnUiThread(() -> {
            setupQuadrantRecyclerView(urgentImportantRecyclerView);
            setupQuadrantRecyclerView(notUrgentImportantRecyclerView);
            setupQuadrantRecyclerView(urgentNotImportantRecyclerView);
            setupQuadrantRecyclerView(notUrgentNotImportantRecyclerView);
            // 为每个象限设置数据
            setQuadrantAdapter(view, urgentImportantRecyclerView, urgentImportantTasks, R.id.urgentImportantEmptyTextView);
            setQuadrantAdapter(view, notUrgentImportantRecyclerView, notUrgentImportantTasks, R.id.notUrgentImportantEmptyTextView);
            setQuadrantAdapter(view, urgentNotImportantRecyclerView, urgentNotImportantTasks, R.id.urgentNotImportantEmptyTextView);
            setQuadrantAdapter(view, notUrgentNotImportantRecyclerView, notUrgentNotImportantTasks, R.id.notUrgentNotImportantEmptyTextView);
        });
    }

    private void setupQuadrantRecyclerView(RecyclerView recyclerView) {
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        // 添加任何其他的 RecyclerView 配置
    }

    private void setQuadrantAdapter(@NonNull View view, RecyclerView recyclerView, List<String> tasks, int emptyTaskTextId) {
        if (tasks.isEmpty()) {
            recyclerView.setVisibility(View.GONE);
            TextView textView = view.findViewById(emptyTaskTextId);
            textView.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            TextView textView = view.findViewById(emptyTaskTextId);
            textView.setVisibility(View.GONE);
            QuadrantAdapter quadrantAdapter = new QuadrantAdapter(tasks);
            recyclerView.setAdapter(quadrantAdapter);
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
