package com.lqipr.exception;

import java.io.Serializable;

/**
 * Created by lqipr on 2016/3/25.
 */
public class MyException extends RuntimeException implements Serializable {
    public MyException() {
        super();
    }

    public MyException(String msg) {
        super(msg);
    }

    public MyException(String msg, Exception e) {
        super(msg, e);
    }
}

