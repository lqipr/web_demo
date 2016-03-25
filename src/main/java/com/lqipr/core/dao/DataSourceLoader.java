package com.lqipr.core.dao;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;

import javax.sql.DataSource;

/**
 * 将数据库连接池注入 spring session工厂中
 * Created by lqipr on 2015/9/6.
 */
public class DataSourceLoader implements BeanFactoryAware {
    private DefaultListableBeanFactory beanFactory;

    public DataSourceLoader() {
    }

    public final DataSource load(String selector){
        return load(selector, selector + "DS");
    }

    private final DataSource load(String selector, String beanName) {
        if(!this.beanFactory.containsBean(beanName)) {
            DaoSelector.setName(selector);
            this.beanFactory.registerSingleton(beanName, DaoHelp.getDataSource());
        }

        return this.beanFactory.getBean(beanName, DataSource.class);
    }

    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = (DefaultListableBeanFactory)beanFactory;
    }

    public static void main(String[] args) {
        new DataSourceLoader().load("test");
    }

}
