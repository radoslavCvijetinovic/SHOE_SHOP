/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rcs.shoe.shop.fx;

import javafx.application.Preloader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 *
 * @author Rajko
 */
public class AppPreloader extends Preloader {    
    Stage preloaderStage;
    StackPane root;
    Scene preloaderScene;

    @Override
    public void init() throws Exception {
        root = new StackPane();

        root.getStyleClass().add(Constants.MAIN_PANNEL_CLASS);
        Label label = new Label("Molimo sačekajte...");
        root.getChildren().add(label);
        root.getStylesheets().add(Constants.STYLE_FILE_PATH);
        preloaderScene = new Scene(root, Constants.PRELOADER_WIDTH, Constants.PRELOADER_HEIGHT);
    }

    @Override
    public void start(Stage stage) throws Exception {
        preloaderStage = stage;
        preloaderStage.setTitle(Constants.APP_TITLE);
        preloaderStage.getIcons().add(new Image(getClass().getResource(Constants.ICON_FILE_PATH).toExternalForm()));
        preloaderStage.setScene(preloaderScene);
        preloaderStage.show();
    }

    @Override
    public void handleProgressNotification(ProgressNotification pn) {
    }

    @Override
    public void handleApplicationNotification(PreloaderNotification pn) {
        if (pn instanceof StateChangeNotification) {
            preloaderStage.hide();
        }
    }

}
