/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rcs.shoe.shop.fx.controller.ui;

import com.rcs.shoe.shop.core.entity.impl.Product;
import com.rcs.shoe.shop.core.entity.impl.ProductQuantityHistory;
import com.rcs.shoe.shop.core.entity.impl.ProductSizes;
import com.rcs.shoe.shop.core.entity.impl.view.V_ProductQuantityHistory;
import com.rcs.shoe.shop.core.service.ProductService;
import com.rcs.shoe.shop.fx.config.ScreensConfig;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
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
    private Label produstNumberLabel;
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

    private String productCodeToEdit;

    private boolean fromSale;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        LOGGER.debug("initialize");
        initializeQuantityFields();
        initializeHandlers();
        reset();

        if (StringUtils.isNotBlank(productCodeToEdit)) {
            searchProduct(productCodeToEdit, true);
        }
    }

    public void setProductCode(String productCode) {
        productCodeToEdit = productCode;
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

        produstNumber.setText("");
        produstNumber.setVisible(false);
        produstNumberLabel.setVisible(false);
        sizesPane.setVisible(false);
        shortcutsPane.setVisible(false);

        productCode.setEditable(true);
        productCode.setDisable(false);
        productCode.setText("");

        saveProdButton.setVisible(false);
        resetProdFormButton.setVisible(false);
        searchProductButton.setVisible(true);

        allZeroRB.selectedProperty().setValue(false);

        if (productCodeToEdit != null) {
            if(fromSale) {
                uIConfig.loadNewSale(productCodeToEdit);
            } else {
                uIConfig.loadProducts();
            }
        }
    }

    public void searchProduct() {
        String prodCode = productCode.getText();
        searchProduct(prodCode, false);
    }

    private void searchProduct(String productCode, boolean skipDialog) {
        if (StringUtils.isNotBlank(productCode)) {
            storedProduct = productService.findByProductCode(productCode);
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
    }

    private void openProductFoundDialog() {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Ova šifra proizvoda je zauzeta!");
        alert.setHeaderText("Sifra proizvoda: " + productCode.textProperty().getValue() + " je zauzeta!");
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
        setQuantities(productService.getProductQuantities(storedProduct.getProductCode()));
        productCode.setEditable(false);
        productCode.setDisable(true);
        productCode.setText(storedProduct.getProductCode());
        produstNumber.setText(storedProduct.getProductNum().toString());
        produstNumber.setVisible(true);
        produstNumberLabel.setVisible(true);
        sizesPane.setVisible(true);
        shortcutsPane.setVisible(true);
        saveProdButton.setVisible(true);
        resetProdFormButton.setVisible(true);
        searchProductButton.setVisible(false);
    }

    private void setQuantities(List<V_ProductQuantityHistory> quantities) {
        for (V_ProductQuantityHistory quantity : quantities) {
            TextField tx = quantityFields.get("text" + quantity.getSize());
            tx.textProperty().setValue(quantity.getQuantity().toString());
            Label label = quantityLabels.get("label" + quantity.getSize());
            label.textProperty().setValue(quantity.getQuantity().toString());
        }
    }

    private void openNewProductForm() {
        productCode.setEditable(false);
        productCode.setDisable(true);
        produstNumber.setText("");
        produstNumber.setVisible(true);
        produstNumberLabel.setVisible(true);
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
                } else {
                    saveNewProduct();
                }
                reset();
            }
        }
    }

    private boolean validate() {
        boolean valid = true;
        String prodNum = produstNumber.textProperty().getValue();
        if (StringUtils.isBlank(prodNum)) {
            if (storedProduct != null) {
                produstNumber.textProperty().setValue(storedProduct.getProductNum().toString());
            } else {
                showProductNumberPopup();
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
        if (!produstNumber.getText().equals(storedProduct.getProductNum().toString())) {
            storedProduct.setProductNum(new Integer(produstNumber.getText()));
        }
        storedProduct.setProductSizes(getProductSizes());

        storedProduct.setProductQuantityHistory(getProductQuantity());
        productService.saveOrUpdateProduct(storedProduct);
    }

    private void saveNewProduct() {
        Product product = new Product();
        product.setActive(Boolean.TRUE);
        product.setProductCode(productCode.getText());
        product.setCreatedBy("rajkofon");
        product.setCreationTime(new Date());
        product.setProductNum(new Integer(produstNumber.getText()));

        product.setProductSizes(getProductSizes());

        product.setProductQuantityHistory(getProductQuantity());

        productService.saveOrUpdateProduct(product);
    }

    private List<ProductSizes> getProductSizes() {
        List<ProductSizes> result = new ArrayList<>();
        Map<Integer, ProductSizes> storedSizes = new HashMap<>();
        if (storedProduct != null) {
            storedSizes
                    = productService.getProductSizesByProdCode(storedProduct.getProductCode());
        }
        for (TextField tx : quantityFields.values()) {
            Integer s = getSize(tx);
            if (storedSizes.get(s) == null) {
                ProductSizes size = new ProductSizes();
                size.setCreatedBy("rajkofon");
                size.setCreationTime(new Date());
                size.setProductCode(productCode.getText());
                size.setSize(s);
                result.add(size);
            } else {
                result.add(storedSizes.get(s));
            }
        }
        return result;
    }

    private List<ProductQuantityHistory> getProductQuantity() {
        List<ProductQuantityHistory> result = new ArrayList<>();
        for (TextField tx : quantityFields.values()) {
            Label label = quantityLabels.get(tx.getId().replaceFirst("text", "label"));
            Integer oldValue = new Integer(label.textProperty().getValue().trim());
            Integer newValue = new Integer(tx.textProperty().getValue().trim());

            if (oldValue > newValue) {
                ProductQuantityHistory quantityHistory = new ProductQuantityHistory();
                quantityHistory.setCreatedBy("rajkofon");
                quantityHistory.setCreationTime(new Date());
                quantityHistory.setProductCode(productCode.getText());
                quantityHistory.setSize(getSize(tx));
                quantityHistory.setType(2);
                quantityHistory.setQuantity(newValue - oldValue);
                result.add(quantityHistory);
            } else if (oldValue < newValue) {
                ProductQuantityHistory quantityHistory = new ProductQuantityHistory();
                quantityHistory.setCreatedBy("rajkofon");
                quantityHistory.setCreationTime(new Date());
                quantityHistory.setProductCode(productCode.getText());
                quantityHistory.setSize(getSize(tx));
                quantityHistory.setType(1);
                quantityHistory.setQuantity(newValue - oldValue);
                result.add(quantityHistory);
            }
        }

        return result;
    }

    private void showProductNumberPopup() {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Redni broj!");
        alert.setHeaderText("Redni broj ne može biti prazan !");
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
