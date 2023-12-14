package com.zmark.mytodo.model.task;

public enum PriorityTypeE {
    URGENCY_IMPORTANT(1, "紧急且重要", true, true),
    URGENCY_NOT_IMPORTANT(2, "紧急不重要", true, false),
    NOT_URGENCY_IMPORTANT(3, "不紧急但重要", false, true),
    NOT_URGENCY_NOT_IMPORTANT(4, "不紧急不重要", false, false);

    private final Integer code;
    private final String desc;
    private final boolean isUrgent;
    private final boolean isImportant;

    PriorityTypeE(Integer code, String desc, boolean isUrgent, boolean isImportant) {
        this.code = code;
        this.desc = desc;
        this.isUrgent = isUrgent;
        this.isImportant = isImportant;
    }

    public int getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public boolean isUrgent() {
        return isUrgent;
    }

    public boolean isImportant() {
        return isImportant;
    }

    public static String[] getPriorityArray() {
        String[] priorityArray = new String[PriorityTypeE.values().length];
        for (int i = 0; i < PriorityTypeE.values().length; i++) {
            priorityArray[i] = PriorityTypeE.values()[i].getDesc();
        }
        return priorityArray;
    }

    public static PriorityTypeE getByCode(Integer code) {
        for (PriorityTypeE priorityTypeE : PriorityTypeE.values()) {
            if (priorityTypeE.getCode() == code) {
                return priorityTypeE;
            }
        }
        return null;
    }

    public static PriorityTypeE getBy(Boolean isUrgent, Boolean isImportant) {
        for (PriorityTypeE priorityTypeE : PriorityTypeE.values()) {
            if (priorityTypeE.isUrgent() == isUrgent && priorityTypeE.isImportant() == isImportant) {
                return priorityTypeE;
            }
        }
        return null;
    }
}
