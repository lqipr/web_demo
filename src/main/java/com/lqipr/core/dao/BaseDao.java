package com.lqipr.core.dao;

/**
 * Created by lqipr on 2015/10/20.
 * 基本的 PO DAO 应该具有的标准功能  增删改 单独查
 */
public interface BaseDao<T> {

    T get(int id);
    int delete(int id);
    int insert(T t);
    int update(T t);

}
