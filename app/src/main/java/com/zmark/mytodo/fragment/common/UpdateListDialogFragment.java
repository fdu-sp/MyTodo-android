package com.zmark.mytodo.fragment.common;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.zmark.mytodo.R;
import com.zmark.mytodo.model.group.TaskGroup;
import com.zmark.mytodo.model.group.TaskListSimple;

import java.util.List;

public class UpdateListDialogFragment extends DialogFragment {

    private EditText editTextName;
    private EditText editTextDescription;
    private Spinner spinnerTaskGroups;

    private final TaskListSimple taskListSimple;
    private final List<TaskGroup> taskGroups;

    public interface OnListUpdatedListener {
        void onListCreated(TaskListSimple updatedTaskListSimple);
    }

    private OnListUpdatedListener onListUpdatedListener;

    public UpdateListDialogFragment(TaskListSimple taskListSimple, List<TaskGroup> taskGroups) {
        this.taskListSimple = taskListSimple;
        this.taskGroups = taskGroups;
    }

    public void setOnListCreatedListener(OnListUpdatedListener onListUpdatedListener) {
        this.onListUpdatedListener = onListUpdatedListener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_add_list_dialog, null);

        editTextName = view.findViewById(R.id.editTextListName);
        editTextDescription = view.findViewById(R.id.editTextListDescription);
        spinnerTaskGroups = view.findViewById(R.id.spinnerTaskGroups);

        // 设置默认值
        editTextName.setText(taskListSimple.getName());

        // 设置下拉框适配器
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(),
                android.R.layout.simple_spinner_item, taskGroups.stream().map(TaskGroup::getName).toArray(String[]::new));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTaskGroups.setAdapter(adapter);

        builder.setView(view)
                .setTitle("更新清单信息")
                .setPositiveButton("确定", (dialog, which) -> {
                    String name = editTextName.getText().toString().trim();
                    String description = editTextDescription.getText().toString().trim();
                    String taskGroupName = spinnerTaskGroups.getSelectedItem().toString();
                    TaskGroup selectedTaskGroup = taskGroups.stream()
                            .filter(taskGroup -> taskGroup.getName().equals(taskGroupName))
                            .findFirst()
                            .orElse(null);
                    Long taskGroupId = (selectedTaskGroup != null) ? selectedTaskGroup.getId() : null;

                    if (onListUpdatedListener != null) {
                        taskListSimple.setName(name);
                        taskListSimple.setDescription(description);
                        taskListSimple.setGroupId(taskGroupId);
                        onListUpdatedListener.onListCreated(taskListSimple);
                    }
                })
                .setNegativeButton("取消", (dialog, which) -> dialog.dismiss());

        return builder.create();
    }
}

