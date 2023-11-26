package com.zmark.mytodo.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
        List<String> urgentImportantTasks = getSampleTasks(4);
        List<String> notUrgentImportantTasks = getSampleTasks(15);
        List<String> urgentNotImportantTasks = getSampleTasks(23);
        List<String> notUrgentNotImportantTasks = getSampleTasks(40);

        setQuadrantAdapter(urgentImportantRecyclerView, urgentImportantTasks);
        setQuadrantAdapter(notUrgentImportantRecyclerView, notUrgentImportantTasks);
        setQuadrantAdapter(urgentNotImportantRecyclerView, urgentNotImportantTasks);
        setQuadrantAdapter(notUrgentNotImportantRecyclerView, notUrgentNotImportantTasks);
    }

    private void setupQuadrantRecyclerView(RecyclerView recyclerView) {
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        // 添加任何其他的 RecyclerView 配置
    }

    private void setQuadrantAdapter(RecyclerView recyclerView, List<String> tasks) {
        QuadrantAdapter quadrantAdapter = new QuadrantAdapter(tasks);
        recyclerView.setAdapter(quadrantAdapter);
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
