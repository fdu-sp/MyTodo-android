package com.zmark.mytodo.fragment.common;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.zmark.mytodo.R;

public class AddGroupDialogFragment extends DialogFragment {
    private static final String TAG = "AddGroupDialogFragment";

    public interface OnGroupCreatedListener {
        void onGroupCreated(String groupName, String description);
    }

    private OnGroupCreatedListener onGroupCreatedListener;

    public void setOnGroupCreatedListener(OnGroupCreatedListener onGroupCreatedListener) {
        this.onGroupCreatedListener = onGroupCreatedListener;
    }


    private EditText editTextGroupName;
    private EditText editTextDescription;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_add_group_dialog, null);

        editTextGroupName = view.findViewById(R.id.editTextGroupName);
        editTextDescription = view.findViewById(R.id.editTextDescription);

        builder.setView(view)
                .setTitle("创建新的分组")
                .setPositiveButton("确定", (dialog, which) -> {
                    String groupName = editTextGroupName.getText().toString().trim();
                    String description = editTextDescription.getText().toString().trim();
                    if (onGroupCreatedListener != null) {
                        onGroupCreatedListener.onGroupCreated(groupName, description);
                    }
                })
                .setNegativeButton("取消", (dialog, which) -> dialog.dismiss());

        return builder.create();
    }
}

