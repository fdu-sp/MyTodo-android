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

public class BottomSortSheetFragment extends BottomSheetDialogFragment {

    private final static String TAG = "BottomSheetFragment";


    public enum GroupTypeE {
        LIST(0, "清单"),
        TAG(1, "标签"),
        DUE_DATE(2, "截止日期"),
        PLAN_DATE(3, "规划日期"),
        PRIORITY(4, "优先级"),
        NONE(5, "无");

        private final int code;
        private final String desc;

        GroupTypeE(int code, String desc) {
            this.code = code;
            this.desc = desc;
        }

        public int getCode() {
            return code;
        }

        public String getDesc() {
            return desc;
        }

        public static GroupTypeE getByCode(int code) {
            for (GroupTypeE groupTypeE : GroupTypeE.values()) {
                if (groupTypeE.getCode() == code) {
                    return groupTypeE;
                }
            }
            return null;
        }

        public static GroupTypeE getByDesc(String desc) {
            for (GroupTypeE groupTypeE : GroupTypeE.values()) {
                if (groupTypeE.getDesc().equals(desc)) {
                    return groupTypeE;
                }
            }
            return null;
        }
    }

    /**
     * 定义分组选择的回调接口
     */
    public interface GroupSelectionListener {
        void onOptionsSelected(GroupTypeE sortType);
    }

    /**
     * 定义排序选择的回调接口
     */
    public interface SortSelectionListener {
        void onOptionsSelected(SortTypeE sortType);
    }

    /**
     * 分组选择 的 回调
     */
    private GroupSelectionListener groupListener;

    /**
     * 排序选择 的 回调
     */
    private SortSelectionListener sortListener;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottom_sheet_layout, container, false);

        // 分组监听器
        RadioGroup groupByRadioGroup = view.findViewById(R.id.radioGroupGroupBy);
        groupByRadioGroup.setOnCheckedChangeListener(this::handleGroupSelection);

        // 排序监听器
        RadioGroup sortByRadioGroup = view.findViewById(R.id.radioGroupSortBy);
        sortByRadioGroup.setOnCheckedChangeListener(this::handleSortSelection);

        return view;
    }

    private void handleGroupSelection(RadioGroup radioGroup, int checkedId) {
        // 获取选中的排序方式
        String groupType = getSelectedOptionText(checkedId);
        // 调用回调函数，将选项传递给调用者
        if (groupListener != null) {
            groupListener.onOptionsSelected(GroupTypeE.getByDesc(groupType));
        }
    }

    private void handleSortSelection(RadioGroup radioGroup, int checkedId) {
        // 获取选中的排序方式
        String sortType = getSelectedOptionText(checkedId);
        // 调用回调函数，将选项传递给调用者
        if (sortListener != null) {
            sortListener.onOptionsSelected(SortTypeE.getByDesc(sortType));
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

    /**
     * 设置group的回调监听器
     */
    public void setGroupListener(GroupSelectionListener listener) {
        this.groupListener = listener;
    }

    /**
     * 设置sort的回调监听器
     */
    public void setSortListener(SortSelectionListener listener) {
        this.sortListener = listener;
    }
}
