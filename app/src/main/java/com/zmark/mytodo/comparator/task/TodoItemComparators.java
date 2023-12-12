package com.zmark.mytodo.comparator.task;

import com.zmark.mytodo.model.TaskSimple;

import java.text.Collator;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * 默认先按照状态排序
 */
public class TodoItemComparators {
    private static final Map<SortTypeE, Comparator<TaskSimple>> sortComparatorMap = new HashMap<>();

    static {
        sortComparatorMap.put(SortTypeE.DUE_DATE_FIRST, dueDatePrecedence());
        sortComparatorMap.put(SortTypeE.PLAN_DATE_FIRST, planDatePrecedence());
        sortComparatorMap.put(SortTypeE.PRIORITY_FIRST, priorityPrecedence());
        sortComparatorMap.put(SortTypeE.TITLE_FIRST, titlePrecedence());
        sortComparatorMap.put(SortTypeE.TAG_FIRST, tagPrecedence());
    }

    public static Comparator<TaskSimple> getComparator(SortTypeE sortType) {
        Comparator<TaskSimple> comparator = sortComparatorMap.get(sortType);
        if (comparator == null) {
            comparator = dueDatePrecedence();
        }
        return comparator;
    }

    private static final Collator chinaCollator = Collator.getInstance(Locale.CHINA);

    private static int compareByStatus(TaskSimple item1, TaskSimple item2) {
        if (item1.getCompleted() && !item2.getCompleted()) {
            return 1;
        } else if (!item1.getCompleted() && item2.getCompleted()) {
            return -1;
        } else {
            return 0;
        }
    }

    private static int compareByTitle(TaskSimple item1, TaskSimple item2) {
        if (item1.getTitle() == null) {
            return 1;
        }
        if (item2.getTitle() == null) {
            return -1;
        }
        return chinaCollator.compare(item1.getTitle(), item2.getTitle());
    }

    private static int compareByTag(TaskSimple item1, TaskSimple item2) {
        String tag1 = item1.getTagString();
        String tag2 = item2.getTagString();
        if (tag1 == null) {
            return 1;
        }
        if (tag2 == null) {
            return -1;
        }
        return chinaCollator.compare(tag1, tag2);
    }

    private static int compareByDueDate(TaskSimple item1, TaskSimple item2) {
        if (item1.getDueDate() == null) {
            return 1;
        }
        if (item2.getDueDate() == null) {
            return -1;
        }
        return item1.getDueDate().compareTo(item2.getDueDate());
    }

    private static int compareByExpectedDate(TaskSimple item1, TaskSimple item2) {
        if (item1.getExpectedDate() == null) {
            return 1;
        }
        if (item2.getExpectedDate() == null) {
            return -1;
        }
        return item1.getExpectedDate().compareTo(item2.getExpectedDate());
    }

    private static int compareByPriority(TaskSimple item1, TaskSimple item2) {
        if (item1.getUrgent() && !item2.getUrgent()) {
            return -1;
        } else if (!item1.getUrgent() && item2.getUrgent()) {
            return 1;
        } else {
            if (item1.getImportant() && !item2.getImportant()) {
                return -1;
            } else if (!item1.getImportant() && item2.getImportant()) {
                return 1;
            } else {
                return 0;
            }
        }
    }

    /**
     * 先按照状态排序，再截止日期优先
     */
    public static Comparator<TaskSimple> dueDatePrecedence() {
        return (o1, o2) -> {
            int statusCompare = compareByStatus(o1, o2);
            if (statusCompare != 0) {
                return statusCompare;
            }
            return compareByDueDate(o1, o2);
        };
    }

    /**
     * 先按照状态排序，再标题优先
     */
    public static Comparator<TaskSimple> titlePrecedence() {
        return (o1, o2) -> {
            int statusCompare = compareByStatus(o1, o2);
            if (statusCompare != 0) {
                return statusCompare;
            }
            return compareByTitle(o1, o2);
        };
    }

    /**
     * 先按照状态排序，再规划日期优先
     */
    public static Comparator<TaskSimple> planDatePrecedence() {
        return (o1, o2) -> {
            int statusCompare = compareByStatus(o1, o2);
            if (statusCompare != 0) {
                return statusCompare;
            }
            return compareByExpectedDate(o1, o2);
        };
    }

    /**
     * 先按照状态排序，再优先级优先
     */
    public static Comparator<TaskSimple> priorityPrecedence() {
        return (o1, o2) -> {
            int statusCompare = compareByStatus(o1, o2);
            if (statusCompare != 0) {
                return statusCompare;
            }
            return compareByPriority(o1, o2);
        };
    }

    /**
     * 先按照状态排序，再标签优先
     */
    public static Comparator<TaskSimple> tagPrecedence() {
        return (o1, o2) -> {
            int statusCompare = compareByStatus(o1, o2);
            if (statusCompare != 0) {
                return statusCompare;
            }
            return compareByTag(o1, o2);
        };
    }
}
