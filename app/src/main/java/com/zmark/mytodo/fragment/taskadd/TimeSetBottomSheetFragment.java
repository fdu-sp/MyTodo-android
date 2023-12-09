package com.zmark.mytodo.fragment.taskadd;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.zmark.mytodo.R;

public class TimeSetBottomSheetFragment extends BottomSheetDialogFragment {
    private static final String TAG = "TimeSetBottomSheetFragment";

    public TimeSetBottomSheetFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_time_set, container, false);

        return view;
    }
}
