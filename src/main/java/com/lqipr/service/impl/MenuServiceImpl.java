package com.lqipr.service.impl;

import com.lqipr.dao.MenuDao;
import com.lqipr.po.Menu;
import com.lqipr.service.MenuService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lqipr on 2015/10/20.
 */
@Service
public class MenuServiceImpl implements MenuService{

    Logger logger = Logger.getLogger(MenuServiceImpl.class);

    @Autowired
    MenuDao menuDao;

    @Override
    public Menu get(int id) {
        return menuDao.get(id);
    }

    @Override
    public int delete(int id) {
        return menuDao.delete(id);
    }

    @Override
    public int add(Menu menu) {
        Map<String, Object> map = new HashMap<>();
        map.put("code", menu.getCode());
        List<Menu> list = menuDao.queryList(map);
        if(list == null || list.size() <= 0){
            return menuDao.insert(menu);
        }
        logger.error(String.format("code已经存在:%s", menu.getCode()));
        return -1;
    }

    @Override
    public int update(Menu menu) {
        return menuDao.update(menu);
    }


}
