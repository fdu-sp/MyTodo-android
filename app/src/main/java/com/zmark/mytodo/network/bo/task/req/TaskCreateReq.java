package com.zmark.mytodo.network.bo.task.req;

import java.util.ArrayList;
import java.util.List;


/**
 * @author ZMark
 * @date 2023/12/4 10:04
 */
public class TaskCreateReq {
    private String title;
    /**
     * 可以为空，但是不可以为null，所以默认值为{}
     */
    private List<String> tagNames = new ArrayList<>();

    private Boolean completed;
    /**
     * content description
     * 不可以为null，所以默认值为""
     */
    private String description = "";

    private Long taskListId;

    private Boolean inMyDay;

    /**
     * priority info
     */
    private Boolean isImportant;
    private Boolean isUrgent;

    /**
     * time info
     */
    private String endDate;
    private String endTime;
    private String reminderTimestamp;
    private Boolean activateCountdown;
    private String expectedExecutionDate;
    private String expectedExecutionStartPeriod;
    private String expectedExecutionEndPeriod;

    public TaskCreateReq() {
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setTagNames(List<String> tagNames) {
        this.tagNames = tagNames;
    }

    public void setCompleted(Boolean completed) {
        this.completed = completed;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setTaskListId(Long taskListId) {
        this.taskListId = taskListId;
    }

    public void setInMyDay(Boolean inMyDay) {
        this.inMyDay = inMyDay;
    }

    public void setImportant(Boolean important) {
        isImportant = important;
    }

    public void setUrgent(Boolean urgent) {
        isUrgent = urgent;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public void setReminderTimestamp(String reminderTimestamp) {
        this.reminderTimestamp = reminderTimestamp;
    }

    public void setActivateCountdown(Boolean activateCountdown) {
        this.activateCountdown = activateCountdown;
    }

    public void setExpectedExecutionDate(String expectedExecutionDate) {
        this.expectedExecutionDate = expectedExecutionDate;
    }

    public void setExpectedExecutionStartPeriod(String expectedExecutionStartPeriod) {
        this.expectedExecutionStartPeriod = expectedExecutionStartPeriod;
    }

    public void setExpectedExecutionEndPeriod(String expectedExecutionEndPeriod) {
        this.expectedExecutionEndPeriod = expectedExecutionEndPeriod;
    }
}
