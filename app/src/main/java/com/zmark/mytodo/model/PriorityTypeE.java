package com.zmark.mytodo.model;

public enum PriorityTypeE {
    URGENCY_IMPORTANT(1, "紧急且重要"),
    URGENCY_NOT_IMPORTANT(2, "紧急不重要"),
    NOT_URGENCY_IMPORTANT(3, "不紧急但重要"),
    NOT_URGENCY_NOT_IMPORTANT(4, "不紧急不重要");

    private final Integer code;
    private final String desc;

    PriorityTypeE(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public int getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}
