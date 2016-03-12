/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rcs.shoe.shop.fx.controller.ui;

import com.rcs.shoe.shop.core.entity.Product;
import com.rcs.shoe.shop.core.entity.V_Products;
import com.rcs.shoe.shop.core.service.ProductService;
import com.rcs.shoe.shop.fx.config.ScreensConfig;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
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

    @Autowired
    private ProductService productService;

    public void initialize(URL url, ResourceBundle rb) {
        TableColumn firstCol = (TableColumn) productsTable.getColumns().get(0);
        PropertyValueFactory p = new PropertyValueFactory<>("productCode");
        firstCol.setCellValueFactory(p);

        TableColumn secCol = (TableColumn) productsTable.getColumns().get(1);
        PropertyValueFactory p2 = new PropertyValueFactory<>("productNum");
        secCol.setCellValueFactory(p2);

        TableColumn thirdCol = (TableColumn) productsTable.getColumns().get(2);
        PropertyValueFactory p3 = new PropertyValueFactory<>("productDesc");
        thirdCol.setCellValueFactory(p3);

        TableColumn quantityCol = (TableColumn) productsTable.getColumns().get(3);
        PropertyValueFactory p4 = new PropertyValueFactory<>("quantity");
        quantityCol.setCellValueFactory(p4);

        List<V_Products> products = productService.findAll();
        ObservableList<V_Products> productsO = FXCollections.observableArrayList(products);
        productsTable.setItems(productsO);
    }

}
