package com.zmark.mytodo.comparator.task;

import com.zmark.mytodo.model.TodoItem;

/**
 * todo 预计使用责任链模式 改造 排序代码
 */
public interface TodoItemComparator {
    int compare(TodoItem item1, TodoItem item2);

    void setNextComparator(TodoItemComparator nextComparator);
}
