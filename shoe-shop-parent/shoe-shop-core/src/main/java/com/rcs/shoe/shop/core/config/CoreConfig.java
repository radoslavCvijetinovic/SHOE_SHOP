/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rcs.shoe.shop.core.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import java.util.Properties;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import liquibase.integration.spring.SpringLiquibase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 *
 * @author Rajko
 */
@Configuration
@EnableJpaRepositories(basePackages = {"com.rcs.shoe.shop.core.repository"})
@EnableTransactionManagement
@PropertySource("classpath:application.properties")
@Import(SecurityConfig.class)
@ComponentScan("com.rcs.shoe.shop.core.service")
public class CoreConfig {

    @Value("${db.username:ssusername987}")
    String dbUsername;

    @Value("${db.password:sspwd987}")
    String dbPassword;

    @Value("${db.url}")
    String databaseUrl;

    @Value("${db.driver}")
    String databaseDriver;

    @Value("${hibernate.dialect}")
    String hibernateDialect;

    @Value("${hibernate.hbm2ddl.auto}")
    String hbm2ddlAuto;

    @Value("${hibernate.ejb.naming_strategy}")
    String hibernateEjbNamingStrategy;

    @Value("${hibernate.show_sql}")
    String hibernateShowSql;

    @Value("${hibernate.format_sql}")
    String hibernateFormatSql;

    @Bean
    public static PropertySourcesPlaceholderConfigurer placeHolderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }

    @Bean(destroyMethod = "close")
    DataSource dataSource() {
        HikariConfig dataSourceConfig = new HikariConfig();
        dataSourceConfig.setDriverClassName(databaseDriver);
        dataSourceConfig.setJdbcUrl(databaseUrl);
        dataSourceConfig.setUsername(dbUsername);
        dataSourceConfig.setPassword(dbPassword);
        return new HikariDataSource(dataSourceConfig);
    }

   @Bean(name = "liquibase")
    public SpringLiquibase liquibase() {
        SpringLiquibase liquibase = new SpringLiquibase();

        liquibase.setDataSource(dataSource());
        liquibase.setChangeLog("classpath:db/db-changelog.xml");

        return liquibase;
    }

    @Bean
    @Autowired
    @DependsOn("liquibase")
    LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dataSource) {
        LocalContainerEntityManagerFactoryBean entityManagerFactoryBean = new LocalContainerEntityManagerFactoryBean();
        entityManagerFactoryBean.setDataSource(dataSource);
        entityManagerFactoryBean.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        entityManagerFactoryBean.setPackagesToScan("com.rcs.shoe.shop.core.entity");

        Properties jpaProperties = new Properties();

        //Configures the used database dialect. This allows Hibernate to create SQL
        //that is optimized for the used database.
        jpaProperties.put("hibernate.dialect", hibernateDialect);

        //Specifies the action that is invoked to the database when the Hibernate
        //SessionFactory is created or closed.
        jpaProperties.put("hibernate.hbm2ddl.auto", hbm2ddlAuto);

        //Configures the naming strategy that is used when Hibernate creates
        //new database objects and schema elements
        //jpaProperties.put("hibernate.ejb.naming_strategy", hibernateEjbNamingStrategy);
        //If the value of this property is true, Hibernate writes all SQL
        //statements to the console.
        jpaProperties.put("hibernate.show_sql", hibernateShowSql);

        //If the value of this property is true, Hibernate will format the SQL
        //that is written to the console.
        jpaProperties.put("hibernate.format_sql", hibernateFormatSql);

        entityManagerFactoryBean.setJpaProperties(jpaProperties);

        return entityManagerFactoryBean;
    }

    @Bean
    JpaTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(entityManagerFactory);
        return transactionManager;
    }

}
