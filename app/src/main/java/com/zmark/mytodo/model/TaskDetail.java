package com.zmark.mytodo.model;

import com.zmark.mytodo.service.bo.tag.resp.TagSimpleResp;
import com.zmark.mytodo.service.bo.task.req.TaskCreateReq;
import com.zmark.mytodo.service.bo.task.resp.TaskDetailResp;
import com.zmark.mytodo.service.bo.task.resp.inner.TaskContentInfoResp;
import com.zmark.mytodo.service.bo.task.resp.inner.TaskPriorityInfoResp;
import com.zmark.mytodo.service.bo.task.resp.inner.TaskTimeInfoResp;

import java.util.ArrayList;
import java.util.List;

public class TaskDetail {
    private Long id;
    private String title;
    private Boolean completed;
    private String completedTime;
    private Boolean archived;
    private List<TagSimpleResp> tags;
    private Boolean inMyDay;
    private Long taskListId;
    private TaskContentInfoResp taskContentInfo;
    private TaskPriorityInfoResp taskPriorityInfo;
    private TaskTimeInfoResp taskTimeInfo;
    private String createTime;
    private String updateTime;

    public TaskDetail() {
        this.id = null;
        this.title = "";
        this.completed = false;
        this.completedTime = null;
        this.archived = false;
        this.tags = new ArrayList<>();
        this.inMyDay = false;
        this.taskListId = null;
        this.taskContentInfo = new TaskContentInfoResp();
        this.taskPriorityInfo = new TaskPriorityInfoResp();
        this.taskTimeInfo = new TaskTimeInfoResp();
        this.createTime = null;
        this.updateTime = null;
    }

    public TaskDetail(TaskDetailResp taskDetailResp) {
        this.id = taskDetailResp.getId();
        this.title = taskDetailResp.getTitle();
        this.completed = taskDetailResp.getCompleted();
        this.completedTime = taskDetailResp.getCompletedTime();
        this.archived = taskDetailResp.getArchived();
        this.tags = taskDetailResp.getTags();
        this.taskListId = taskDetailResp.getTaskListId();
        this.inMyDay = taskDetailResp.isInMyDay();
        this.taskContentInfo = taskDetailResp.getTaskContentInfo();
        this.taskPriorityInfo = taskDetailResp.getTaskPriorityInfo();
        this.taskTimeInfo = taskDetailResp.getTaskTimeInfo();
        this.createTime = taskDetailResp.getCreateTime();
        this.updateTime = taskDetailResp.getUpdateTime();
    }

    public TaskCreateReq toTaskCreateReq() {
        TaskCreateReq createReq = new TaskCreateReq();
        createReq.setTitle(this.title);
        List<String> tagNames = new ArrayList<>();
        for (TagSimpleResp tag : this.tags) {
            tagNames.add(tag.getTagPath());
        }
        createReq.setTagNames(tagNames);
        createReq.setDescription(this.taskContentInfo.getDescription());
        createReq.setTaskListId(this.taskListId);
        createReq.setImportant(this.taskPriorityInfo.getImportant());
        createReq.setUrgent(this.taskPriorityInfo.getUrgent());
        createReq.setEndDate(this.taskTimeInfo.getEndDate());
        createReq.setEndTime(this.taskTimeInfo.getEndTime());
        createReq.setActivateCountdown(this.taskTimeInfo.getActivateCountdown());
        createReq.setExpectedExecutionDate(this.taskTimeInfo.getExpectedExecutionDate());
        createReq.setExpectedExecutionStartPeriod(this.taskTimeInfo.getExpectedExecutionStartPeriod());
        createReq.setExpectedExecutionEndPeriod(this.taskTimeInfo.getExpectedExecutionEndPeriod());
        return createReq;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Boolean getCompleted() {
        return completed;
    }

    public void setCompleted(Boolean completed) {
        this.completed = completed;
    }


    public void complete() {
        this.completed = true;
    }

    public void unComplete() {
        this.completed = false;
    }

    public String getCompletedTime() {
        return completedTime;
    }

    public void setCompletedTime(String completedTime) {
        this.completedTime = completedTime;
    }

    public Boolean getArchived() {
        return archived;
    }

    public void setArchived(Boolean archived) {
        this.archived = archived;
    }

    public List<TagSimpleResp> getTags() {
        return tags;
    }

    public String getDetailTagString() {
        StringBuilder stringBuilder = new StringBuilder();
        for (TagSimpleResp tag : tags) {
            stringBuilder.append(tag.getTagName()).append(" ");
        }
        return stringBuilder.toString();
    }

    public void setTags(List<TagSimpleResp> tags) {
        this.tags = tags;
    }

    public void addTag(TagSimpleResp tag) {
        this.tags.add(tag);
    }

    public void removeTag(TagSimpleResp tag) {
        this.tags.remove(tag);
    }

    public Boolean getInMyDay() {
        return inMyDay;
    }

    public Long getTaskListId() {
        return taskListId;
    }

    public void setTaskListId(Long taskListId) {
        this.taskListId = taskListId;
    }

    public Boolean isInMyDay() {
        return inMyDay;
    }

    public void setInMyDay(Boolean inMyDay) {
        this.inMyDay = inMyDay;
    }

    public TaskContentInfoResp getTaskContentInfo() {
        return taskContentInfo;
    }

    public void setTaskContentInfo(TaskContentInfoResp taskContentInfo) {
        this.taskContentInfo = taskContentInfo;
    }

    public TaskPriorityInfoResp getTaskPriorityInfo() {
        return taskPriorityInfo;
    }

    public void setTaskPriorityInfo(TaskPriorityInfoResp taskPriorityInfo) {
        this.taskPriorityInfo = taskPriorityInfo;
    }

    public TaskTimeInfoResp getTaskTimeInfo() {
        return taskTimeInfo;
    }

    public void setTaskTimeInfo(TaskTimeInfoResp taskTimeInfo) {
        this.taskTimeInfo = taskTimeInfo;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }
}
