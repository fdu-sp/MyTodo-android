package com.zmark.mytodo.service.bo.task.resp.inner;


import com.zmark.mytodo.model.PriorityTypeE;

/**
 * @author ZMark
 * @date 2023/12/4 10:14
 */
public class TaskPriorityInfoResp {
    private Boolean isImportant;
    private Boolean isUrgent;

    public PriorityTypeE getPriorityType() {
        if (isImportant && isUrgent) {
            return PriorityTypeE.URGENCY_IMPORTANT;
        } else if (isImportant) {
            return PriorityTypeE.NOT_URGENCY_IMPORTANT;
        } else if (isUrgent) {
            return PriorityTypeE.URGENCY_NOT_IMPORTANT;
        } else {
            return PriorityTypeE.NOT_URGENCY_NOT_IMPORTANT;
        }
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
}
