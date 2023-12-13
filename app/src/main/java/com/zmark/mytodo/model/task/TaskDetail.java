package com.zmark.mytodo.model.task;

import com.zmark.mytodo.model.tag.TagSimple;
import com.zmark.mytodo.service.bo.task.req.TaskCreateReq;
import com.zmark.mytodo.service.bo.task.req.TaskUpdateReq;
import com.zmark.mytodo.service.bo.task.resp.TaskDetailResp;
import com.zmark.mytodo.service.bo.task.resp.inner.TaskContentInfoResp;
import com.zmark.mytodo.service.bo.task.resp.inner.TaskPriorityInfoResp;
import com.zmark.mytodo.service.bo.task.resp.inner.TaskTimeInfoResp;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TaskDetail {
    private Long id;
    private String title;
    private Boolean completed;
    private String completedTime;
    private Boolean archived;
    private List<TagSimple> tags;
    private Boolean inMyDay;
    private Long taskListId;
    private String taskListName;
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
        this.taskListName = null;
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
        this.tags = taskDetailResp.getTags().stream().map(TagSimple::new).collect(Collectors.toList());
        this.taskListId = taskDetailResp.getTaskListId();
        this.taskListName = taskDetailResp.getTaskListName();
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
        for (TagSimple tag : this.tags) {
            tagNames.add(tag.getTagPath());
        }
        createReq.setTagNames(tagNames);
        createReq.setCompleted(this.completed);
        createReq.setDescription(this.taskContentInfo.getDescription());
        createReq.setTaskListId(this.taskListId);
        createReq.setInMyDay(this.inMyDay);
        createReq.setImportant(this.taskPriorityInfo.getImportant());
        createReq.setUrgent(this.taskPriorityInfo.getUrgent());
        createReq.setEndDate(this.taskTimeInfo.getEndDate());
        createReq.setEndTime(this.taskTimeInfo.getEndTime());
        createReq.setReminderTimestamp(this.taskTimeInfo.getReminderTimestamp());
        createReq.setActivateCountdown(this.taskTimeInfo.getActivateCountdown());
        createReq.setExpectedExecutionDate(this.taskTimeInfo.getExpectedExecutionDate());
        createReq.setExpectedExecutionStartPeriod(this.taskTimeInfo.getExpectedExecutionStartPeriod());
        createReq.setExpectedExecutionEndPeriod(this.taskTimeInfo.getExpectedExecutionEndPeriod());
        return createReq;
    }

    public TaskUpdateReq toTaskUpdateReq() {
        TaskUpdateReq updateReq = new TaskUpdateReq();
        updateReq.setId(this.id);
        updateReq.setTitle(this.title);
        updateReq.setCompleted(this.completed);
        updateReq.setCompletedTime(this.completedTime);
        updateReq.setArchived(this.archived);
        updateReq.setTags(this.tags.stream().map(TagSimple::toTagSimpleResp).collect(Collectors.toList()));
        updateReq.setTaskListId(this.taskListId);
        updateReq.setTaskListName(this.taskListName);
        updateReq.setInMyDay(this.inMyDay);
        updateReq.setTaskContentInfo(this.taskContentInfo);
        updateReq.setTaskPriorityInfo(this.taskPriorityInfo);
        updateReq.setTaskTimeInfo(this.taskTimeInfo);
        updateReq.setCreateTime(this.createTime);
        updateReq.setUpdateTime(this.updateTime);
        return updateReq;
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

    public List<TagSimple> getTags() {
        return tags;
    }

    public String getDetailTagString() {
        StringBuilder stringBuilder = new StringBuilder();
        for (TagSimple tag : tags) {
            stringBuilder.append(tag.getTagName()).append(" ");
        }
        return stringBuilder.toString();
    }

    public void setTags(List<TagSimple> tags) {
        this.tags = tags;
    }

    public void addTag(TagSimple tag) {
        if (this.tags == null) {
            this.tags = new ArrayList<>();
        }
        for (TagSimple tagSimple : this.tags) {
            if (tagSimple.getId().equals(tag.getId())) {
                return;
            }
        }
        this.tags.add(tag);
    }

    public void removeTag(TagSimple tag) {
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

    public String getTaskListName() {
        return taskListName;
    }

    public void setTaskListName(String taskListName) {
        this.taskListName = taskListName;
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
