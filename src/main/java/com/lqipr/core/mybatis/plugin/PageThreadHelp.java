package com.lqipr.core.mybatis.plugin;

/**
 * 保证当前线程中只有一个page
 * Created by lqipr on 2015/9/17.
 */
public class PageThreadHelp {
    public static final ThreadLocal<Page> localPage = new ThreadLocal<Page>();
    public static void setPageByPage(int pageIndex, int limit){
        localPage.set(new Page(pageIndex, limit, Page.TYPE_PAGE));
    }

    public static void setPageById(int pageIndex, int limit){
        localPage.set(new Page(pageIndex, limit, Page.TYPE_ID));
    }

    public static Page get(){
        return localPage.get();
    }
    public static void clear(){
        localPage.remove();
    }

}
