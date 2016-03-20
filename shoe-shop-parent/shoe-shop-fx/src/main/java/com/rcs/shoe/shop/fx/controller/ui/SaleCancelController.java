/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rcs.shoe.shop.fx.controller.ui;

import com.rcs.shoe.shop.core.entity.impl.ProductHistory;
import com.rcs.shoe.shop.core.service.ProductService;
import com.rcs.shoe.shop.fx.config.ScreensConfig;
import com.rcs.shoe.shop.fx.utils.SecurityUtils;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author Rajko
 */
public class SaleCancelController extends Controller implements Initializable {

    @FXML
    private TextField saleIdTx;
    @FXML
    private PasswordField passwordTx;
    @FXML
    private TextField saleIdValTx;
    @FXML
    private TextField productCodeTx;
    @FXML
    private TextField productNumTx;
    @FXML
    private TextField sizeTx;
    @FXML
    private TextField timeSaleTx;
    @FXML
    private Pane salePane;

    @Autowired
    private ProductService productService;
    
    private ProductHistory saleToCancel;

    public SaleCancelController(ScreensConfig uIConfig) {
        super(uIConfig);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        reset();

        saleIdTx.textProperty().addListener(getDigitChangeListener(saleIdTx));
    }

    public void reset() {
        saleToCancel = null;
        
        saleIdTx.setText("");
        passwordTx.setText("");

        saleIdValTx.setDisable(true);
        productCodeTx.setDisable(true);
        productNumTx.setDisable(true);
        sizeTx.setDisable(true);
        timeSaleTx.setDisable(true);

        salePane.setVisible(false);
    }

    public void searchSale() {
        if (StringUtils.isBlank(saleIdTx.textProperty().getValue())) {
            showInformationPopup("Unesite id prodaje.", "Unesite id prodaje.", null);
            reset();
            return;
        }

        if (StringUtils.isBlank(passwordTx.textProperty().getValue()) || !SecurityUtils.getPassword().equals(passwordTx.textProperty().getValue().trim())) {
            showInformationPopup("Pogrešna lozinka.", "Uneta lozinka ne pripada trenutnom korisniku!.", null);
            reset();
            return;
        }
        Long id = Long.parseLong(saleIdTx.textProperty().getValue());
        ProductHistory sale = productService.findHistoryById(id);

        if (sale == null || sale.getType() != 3) {
            showInformationPopup("Prodaja za id nije pronadjena.", "Prodaja za id nije pronadjena!.", null);
            return;
        } else {
            setSaleData(sale);
        }
    }

    public void cancelSale() {
        if (showConfirmPopup("Potvrda storniranja!", "Da li ste sigurni da želite da stornirate ovu prodaju?")) {
            saleToCancel.setType(4);
            saleToCancel.setQuantity(0);
            
            productService.updateProductHistory(saleToCancel);
            
            showInformationPopup("Prodaj stornirana", "Prodaja ID: "+saleToCancel.getId() + " uspešno stornirana!", null);
            reset();
            
        }
    }

    private void setSaleData(ProductHistory sale) {
        this.saleToCancel = sale;
        
        saleIdValTx.setText(sale.getId().toString());
        productCodeTx.setText(sale.getProductCode());
        productNumTx.setText(sale.getProductNum().toString());
        sizeTx.setText(sale.getSize().toString());
        timeSaleTx.setText(sale.getCreationTime().toString());

        salePane.setVisible(true);
    }

}
