package com.zmark.mytodo.model.group;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.interpolator.view.animation.FastOutSlowInInterpolator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.zmark.mytodo.R;
import com.zmark.mytodo.handler.OnListClickListener;

import java.util.ArrayList;
import java.util.List;

public class TaskGroupAdapter extends RecyclerView.Adapter<TaskGroupAdapter.TaskGroupViewHolder> {

    private final List<TaskGroup> taskGroups;

    private OnListClickListener onListClickListener;

    public void setOnItemClickListener(OnListClickListener listener) {
        this.onListClickListener = listener;
    }

    public TaskGroupAdapter(List<TaskGroup> taskGroups) {
        this.taskGroups = taskGroups;
    }

    @NonNull
    @Override
    public TaskGroupViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_group_item, parent, false);
        TaskGroupViewHolder viewHolder = new TaskGroupViewHolder(view);
        viewHolder.setOnItemClickListener(task -> {
            if (onListClickListener != null) {
                onListClickListener.onItemClick(task);
            }
        });
        return viewHolder;
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
        private final TextView groupNameTextView;
        private final TextView groupListCountTextView;
        private final TaskListAdapter taskListAdapter;
        private final ImageView groupFoldImageView;
        private final RecyclerView taskGroupItemRecyclerView;

        private OnListClickListener onListClickListener;

        public void setOnItemClickListener(OnListClickListener listener) {
            this.onListClickListener = listener;
        }

        public TaskGroupViewHolder(@NonNull View itemView) {
            super(itemView);
            groupNameTextView = itemView.findViewById(R.id.groupNameTextView);
            taskGroupItemRecyclerView = itemView.findViewById(R.id.taskGroupItemRecyclerView);
            groupListCountTextView = itemView.findViewById(R.id.groupListCountTextView);
            groupFoldImageView = itemView.findViewById(R.id.groupFoldImageView);

            // Create a TaskAdapter for the nested RecyclerView
            taskListAdapter = new TaskListAdapter(new ArrayList<>());
            taskGroupItemRecyclerView.setLayoutManager(new LinearLayoutManager(itemView.getContext()));
            taskGroupItemRecyclerView.setAdapter(taskListAdapter);
            taskListAdapter.setOnItemClickListener(task -> {
                if (onListClickListener != null) {
                    onListClickListener.onItemClick(task);
                }
            });
        }

        @SuppressLint("NotifyDataSetChanged")
        public void bind(TaskGroup taskGroup) {
            groupNameTextView.setText(taskGroup.getName());
            groupListCountTextView.setText(String.valueOf(taskGroup.getTaskListSimpleList().size()));

            groupFoldImageView.setOnClickListener(v -> {
                // 处理点击事件
                handleGroupFoldClick(taskGroup);
            });

            // Update the task list in the nested RecyclerView
            taskListAdapter.setTasks(taskGroup.getTaskListSimpleList());
            taskListAdapter.notifyDataSetChanged();
        }

        private void animateScale(View view, boolean isExpanded) {
            AnimatorSet animatorSet = new AnimatorSet();
            animatorSet.playTogether(
                    ObjectAnimator.ofFloat(view, "scaleX", isExpanded ? 1.0f : 0.0f, isExpanded ? 0.0f : 1.0f),
                    ObjectAnimator.ofFloat(view, "scaleY", isExpanded ? 1.0f : 0.0f, isExpanded ? 0.0f : 1.0f)
            );
            animatorSet.setInterpolator(new FastOutSlowInInterpolator());
            animatorSet.setDuration(300);
            animatorSet.start();
        }

        private void handleGroupFoldClick(TaskGroup taskGroup) {
            boolean isExpanded = taskGroup.isExpanded();
            isExpanded = !isExpanded;
            taskGroup.setExpanded(isExpanded);
            // 切换图标
            groupFoldImageView.setImageResource(isExpanded ? R.drawable.ic_arrow_right : R.drawable.ic_arrow_down);

            // 控制 RecyclerView 的可见性
            taskGroupItemRecyclerView.setVisibility(isExpanded ? View.GONE : View.VISIBLE);

            // 应用列表折叠的动画
            animateScale(taskGroupItemRecyclerView, isExpanded);
        }

    }
}
