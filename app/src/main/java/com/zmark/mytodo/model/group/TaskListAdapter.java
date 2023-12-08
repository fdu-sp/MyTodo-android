package com.zmark.mytodo.model.group;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.zmark.mytodo.R;

import java.util.List;

public class TaskListAdapter extends RecyclerView.Adapter<TaskListAdapter.TaskViewHolder> {

    private final List<TaskListSimple> tasks;

    public TaskListAdapter(List<TaskListSimple> tasks) {
        this.tasks = tasks;
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_list_item, parent, false);
        return new TaskViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        TaskListSimple task = tasks.get(position);
        holder.bind(task);
    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }

    public void setTasks(List<TaskListSimple> tasks) {
        this.tasks.clear();
        this.tasks.addAll(tasks);
    }

    static class TaskViewHolder extends RecyclerView.ViewHolder {
        // todo 绑定点击事件
        private final TextView taskTitleView;
        private final TextView taskCountView;

        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);
            taskTitleView = itemView.findViewById(R.id.taskListTitleTextView);
            taskCountView = itemView.findViewById(R.id.taskListCountTextView);
        }

        public void bind(TaskListSimple taskList) {
            taskTitleView.setText(taskList.getName());
            taskCountView.setText(String.valueOf(taskList.getCount()));
        }
    }
}
