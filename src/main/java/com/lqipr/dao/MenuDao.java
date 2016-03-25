package com.lqipr.dao;

import com.lqipr.core.dao.BaseDao;
import com.lqipr.po.Menu;

import java.util.List;
import java.util.Map;

/**
 * Created by lqipr on 2015/10/20.
 */
public interface MenuDao extends BaseDao<Menu> {

    List<Menu> queryList(Map<String, Object> map);
}
