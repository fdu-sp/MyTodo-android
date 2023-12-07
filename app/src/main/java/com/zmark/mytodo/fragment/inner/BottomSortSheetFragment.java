package com.zmark.mytodo.fragment.inner;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;

import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
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

    private final GroupTypeE groupTypeE;
    private final SortTypeE sortTypeE;

    public BottomSortSheetFragment(GroupTypeE groupTypeE, SortTypeE sortTypeE) {
        this.groupTypeE = groupTypeE;
        this.sortTypeE = sortTypeE;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottom_sheet_layout, container, false);
        // 注册表单选项
        this.registerSelectionChips(view);
        return view;
    }

    private void registerSelectionChips(View view) {
        // 分组方式选择
        ChipGroup chipGroupGroupBy = view.findViewById(R.id.chipGroupGroupBy);
        for (int i = 0; i < chipGroupGroupBy.getChildCount(); i++) {
            View child = chipGroupGroupBy.getChildAt(i);
            if (child instanceof Chip) {
                Chip chip = (Chip) child;
                chip.setOnCheckedChangeListener((buttonView, isChecked) -> {
                    if (isChecked) {
                        // 取消其他Chip的选中状态
                        uncheckOtherChips(chipGroupGroupBy, chip);
                        handleGroupSelection(chip);
                    }
                });

                if (chip.getText().equals(groupTypeE.getDesc())) {
                    chip.setChecked(true);
                }
            }
        }

        // 排序方式选择
        ChipGroup chipGroupSortBy = view.findViewById(R.id.chipGroupSortBy);
        for (int i = 0; i < chipGroupSortBy.getChildCount(); i++) {
            View child = chipGroupSortBy.getChildAt(i);
            if (child instanceof Chip) {
                Chip chip = (Chip) child;
                chip.setOnCheckedChangeListener((buttonView, isChecked) -> {
                    if (isChecked) {
                        // 取消其他Chip的选中状态
                        uncheckOtherChips(chipGroupSortBy, chip);
                        handleSortSelection(chip);
                    }
                });

                if (chip.getText().equals(sortTypeE.getDesc())) {
                    chip.setChecked(true);
                }
            }
        }
    }

    private void handleGroupSelection(Chip chip) {
        // 获取选中的排序方式
        String groupType = String.valueOf(chip.getText());
        // 调用回调函数，将选项传递给调用者
        if (groupListener != null) {
            groupListener.onOptionsSelected(GroupTypeE.getByDesc(groupType));
        }
    }

    private void handleSortSelection(Chip chip) {
        // 获取选中的排序方式
        String sortType = String.valueOf(chip.getText());
        // 调用回调函数，将选项传递给调用者
        if (sortListener != null) {
            sortListener.onOptionsSelected(SortTypeE.getByDesc(sortType));
        }
    }

    private void uncheckOtherChips(ChipGroup chipGroup, Chip selectedChip) {
        for (int i = 0; i < chipGroup.getChildCount(); i++) {
            View child = chipGroup.getChildAt(i);
            if (child instanceof Chip) {
                Chip chip = (Chip) child;
                if (chip != selectedChip) {
                    chip.setChecked(false);
                }
            }
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
