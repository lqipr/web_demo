package com.lqipr.core.dao;

/**
 * jndi 线程唯一 保证当前线程中只有一个DataSource
 * Created by lqipr on 2015/9/6.
 */
public abstract class DaoSelector {
    private static final ThreadLocal<String> dbSelector = new ThreadLocal();

    public DaoSelector() {
    }

    public static void setName(String name) {
        dbSelector.set(name);
    }

    public static String getName() {
        return dbSelector.get();
    }

    public static void clearName() {
        dbSelector.remove();
    }

}
