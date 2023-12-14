package com.zmark.mytodo.fragment;

import static com.zmark.mytodo.service.invariant.Msg.CLIENT_REQUEST_ERROR;
import static com.zmark.mytodo.service.invariant.Msg.SERVER_INTERNAL_ERROR;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.zmark.mytodo.MainApplication;
import com.zmark.mytodo.model.task.TaskDetail;
import com.zmark.mytodo.service.ApiUtils;
import com.zmark.mytodo.service.bo.task.resp.TaskDetailResp;
import com.zmark.mytodo.service.impl.MyDayTaskServiceImpl;
import com.zmark.mytodo.service.impl.TaskServiceImpl;
import com.zmark.mytodo.service.result.Result;
import com.zmark.mytodo.utils.TimeUtils;

import java.util.Optional;

import retrofit2.Call;

/**
 * 任务详情页
 */
public class TaskDetailBottomSheetFragment extends AddTaskBottomSheetFragment {
    private static final String TAG = "TaskDetailBottomSheetFragment";

    public interface OnTaskCompleteStateListener {
        void onTaskCompleteStateChanged(TaskDetail taskDetail);
    }

    public interface OnTaskUpdateListener {
        void onTaskUpdated(TaskDetail taskDetail);
    }

    public interface OnTaskDeleteListener {
        void onTaskDeleted(TaskDetail taskDetail);
    }

    private OnTaskCompleteStateListener onTaskCompleteStateListener;
    private OnTaskUpdateListener onTaskUpdateListener;
    private OnTaskDeleteListener onTaskDeleteListener;


    public TaskDetailBottomSheetFragment(TaskDetail taskDetail) {
        super(taskDetail);
    }

    public void setOnTaskCompleteStateListener(OnTaskCompleteStateListener listener) {
        this.onTaskCompleteStateListener = listener;
    }

    public void setOnTaskUpdateListener(OnTaskUpdateListener listener) {
        this.onTaskUpdateListener = listener;
    }

    public void setOnTaskDeleteListener(OnTaskDeleteListener listener) {
        this.onTaskDeleteListener = listener;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // 显示确认按钮
        confirmButton.setVisibility(View.VISIBLE);

        // 设置创建时间
        this.bottomCardView.setVisibility(View.VISIBLE);
        this.updateTimeCreateUI();

        // 设置删除
        this.deleteImageView.setVisibility(View.VISIBLE);
    }

    @Override
    protected void handleBackButtonClick(View view) {
        this.updateTask();
    }

    @Override
    protected void handleConfirmButtonClick(View view) {
        this.updateTask();
    }

    /**
     * 仅当成功时才会dismiss
     */
    private void updateTask() {
        String title = taskTitle.getText().toString();
        String description = Optional.of(editTextMultiLine.getText().toString()).orElse("");
        taskDetail.setTitle(title);
        taskDetail.getTaskContentInfo().setDescription(description);
        Call<Result<TaskDetailResp>> call = MainApplication.getTaskService().updateTask(taskDetail.toTaskUpdateReq());
        ApiUtils.doRequest(call, new ApiUtils.Callbacks<TaskDetailResp>() {
            @Override
            public void onSuccess(TaskDetailResp data) {
                Log.i(TAG, "onSuccess: " + data);
                taskDetail = new TaskDetail(data);
                Toast.makeText(getContext(), "更新成功", Toast.LENGTH_SHORT).show();

                if (onTaskUpdateListener != null) {
                    onTaskUpdateListener.onTaskUpdated(taskDetail);
                }
                dismiss();
            }

            @Override
            public void onFailure(Integer code, String msg) {
                Log.w(TAG, "onFailure: " + code + " " + msg);
                Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onClientRequestError(Throwable t) {
                Log.e(TAG, "onClientRequestError: ", t);
                Toast.makeText(getContext(), CLIENT_REQUEST_ERROR, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onServerInternalError() {
                Toast.makeText(getContext(), SERVER_INTERNAL_ERROR, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void handleCheckBoxClick(View view, boolean isChecked) {
        if (isChecked) {
            taskDetail.complete();
            TaskServiceImpl.completeTask(taskDetail.getId());
        } else {
            taskDetail.unComplete();
            TaskServiceImpl.unCompleteTask(taskDetail.getId());
        }
        this.updateCheckBoxUI();
        if (onTaskCompleteStateListener != null) {
            onTaskCompleteStateListener.onTaskCompleteStateChanged(taskDetail);
        }
    }

    @Override
    protected void handleAddToMyDayClick(View view) {
        // 设置 添加到我的一天
        this.addToMyDayLayout.setOnClickListener(v -> {
            if (taskDetail.isInMyDay()) {
                MyDayTaskServiceImpl.removeFromMyDayList(taskDetail.getId(), new MyDayTaskServiceImpl.Callbacks() {
                    @Override
                    public void onSuccess() {
                        taskDetail.setInMyDay(false);
                        updateAddToMyDayUI();
                        // 如果是 我的一天 任务列表，需要通知上层更新
                        if (onTaskUpdateListener != null) {
                            onTaskUpdateListener.onTaskUpdated(taskDetail);
                        }
                    }

                    @Override
                    public void onFailure(Integer code, String msg) {
                        Log.w(TAG, "onFailure: " + code + " " + msg);
                        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onClientRequestError(Throwable t) {
                        Log.e(TAG, "onClientRequestError: ", t);
                        Toast.makeText(getContext(), CLIENT_REQUEST_ERROR, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onServerInternalError() {
                        Toast.makeText(getContext(), SERVER_INTERNAL_ERROR, Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                MyDayTaskServiceImpl.addTaskToMyDay(taskDetail.getId(), new MyDayTaskServiceImpl.Callbacks() {
                    @Override
                    public void onSuccess() {
                        taskDetail.setInMyDay(true);
                        updateAddToMyDayUI();
                        // 如果是 我的一天 任务列表，需要通知上层更新
                        if (onTaskUpdateListener != null) {
                            onTaskUpdateListener.onTaskUpdated(taskDetail);
                        }
                    }

                    @Override
                    public void onFailure(Integer code, String msg) {
                        Log.w(TAG, "onFailure: " + code + " " + msg);
                        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onClientRequestError(Throwable t) {
                        Log.e(TAG, "onClientRequestError: ", t);
                        Toast.makeText(getContext(), CLIENT_REQUEST_ERROR, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onServerInternalError() {
                        Toast.makeText(getContext(), SERVER_INTERNAL_ERROR, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        this.updateAddToMyDayUI();
    }

    @Override
    protected void handleDeleteImageView(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("确认删除");
        builder.setMessage("你确定要删除吗？");
        // 添加确认按钮
        builder.setPositiveButton("是", (dialog, which) -> {
            Call<Result<String>> call = MainApplication.getTaskService().deleteTaskById(taskDetail.getId());
            ApiUtils.doRequest(call, new ApiUtils.Callbacks<String>() {
                @Override
                public void onSuccess(String data) {
                    Log.i(TAG, "onSuccess: " + data);
                    Toast.makeText(getContext(), "删除成功", Toast.LENGTH_SHORT).show();
                    if (onTaskDeleteListener != null) {
                        onTaskDeleteListener.onTaskDeleted(taskDetail);
                    }
                    dismiss();
                }

                @Override
                public void onFailure(Integer code, String msg) {
                    Log.w(TAG, "onFailure: " + code + " " + msg);
                    Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onClientRequestError(Throwable t) {
                    Log.e(TAG, "onClientRequestError: ", t);
                    Toast.makeText(getContext(), CLIENT_REQUEST_ERROR, Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onServerInternalError() {
                    Toast.makeText(getContext(), SERVER_INTERNAL_ERROR, Toast.LENGTH_SHORT).show();
                }
            });
        });
        // 添加取消按钮
        builder.setNegativeButton("否", (dialog, which) -> {
            dialog.dismiss(); // 关闭对话框
        });
        // 创建并显示对话框
        builder.create().show();
    }

    private void updateTimeCreateUI() {
        requireActivity().runOnUiThread(() -> {
            String timeCreateStr = "创建于 " +
                    TimeUtils.getFormattedDateStrFromTimeStamp(taskDetail.getCreateTime());
            this.timeCreateShowTextView.setText(timeCreateStr);
        });
    }
}
