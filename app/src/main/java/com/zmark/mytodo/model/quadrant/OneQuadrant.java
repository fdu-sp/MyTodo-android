package com.zmark.mytodo.model.quadrant;


import com.zmark.mytodo.model.task.TaskSimple;
import com.zmark.mytodo.service.bo.quadrant.resp.OneQuadrantDetailResp;

import java.util.List;

/**
 * @author ZMark
 * @date 2023/12/13 3:22
 */

public class OneQuadrant {
    private String title;
    private List<TaskSimple> tasks;

    public OneQuadrant(OneQuadrantDetailResp oneQuadrantDetailResp) {
        this.title = oneQuadrantDetailResp.getTitle();
        this.tasks = TaskSimple.from(oneQuadrantDetailResp.getTasks());
    }

    public String getTitle() {
        return title;
    }

    public List<TaskSimple> getTasks() {
        return tasks;
    }
}
