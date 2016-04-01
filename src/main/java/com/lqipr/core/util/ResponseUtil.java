package com.lqipr.core.util;

import com.alibaba.fastjson.JSONObject;

/**
 * Created by lqipr on 2016/3/31.
 */
public class ResponseUtil {
    private final static String DEFAULT_MSG;

    private final static String STATUS_NAME = "status";
    private final static String MESSAGE_NAME = "message";
    private final static String RESULT_NAME = "result";

    private final static String ERROR_STATUS_MSG = "系统异常请联系客服人员";

    public final static String ERROR_STATUS = "9999";
    public final static String SUCCESS_STATUS = "0000";
    public final static String BUSINESS_STATUS = "9998";



    static{
        JSONObject obj = new JSONObject();
        obj.put(STATUS_NAME, ERROR_STATUS);
        obj.put(MESSAGE_NAME, ERROR_STATUS_MSG);
        DEFAULT_MSG = obj.toJSONString();
    }

    public static String getDefaultMsg() {
        return DEFAULT_MSG;
    }

    public static String getResponseMessage(String status, String message, Object o){
        JSONObject obj = new JSONObject();
        obj.put(STATUS_NAME, status);
        obj.put(MESSAGE_NAME, message);
        obj.put(RESULT_NAME, o);
        return obj.toJSONString();
    }
    public static String getResponseMessage(String status, String message){
        return result(status, message);
    }
    public static String getResponseMessage(String message){
        return result(BUSINESS_STATUS, message);
    }

    private static String result(String status, String message){
        JSONObject obj = new JSONObject();
        obj.put(STATUS_NAME, status);
        obj.put(MESSAGE_NAME, message);
        return obj.toJSONString();
    }
}
