package com.zmark.mytodo.config;

public class Config {
    //    private static final String REAR_BASE_URL = "http://192.168.31.61:8787/api/";
    /**
     * baseUrl must end in /
     */
    private static final String REAR_BASE_URL = "http://10.117.65.255:8787/api/";

    public static String getRearBaseUrl() {
        return REAR_BASE_URL;
    }
}
