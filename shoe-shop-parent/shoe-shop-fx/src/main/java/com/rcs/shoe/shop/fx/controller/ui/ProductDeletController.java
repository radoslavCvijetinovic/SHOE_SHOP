/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rcs.shoe.shop.fx.controller.ui;

import com.rcs.shoe.shop.core.entity.impl.Product;
import com.rcs.shoe.shop.core.entity.impl.ProductHistory;
import com.rcs.shoe.shop.core.entity.impl.view.V_Products;
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
public class ProductDeletController extends Controller implements Initializable {

    @FXML
    private TextField productNumberText;
    @FXML
    private PasswordField passwordTx;
    @FXML
    private Pane productPane;
    @FXML
    private TextField productNumValueTx;
    @FXML
    private TextField productCodeValueTx;
    @FXML
    private TextField quantityValueText;

    @Autowired
    private ProductService productService;
    
    private V_Products productToDelete;

    public ProductDeletController(ScreensConfig uIConfig) {
        super(uIConfig);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        productNumberText.textProperty().addListener(getDigitChangeListener(productNumberText));
        productNumValueTx.setDisable(true);
        productCodeValueTx.setDisable(true);
        quantityValueText.setDisable(true);
        reset();
    }

    public void reset() {
        productPane.setVisible(false);
        productToDelete = null;
        productNumberText.setText("");
        passwordTx.setText("");
    }

    public void searchProduct() {
        String productNum = productNumberText.textProperty().getValue();
        if (StringUtils.isBlank(productNum)) {
            showInformationPopup("Unesite redni broj.", "Unesite redni broj.", null);
            reset();
            return;
        }

        if (StringUtils.isBlank(passwordTx.textProperty().getValue()) || !SecurityUtils.getPassword().equals(passwordTx.textProperty().getValue().trim())) {
            showInformationPopup("Pogrešna lozinka.", "Uneta lozinka ne pripada trenutnom korisniku!", null);
            reset();
            return;
        }
        Integer prodNum = Integer.parseInt(productNum);
        V_Products product = productService.findProductDetailsByProductNum(prodNum);

        if (product == null) {
            showInformationPopup("Proizvod nije pronadjen.", "Proizvod nije pronadjen!", null);
            return;
        } else {
            setProduct(product);
        }
    }

    public void deleteProduct() {
        if (showConfirmPopup("Potvrda brisanja!", "Da li ste sigurni da želite da obršete ovaj proizvod?", "Brisanjem proizvoda brišete istoriju promena za ovaj proizvod!")) {

            productService.deleteProduct(productToDelete.getId());

            showInformationPopup("Proizvod obrisan",
                    "Proizvod"
                            + "\nredni broj: " + productToDelete.getProductNum().toString() + ","
                            + "\nšifra proizvoda: " + productToDelete.getProductCode()
                            + "\nje uspešno obrisan!",
                    null);
            reset();

        }
    }

    private void setProduct(V_Products product) {
        productToDelete = product;
        productNumValueTx.textProperty().setValue(product.getProductNum().toString());
        productCodeValueTx.textProperty().setValue(product.getProductCode());
        quantityValueText.textProperty().setValue(product.getQuantity().toString());
        productPane.setVisible(true);
    }

}
