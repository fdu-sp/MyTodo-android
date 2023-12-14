package com.zmark.mytodo.fragment.common;

import android.app.Dialog;
import android.content.DialogInterface;
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

import java.util.List;

public class AddListDialogFragment extends DialogFragment {

    private EditText editTextName;
    private EditText editTextDescription;
    private Spinner spinnerTaskGroups;

    private final List<TaskGroup> taskGroups;

    public interface OnListCreatedListener {
        void onListCreated(String listName, String description, Long taskGroupId);
    }

    private OnListCreatedListener onListCreatedListener;

    public AddListDialogFragment(List<TaskGroup> taskGroups) {
        this.taskGroups = taskGroups;
    }

    public void setOnListCreatedListener(OnListCreatedListener onListCreatedListener) {
        this.onListCreatedListener = onListCreatedListener;
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

        // 设置下拉框适配器
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(),
                android.R.layout.simple_spinner_item, taskGroups.stream().map(TaskGroup::getName).toArray(String[]::new));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTaskGroups.setAdapter(adapter);

        builder.setView(view)
                .setTitle("新增清单")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String name = editTextName.getText().toString().trim();
                        String description = editTextDescription.getText().toString().trim();
                        String taskGroupName = spinnerTaskGroups.getSelectedItem().toString();
                        TaskGroup selectedTaskGroup = taskGroups.stream()
                                .filter(taskGroup -> taskGroup.getName().equals(taskGroupName))
                                .findFirst()
                                .orElse(null);
                        Long taskGroupId = (selectedTaskGroup != null) ? selectedTaskGroup.getId() : null;

                        if (onListCreatedListener != null) {
                            onListCreatedListener.onListCreated(name, description, taskGroupId);
                        }
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        return builder.create();
    }
}

