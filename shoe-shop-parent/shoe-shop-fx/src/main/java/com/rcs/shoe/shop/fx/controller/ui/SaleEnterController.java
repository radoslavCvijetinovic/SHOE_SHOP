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
import com.rcs.shoe.shop.fx.utils.SecurityUtils;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
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

    private Integer productNumber;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeHandlers();
        initializeQuantityFields();
        reset();

        if (productNumber != null) {
            searchProduct(productNumber, null, false);
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
        productCode.setText("");
        produstNumberText.setText("");
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
        String prodNumberTx = produstNumberText.getText();

        if (NumberUtils.isDigits(prodNumberTx)) {
            searchProduct(Integer.parseInt(prodNumberTx), prodCode, false);
        } else {
            searchProduct(null, prodCode, false);
        }
    }

    private void searchProduct(Integer productNumber, String productCode, boolean skipDialog) {
        if (StringUtils.isNotBlank(productCode) && productNumber != null) {
            storedProduct = productService.findByProductCodeAndProductNum(productCode, productNumber);
        } else if (productNumber != null) {
            storedProduct = productService.findByProductNum(productNumber);
        } else if (StringUtils.isNotBlank(productCode)) {
            List<Product> prodList = productService.findByProductCode(productCode);
            if (prodList.size() == 1) {
                storedProduct = prodList.get(0);
            } else if (prodList.size() > 1) {
                List<String> choises = getProductNumbers(prodList);
                String prodNum = showChoiseDialog("Unos prodaje.",
                        "Više proizvoda je pronadjeno sa šifrom: " + productCode + "!",
                        "Izaberi redni broj: ",
                        choises,
                        choises.get(0));
                if (NumberUtils.isDigits(prodNum)) {
                    Integer pn = Integer.parseInt(prodNum);
                    for (Product p : prodList) {
                        if (p.getProductNum().equals(pn)) {
                            storedProduct = p;
                            break;
                        }
                    }
                }
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
        setQuantities(productService.getProductQuantitiesMap(storedProduct.getProductNum()));
        productCode.textProperty().setValue(storedProduct.getProductCode());
        produstNumberText.textProperty().setValue(storedProduct.getProductNum().toString());

        sizesPane.setVisible(true);
        resetSaleButton.setVisible(true);
        quitSaleButton.setVisible(true);
        searchProductButton.setVisible(false);
        productCode.setDisable(true);
        produstNumberText.setDisable(true);
    }

    private void setQuantities(Map<Integer, V_ProductHistory> map) {
        for (Label label : quantityLabels.values()) {
            Button button = quantityButtons.get("button" + getSize(label));
            V_ProductHistory quantity = map.get(getSize(label));
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
        if (SecurityUtils.isAdmin()) {
            if (showConfirmPopup("Prodaja",
                    "Pokušali ste da unesete prodaju za proizvod koji nije na stanju.",
                    "Da li želite da izmenite stanje za ovaj proizvod?")) {
                uIConfig.loadEditProduct(storedProduct.getProductNum(), true);
            }
        } else {
            showInformationPopup("Prodaja",
                    "Pokušali ste da unesete prodaju za proizvod koji nije na stanju.",
                    "Kontaktirajte korisnika sa administratorskim pravom radi izmene stanja.");
        }
    }

    private void enterSale(Button button) {
        String message = "Prodaja: \n"
                + "šifra: " + storedProduct.getProductCode() + ",\n"
                + "redni broj: " + storedProduct.getProductNum() + ",\n"
                + "veličina: " + getSize(button);
        if (showConfirmPopup("Da li ste sigurni?", message)) {
            ProductHistory quantityHistory = new ProductHistory();
            quantityHistory.setProductCode(storedProduct.getProductCode());
            quantityHistory.setProductNum(storedProduct.getProductNum());
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
        uIConfig.loadEditProduct(storedProduct.getProductNum(), true);
    }

    public void setProductNumber(Integer productNumber) {
        this.productNumber = productNumber;
    }

    private List<String> getProductNumbers(List<Product> prodList) {
        List<String> result = new ArrayList<>();
        for (Product p : prodList) {
            result.add(p.getProductNum().toString());
        }
        return result;
    }

}
