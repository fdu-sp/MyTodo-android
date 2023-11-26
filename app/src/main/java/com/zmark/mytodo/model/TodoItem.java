package com.zmark.mytodo.model;

import java.util.ArrayList;
import java.util.List;

public class TodoItem {
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

    static public List<TodoItem> getToDoList(int dataNum) {
        List<TodoItem> todoList = new ArrayList<>();
        for (int i = 0; i < dataNum; i++) {
            List<String> tags = new ArrayList<>();
            for (int j = 0; j < 3; j++) {
                tags.add("标签" + j);
            }
            // i%31得到的是0-30的整数，加上1得到1-31的整数，取两位的整数（只有一位的前面补零），作为日期
            int modNum = i % 31 + 1;
            String dueDate = "2023-10-" + (modNum < 10 ? "0" + modNum : modNum);
            todoList.add(new TodoItem("任务" + i, "这是任务" + i + "的描述", tags, dueDate, i % 2 == 0));
        }
        return todoList;
    }

}
