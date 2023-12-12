package com.zmark.mytodo.service.bo.quadrant.resp;


import com.zmark.mytodo.service.bo.task.resp.TaskSimpleResp;

import java.util.List;

/**
 * @author ZMark
 * @date 2023/12/13 3:22
 */

public class OneQuadrantDetailResp {
    private String title;
    private List<TaskSimpleResp> tasks;
    
    public String getTitle() {
        return title;
    }

    public List<TaskSimpleResp> getTasks() {
        return tasks;
    }
}
