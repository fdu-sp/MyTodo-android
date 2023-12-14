package com.zmark.mytodo.network.bo.quadrant.resp;


import com.zmark.mytodo.network.bo.list.resp.TaskListSimpleResp;

/**
 * @author ZMark
 * @date 2023/12/13 3:21
 */

public class FourQuadrantDetailResp {
    private TaskListSimpleResp taskListInfo;
    private OneQuadrantDetailResp urgentAndImportant;
    private OneQuadrantDetailResp urgentAndNotImportant;
    private OneQuadrantDetailResp notUrgentAndImportant;
    private OneQuadrantDetailResp notUrgentAndNotImportant;

    public TaskListSimpleResp getTaskListInfo() {
        return taskListInfo;
    }

    public OneQuadrantDetailResp getUrgentAndImportant() {
        return urgentAndImportant;
    }

    public OneQuadrantDetailResp getUrgentAndNotImportant() {
        return urgentAndNotImportant;
    }

    public OneQuadrantDetailResp getNotUrgentAndImportant() {
        return notUrgentAndImportant;
    }

    public OneQuadrantDetailResp getNotUrgentAndNotImportant() {
        return notUrgentAndNotImportant;
    }
}
