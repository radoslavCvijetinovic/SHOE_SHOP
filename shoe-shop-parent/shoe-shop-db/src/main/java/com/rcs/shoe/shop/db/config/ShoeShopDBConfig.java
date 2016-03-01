/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rcs.shoe.shop.db.config;

import com.rcs.shoe.shop.db.LiquibaseExecutor;
import com.rcs.shoe.shop.common.exceptions.ShoeShopException;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;

/**
 *
 * @author Rajko
 */
@Configuration
@PropertySource("classpath:application.properties")
public class ShoeShopDBConfig {

    @Value("${db.username:ssusername987}")
    String dbUsername;
    
    @Value("${db.password:sspwd987}")
    String dbPassword;
    
    @Value("${db.url}")
    String databaseUrl;
    
    @Value("${db.driver}")
    String databaseDriver;

    @Bean
    public static PropertySourcesPlaceholderConfigurer placeHolderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }

    @Bean
    public DataSource dataSource() throws ClassNotFoundException {
        SimpleDriverDataSource simpleDriverDataSource = new SimpleDriverDataSource();
        simpleDriverDataSource.setDriverClass(getClassByName(databaseDriver));
        simpleDriverDataSource.setUrl(databaseUrl);
        simpleDriverDataSource.setUsername(dbUsername);
        simpleDriverDataSource.setPassword(dbPassword);
        return simpleDriverDataSource;
    }

    private Class getClassByName(String className) throws ClassNotFoundException {
        return Class.forName(className);
    }
    
    @Bean
    @Autowired
    public LiquibaseExecutor liquibase(DataSource dataSource) throws ShoeShopException {
        LiquibaseExecutor liquibaseExecutor = new LiquibaseExecutor(dataSource);
        return liquibaseExecutor;
    }

}
