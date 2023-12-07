package com.zmark.mytodo.api.vo.task.resp;


import com.zmark.mytodo.api.vo.tag.resp.TagSimpleResp;

import java.util.List;

/**
 * @author ZMark
 * @date 2023/12/4 10:20
 */
public class TaskSimpleResp {
    private Long id;
    private String title;
    private String description;
    private String dueDate;
    private String expectedDate;
    private Boolean isImportant;
    private Boolean isUrgent;
    private Boolean completed;
    private String completedTime;
    private Boolean archived;
    private List<TagSimpleResp> tags;
    private String createTime;
    private String updateTime;

    public TaskSimpleResp(Long id, String title, String description, String dueDate, String expectedDate, Boolean isImportant, Boolean isUrgent, Boolean completed, String completedTime, Boolean archived, List<TagSimpleResp> tags, String createTime, String updateTime) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.dueDate = dueDate;
        this.expectedDate = expectedDate;
        this.isImportant = isImportant;
        this.isUrgent = isUrgent;
        this.completed = completed;
        this.completedTime = completedTime;
        this.archived = archived;
        this.tags = tags;
        this.createTime = createTime;
        this.updateTime = updateTime;
    }

    public TaskSimpleResp() {
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public String getExpectedDate() {
        return expectedDate;
    }

    public void setExpectedDate(String expectedDate) {
        this.expectedDate = expectedDate;
    }

    public Boolean getImportant() {
        return isImportant;
    }

    public void setImportant(Boolean important) {
        isImportant = important;
    }

    public Boolean getUrgent() {
        return isUrgent;
    }

    public void setUrgent(Boolean urgent) {
        isUrgent = urgent;
    }

    public Boolean getCompleted() {
        return completed;
    }

    public void setCompleted(Boolean completed) {
        this.completed = completed;
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

    public void setTags(List<TagSimpleResp> tags) {
        this.tags = tags;
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
