package com.zmark.mytodo.comparator.task;

import com.zmark.mytodo.model.TaskSimple;

/**
 * @see TaskSimpleComparator
 */
public class StatusComparator implements TaskSimpleComparator {
    private TaskSimpleComparator nextComparator;

    @Override
    public int compare(TaskSimple item1, TaskSimple item2) {
        if (item1.getCompleted() && !item2.getCompleted()) {
            return 1;
        } else if (!item1.getCompleted() && item2.getCompleted()) {
            return -1;
        } else {
            return nextComparator != null ? nextComparator.compare(item1, item2) : 0;
        }
    }

    @Override
    public void setNextComparator(TaskSimpleComparator nextComparator) {
        this.nextComparator = nextComparator;
    }
}
