/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rcs.shoe.shop.fx.controller.ui;

import com.rcs.shoe.shop.fx.config.ScreensConfig;
import java.util.Optional;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

/**
 *
 * @author Rajko
 */
public abstract class Controller {

    protected ScreensConfig uIConfig;

    public Controller(ScreensConfig uIConfig) {
        this.uIConfig = uIConfig;
    }

    protected boolean showConfirmPopup(String title, String header) {
        return showConfirmPopup(title, header, "");
    }

    protected boolean showConfirmPopup(String title, String header, String contentText) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(contentText);

        ButtonType okButton = new ButtonType("Da", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelButton = new ButtonType("Ne", ButtonBar.ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(okButton, cancelButton);

        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.getIcons().add(uIConfig.getIcon());

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get().getButtonData() == ButtonBar.ButtonData.OK_DONE) {
            return true;
        }
        return false;
    }

    protected void showInformationPopup(String title, String headerText, String contentText) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);

        ButtonType okButton = new ButtonType("Nastavi", ButtonBar.ButtonData.OK_DONE);

        alert.getButtonTypes().setAll(okButton);

        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.getIcons().add(uIConfig.getIcon());

        alert.showAndWait();
    }

    protected ChangeListener getDigitChangeListener(final TextField textField) {
        ChangeListener<String> result = new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (StringUtils.isBlank(newValue)) {
                    textField.textProperty().setValue("");
                } else if (NumberUtils.isDigits(newValue)) {
                    textField.textProperty().setValue(newValue);
                } else {
                    textField.textProperty().setValue(oldValue);
                }
            }
        };
        return result;
    }

    protected Integer getSize(TextField tx) {
        return new Integer(tx.getId().replaceFirst("text", ""));
    }

    protected Integer getSize(Button button) {
        return new Integer(button.getId().replaceFirst("button", ""));
    }

    protected Integer getSize(Label label) {
        return new Integer(label.getId().replaceFirst("label", ""));
    }
}
