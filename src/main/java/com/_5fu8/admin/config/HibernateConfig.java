package com._5fu8.admin.config;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.io.IOException;
import java.util.Properties;

@Configuration
@Slf4j
public class HibernateConfig {

    @Autowired
    Environment environment;

    @Resource
    DataSource dataSource;

    @Bean
    public LocalSessionFactoryBean sessionFactoryBean() throws IOException {
        LocalSessionFactoryBean sessionFactoryBean = new LocalSessionFactoryBean();
        sessionFactoryBean.setDataSource(dataSource);
        sessionFactoryBean.setPackagesToScan(new String[] { "net.hncu.notes.table" });
        sessionFactoryBean.setHibernateProperties(hibernateProperties());
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        sessionFactoryBean.setMappingLocations(resolver.getResources("classpath:hbm/*.hbm.xml"));
        //sessionFactoryBean.setConfigLocation(resolver.getResource("classpath:hibernate.cfg.xml"));
        return sessionFactoryBean;
    }

    //获取hibernate配置
    private Properties hibernateProperties() {
        Properties properties = new Properties();
        properties.put("hibernate.dialect", environment.getRequiredProperty("hibernate.dialect"));
        properties.put("hibernate.show_sql", environment.getRequiredProperty("hibernate.show_sql"));
        properties.put("hibernate.format_sql", environment.getRequiredProperty("hibernate.format_sql"));
        properties.put("hibernate.hbm2ddl.auto", environment.getRequiredProperty("hibernate.hbm2ddl.auto"));
        return properties;
    }

    // 事务管理
    @Bean
    @Autowired
    public HibernateTransactionManager transactionManager(SessionFactory sf) {
        HibernateTransactionManager txManager = new HibernateTransactionManager();
        txManager.setSessionFactory(sf);
        return txManager;
    }
}
