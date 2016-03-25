package com.lqipr.service;

import com.lqipr.po.User;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by lqipr on 2015/9/6.
 */
public interface UserService {

    User get(int id);

    int add(User user);
    int update(User user);


    List<User> getUser(int startId, int limit);

    List<User> getUserPageByPage(int id, String userName, int pageIndex, int pageSize);

    List<User> getUserPageById(int id, String userName, int pageIndex, int pageSize);

    @Transactional
    void testTransactional(User user);
}
