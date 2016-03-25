package com.lqipr.service;

import com.lqipr.base.WebConfig;
import com.lqipr.po.User;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * Created by lqipr on 2015/9/16.
 */
public class UserSerivceTest {
    static ApplicationContext applicationContext = new AnnotationConfigApplicationContext(WebConfig.class);
    static UserService service = applicationContext.getBean("userServiceImpl", UserService.class);

    @Test
    public void get(){
        System.out.println(service.get(6));
    }

    @Test
    public void testTran(){
        User user = new User();
        user.setUserName("名字1");
        user.setPhone("110");
        service.testTransactional(user);
    }
    @Test
    public void testQueryPage(){
        for (int i = 0; i < 10; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    System.out.println(service.getUserPageByPage(0, "名字1", 0, 2));
                }
            }).start();
        }
        System.out.println(service.getUserPageByPage(0, "名字1", 0, 2));
    }

    @Test
    public void testQueryId(){
        System.out.println(service.getUserPageById(0, "名字1", 0, 2));
    }




}
