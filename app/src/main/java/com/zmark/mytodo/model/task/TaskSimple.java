package com.zmark.mytodo.model.task;

import com.zmark.mytodo.service.bo.tag.resp.TagSimpleResp;
import com.zmark.mytodo.service.bo.task.resp.TaskSimpleResp;

import java.util.ArrayList;
import java.util.List;

public class TaskSimple {
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

    /**
     * 是否处于更新中
     */
    private boolean binding = false;

    public TaskSimple(Long id, String title, String description, String dueDate, String expectedDate, Boolean isImportant, Boolean isUrgent, Boolean completed, String completedTime, Boolean archived, List<TagSimpleResp> tags, String createTime, String updateTime) {
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

    public TaskSimple() {
    }

    public TaskSimple(TaskSimpleResp resp) {
        this.id = resp.getId();
        this.title = resp.getTitle();
        this.description = resp.getDescription();
        this.dueDate = resp.getDueDate();
        this.expectedDate = resp.getExpectedDate();
        this.isImportant = resp.getImportant();
        this.isUrgent = resp.getUrgent();
        this.completed = resp.getCompleted();
        this.completedTime = resp.getCompletedTime();
        this.archived = resp.getArchived();
        this.tags = resp.getTags();
        this.createTime = resp.getCreateTime();
        this.updateTime = resp.getUpdateTime();
    }

    public static List<TaskSimple> from(List<TaskSimpleResp> taskSimpleRespList) {
        List<TaskSimple> todoItemList = new ArrayList<>();
        taskSimpleRespList.forEach(taskSimpleResp -> todoItemList.add(new TaskSimple(taskSimpleResp)));
        return todoItemList;
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

    public boolean isBinding() {
        return binding;
    }

    public void setBinding(boolean binding) {
        this.binding = binding;
    }

    public String getTagString() {
        StringBuilder tagsStringBuilder = new StringBuilder();
        for (TagSimpleResp tagSimpleResp : this.getTags()) {
            String tag = tagSimpleResp.getTagName();
            if (tag.isEmpty()) {
                continue;
            }
            if (tag.length() > 5) {
                tag = tag.substring(0, Math.min(tag.length() - 1, 10)) + "...";
            }
            if (tag.endsWith(",")) {
                tag = tag.substring(0, tag.length() - 1);
            }
            if (tag.startsWith(",")) {
                tag = tag.substring(1);
            }
            if (this.getTags().indexOf(tagSimpleResp) == this.getTags().size() - 1) {
                tagsStringBuilder.append(tag);
            } else {
                tagsStringBuilder.append(tag).append(", ");
            }
        }
        return tagsStringBuilder.toString().trim();
    }

    public boolean isRecurring() {
        return false;
    }

    public void complete() {
        this.setCompleted(true);
    }

    public void unComplete() {
        this.setCompleted(false);
    }
}
