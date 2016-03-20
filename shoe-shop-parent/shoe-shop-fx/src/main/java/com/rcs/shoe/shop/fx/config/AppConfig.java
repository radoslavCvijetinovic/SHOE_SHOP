/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rcs.shoe.shop.fx.config;

import com.rcs.shoe.shop.core.config.CoreConfig;
import com.rcs.shoe.shop.fx.controller.LanguageController;
import com.rcs.shoe.shop.fx.model.LanguageModel;
import com.rcs.shoe.shop.fx.controller.ReportingController;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import net.sf.dynamicreports.jasper.builder.JasperReportBuilder;
import net.sf.dynamicreports.report.builder.DynamicReports;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;

/**
 *
 * @author Rajko
 */
@Configuration
@PropertySource("classpath:application.properties")
@Import({CoreConfig.class, ScreensConfig.class})
public class AppConfig {

    @Bean
    public static PropertySourcesPlaceholderConfigurer placeHolderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }

    @Bean
    LanguageModel languageModel() {
        return new LanguageModel();
    }

    @Bean
    LanguageController languageController() {
        return new LanguageController(languageModel());
    }

    @Bean
    @Scope("prototype")
    JasperReportBuilder getReportBuilder() {
        return DynamicReports.report();
    }

    @Bean
    ReportingController getReportingUtils() {
        return new ReportingController();
    }

}
