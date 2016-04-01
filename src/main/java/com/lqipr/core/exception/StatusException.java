package com.lqipr.core.exception;

/**
 * Created by lqipr on 2016/3/31.
 */
public class StatusException extends RuntimeException {
    private String status;
    private String message;

    public StatusException(String status, String message){
        this.status = status;
        this.message = message;
    }


    public String getStatus() {
        return status;
    }

    @Override
    public String getMessage() {
        return message;
    }

}
