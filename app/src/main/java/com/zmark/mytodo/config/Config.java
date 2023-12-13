package com.zmark.mytodo.config;

public class Config {
    private static final String AGREEMENT = "http://";

    private static final String SERVER_IP = "10.117.65.255";
//    private static final String SERVER_IP = "192.168.31.61";

    private static final String SERVER_PORT = "8787";

    /**
     * baseUrl must end in /
     */
    private static final String REAR_BASE_URL = AGREEMENT + SERVER_IP + ":" + SERVER_PORT + "/api/";

    public static String getRearBaseUrl() {
        return REAR_BASE_URL;
    }
}
