package com.zmark.mytodo.fragment.inner;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.zmark.mytodo.R;
import com.zmark.mytodo.comparator.task.SortTypeE;

public class BottomSheetFragment extends BottomSheetDialogFragment {

    private final static String TAG = "BottomSheetFragment";

    /**
     * 定义回调接口
     */
    public interface BottomSheetListener {
        void onOptionsSelected(ChoiceType choiceType, SortTypeE sortType);
    }

    public enum ChoiceType {
        GROUP_BY,
        SORT_BY
    }

    /**
     * 分组选择 的 回调
     */
    private BottomSheetListener groupListener;

    /**
     * 排序选择 的 回调
     */
    private BottomSheetListener sortListener;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottom_sheet_layout, container, false);

        // 分组监听器
        RadioGroup groupByRadioGroup = view.findViewById(R.id.radioGroupGroupBy);
        groupByRadioGroup.setOnCheckedChangeListener(this::handleSortSelection);

        // 排序监听器
        RadioGroup sortByRadioGroup = view.findViewById(R.id.radioGroupSortBy);
        sortByRadioGroup.setOnCheckedChangeListener(this::handleSortSelection);

        return view;
    }

    private void handleSortSelection(RadioGroup radioGroup, int checkedId) {
        // 获取选中的排序方式
        String sortType = getSelectedOptionText(checkedId);
        // 调用回调函数，将选项传递给调用者
        if (sortListener != null) {
            sortListener.onOptionsSelected(ChoiceType.SORT_BY, SortTypeE.getByDesc(sortType));
        }
    }

    /**
     * 辅助方法：获取选中的选项文本
     */
    private String getSelectedOptionText(int selectedId) {
        RadioButton selectedRadioButton = requireView().findViewById(selectedId);
        if (selectedRadioButton != null) {
            return selectedRadioButton.getText().toString();
        }
        return "";
    }

    // 设置sort的回调监听器
    public void setSortListener(BottomSheetListener listener) {
        this.sortListener = listener;
    }
}
