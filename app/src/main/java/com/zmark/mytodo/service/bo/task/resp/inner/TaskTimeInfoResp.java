package com.zmark.mytodo.service.bo.task.resp.inner;


/**
 * @author ZMark
 * @date 2023/12/4 10:18
 */
public class TaskTimeInfoResp {
    private String endDate;
    private String endTime;
    private Boolean activateCountdown;
    private String expectedExecutionDate;
    private String expectedExecutionStartPeriod;
    private String expectedExecutionEndPeriod;

    public TaskTimeInfoResp() {
        this.endDate = null;
        this.endTime = null;
        this.activateCountdown = false;
        this.expectedExecutionDate = null;
        this.expectedExecutionStartPeriod = null;
        this.expectedExecutionEndPeriod = null;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public Boolean getActivateCountdown() {
        return activateCountdown;
    }

    public void setActivateCountdown(Boolean activateCountdown) {
        this.activateCountdown = activateCountdown;
    }

    public String getExpectedExecutionDate() {
        return expectedExecutionDate;
    }

    public void setExpectedExecutionDate(String expectedExecutionDate) {
        this.expectedExecutionDate = expectedExecutionDate;
    }

    public String getExpectedExecutionStartPeriod() {
        return expectedExecutionStartPeriod;
    }

    public void setExpectedExecutionStartPeriod(String expectedExecutionStartPeriod) {
        this.expectedExecutionStartPeriod = expectedExecutionStartPeriod;
    }

    public String getExpectedExecutionEndPeriod() {
        return expectedExecutionEndPeriod;
    }

    public void setExpectedExecutionEndPeriod(String expectedExecutionEndPeriod) {
        this.expectedExecutionEndPeriod = expectedExecutionEndPeriod;
    }
}
