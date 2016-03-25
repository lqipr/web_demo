package com.lqipr.dao;

import com.lqipr.core.dao.BaseDao;
import com.lqipr.po.User;

import java.util.List;
import java.util.Map;

/**
 * Created by lqipr on 2015/9/6.
 */
public interface UserDao extends BaseDao<User> {

    List<User> queryUserPage(Map<String, Object> map);
}
