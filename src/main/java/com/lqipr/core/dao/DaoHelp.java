package com.lqipr.core.dao;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;
import java.beans.Introspector;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 获取创建数据库连接池
 * Created by lqipr on 2015/9/6.
 */
public abstract class DaoHelp {
    private static final Map<String, DataSource> map = new ConcurrentHashMap();

    public DaoHelp() {
    }

    public static DataSource getDataSource() {
        String name = DaoSelector.getName();
        if(name == null) {
            throw new IllegalStateException("当前线程变量中的数据源为空");
        } else {
            return getDataSource(name);
        }
    }

    public static DataSource getDataSource(String name) {
        DataSource ds = map.get(name);
        if(ds != null) {
            return ds;
        } else {
            synchronized(map) {
                ds = map.get(name);
                if(ds != null) {
                    return ds;
                } else {
                    load(name);
                    ds = map.get(name);
                    return ds;
                }
            }
        }
    }

    private static void load(String name){
        Properties props = loadProperties(name);
        if(props==null){
            throw new RuntimeException("load Properties file " + name + "_db.properties error");
        }
        load(name, props);
    }

    /**
     * DB配置文件加载专用
     * @param name
     * @return
     */
    private static Properties loadProperties(String name){
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        InputStream input;
        if(loader == null) {
            input = ClassLoader.getSystemResourceAsStream("properties/" + name + "_db.properties");
        } else {
            input = loader.getResourceAsStream("properties/" + name + "_db.properties");
        }

        Properties props = null;
        if(input == null) {
            return props;
        }else {
            props = new Properties();
            try {
                props.load(input);
            } catch (IOException e) {
                return null;
            }
            return props;
        }
    }

    /**
     * 生成PoolDataSource
     * @param name
     * @param props
     */
    private static void load(String name, Properties props) {

        try {
            String jndi = props.getProperty("jndiName");
            DataSource ds;
            if(jndi != null) {
                String jndiPrefix=props.getProperty("jndiPrefix","java:comp/env/jdbc/");
                if(!jndiPrefix.endsWith("/")) jndiPrefix=jndiPrefix+"/";
                Context ctx=new InitialContext();
                ds=(DataSource) ctx.lookup(jndiPrefix+jndi);
                map.put(name, ds);
                return;
            }

            //MYSQL 调用构造生成
//            Class clazz = Class.forName("com.mysql.jdbc.jdbc2.optional.MysqlConnectionPoolDataSource");
//            Constructor constructor = clazz.getConstructor();
//            ds = (DataSource)constructor.newInstance();

            //UCP  调用某个方法获取结果
            Class<?> clazz=Class.forName("oracle.ucp.jdbc.PoolDataSourceFactory");
            Method method=clazz.getMethod("getPoolDataSource",new Class[0]);
            ds=(DataSource) method.invoke(null,(Object[]) null);

            setDataSource(ds, props);
            map.put(name, ds);
        } catch (Exception var15) {
            var15.printStackTrace();
        }

    }

    public static void setDataSource(DataSource ds,Properties props){
        for(Method m:ds.getClass().getMethods()){
            String prop=m.getName();
            if(!prop.startsWith("set")||prop.length()<4||m.getParameterTypes().length!=1) continue;
            prop=Introspector.decapitalize(prop.substring(3));
            Object o=props.getProperty(prop);
            if(o==null) continue;
            Class<?> cls=m.getParameterTypes()[0];
            if(cls!=String.class){
                o=stringToPrimitive(cls,(String)o);
            }
            try {
                m.invoke(ds,o);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static Object stringToPrimitive(Class<?> type,String value) {

        if (type==Boolean.TYPE||type==Boolean.class) {
            if (value == null)	return Boolean.FALSE;
            return Boolean.valueOf(value);
        }
        if (value == null)
            value = "0";
        if (type==Integer.TYPE||type==Integer.class)
            return Integer.valueOf(value);
        if (type==Long.TYPE||type==Long.class)
            return Long.valueOf(value);
        if (type==Double.TYPE||type==Double.class)
            return Double.valueOf(value);
        if (type==Float.TYPE||type==Float.class)
            return Float.valueOf(value);
        if (type==Byte.TYPE||type==Byte.class)
            return Byte.valueOf(value);
        if (type==Short.TYPE||type==Short.class)
            return Short.valueOf(value);
        return null;
    }

}
