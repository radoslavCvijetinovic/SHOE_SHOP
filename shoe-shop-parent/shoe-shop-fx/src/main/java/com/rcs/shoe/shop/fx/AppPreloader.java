/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rcs.shoe.shop.fx;

import javafx.application.Preloader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

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

        Image spinnerImage = new Image(getClass().getResource(Constants.SPINNER_FILE_PATH).toExternalForm());
        ImageView iv1 = new ImageView();
        iv1.setFitHeight(50);
        iv1.setFitWidth(50);
        iv1.setImage(spinnerImage);
        
        HBox box = new HBox();
        box.getChildren().add(iv1);
        root.getChildren().add(box);

        root.getStylesheets().add(Constants.STYLE_FILE_PATH);
        preloaderScene = new Scene(root, Constants.PRELOADER_WIDTH, Constants.PRELOADER_HEIGHT);
        preloaderScene.setFill(null);
    }

    @Override
    public void start(Stage stage) throws Exception {
        preloaderStage = stage;
        preloaderStage.initStyle(StageStyle.TRANSPARENT);
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
