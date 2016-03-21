/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rcs.shoe.shop.fx.controller.ui;

import com.rcs.shoe.shop.core.entity.impl.Product;
import com.rcs.shoe.shop.core.entity.impl.ProductHistory;
import com.rcs.shoe.shop.core.entity.impl.view.V_ProductHistory;
import com.rcs.shoe.shop.core.service.ProductService;
import com.rcs.shoe.shop.fx.config.ScreensConfig;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
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

    @FXML
    private TextField productCode;
    @FXML
    private TextField produstNumber;
    @FXML
    private Label productCodeLabel;
    @FXML
    private Pane sizesPane;
    @FXML
    private Pane shortcutsPane;
    @FXML
    private Button saveProdButton;
    @FXML
    private Button resetProdFormButton;
    @FXML
    private Button searchProductButton;
    @FXML
    private RadioButton allZeroRB;

    @Autowired
    private ProductService productService;

    private Map<String, TextField> quantityFields;

    private Map<String, Label> quantityLabels;

    private Product storedProduct;

    private Integer productNumberToEdit;

    private boolean fromSale;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        LOGGER.debug("initialize");
        initializeQuantityFields();
        initializeHandlers();
        reset();

        if (productNumberToEdit != null) {
            searchProduct(productNumberToEdit, true);
        }
    }

    public void setProductNumber(Integer productNumber) {
        productNumberToEdit = productNumber;
    }

    private void initializeHandlers() {
        produstNumber.textProperty().addListener(getDigitChangeListener(produstNumber));
    }

    private void initializeQuantityFields() {
        quantityFields = new HashMap<>();
        quantityLabels = new HashMap<>();
        ObservableList<Node> children = sizesPane.getChildren();
        for (Node node : children) {
            if (node instanceof TextField) {
                TextField tx = (TextField) node;
                tx.textProperty().addListener(getDigitChangeListener(tx));
                quantityFields.put(tx.getId(), tx);
            } else if (node instanceof Label) {
                Label label = (Label) node;
                if (label.getId() != null && label.getId().startsWith("label")) {
                    quantityLabels.put(label.getId(), label);
                }
            }
        }
    }

    private void reset() {
        storedProduct = null;

        quantityFields.values().stream().forEach((tx) -> {
            tx.textProperty().setValue("0");
        });
        quantityLabels.values().stream().forEach((label) -> {
            label.textProperty().setValue("0");
        });

        productCode.setText("");
        productCode.setVisible(false);
        productCodeLabel.setVisible(false);
        sizesPane.setVisible(false);
        shortcutsPane.setVisible(false);

        produstNumber.setEditable(true);
        produstNumber.setDisable(false);
        produstNumber.setText("");

        saveProdButton.setVisible(false);
        resetProdFormButton.setVisible(false);
        searchProductButton.setVisible(true);

        allZeroRB.selectedProperty().setValue(false);

        if (productNumberToEdit != null) {
            if (fromSale) {
                uIConfig.loadNewSale(productNumberToEdit);
            } else {
                uIConfig.loadProducts();
            }
        }
    }

    public void searchProduct() {
        if (NumberUtils.isDigits(produstNumber.getText())) {
            Integer prodNumber = Integer.parseInt(produstNumber.getText());
            searchProduct(prodNumber, false);
        }
    }

    private void searchProduct(Integer productNumber, boolean skipDialog) {
        storedProduct = productService.findByProductNum(productNumber);

        if (storedProduct != null) {
            if (skipDialog) {
                openEditForm();
            } else {
                openProductFoundDialog();
            }
        } else {
            openNewProductForm();
        }
    }

    private void openProductFoundDialog() {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Ovaj reni broj je zauzet!");
        alert.setHeaderText("Redni broj: " + produstNumber.textProperty().getValue() + " je zauzet!");
        alert.setContentText("Da li želite da izmenite stanje za ovaj proizvod?");

        ButtonType okButton = new ButtonType("Da", ButtonData.OK_DONE);
        ButtonType cancelButton = new ButtonType("Ne", ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(okButton, cancelButton);

        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.getIcons().add(uIConfig.getIcon());

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get().getButtonData() == ButtonData.OK_DONE) {
            openEditForm();
        }
    }

    private void openEditForm() {
        setQuantities(productService.getProductQuantities(storedProduct.getProductNum()));
        productCodeLabel.setVisible(true);
        productCode.setVisible(true);
        productCode.setText(storedProduct.getProductCode());
        produstNumber.setText(storedProduct.getProductNum().toString());
        produstNumber.setDisable(true);
        produstNumber.setEditable(false);
        sizesPane.setVisible(true);
        shortcutsPane.setVisible(true);
        saveProdButton.setVisible(true);
        resetProdFormButton.setVisible(true);
        searchProductButton.setVisible(false);
    }

    private void setQuantities(List<V_ProductHistory> quantities) {
        for (V_ProductHistory quantity : quantities) {
            TextField tx = quantityFields.get("text" + quantity.getSize());
            tx.textProperty().setValue(quantity.getQuantity().toString());
            Label label = quantityLabels.get("label" + quantity.getSize());
            label.textProperty().setValue(quantity.getQuantity().toString());
        }
    }

    private void openNewProductForm() {

        productCode.setEditable(true);
        productCode.setDisable(false);
        productCode.setVisible(true);
        productCode.setText("");
        productCodeLabel.setVisible(true);

        produstNumber.setVisible(true);
        produstNumber.setEditable(false);
        produstNumber.setDisable(true);
        produstNumber.setVisible(true);

        sizesPane.setVisible(true);
        shortcutsPane.setVisible(true);
        saveProdButton.setVisible(true);
        resetProdFormButton.setVisible(true);
        searchProductButton.setVisible(false);
    }

    public void saveProduct() {
        if (validate()) {
            if (showConfirmPopup("Potvrdi izmene.", "Da li ste sigurni da želite da sačuvate promene?")) {
                if (storedProduct != null) {
                    updateExistingProduct();
                    showProductUpdatedInfo();
                } else {
                    saveNewProduct();
                    showProductSavedInfo();
                }
                reset();
            }
        }
    }

    private void showProductSavedInfo() {
        showInformationPopup("Proizvod sačuvan!", "Novi unos uspešno sačuvan.", "");
    }

    private void showProductUpdatedInfo() {
        showInformationPopup("Proizvod sačuvan!", "Proizvod uspešno ažuriran.", "");
    }

    private boolean validate() {
        boolean valid = true;
        String prodCode = productCode.textProperty().getValue();
        if (StringUtils.isBlank(prodCode)) {
            if (storedProduct != null) {
                productCode.textProperty().setValue(storedProduct.getProductCode());
            } else {
                showProductCodePopup();
                valid = false;
            }
        }
        return valid;
    }

    public void resetProductForm() {
        if (showConfirmPopup("Potvrda", "Da li ste sigurni da želite da odustanete?")) {
            reset();
        }
    }

    private void updateExistingProduct() {
        storedProduct.setProductHistory(getProductQuantity());
        if (!productCode.getText().equals(storedProduct.getProductCode())) {
            storedProduct.setProductCode(productCode.getText());
        }

        productService.saveOrUpdateProduct(storedProduct);
    }

    private void saveNewProduct() {
        Product product = new Product();
        product.setActive(Boolean.TRUE);
        product.setProductCode(productCode.getText());
        product.setProductNum(new Integer(produstNumber.getText()));

        product.setProductHistory(getProductQuantity());

        productService.saveOrUpdateProduct(product);
    }

    private List<ProductHistory> getProductQuantity() {
        List<ProductHistory> result = new ArrayList<>();
        for (TextField tx : quantityFields.values()) {
            Label label = quantityLabels.get(tx.getId().replaceFirst("text", "label"));
            Integer oldValue = new Integer(label.textProperty().getValue().trim());
            Integer newValue = new Integer(tx.textProperty().getValue().trim());

            if (oldValue > newValue) {
                ProductHistory quantityHistory = new ProductHistory();
                quantityHistory.setProductNum(Integer.parseInt(produstNumber.getText()));
                if (storedProduct != null) {
                     quantityHistory.setProductCode(storedProduct.getProductCode());
                } else {
                    quantityHistory.setProductCode(productCode.getText());
                }
                quantityHistory.setSize(getSize(tx));
                quantityHistory.setType(2);
                quantityHistory.setQuantity(newValue - oldValue);
                result.add(quantityHistory);
            } else if (oldValue < newValue) {
                ProductHistory quantityHistory = new ProductHistory();
                quantityHistory.setProductCode(productCode.getText());
                quantityHistory.setProductNum(Integer.parseInt(produstNumber.getText()));
                if (storedProduct != null) {
                     quantityHistory.setProductCode(storedProduct.getProductCode());
                } else {
                    quantityHistory.setProductCode(productCode.getText());
                }
                quantityHistory.setSize(getSize(tx));
                quantityHistory.setType(1);
                quantityHistory.setQuantity(newValue - oldValue);
                result.add(quantityHistory);
            }
        }

        return result;
    }

    private void showProductCodePopup() {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Šifra proizvoda!");
        alert.setHeaderText("Šifra ne može biti prazna!");
        alert.setContentText("");

        ButtonType okButton = new ButtonType("Nastavi", ButtonData.OK_DONE);

        alert.getButtonTypes().setAll(okButton);

        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.getIcons().add(uIConfig.getIcon());

        alert.showAndWait();
    }

    public void shortcutsChange() {
        if (allZeroRB.selectedProperty().getValue()) {
            quantityFields.values().stream().forEach((tx) -> {
                tx.textProperty().setValue("0");
            });
        }
    }

    public void setFromSale(boolean fromSale) {
        this.fromSale = fromSale;
    }
}
