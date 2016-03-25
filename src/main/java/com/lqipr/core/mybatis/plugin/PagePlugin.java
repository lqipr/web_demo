package com.lqipr.core.mybatis.plugin;

import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.util.Properties;

/**
 * Mybatis查询分页
 * PageIntercept 适配此插件
 * PageThreadHelp 设置当前线程分页插件
 * ID方式分页必须带 where
 * 例:where 1=1
 * Created by lqipr on 2015/9/17.
 */
@Intercepts({ @Signature(type = StatementHandler.class, method = "prepare", args = { Connection.class }) })
public class PagePlugin implements Interceptor {

    Logger logger = Logger.getLogger(PagePlugin.class);

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
            StatementHandler statementHandler = (StatementHandler) invocation.getTarget();
            MetaObject metaStatementHandler = SystemMetaObject.forObject(statementHandler);
            MappedStatement mappedStatement = (MappedStatement) metaStatementHandler.getValue("delegate.mappedStatement");
            if (SqlCommandType.SELECT == mappedStatement.getSqlCommandType()) {
                //获取由拦截器适配当前线程中的分页插件
                Page page = PageThreadHelp.get();
                if (page != null) {
                    try {
                        BoundSql boundSql = (BoundSql) metaStatementHandler.getValue("delegate.boundSql");
                        metaStatementHandler.setValue("delegate.boundSql.sql", page.getSql(boundSql.getSql()));
                    } catch (Exception e) {
                        logger.error(e);
                    } finally {
                        //用完以后清除当前线程中的分页插件
                        PageThreadHelp.clear();
                    }
                }
            }

        Object result = invocation.proceed();
        return result;
    }



    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {
        logger.debug("====setProperties===");
        logger.debug(properties.values());
    }
}
