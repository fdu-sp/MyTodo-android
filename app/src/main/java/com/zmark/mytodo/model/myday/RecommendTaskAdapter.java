package com.zmark.mytodo.model.myday;

import static com.zmark.mytodo.service.invariant.Msg.CLIENT_REQUEST_ERROR;
import static com.zmark.mytodo.service.invariant.Msg.SERVER_INTERNAL_ERROR;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.zmark.mytodo.MainApplication;
import com.zmark.mytodo.R;
import com.zmark.mytodo.model.TaskSimple;
import com.zmark.mytodo.service.impl.TaskServiceImpl;
import com.zmark.mytodo.service.result.Result;
import com.zmark.mytodo.service.result.ResultCode;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecommendTaskAdapter extends RecyclerView.Adapter<RecommendTaskAdapter.ViewHolder> {
    private final static String TAG = "RecommendTaskAdapter";
    private RecommendTaskList recommendTaskList;

    public RecommendTaskAdapter() {
        this.recommendTaskList = new RecommendTaskList();
    }

    public void setRecommendTaskList(RecommendTaskList recommendTaskList) {
        this.recommendTaskList = recommendTaskList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.my_day_recommend_task_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        List<TaskSimple> taskSimpleRespList = recommendTaskList.getTaskSimpleRespList();
        TaskSimple taskSimple = taskSimpleRespList.get(position);
        holder.bind(taskSimple);
    }

    @Override
    public int getItemCount() {
        return recommendTaskList.getSize();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        public CheckBox checkBox;
        public TextView taskTitleTextView;
        public TextView tagsTextView;
        public TextView dueDateTextView;
        public ImageView recurringIconImageView;
        public View addToMyDayLayoutButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            checkBox = itemView.findViewById(R.id.checkBox);
            taskTitleTextView = itemView.findViewById(R.id.todoTitle);
            tagsTextView = itemView.findViewById(R.id.tagLayout);
            dueDateTextView = itemView.findViewById(R.id.dueDate);
            recurringIconImageView = itemView.findViewById(R.id.recurringIcon);
            addToMyDayLayoutButton = itemView.findViewById(R.id.addToMyDayLayoutButton);
        }

        public void bind(TaskSimple taskSimple) {
            if (!taskSimple.isBinding()) {
                taskTitleTextView.setText(taskSimple.getTitle());
                // 设置CheckBox的选中状态和事件
                checkBox.setOnCheckedChangeListener(null);
                // 使用 setTag 区分不同的 CheckBox
                checkBox.setTag(taskSimple.getId());
                checkBox.setChecked(taskSimple.getCompleted());
                checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                    if (isChecked) {
                        taskSimple.setCompleted(true);
                        TaskServiceImpl.completeTask(taskSimple.getId());
                    } else {
                        taskSimple.setCompleted(false);
                        TaskServiceImpl.unCompleteTask(taskSimple.getId());
                    }
                    // 通知Adapter刷新特定位置的项目
                    // 使用 Handler 延迟执行 notifyItemChanged
                    new Handler(Looper.getMainLooper()).post(() -> {
                        // 设置标志位，表示正在执行onBindViewHolder
                        taskSimple.setBinding(true);
                        notifyItemChanged(getAdapterPosition());
                        // 重置标志位
                        taskSimple.setBinding(false);
                    });
                });
                // 显示标签
                String tagsString = taskSimple.getTagString();
                if (!tagsString.isEmpty()) {
                    if (tagsString.length() >= 6) {
                        tagsString = tagsString.substring(0, 6) + "...";
                    }
                    tagsTextView.setText(tagsString);
                } else {
                    tagsTextView.setVisibility(View.GONE);
                }
                // 显示到期时间
                dueDateTextView.setText(taskSimple.getDueDate());
                // 显示循环图标（如果是循环任务）
                recurringIconImageView.setVisibility(taskSimple.isRecurring() ? View.VISIBLE : View.GONE);
                // 重要：确保每次设置 CheckBox 状态时都明确指定，避免 RecyclerView 复用导致的问题
                checkBox.setChecked(taskSimple.getCompleted());
                // 设置添加到我的一天的按钮
                addToMyDayLayoutButton.setOnClickListener(v -> {
                    addTaskToMyDay(taskSimple.getId());
                });
            }
        }

        private void addTaskToMyDay(Long taskId) {
            Call<Result<Object>> call = MainApplication.getMyDayTaskService().addTaskToMyDay(taskId);
            call.enqueue(new Callback<Result<Object>>() {
                @Override
                public void onResponse(@NonNull Call<Result<Object>> call, @NonNull Response<Result<Object>> response) {
                    if (response.isSuccessful()) {
                        Result<Object> result = response.body();
                        if (result == null) {
                            Log.w(TAG, "result is null");
                            Toast.makeText(itemView.getContext(), SERVER_INTERNAL_ERROR, Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if (result.getCode() == null || result.getCode() != ResultCode.SUCCESS.getCode()) {
                            Log.w(TAG, "code:" + result.getCode() + " onResponse: " + result.getMsg());
                            Toast.makeText(itemView.getContext(), result.getMsg(), Toast.LENGTH_SHORT).show();
                            return;
                        }
                        Log.i(TAG, "onResponse: 任务添加到我的一天");
                        // 1. 从推荐任务列表中移除该任务
                        recommendTaskList.removeTask(taskId);
                        // 2. 通知 Adapter 刷新
                        notifyItemRemoved(getAdapterPosition());
                    }
                }

                @Override
                public void onFailure(@NonNull Call<Result<Object>> call, @NonNull Throwable t) {
                    Log.e(TAG, "onFailure: ", t);
                    Toast.makeText(itemView.getContext(), CLIENT_REQUEST_ERROR, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
