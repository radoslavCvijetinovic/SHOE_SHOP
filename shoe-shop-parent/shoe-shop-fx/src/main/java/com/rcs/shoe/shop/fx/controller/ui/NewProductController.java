/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rcs.shoe.shop.fx.controller.ui;

import com.rcs.shoe.shop.core.entity.Product;
import com.rcs.shoe.shop.core.service.ProductService;
import com.rcs.shoe.shop.fx.config.ScreensConfig;
import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author Rajko
 */
public class NewProductController extends Controller implements Initializable {

    private static final Logger LOGGER = LoggerFactory.getLogger(NewProductController.class);

    public NewProductController(ScreensConfig uIConfig) {
        super(uIConfig);
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        LOGGER.debug("initialize");
        initializeHandlers();
    }

    @FXML
    private TextField productCode;
    @FXML
    private TextField produstNumber;
    @FXML
    private Pane sizesPane;

    @Autowired
    private ProductService productService;

    public void saveProduct() {

        Product product = new Product();
        product.setActive(Boolean.TRUE);
        product.setProductCode(productCode.getText());
        product.setCreatedBy("rajkofon");
        product.setCreationTime(new Date());
        product.setProductNum(new Integer(produstNumber.getText()));

        productService.saveProduct(product);
    }

    public void resetProductForm() {
    }

    private void initializeHandlers() {
        ObservableList<Node> children = sizesPane.getChildren();
        for (Node node : children) {
            if (node instanceof Slider) {
                Slider slider = (Slider) node;
                slider.setMin(0);
                slider.setMax(30);
                slider.setValue(0);
                slider.setMinorTickCount(30);
                slider.setMinorTickCount(0);
                slider.setSnapToTicks(true);
                String labelId = "#label"
                        + slider.getId().substring(slider.getId().length() - 2, slider.getId().length());
                LOGGER.debug("slider.getId(): " + slider.getId());
                LOGGER.debug("labelId: " + labelId);
                slider.valueProperty().addListener(new ChangeListener<Number>() {
                    @Override
                    public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                        Label label = (Label) sizesPane.lookup(labelId);
                        label.setText(newValue.toString());
                    }
                });
            }
        }
    }

}
