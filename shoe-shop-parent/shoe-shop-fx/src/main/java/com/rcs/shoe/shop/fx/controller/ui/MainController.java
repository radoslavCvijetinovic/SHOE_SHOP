/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rcs.shoe.shop.fx.controller.ui;

import com.rcs.shoe.shop.core.service.UserService;
import com.rcs.shoe.shop.fx.config.ScreensConfig;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 *
 * @author Rajko
 */
public class MainController extends Controller implements Initializable {

    @FXML
    private Label loggedinUserValue;

    @FXML
    private StackPane mainStackPane;

    @Autowired
    private UserService userService;

    public MainController(ScreensConfig uIConfig) {
        super(uIConfig);
    }

    public void initialize(URL url, ResourceBundle rb) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        loggedinUserValue.setText(authentication.getName());
    }

    public void logOut() {
        userService.logOut();

        uIConfig.loadLogin();
    }

    public void noImpl() {
        uIConfig.loadNotImplemented();
    }

    public void listProducts() {
        uIConfig.loadProducts();
    }

    public void newProduct() {
        uIConfig.loadNewProduct();
    }
    
    public void newSale(){
        uIConfig.loadNewSale();
    }

    public StackPane getMainStackPane() {
        return this.mainStackPane;
    }
}
