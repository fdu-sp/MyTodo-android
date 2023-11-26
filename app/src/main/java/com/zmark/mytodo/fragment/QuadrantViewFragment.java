package com.zmark.mytodo.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.zmark.mytodo.R;
import com.zmark.mytodo.model.QuadrantAdapter;

import java.util.ArrayList;
import java.util.List;

public class QuadrantViewFragment extends Fragment {

    private RecyclerView urgentImportantRecyclerView;
    private RecyclerView notUrgentImportantRecyclerView;
    private RecyclerView urgentNotImportantRecyclerView;
    private RecyclerView notUrgentNotImportantRecyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_quadrant_view, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        urgentImportantRecyclerView = view.findViewById(R.id.urgentImportantRecyclerView);
        notUrgentImportantRecyclerView = view.findViewById(R.id.notUrgentImportantRecyclerView);
        urgentNotImportantRecyclerView = view.findViewById(R.id.urgentNotImportantRecyclerView);
        notUrgentNotImportantRecyclerView = view.findViewById(R.id.notUrgentNotImportantRecyclerView);

        setupQuadrantRecyclerView(urgentImportantRecyclerView);
        setupQuadrantRecyclerView(notUrgentImportantRecyclerView);
        setupQuadrantRecyclerView(urgentNotImportantRecyclerView);
        setupQuadrantRecyclerView(notUrgentNotImportantRecyclerView);

        // 为每个象限设置数据
        List<String> urgentImportantTasks = new ArrayList<>();
        List<String> notUrgentImportantTasks = getSampleTasks(15);
        List<String> urgentNotImportantTasks = getSampleTasks(4);
        List<String> notUrgentNotImportantTasks = getSampleTasks(40);

        setQuadrantAdapter(view, urgentImportantRecyclerView, urgentImportantTasks, R.id.urgentImportantEmptyTextView);
        setQuadrantAdapter(view, notUrgentImportantRecyclerView, notUrgentImportantTasks, R.id.notUrgentImportantEmptyTextView);
        setQuadrantAdapter(view, urgentNotImportantRecyclerView, urgentNotImportantTasks, R.id.urgentNotImportantEmptyTextView);
        setQuadrantAdapter(view, notUrgentNotImportantRecyclerView, notUrgentNotImportantTasks, R.id.notUrgentNotImportantEmptyTextView);
    }

    private void setupQuadrantRecyclerView(RecyclerView recyclerView) {
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        // 添加任何其他的 RecyclerView 配置
    }

    private void setQuadrantAdapter(RecyclerView recyclerView, List<String> tasks) {
        QuadrantAdapter quadrantAdapter = new QuadrantAdapter(tasks);
        recyclerView.setAdapter(quadrantAdapter);
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
