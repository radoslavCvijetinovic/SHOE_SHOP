/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rcs.shoe.shop.fx;

import com.rcs.shoe.shop.fx.config.AppConfig;
import com.rcs.shoe.shop.fx.config.ScreensConfig;
import com.rcs.shoe.shop.fx.model.LanguageModel;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.application.Preloader;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 *
 * @author Rajko
 */
public class App extends Application {

    private static final Logger LOGGER = LoggerFactory.getLogger(App.class);
    private ScreensConfig uiConfig;
    private LanguageModel lang;

    @Override
    public void init() throws Exception {
        // Do some heavy lifting
        Platform.setImplicitExit(true);

        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.register(AppConfig.class);
        context.refresh();

        uiConfig = context.getBean(ScreensConfig.class);
        lang = context.getBean(LanguageModel.class);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        LOGGER.debug("Starting application");

        uiConfig.setLangModel(lang);
        uiConfig.setPrimaryStage(primaryStage);
        uiConfig.showMainScreen();
        uiConfig.loadLogin();

        notifyPreloader(
                new Preloader.StateChangeNotification(Preloader.StateChangeNotification.Type.BEFORE_START));
    }

}
