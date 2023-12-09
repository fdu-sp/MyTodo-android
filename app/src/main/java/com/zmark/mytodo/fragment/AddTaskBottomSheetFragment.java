package com.zmark.mytodo.fragment;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.zmark.mytodo.R;
import com.zmark.mytodo.api.bo.task.req.TaskCreatReq;

import java.util.Objects;

public class AddTaskBottomSheetFragment extends BottomSheetDialogFragment {

    public AddTaskBottomSheetFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View bottomSheetView = inflater.inflate(R.layout.fragment_add_task, container, false);

        // 获取底部抽屉中的 EditText
        EditText newTaskTitleInput = bottomSheetView.findViewById(R.id.newTaskTitleInput);
        EditText newTaskDescriptionInput = bottomSheetView.findViewById(R.id.newTaskDescriptionInput);

        // 设置底部抽屉中的确定按钮
        bottomSheetView.findViewById(R.id.confirmButton).setOnClickListener(view -> {
            // 处理用户点击确定按钮的逻辑
            if (newTaskTitleInput.getText().toString().isEmpty()) {
                // 如果待办事项为空，则不执行添加操作
                dismiss(); // 关闭底部抽屉
            }
            String title = newTaskTitleInput.getText().toString();
            String description = newTaskDescriptionInput.getText().toString();
            // 执行添加待办事项的操作
            TaskCreatReq createReq = new TaskCreatReq(title, description);
            // todo
            // createNewTask(createReq);
            // 关闭底部抽屉
            dismiss();
        });
        
        // 在输入法弹出时调整窗口的大小
        Dialog dialog = getDialog();
        if (dialog != null) {
            // 设置Dialog的windowSoftInputMode属性
            Objects.requireNonNull(dialog.getWindow()).setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        }
        return bottomSheetView;
    }
}
