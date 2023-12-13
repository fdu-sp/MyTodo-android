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

import com.zmark.mytodo.MainApplication;
import com.zmark.mytodo.model.group.TaskListSimple;
import com.zmark.mytodo.model.task.TaskDetail;
import com.zmark.mytodo.service.ApiUtils;
import com.zmark.mytodo.service.bo.task.resp.TaskDetailResp;
import com.zmark.mytodo.service.impl.MyDayTaskServiceImpl;
import com.zmark.mytodo.service.impl.TaskServiceImpl;
import com.zmark.mytodo.service.result.Result;
import com.zmark.mytodo.utils.TimeUtils;

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

    private OnTaskCompleteStateListener onTaskCompleteStateListener;

    private OnTaskUpdateListener onTaskUpdateListener;

    private final TaskListSimple taskListSimple;

    public TaskDetailBottomSheetFragment(TaskListSimple taskListSimple, TaskDetail taskDetail) {
        super(taskDetail);
        this.taskListSimple = taskListSimple;
    }

    public void setOnTaskCompleteStateListener(OnTaskCompleteStateListener listener) {
        this.onTaskCompleteStateListener = listener;
    }

    public void setOnTaskUpdateListener(OnTaskUpdateListener listener) {
        this.onTaskUpdateListener = listener;
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
        this.taskListNameTextView.setText(taskListSimple.getName());
        // 设置checkbox的事件和选中状态
        this.checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                taskDetail.complete();
                TaskServiceImpl.completeTask(taskDetail.getId());
            } else {
                taskDetail.unComplete();
                TaskServiceImpl.unCompleteTask(taskDetail.getId());
            }
            if (onTaskCompleteStateListener != null) {
                onTaskCompleteStateListener.onTaskCompleteStateChanged(taskDetail);
            }
        });
        this.checkBox.setChecked(taskDetail.getCompleted());
        // 显示确认按钮
        confirmButton.setVisibility(View.VISIBLE);

        // 设置 添加到我的一天
        // todo 如果是 我的一天 任务列表，需要通知上层更新
        this.addToMyDayLayout.setOnClickListener(v -> {
            if (taskDetail.isInMyDay()) {
                MyDayTaskServiceImpl.removeFromMyDayList(taskDetail.getId(), new MyDayTaskServiceImpl.Callbacks() {
                    @Override
                    public void onSuccess() {
                        taskDetail.setInMyDay(false);
                        updateAddToMyDayUI();
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

        // 设置创建时间
        this.bottomCardView.setVisibility(View.VISIBLE);
        this.updateTimeCreateUI();

        // 设置删除
        this.deleteImageView.setOnClickListener(v -> {
            // todo
        });
    }

    @Override
    protected void handleConfirmButtonClick(View view) {
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

    private void updateTimeCreateUI() {
        requireActivity().runOnUiThread(() -> {
            String timeCreateStr = "创建于 " +
                    TimeUtils.getFormattedDateStrFromTimeStamp(taskDetail.getCreateTime());
            this.timeCreateShowTextView.setText(timeCreateStr);
        });
    }
}
