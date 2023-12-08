package com.zmark.mytodo.model.group;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.zmark.mytodo.R;

import java.util.ArrayList;
import java.util.List;

public class TaskGroupAdapter extends RecyclerView.Adapter<TaskGroupAdapter.TaskGroupViewHolder> {

    private final List<TaskGroup> taskGroups;

    public TaskGroupAdapter(List<TaskGroup> taskGroups) {
        this.taskGroups = taskGroups;
    }

    @NonNull
    @Override
    public TaskGroupViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_group_item, parent, false);
        return new TaskGroupViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskGroupViewHolder holder, int position) {
        TaskGroup taskGroup = taskGroups.get(position);
        holder.bind(taskGroup);
    }

    @Override
    public int getItemCount() {
        return taskGroups.size();
    }

    static class TaskGroupViewHolder extends RecyclerView.ViewHolder {
        // todo 点击事件
        private final TextView groupNameTextView;
        private final TextView groupListCountTextView;
        private final TaskListAdapter taskListAdapter;

        public TaskGroupViewHolder(@NonNull View itemView) {
            super(itemView);
            groupNameTextView = itemView.findViewById(R.id.groupNameTextView);
            RecyclerView taskGroupItemRecyclerView = itemView.findViewById(R.id.taskGroupItemRecyclerView);
            groupListCountTextView = itemView.findViewById(R.id.groupListCountTextView);

            // Create a TaskAdapter for the nested RecyclerView
            taskListAdapter = new TaskListAdapter(new ArrayList<>());
            taskGroupItemRecyclerView.setLayoutManager(new LinearLayoutManager(itemView.getContext()));
            taskGroupItemRecyclerView.setAdapter(taskListAdapter);
        }

        @SuppressLint("NotifyDataSetChanged")
        public void bind(TaskGroup taskGroup) {
            groupNameTextView.setText(taskGroup.getName());
            groupListCountTextView.setText(String.valueOf(taskGroup.getTaskListSimpleList().size()));

            // Update the task list in the nested RecyclerView
            taskListAdapter.setTasks(taskGroup.getTaskListSimpleList());
            taskListAdapter.notifyDataSetChanged();
        }
    }
}
