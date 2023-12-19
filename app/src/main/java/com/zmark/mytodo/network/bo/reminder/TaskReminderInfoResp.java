package com.zmark.mytodo.network.bo.reminder;

/**
 * @author ZMark
 * @date 2023/12/19 14:37
 */
public class TaskReminderInfoResp {
    private Long taskId;
    private String reminderTimestamp;

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public String getReminderTimestamp() {
        return reminderTimestamp;
    }

    public void setReminderTimestamp(String reminderTimestamp) {
        this.reminderTimestamp = reminderTimestamp;
    }
}
