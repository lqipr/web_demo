package com.lqipr.service.impl;

import com.lqipr.core.util.MD5Util;
import com.lqipr.dao.UserDao;
import com.lqipr.po.User;
import com.lqipr.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lqipr on 2015/9/6.
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    @Override
    public User get(int id) {
        return userDao.get(id);
    }

    @Override
    public int add(User user) {
        if(user != null && user.getPassWord() != null)
                user.setPassWord(MD5Util.MD5(user.getPassWord()));
        return userDao.insert(user);
    }

    @Override
    public int update(User user) {
        return userDao.update(user);
    }

    @Override
    public List<User> getUser(int startId, int limit) {
        return null;
    }

    @Override
    public List<User> getUserPageByPage(int id, String userName, int pageIndex, int pageSize) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        map.put("userName", userName);
        return userDao.queryUserPage(map);
    }
    @Override
    public List<User> getUserPageById(int id, String userName, int pageIndex, int pageSize) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        map.put("userName", userName);
        return userDao.queryUserPage(map);
    }

    @Override
    public void testTransactional(User user){
        userDao.insert(user);
        System.out.println(user.getId());
        user.setPhone(user.getPhone() + "ADD");
        userDao.update(user);
        System.out.println(userDao.get(user.getId()));
    }

}
