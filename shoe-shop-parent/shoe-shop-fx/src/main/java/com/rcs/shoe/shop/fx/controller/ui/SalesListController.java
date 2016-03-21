/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rcs.shoe.shop.fx.controller.ui;

import com.rcs.shoe.shop.core.entity.impl.ProductHistory;
import com.rcs.shoe.shop.core.service.ProductService;
import com.rcs.shoe.shop.fx.config.ScreensConfig;
import com.rcs.shoe.shop.fx.controller.ReportingController;
import com.rcs.shoe.shop.fx.utils.DateUtils;
import java.net.URL;
import java.time.LocalDate;
import java.util.Date;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author Rajko
 */
public class SalesListController extends Controller implements Initializable {

    @FXML
    private TableView salesTable;
    @FXML
    private DatePicker createdDateDpFrom;
    @FXML
    private DatePicker createdDateDpTo;

    @Autowired
    private ProductService productService;
    @Autowired
    private ReportingController reportingController;

    private ObservableList<ProductHistory> sales;

    public SalesListController(ScreensConfig uIConfig) {
        super(uIConfig);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        createdDateDpFrom.setValue(LocalDate.now());
        createdDateDpTo.setValue(LocalDate.now().plusDays(1));
        initializeTable();
        searchSales();
    }

    public void filterTable() {
        searchSales();
    }

    private void initializeTable() {
        TableColumn firstCol = (TableColumn) salesTable.getColumns().get(0);
        PropertyValueFactory p1 = new PropertyValueFactory<>("id");
        firstCol.setCellValueFactory(p1);

        TableColumn secCol = (TableColumn) salesTable.getColumns().get(1);
        PropertyValueFactory p2 = new PropertyValueFactory<>("productNum");
        secCol.setCellValueFactory(p2);

        TableColumn thirdCol = (TableColumn) salesTable.getColumns().get(2);
        PropertyValueFactory p3 = new PropertyValueFactory<>("productCode");
        thirdCol.setCellValueFactory(p3);

        TableColumn fourthCol = (TableColumn) salesTable.getColumns().get(3);
        PropertyValueFactory p4 = new PropertyValueFactory<>("size");
        fourthCol.setCellValueFactory(p4);

        TableColumn fiftCol = (TableColumn) salesTable.getColumns().get(4);
        PropertyValueFactory p5 = new PropertyValueFactory<>("creationTime");
        fiftCol.setCellValueFactory(p5);

    }

    private void searchSales() {
        Date from = DateUtils.convert(createdDateDpFrom.getValue());
        Date to = DateUtils.convert(createdDateDpTo.getValue());

        sales = FXCollections.observableArrayList(
                productService.getSalesHistoryByDate(from, to));

        salesTable.setItems(sales);
    }

    public void loadReports() {
        Date from = DateUtils.convert(createdDateDpFrom.getValue());
        Date to = DateUtils.convert(createdDateDpTo.getValue());
        reportingController.salesToPdf(sales, from, to);

    }

}
