package com.zmark.mytodo.model;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.zmark.mytodo.R;

import java.util.List;

public class TodoListAdapter extends RecyclerView.Adapter<TodoListAdapter.ViewHolder> {
    private static final String TAG = "TodoListAdapter";

    private final List<TodoItem> todoList;

    public TodoListAdapter(List<TodoItem> todoList) {
        this.todoList = todoList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_todo, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TodoItem todoItem = todoList.get(position);
        if (!todoItem.isBinding()) {
            Log.i(TAG, "position: " + position + (todoItem.isDone() ? " 已完成" : " 未完成"));
            holder.titleTextView.setText(todoItem.getTitle());
            // 设置CheckBox的选中状态和事件
            holder.checkBox.setOnCheckedChangeListener(null);
            // 使用 setTag 区分不同的 CheckBox
            holder.checkBox.setTag(position);
            holder.checkBox.setChecked(todoItem.isDone());
            holder.checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                // 处理选中和取消选中事件
                if (isChecked) {
                    Log.i(TAG, "onCheckedChanged: 选中");
                    // 处理选中事件
                    // 在此处执行你的操作
                    todoItem.changeToBeDone(); // 更新数据项的选中状态
                } else {
                    Log.i(TAG, "onCheckedChanged: 取消选中");
                    // 处理取消选中事件
                    // 在此处执行你的操作
                    todoItem.changeToBeUndone(); // 更新数据项的选中状态
                }
                // 通知Adapter刷新特定位置的项目
                // 使用 Handler 延迟执行 notifyItemChanged
                new Handler(Looper.getMainLooper()).post(() -> {
                    // 设置标志位，表示正在执行onBindViewHolder
                    todoItem.setBinding(true);
                    notifyItemChanged(position);
                    // 重置标志位
                    todoItem.setBinding(false);
                });
            });
            // 显示标签
            String tagsString = todoItem.getTagString();
            if (!tagsString.isEmpty()) {
                holder.tagsTextView.setText(tagsString);
            } else {
                holder.tagsTextView.setVisibility(View.GONE);
            }

            // 显示到期时间
            holder.dueDateTextView.setText(todoItem.getDueDate());

            // 显示循环图标（如果是循环任务）
            if (todoItem.isRecurring()) {
                holder.recurringIconImageView.setVisibility(View.VISIBLE);
            } else {
                holder.recurringIconImageView.setVisibility(View.GONE);
            }
            // 重要：确保每次设置 CheckBox 状态时都明确指定，避免 RecyclerView 复用导致的问题
            holder.checkBox.setChecked(todoItem.isDone());
        }
    }

    @Override
    public int getItemCount() {
        return todoList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public CheckBox checkBox;
        public TextView titleTextView;
        public TextView tagsTextView;
        public TextView dueDateTextView;
        public ImageView recurringIconImageView;

        public ViewHolder(View view) {
            super(view);
            checkBox = view.findViewById(R.id.checkBox);
            titleTextView = view.findViewById(R.id.todoTitle);
            tagsTextView = view.findViewById(R.id.tagLayout);
            dueDateTextView = view.findViewById(R.id.dueDate);
            recurringIconImageView = view.findViewById(R.id.recurringIcon);
        }
    }
}
