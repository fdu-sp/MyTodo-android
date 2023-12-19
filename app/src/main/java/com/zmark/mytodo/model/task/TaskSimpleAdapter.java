package com.zmark.mytodo.model.task;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.zmark.mytodo.MainApplication;
import com.zmark.mytodo.R;
import com.zmark.mytodo.handler.OnTaskContentClickListener;
import com.zmark.mytodo.model.task.touch.TaskSimpleTouchHelperAdapter;
import com.zmark.mytodo.network.impl.TaskServiceImpl;
import com.zmark.mytodo.utils.TimeUtils;

import java.util.ArrayList;
import java.util.List;

public class TaskSimpleAdapter extends RecyclerView.Adapter<TaskSimpleAdapter.ViewHolder> implements TaskSimpleTouchHelperAdapter {
    private static final String TAG = "TaskSimpleAdapter";

    private final Context context;
    private final List<TaskSimple> todoList;

    private OnTaskContentClickListener onTaskContentClickListener;

    public TaskSimpleAdapter(Context context, List<TaskSimple> todoList) {
        this.context = context;
        this.todoList = new ArrayList<>();
        this.todoList.addAll(todoList);
    }

    public void setOnTaskContentClickListener(OnTaskContentClickListener listener) {
        this.onTaskContentClickListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.task_simple_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TaskSimple todoItem = todoList.get(position);
        if (!todoItem.isBinding()) {
            Log.i(TAG, "position: " + position + (todoItem.getCompleted() ? " 已完成" : " 未完成"));
            holder.titleTextView.setText(todoItem.getTitle());
            // 设置CheckBox的选中状态和事件
            holder.checkBox.setOnCheckedChangeListener(null);
            // 使用 setTag 区分不同的 CheckBox
            holder.checkBox.setTag(position);
            holder.checkBox.setChecked(todoItem.getCompleted());
            holder.checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                // 处理选中和取消选中事件
                if (isChecked) {
                    Log.i(TAG, "onCheckedChanged: 选中");
                    // 处理选中事件
                    todoItem.complete(); // 更新数据项的选中状态
                    // 调用后端接口，完成任务
                    TaskServiceImpl.completeTask(todoItem.getId());
                } else {
                    Log.i(TAG, "onCheckedChanged: 取消选中");
                    // 处理取消选中事件
                    todoItem.unComplete(); // 更新数据项的选中状态
                    // 调用后端接口，取消完成任务
                    TaskServiceImpl.unCompleteTask(todoItem.getId());
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
            // 设置任务内容的点击事件
            holder.taskContentLayout.setOnClickListener(v -> {
                if (onTaskContentClickListener != null) {
                    onTaskContentClickListener.onTaskContentClick(todoItem);
                }
            });

            // 显示标签
            String tagsString = todoItem.getTagString();
            if (!tagsString.isEmpty()) {
                if (tagsString.length() >= 6) {
                    tagsString = tagsString.substring(0, 6) + "...";
                }
                holder.tagsTextView.setText(tagsString);
            } else {
                holder.tagsTextView.setVisibility(View.GONE);
            }
            // 显示到期时间
            holder.dueDateTextView.setText(todoItem.getDueDate());
            // 已过期的任务，设置颜色
            if (!todoItem.getCompleted() && TimeUtils.isDateOverdue(todoItem.getDueDate())) {
                holder.dueDateTextView.setTextColor(MainApplication.getOverdueTaskTextColor());
            } else {
                holder.dueDateTextView.setTextColor(MainApplication.getTextColor());
            }

            // 显示循环图标（如果是循环任务）
            if (todoItem.isRecurring()) {
                holder.recurringIconImageView.setVisibility(View.VISIBLE);
            } else {
                holder.recurringIconImageView.setVisibility(View.GONE);
            }

            // 已完成的任务，需要设置颜色
            if (todoItem.getCompleted()) {
                holder.titleTextView.setTextColor(MainApplication.getCompletedTaskTextColor());
            } else {
                holder.titleTextView.setTextColor(MainApplication.getTextColor());
            }

            // 重要：确保每次设置 CheckBox 状态时都明确指定，避免 RecyclerView 复用导致的问题
            holder.checkBox.setChecked(todoItem.getCompleted());
        }
    }

    @Override
    public int getItemCount() {
        return todoList.size();
    }

    @Override
    public void onItemMove(int fromPosition, int toPosition) {
    }

    @Override
    public void onItemLeftSwipe(int adapterPosition) {
        Log.d(TAG, "onItemLeftSwipe: " + adapterPosition);

        // 构建 AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("确认删除");
        builder.setMessage("确定要删除这个任务吗？");

        // 确定按钮
        builder.setPositiveButton("确定", (dialog, which) -> {
            // 从列表中删除任务
            TaskSimple taskToRemove = todoList.remove(adapterPosition);
            // 通知适配器移除了项目
            notifyItemRemoved(adapterPosition);
            // 可选：调用后端服务删除任务
            TaskServiceImpl.deleteTask(taskToRemove.getId());

            // 关闭对话框
            dialog.dismiss();
        });

        // 取消按钮
        builder.setNegativeButton("取消", (dialog, which) -> {
            // 用户取消删除操作，通知适配器数据发生了变化，以便恢复视图状态
            notifyItemChanged(adapterPosition);

            // 关闭对话框
            dialog.dismiss();
        });
        // 显示对话框
        builder.show();
    }


    @Override
    public void onItemRightSwipe(int adapterPosition) {
        Log.d(TAG, "onItemRightSwipe: " + adapterPosition);
        // 右滑完成/取消完成任务
        TaskSimple task = todoList.get(adapterPosition);
        if (task.getCompleted()) {
            // 如果任务已完成，取消完成
            task.unComplete();
            TaskServiceImpl.unCompleteTask(task.getId());
        } else {
            // 如果任务未完成，标记为完成
            task.complete();
            TaskServiceImpl.completeTask(task.getId());
        }
        // 通知适配器项目发生更改
        notifyItemChanged(adapterPosition);
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        public CheckBox checkBox;
        public LinearLayout taskContentLayout;
        public TextView titleTextView;
        public TextView tagsTextView;
        public TextView dueDateTextView;
        public ImageView recurringIconImageView;

        public ViewHolder(View view) {
            super(view);
            checkBox = view.findViewById(R.id.checkBox);
            taskContentLayout = view.findViewById(R.id.taskContentLayout);
            titleTextView = view.findViewById(R.id.todoTitle);
            tagsTextView = view.findViewById(R.id.tagLayout);
            dueDateTextView = view.findViewById(R.id.dueDate);
            recurringIconImageView = view.findViewById(R.id.recurringIcon);
        }
    }
}
