/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rcs.shoe.shop.fx.controller.ui;

import com.rcs.shoe.shop.core.entity.impl.view.V_Products;
import com.rcs.shoe.shop.core.service.ProductService;
import com.rcs.shoe.shop.fx.config.ScreensConfig;
import com.rcs.shoe.shop.fx.controller.ReportingController;
import com.rcs.shoe.shop.fx.utils.SecurityUtils;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author Rajko
 */
public class ProductsController extends Controller implements Initializable {

    public ProductsController(ScreensConfig uIConfig) {
        super(uIConfig);
    }
    @FXML
    private TableView productsTable;
    @FXML
    private TextField productCode;
    @FXML
    private TextField productNumber;
    @FXML
    private Label selProductCode;
    @FXML
    private Label selProductNum;
    @FXML
    private Button changeProdStateBtn;

    @Autowired
    private ProductService productService;
    @Autowired
    private ReportingController reportingController;

    private FilteredList<V_Products> products;

    public void initialize(URL url, ResourceBundle rb) {
        TableColumn firstCol = (TableColumn) productsTable.getColumns().get(0);
        PropertyValueFactory p1 = new PropertyValueFactory<>("productNum");
        firstCol.setCellValueFactory(p1);

        TableColumn secCol = (TableColumn) productsTable.getColumns().get(1);
        PropertyValueFactory p2 = new PropertyValueFactory<>("productCode");
        secCol.setCellValueFactory(p2);

        TableColumn thirdCol = (TableColumn) productsTable.getColumns().get(2);
        PropertyValueFactory p3 = new PropertyValueFactory<>("quantity");
        thirdCol.setCellValueFactory(p3);

        productsTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null && newSelection instanceof V_Products) {
                V_Products selectedProd = (V_Products) newSelection;
                selProductCode.textProperty().setValue(selectedProd.getProductCode());
                selProductNum.textProperty().setValue(selectedProd.getProductNum().toString());
            }
        });

        ObservableList<V_Products> prod = FXCollections.observableArrayList(productService.findAll());
        products = new FilteredList<>(prod, p -> true);
        productsTable.setItems(products);

        initVisibility();
    }

    private void initVisibility() {
        changeProdStateBtn.visibleProperty().setValue(SecurityUtils.isAdmin());
    }

    public void filterTable() {
        String prodCode = productCode.textProperty().getValue();
        String prodNumber = productNumber.textProperty().getValue();

        if (StringUtils.isBlank(prodCode) && StringUtils.isBlank(prodNumber)) {
            products.setPredicate(prod -> true);
        } else if (StringUtils.isNotBlank(prodCode) && StringUtils.isNotBlank(prodNumber)) {
            products.setPredicate(
                    prod -> prod.getProductCode().equals(prodCode)
                    && prod.getProductNum().toString().equals(prodNumber));
        } else if (StringUtils.isNotBlank(prodCode)) {
            products.setPredicate(
                    prod -> prod.getProductCode().equals(prodCode));
        } else {
            products.setPredicate(
                    prod -> prod.getProductNum().toString().equals(prodNumber));
        }
    }

    public void changeProductState() {
        String text = selProductNum.textProperty().getValue();

        if (NumberUtils.isDigits(text)) {
            Integer prouctNumber = Integer.parseInt(selProductNum.textProperty().getValue());
            uIConfig.loadEditProduct(prouctNumber, false);
        } else {
            showSelectProductpopup();
        }
    }

    public void productSale() {
        String text = selProductNum.textProperty().getValue();

        if (NumberUtils.isDigits(text)) {
            Integer prouctNumber = Integer.parseInt(selProductNum.textProperty().getValue());
            uIConfig.loadNewSale(prouctNumber);
        } else {
            showSelectProductpopup();
        }
    }

    private void showSelectProductpopup() {
        showInformationPopup("Selektuje proizvod",
                "Da bi ste izvršili akciju, selektujte željeni proizvod u tabeli.",
                "");
    }
    
    public void productsReport() {
        reportingController.productsToPdf(products);
    }

}
