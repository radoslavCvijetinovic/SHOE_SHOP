package com.rcs.shoe.shop.fx;

import com.rcs.shoe.shop.common.exceptions.ShoeShopException;
import com.rcs.shoe.shop.db.LiquibaseExecutor;
import com.rcs.shoe.shop.db.config.ShoeShopDBConfig;
import com.rcs.shoe.shop.fx.config.AppConfig;
import com.rcs.shoe.shop.fx.config.ScreensConfig;
import com.rcs.shoe.shop.fx.model.LanguageModel;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class MainApp extends Application {

    private static final Logger LOGGER = LoggerFactory.getLogger(MainApp.class);

    public static void main(String[] args) throws Exception {
        launch(args);
    }

    public void start(Stage stage) throws Exception {

        LOGGER.debug("Starting application");

        Platform.setImplicitExit(true);

        initializeDatabase();

        startApp(stage);

    }

    private void initializeDatabase() throws ShoeShopException {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.register(ShoeShopDBConfig.class);
        context.refresh();

        LiquibaseExecutor liquibaseExecutor = context.getBean(LiquibaseExecutor.class);
        liquibaseExecutor.update();
        context.close();
    }

    private void startApp(Stage stage) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.register(AppConfig.class);
        context.refresh();

        ScreensConfig uiConfig = context.getBean(ScreensConfig.class);
        LanguageModel lang = context.getBean(LanguageModel.class);

        uiConfig.setLangModel(lang);
        uiConfig.setPrimaryStage(stage);
        uiConfig.showMainScreen();
        uiConfig.loadLogin();
    }
}
