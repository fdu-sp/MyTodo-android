package com.zmark.mytodo.model.task;

import android.app.Activity;
import android.content.res.ColorStateList;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.zmark.mytodo.MainApplication;
import com.zmark.mytodo.R;
import com.zmark.mytodo.handler.OnTaskContentClickListener;
import com.zmark.mytodo.invariant.Msg;
import com.zmark.mytodo.network.ApiUtils;
import com.zmark.mytodo.network.result.Result;

import java.util.List;

import retrofit2.Call;

public class QuadrantTaskItemAdapter extends RecyclerView.Adapter<QuadrantTaskItemAdapter.QuadrantViewHolder> {
    private final static String TAG = "QuadrantTaskItemAdapter";
    private final Activity activity;
    private final List<TaskSimple> tasks;
    private final ColorStateList colorStateList;

    private OnTaskContentClickListener onTaskContentClickListener;

    public interface OnTaskCompleteStateListener {
        void onTaskCompleteStateChanged(TaskSimple taskSimple);
    }

    private static OnTaskCompleteStateListener onTaskCompleteStateListener;

    public QuadrantTaskItemAdapter(@NonNull Activity activity, @NonNull List<TaskSimple> tasks,
                                   @NonNull ColorStateList colorStateList) {
        this.activity = activity;
        this.tasks = tasks;
        this.colorStateList = colorStateList;
    }

    public static void setOnTaskCompleteStateListener(OnTaskCompleteStateListener onTaskCompleteStateListener) {
        QuadrantTaskItemAdapter.onTaskCompleteStateListener = onTaskCompleteStateListener;
    }

    public void setOnTaskContentClickListener(OnTaskContentClickListener listener) {
        this.onTaskContentClickListener = listener;
    }

    @NonNull
    @Override
    public QuadrantViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.quadrant_task_item, parent, false);
        return new QuadrantViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull QuadrantViewHolder holder, int position) {
        TaskSimple task = tasks.get(position);
        holder.bind(task);
    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }

    class QuadrantViewHolder extends RecyclerView.ViewHolder {

        private final TextView taskTitleView;
        private final CheckBox taskCheckBox;

        public QuadrantViewHolder(@NonNull View itemView) {
            super(itemView);
            taskTitleView = itemView.findViewById(R.id.taskTitle);
            taskCheckBox = itemView.findViewById(R.id.taskCheckBox);
        }

        public void bind(TaskSimple task) {
            Log.d(TAG, "bind: " + task);
            // 设置checkbox的事件和选中状态
            this.taskCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked) {
                    task.complete();
                    Call<Result<String>> call = MainApplication.getTaskService().completeTask(task.getId());
                    ApiUtils.doRequest(call, new ApiUtils.Callbacks<String>() {
                        @Override
                        public void onSuccess(String data) {
                            Log.i(TAG, "onSuccess: " + data);
                            updateUI(task);
                            if (onTaskCompleteStateListener != null) {
                                onTaskCompleteStateListener.onTaskCompleteStateChanged(task);
                            }
                        }

                        @Override
                        public void onFailure(Integer code, String msg) {
                            Log.w(TAG, "onFailure: " + code + " " + msg);
                            Toast.makeText(itemView.getContext(), msg, Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onClientRequestError(Throwable t) {
                            Log.e(TAG, "onFailure: ", t);
                            Toast.makeText(itemView.getContext(), Msg.CLIENT_REQUEST_ERROR, Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onServerInternalError() {
                            Toast.makeText(itemView.getContext(), Msg.SERVER_INTERNAL_ERROR, Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    task.unComplete();
                    Call<Result<String>> call = MainApplication.getTaskService().unCompleteTask(task.getId());
                    ApiUtils.doRequest(call, new ApiUtils.Callbacks<String>() {
                        @Override
                        public void onSuccess(String data) {
                            Log.i(TAG, "onSuccess: " + data);
                            updateUI(task);
                            if (onTaskCompleteStateListener != null) {
                                onTaskCompleteStateListener.onTaskCompleteStateChanged(task);
                            }
                        }

                        @Override
                        public void onFailure(Integer code, String msg) {
                            Log.w(TAG, "onFailure: " + code + " " + msg);
                            Toast.makeText(itemView.getContext(), msg, Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onClientRequestError(Throwable t) {
                            Log.e(TAG, "onFailure: ", t);
                            Toast.makeText(itemView.getContext(), Msg.CLIENT_REQUEST_ERROR, Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onServerInternalError() {
                            Toast.makeText(itemView.getContext(), Msg.SERVER_INTERNAL_ERROR, Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });
            this.taskCheckBox.setButtonTintList(colorStateList);
            this.taskTitleView.setText(task.getTitle());
            this.taskTitleView.setOnClickListener(v -> {
                if (onTaskContentClickListener != null) {
                    onTaskContentClickListener.onTaskContentClick(task);
                }
            });
            this.taskCheckBox.setChecked(task.getCompleted());
        }

        public void updateUI(TaskSimple task) {
            activity.runOnUiThread(() -> {
                this.taskTitleView.setText(task.getTitle());
                this.taskCheckBox.setChecked(task.getCompleted());
            });
        }
    }
}
