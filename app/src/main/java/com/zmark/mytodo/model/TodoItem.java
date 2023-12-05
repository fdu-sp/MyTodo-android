package com.zmark.mytodo.model;

import com.zmark.mytodo.api.vo.task.resp.TaskSimpleResp;

import java.util.ArrayList;
import java.util.List;

public class TodoItem {
    private Long id;
    private String title;
    private String description;
    private List<String> tags;
    private String dueDate;
    private boolean isRecurring;
    private boolean isDone;

    public TodoItem(String title, String description, List<String> tags, String dueDate, boolean isRecurring) {
        this.title = title;
        this.description = description;
        this.tags = tags;
        this.dueDate = dueDate;
        this.isRecurring = isRecurring;
        this.isDone = false;
    }

    public TodoItem(TaskSimpleResp taskSimpleResp) {
        this.id = taskSimpleResp.getId();
        this.title = taskSimpleResp.getTitle();
        this.description = "";
        this.tags = new ArrayList<>();
        taskSimpleResp.getTags().forEach(tagSimpleResp -> this.tags.add(tagSimpleResp.getTagName()));
        this.dueDate = taskSimpleResp.getDueDate();
        this.isRecurring = false;
        this.isDone = taskSimpleResp.getCompleted();
    }

    public static List<TodoItem> from(List<TaskSimpleResp> taskSimpleRespList) {
        List<TodoItem> todoItemList = new ArrayList<>();
        taskSimpleRespList.forEach(taskSimpleResp -> todoItemList.add(new TodoItem(taskSimpleResp)));
        return todoItemList;
    }

    public String getTagString() {
        StringBuilder tagsStringBuilder = new StringBuilder();
        for (String tag : this.getTags()) {
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
            if (this.getTags().indexOf(tag) == this.getTags().size() - 1) {
                tagsStringBuilder.append(tag);
            } else {
                tagsStringBuilder.append(tag).append(", ");
            }
        }
        return tagsStringBuilder.toString().trim();
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public List<String> getTags() {
        return tags;
    }

    public String getDueDate() {
        return dueDate;
    }

    public boolean isRecurring() {
        return isRecurring;
    }

    public boolean isDone() {
        return isDone;
    }

    public void changeToBeDone() {
        this.isDone = true;
    }

    public void changeToBeUndone() {
        this.isDone = false;
    }
}
