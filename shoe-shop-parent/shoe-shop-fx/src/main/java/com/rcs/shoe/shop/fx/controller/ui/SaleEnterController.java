/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rcs.shoe.shop.fx.controller.ui;

import com.rcs.shoe.shop.core.entity.impl.Product;
import com.rcs.shoe.shop.core.entity.impl.ProductQuantityHistory;
import com.rcs.shoe.shop.core.entity.impl.view.V_ProductQuantityHistory;
import com.rcs.shoe.shop.core.service.ProductService;
import com.rcs.shoe.shop.fx.config.ScreensConfig;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.AccessibleRole;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.effect.BlendMode;
import javafx.scene.layout.Pane;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author Rajko
 */
public class SaleEnterController extends Controller implements Initializable {

    public SaleEnterController(ScreensConfig uIConfig) {
        super(uIConfig);
    }

    @FXML
    private TextField productCode;
    @FXML
    private TextField produstNumberText;
    @FXML
    private Label produstNumberLabel;
    @FXML
    private Pane sizesPane;
    @FXML
    private Button searchProductButton;
    @FXML
    private Button resetSaleButton;
    @FXML
    private Button quitSaleButton;

    @Autowired
    private ProductService productService;

    private Product storedProduct;

    private Map<String, Label> quantityLabels;

    private Map<String, Button> quantityButtons;

    private String productCodeStr;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeHandlers();
        initializeQuantityFields();
        reset();

        if (productCodeStr != null) {
            searchProduct(productCodeStr, null, false);
        }
    }

    private void reset() {
        sizesPane.setVisible(false);
        resetSaleButton.setVisible(false);
        quitSaleButton.setVisible(false);
        searchProductButton.setVisible(true);
        productCode.setDisable(false);
        produstNumberText.setDisable(false);

        storedProduct = null;
    }
    
    private void initializeHandlers() {
        produstNumberText.textProperty().addListener(getDigitChangeListener(produstNumberText));
    }

    private void initializeQuantityFields() {
        quantityLabels = new HashMap<>();
        quantityButtons = new HashMap<>();
        ObservableList<Node> children = sizesPane.getChildren();
        for (Node node : children) {
            if (node instanceof Label && node.getId() != null && node.getId().startsWith("label")) {
                Label label = (Label) node;
                quantityLabels.put(label.getId(), label);
            } else if (node instanceof Button) {
                Button button = (Button) node;
                if (button.getId() != null && button.getId().startsWith("button")) {
                    quantityButtons.put(button.getId(), button);
                }
            }
        }
    }

    public void searchProduct() {
        String prodCode = productCode.getText();
        String prodNumber = produstNumberText.getText();

        searchProduct(prodCode, prodNumber, false);
    }

    private void searchProduct(String productCode, String productNumber, boolean skipDialog) {
        if (StringUtils.isNotBlank(productCode) && StringUtils.isNotBlank(productNumber)) {
            storedProduct = productService.findByProductCodeAndProductNum(productCode, productNumber);
        } else if (StringUtils.isNotBlank(productCode)) {
            storedProduct = productService.findByProductCode(productCode);
        } else if (StringUtils.isNotBlank(productNumber)) {
            List<Product> prodList = productService.findByProductNum(productNumber);
            if (prodList.size() == 1) {
                storedProduct = prodList.get(0);
            } else if (prodList.size() > 1) {
                showInformationPopup("Unesite šifru proizvoda.",
                        "Više proizvoda je pronadjeno sa rednim brojem: " + productNumber + "!",
                        "Morate uneti i šifru proizvoda.");
                return;
            }
        }
        if (storedProduct != null) {
            setProductOnForm();
        } else {
            openProductNotFoundDialog();
        }
    }

    private void openProductNotFoundDialog() {
        showInformationPopup("Proizvod nije pronadjen!", "Za unete kriterijume proizvod nije pronadjen!", "");
    }

    private void setProductOnForm() {
        setQuantities(productService.getProductQuantitiesMap(storedProduct.getProductCode()));
        productCode.textProperty().setValue(storedProduct.getProductCode());
        produstNumberText.textProperty().setValue(storedProduct.getProductNum().toString());

        sizesPane.setVisible(true);
        resetSaleButton.setVisible(true);
        quitSaleButton.setVisible(true);
        searchProductButton.setVisible(false);
        productCode.setDisable(true);
        produstNumberText.setDisable(true);
    }

    private void setQuantities(Map<Integer, V_ProductQuantityHistory> map) {
        for (Label label : quantityLabels.values()) {
            Button button = quantityButtons.get("button" + getSize(label));
            V_ProductQuantityHistory quantity = map.get(getSize(label));
            if (quantity != null && quantity.getQuantity() > 0) {
                button.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent e) {
                        enterSale(button);
                    }
                });
                button.setDefaultButton(true);
                label.textProperty().setValue(quantity.getQuantity().toString());
            } else {
                button.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent e) {
                        noQuantitySale();
                    }
                });
                button.setDefaultButton(false);
                label.textProperty().setValue("0");
            }
        }
    }

    private void noQuantitySale() {
        if (showConfirmPopup("Prodaja",
                "Pokušali ste da unesete prodaju za proizvod koji nije na stanju.",
                "Da li želite da izmenite stanje za ovaj proizvod?")) {
            uIConfig.loadEditProduct(storedProduct.getProductCode(), true);
        }
    }

    private void enterSale(Button button) {
        String message = "Prodaja: \n"
                + "šifra: " + storedProduct.getProductCode() + ",\n"
                + "redni broj: " + storedProduct.getProductNum() + ",\n"
                + "veličina: " + getSize(button);
        if (showConfirmPopup("Da li ste sigurni?", message)) {
            ProductQuantityHistory quantityHistory = new ProductQuantityHistory();
            quantityHistory.setCreatedBy("rajkofon");
            quantityHistory.setCreationTime(new Date());
            quantityHistory.setProductCode(storedProduct.getProductCode());
            quantityHistory.setSize(getSize(button));
            quantityHistory.setType(3);
            quantityHistory.setQuantity(-1);

            productService.saveProductQuantityHistory(quantityHistory);

            showInformationPopup("Uspešan unos!", "Prodaja je uspešno evidentirana.", "");
            
            setProductOnForm();
        }

    }

    public void resetForm() {
        reset();
    }

    public void quitForm() {
        if (showConfirmPopup("Da li ste sigurni?", "Da li ste sigurni da želite da odustanete?")) {
            uIConfig.loadProducts();
        }
    }

    public void changeProductQuantity() {
        uIConfig.loadEditProduct(storedProduct.getProductCode(), true);
    }

    public void setProductCode(String productCode) {
        this.productCodeStr = productCode;
    }


}
