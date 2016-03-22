package com.rcs.shoe.shop.fx.controller;

import com.rcs.shoe.shop.core.entity.impl.ProductHistory;
import com.rcs.shoe.shop.core.entity.impl.view.V_Products;
import java.awt.Color;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javafx.application.Platform;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import net.sf.dynamicreports.jasper.builder.JasperReportBuilder;
import net.sf.dynamicreports.report.builder.DynamicReports;
import static net.sf.dynamicreports.report.builder.DynamicReports.stl;
import net.sf.dynamicreports.report.builder.column.Columns;
import net.sf.dynamicreports.report.builder.component.Components;
import net.sf.dynamicreports.report.builder.datatype.DataTypes;
import net.sf.dynamicreports.report.builder.style.StyleBuilder;
import net.sf.dynamicreports.report.constant.HorizontalAlignment;
import net.sf.dynamicreports.report.exception.DRException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ReportingController {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(ReportingController.class);
    
    public ReportingController() {
    }
    
    private StyleBuilder getColumnTitleStyle() {
        StyleBuilder boldStyle = stl.style().bold();
        StyleBuilder boldCenteredStyle = stl.style(boldStyle).setHorizontalAlignment(HorizontalAlignment.CENTER);
        StyleBuilder columnTitleStyle = stl.style(boldCenteredStyle)
                .setBorder(stl.pen1Point())
                .setBackgroundColor(Color.LIGHT_GRAY);
        return columnTitleStyle;
    }
    
    private JasperReportBuilder getSalesReportBuilder(List<ProductHistory> sales, Date from, Date to) {
        JasperReportBuilder builder = DynamicReports.report();
        builder.columns(
                Columns.column("Redni broj", "productNum", DataTypes.integerType())
                .setHorizontalAlignment(HorizontalAlignment.CENTER),
                Columns.column("Sifra proizvoda", "productCode", DataTypes.stringType())
                .setHorizontalAlignment(HorizontalAlignment.CENTER),
                Columns.column("Velicina", "size", DataTypes.integerType())
                .setHorizontalAlignment(HorizontalAlignment.CENTER),
                Columns.column("Vreme", "creationTime", DataTypes.dateType()).setPattern("yyyy-MM-dd HH:mm")
                .setHorizontalAlignment(HorizontalAlignment.CENTER))
                .pageFooter(Components.pageXofY())
                .highlightDetailEvenRows()
                .setColumnTitleStyle(getColumnTitleStyle());
        
        builder.title(Components.verticalList().add(Components.text("Prodaja")
                .setHorizontalAlignment(HorizontalAlignment.CENTER),
                Components.text("Od: " + getSimpleDate(from))
                .setHorizontalAlignment(HorizontalAlignment.LEFT),
                Components.text("Do: " + getSimpleDate(to))
                .setHorizontalAlignment(HorizontalAlignment.LEFT))
        );
        builder.setDataSource(sales);
        return builder;
    }
    
    private JasperReportBuilder getProductsReportBuilder(List<V_Products> products) {
        JasperReportBuilder builder = DynamicReports.report();
        builder.columns(
                Columns.column("Redni broj", "productNum", DataTypes.integerType())
                .setHorizontalAlignment(HorizontalAlignment.CENTER),
                Columns.column("Sifra proizvoda", "productCode", DataTypes.stringType())
                .setHorizontalAlignment(HorizontalAlignment.CENTER),
                Columns.column("Kolicina", "quantity", DataTypes.integerType())
                .setHorizontalAlignment(HorizontalAlignment.CENTER))
                .pageFooter(Components.pageXofY())
                .highlightDetailEvenRows()
                .setColumnTitleStyle(getColumnTitleStyle());
        builder.title(Components.verticalList().add(Components.text("Stanje")
                .setHorizontalAlignment(HorizontalAlignment.CENTER),
                Components.text("Datum: " + getSimpleDate(new Date()))
                .setHorizontalAlignment(HorizontalAlignment.LEFT))
        );
        builder.setDataSource(products);
        return builder;
    }
    
    private void toPDF(JasperReportBuilder builder) {
        FileOutputStream fos = null;
        try {
            DirectoryChooser directoryChooser = new DirectoryChooser();
            File selectedDirectory = directoryChooser.showDialog(new Stage());
            if (selectedDirectory != null) {
                String filePath = getPDFFileAbsolutePath(selectedDirectory, "prodaja", 0);
                fos = new FileOutputStream(filePath);
                builder.toPdf(fos);
            }
            
        } catch (DRException | FileNotFoundException e) {
            LOGGER.error("", e);
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    LOGGER.error("", e);
                }
            }
        }
    }
    
    public void salesToPdf(List<ProductHistory> sales, Date from, Date to) {
        
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                toPDF(getSalesReportBuilder(sales, from, to));
            }
        });
    }
    
    public void productsToPdf(List<V_Products> products) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                toPDF(getProductsReportBuilder(products));
            }
        });
    }
    
    private String getPDFFileAbsolutePath(File selectedDirectory, String fileName, int index) {
        boolean exists
                = new File(selectedDirectory.getAbsolutePath(), getPDFFileName(fileName, index)).exists();
        if (exists) {
            return getPDFFileAbsolutePath(selectedDirectory, fileName, ++index);
        }
        return selectedDirectory.getAbsolutePath() + getPDFFileName(fileName, index);
    }
    
    private String getPDFFileName(String fileName, int index) {
        String datePart = getSimpleDate(new Date());
        return "/" + fileName + "_" + datePart + "_" + index + "_" + ".pdf";
    }
    
    private String getSimpleDate(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(date);
    }
    
}
