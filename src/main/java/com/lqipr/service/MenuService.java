package com.lqipr.service;

import com.lqipr.po.Menu;

/**
 * Created by lqipr on 2015/10/20.
 * 菜单管理service
 */
public interface MenuService {

    Menu get(int id);
    int delete(int id);
    int add(Menu menu);
    int update(Menu menu);
    
}
