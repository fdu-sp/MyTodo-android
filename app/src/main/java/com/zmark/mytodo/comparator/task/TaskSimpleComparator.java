package com.zmark.mytodo.comparator.task;

import com.zmark.mytodo.model.task.TaskSimple;

/**
 * todo 预计使用责任链模式 改造 排序代码
 */
public interface TaskSimpleComparator {
    int compare(TaskSimple item1, TaskSimple item2);

    void setNextComparator(TaskSimpleComparator nextComparator);
}
