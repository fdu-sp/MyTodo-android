package com.zmark.mytodo.comparator.task;

public enum SortTypeE {
    DUE_DATE_FIRST(0, "截止日期"),
    TITLE_FIRST(1, "标题"),
    TAG_FIRST(2, "标签");

    private final int code;
    private final String desc;

    SortTypeE(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public int getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public static SortTypeE getByCode(int code) {
        for (SortTypeE sortTypeE : SortTypeE.values()) {
            if (sortTypeE.getCode() == code) {
                return sortTypeE;
            }
        }
        return null;
    }

    public static SortTypeE getByDesc(String desc) {
        for (SortTypeE sortTypeE : SortTypeE.values()) {
            if (sortTypeE.getDesc().equals(desc)) {
                return sortTypeE;
            }
        }
        return null;
    }
}
