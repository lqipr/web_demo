package com.lqipr.core.mybatis.plugin;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by lqipr on 2015/9/17.
 */
public class Page {
    private int limit;//条数
    private int pageIndex;//页码
    private String column = "id";//id列名
    private int type;
    public static final int TYPE_PAGE = 1;
    public static final int TYPE_ID = 2;
    private static final String WHERE = " where ";

    public Page(int pageIndex, int limit, int type){
        this.pageIndex = pageIndex;
        this.limit = limit;
        this.type = type;
    }


    public String getSql(String sql){
        if(type == TYPE_PAGE) {
            StringBuffer sb = new StringBuffer(sql);
            sb.append(" limit ").append(pageIndex * limit < 0 ? 0 : pageIndex * limit)
                    .append(", ").append(limit);
            return sb.toString();
        }else if(type == TYPE_ID){
            String sqlLower = sql.toLowerCase();
            int wi = sqlLower.indexOf(WHERE);
            if(wi > 0) { //带where
                int newPosition = wi + WHERE.length();
                StringBuffer sb = new StringBuffer();
                sb.append(sql.substring(0, newPosition))
                        .append(" ").append(column).append(" > ").append(pageIndex).append(" and ")
                        .append(sql.substring(newPosition))
                        .append(" order by ").append(column).append(" desc ")
                        .append(" limit ").append(limit);
                return sb.toString();
            }else{ //不带where
                int newPosition = sql.length();//默认在最后加

                if(sqlLower.matches(".* group[ ]+by .*")){//分组加分页的情况
                    Pattern pattern = Pattern.compile(" group[ ]+by ");
                    Matcher sqlMatcher = pattern.matcher(sqlLower);
                    if(sqlMatcher.find()) {
                        newPosition = sqlLower.indexOf(sqlMatcher.group());
                    }
                }

                StringBuffer sb = new StringBuffer();
                sb.append(sql.substring(0, newPosition))
                        .append(WHERE).append(column).append(" > ").append(pageIndex)
                        .append(sql.substring(newPosition))
                        .append(" order by ").append(column).append(" desc ")
                        .append(" limit ").append(limit);
                return sb.toString();
            }
        }
        return sql;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public int getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(int pageIndex) {
        this.pageIndex = pageIndex;
    }

    public String getColumn() {
        return column;
    }

    public void setColumn(String column) {
        this.column = column;
    }

    public static void main(String[] args) {
        String sql = "select * from asdf group  by asdf";
        System.out.println(new Page(0, 20, 2).getSql(sql));
//        System.out.println(sql.matches(".* group[ ]+by .*"));
//
//        Pattern pattern = Pattern.compile(" group[ ]+by ");
//        Matcher sqlMatcher = pattern.matcher(sql);
//        if(sqlMatcher.find()){
//            String group = sqlMatcher.group();
//            System.out.println(group);
//            System.out.println(sql.indexOf(group));
//            int group_idx = sql.indexOf(group);
//            System.out.println(sql.substring(group_idx));
//            System.out.println(sql.substring(0, group_idx));
//            System.out.println(sql.substring(group_idx));
//        }
    }
}
