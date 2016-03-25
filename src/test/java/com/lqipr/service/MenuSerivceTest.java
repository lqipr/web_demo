package com.lqipr.service;

import com.lqipr.base.WebConfig;
import com.lqipr.po.Menu;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * Created by lqipr on 2015/9/16.
 */
public class MenuSerivceTest {
    static ApplicationContext applicationContext = new AnnotationConfigApplicationContext(WebConfig.class);
    static MenuService service = applicationContext.getBean("menuServiceImpl", MenuService.class);

    @Test
    public void add() {
        Menu menu = new Menu();
        menu.setCode("0002");
        menu.setName("菜单2");
        menu.setValue("");
        menu.setType(1);
        service.add(menu);
    }

    @Test
    public void get() {
        System.out.println(service.get(1));
    }


    @Test
    public void update() {
        Menu menu = service.get(1);
        menu.setName("菜单1更新一下");
        service.update(menu);
        System.out.println(menu);
    }

    @Test
    public void delete() {
        service.delete(2);
    }


}
