package com.lqipr.core.exception;

import java.io.Serializable;

/**
 * 业务异常
 * Created by lqipr on 2016/3/25.
 */
public class BusinessException extends RuntimeException implements Serializable {
    public BusinessException() {
    }

    public BusinessException(String message) {
        super(message);
    }

    public BusinessException(String message, Throwable cause) {
        super(message, cause);
    }

    public BusinessException(Throwable cause) {
        super(cause);
    }
}

