package com.lqipr.base;

import com.lqipr.core.dao.DataSourceLoader;
import com.lqipr.core.exception.ActionExceptionMapper;
import com.lqipr.core.util.PropertiesUtil;
import com.lqipr.service.UserService;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.log4j.PropertyConfigurator;
import org.jboss.resteasy.spi.ResteasyProviderFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.*;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.Properties;

/**
 * Created by lqipr on 2015/9/6.
 */

@Configuration
@EnableTransactionManagement
@ComponentScan("com.lqipr*")
@MapperScan("com.lqipr.dao*")
@EnableAspectJAutoProxy(proxyTargetClass = true)
public class WebConfig extends DataSourceLoader {

    static{
        init();
    }

    public static void init() {
        try {
            Properties properties = PropertiesUtil.getProperties("properties/log4j.properties");
            System.out.println("classpath:"+WebConfig.class.getResource(""));
            if (properties != null)
                PropertyConfigurator.configure(properties);
        } catch (Exception e) {
            System.out.println("LOG4J加载失败");

        }
    }

    @Bean
    public SqlSessionFactory testSessionFactory() throws Exception {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(getDataSource("test"));
        DefaultResourceLoader dc = new DefaultResourceLoader();
        bean.setConfigLocation(dc.getResource("classpath:mybatis/test_mapper_config.xml"));
        return bean.getObject();
    }

    @Bean
    public DataSourceTransactionManager testTransactionManager() {
        DataSourceTransactionManager transactionManager = new DataSourceTransactionManager();
        transactionManager.setDataSource(getDataSource("test"));
        return transactionManager;
    }

    @Autowired
    public void setActionExceptionMapper(){
        ResteasyProviderFactory.getInstance().register(new ActionExceptionMapper());
    }

    public DataSource getDataSource(String name) {
        return load(name);
    }

    public static void main(String[] args) {
        ApplicationContext applicationContext = new AnnotationConfigApplicationContext(WebConfig.class);
        final UserService userService = applicationContext.getBean("userServiceImpl", UserService.class);

        //MYSQL 1966 868 837 821 776
        //UCP 888 122 123 118 132

        for (int i = 0; i < 5; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    long l = System.currentTimeMillis();
                    for (int i = 0; i < 100; i++) {
                        System.out.println("------"+userService.get(1));
                    }
                    System.out.println("耗时："+(System.currentTimeMillis()-l));
                }
            }).run();
        }
    }

}
