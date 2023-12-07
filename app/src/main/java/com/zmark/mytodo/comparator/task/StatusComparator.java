package com.zmark.mytodo.comparator.task;

import com.zmark.mytodo.model.TodoItem;

/**
 * @see TodoItemComparator
 */
public class StatusComparator implements TodoItemComparator {
    private TodoItemComparator nextComparator;

    @Override
    public int compare(TodoItem item1, TodoItem item2) {
        if (item1.isDone() && !item2.isDone()) {
            return 1;
        } else if (!item1.isDone() && item2.isDone()) {
            return -1;
        } else {
            return nextComparator != null ? nextComparator.compare(item1, item2) : 0;
        }
    }

    @Override
    public void setNextComparator(TodoItemComparator nextComparator) {
        this.nextComparator = nextComparator;
    }
}
