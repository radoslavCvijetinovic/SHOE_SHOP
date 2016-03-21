package com.rcs.shoe.shop.fx.config;

import com.rcs.shoe.shop.fx.Constants;
import com.rcs.shoe.shop.fx.controller.ui.Controller;
import com.rcs.shoe.shop.fx.controller.ui.LoginController;
import com.rcs.shoe.shop.fx.controller.ui.MainController;
import com.rcs.shoe.shop.fx.controller.ui.NewProductController;
import com.rcs.shoe.shop.fx.controller.ui.NotImplementedController;
import com.rcs.shoe.shop.fx.controller.ui.ProductsController;
import com.rcs.shoe.shop.fx.controller.ui.SaleCancelController;
import com.rcs.shoe.shop.fx.controller.ui.SaleEnterController;
import com.rcs.shoe.shop.fx.controller.ui.SalesListController;
import com.rcs.shoe.shop.fx.model.LanguageModel;
import java.net.URL;
import java.util.Observable;
import java.util.Observer;
import java.util.ResourceBundle;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Callback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;

/**
 *
 * @author Rajko
 */
@Configuration
@Lazy
public class ScreensConfig implements Observer {

    private static final Logger LOGGER = LoggerFactory.getLogger(ScreensConfig.class);

    private Stage stage;
    private Scene scene;
    private LanguageModel lang;
    private StackPane root;
    private StackPane mainStackPane;
    private Image icon = null;

    public void setPrimaryStage(Stage primaryStage) {
        this.stage = primaryStage;
    }

    public void setLangModel(LanguageModel lang) {
        if (this.lang != null) {
            this.lang.deleteObserver(this);
        }
        lang.addObserver(this);
        this.lang = lang;
    }

    public ResourceBundle getBundle() {
        return lang.getBundle();
    }

    public void showMainScreen() {
        root = new StackPane();
        root.getStylesheets().add(Constants.STYLE_FILE_PATH);
        stage.setTitle(Constants.APP_TITLE);
        icon = new Image(getClass().getResource(Constants.ICON_FILE_PATH).toExternalForm());
        stage.getIcons().add(icon);
        scene = new Scene(root, Constants.APP_WIDTH, Constants.APP_HEIGHT);
        stage.setScene(scene);
        stage.setResizable(false);

        stage.setOnHiding(new EventHandler<WindowEvent>() {
            public void handle(WindowEvent event) {
                LOGGER.debug("Closing the app!");
                System.exit(0);
                // TODO are you sure you want to exit dialog
            }
        });

        stage.show();
    }

    private void setNode(Node node) {
        root.getChildren().setAll(node);
    }

    private void setNodeToMain(Node node) {
        mainStackPane.getChildren().setAll(node);
    }

    public void removeNode(Node node) {
        root.getChildren().remove(node);
    }

    public void removeNodeFromMain(Node node) {
        mainStackPane.getChildren().remove(node);
    }

    public void loadLogin() {
        try {
            setNode(getNode(loginController(), getClass().getResource(Constants.LOGIN_FXML)));
        } catch (Throwable e) {
            LOGGER.error("LoadLogin error: ", e);
        }
    }

    public void loadMain() {
        try {
            MainController mainController = mainController();
            Node mainNode = getNode(mainController, getClass().getResource(Constants.MAIN_FXML));
            mainStackPane = mainController.getMainStackPane();
            setNode(mainNode);
            loadProducts();
        } catch (Throwable e) {
            LOGGER.error("LoadMain error: ", e);
        }
    }

    public void loadProducts() {
        setNodeToMain(getNode(productsController(), getClass().getResource(Constants.PRODUCTS_FXML)));
    }

    public void loadNewProduct() {
        setNodeToMain(getNode(newProductController(), getClass().getResource(Constants.NEW_PRODUCT_FXML)));
    }

    public void loadEditProduct(Integer productNumber, boolean fromSale) {
        NewProductController controller = newProductController();
        controller.setProductNumber(productNumber);
        controller.setFromSale(fromSale);
        setNodeToMain(getNode(controller, getClass().getResource(Constants.NEW_PRODUCT_FXML)));
    }

    public void loadNotImplemented() {
        setNodeToMain(getNode(notImplementedController(), getClass().getResource(Constants.NOT_IMPL_FXML)));
    }

    public void loadNewSale() {
        setNodeToMain(getNode(newSaleEnterController(), getClass().getResource(Constants.SALE_FXML)));
    }

    public void loadNewSale(Integer productNumber) {
        SaleEnterController saleEnterController = newSaleEnterController();
        saleEnterController.setProductNumber(productNumber);
        setNodeToMain(getNode(saleEnterController, getClass().getResource(Constants.SALE_FXML)));
    }

    public void loadSalesList() {
        setNodeToMain(getNode(newSalesListController(), getClass().getResource(Constants.SALES_LIST_FXML)));
    }

    public void loadSaleCancel() {
        setNodeToMain(getNode(newSaleCancelController(), getClass().getResource(Constants.SALE_CANCEL_FXML)));
    }

    public Image getIcon() {
        return icon;
    }

    private Node getNode(final Controller controller, URL location) {
        FXMLLoader loader = new FXMLLoader(location, lang.getBundle());
        loader.setControllerFactory(new Callback<Class<?>, Object>() {
            public Object call(Class<?> aClass) {
                return controller;
            }
        });

        try {
            return (Node) loader.load();
        } catch (Exception e) {
            LOGGER.error("GetNode error: ", e);
            return null;
        }
    }

    @Bean
    @Scope("prototype")
    LoginController loginController() {
        return new LoginController(this);
    }

    @Bean
    @Scope("prototype")
    MainController mainController() {
        return new MainController(this);
    }

    @Bean
    @Scope("prototype")
    ProductsController productsController() {
        return new ProductsController(this);
    }

    @Bean
    @Scope("prototype")
    NotImplementedController notImplementedController() {
        return new NotImplementedController(this);
    }

    @Bean
    @Scope("prototype")
    NewProductController newProductController() {
        return new NewProductController(this);
    }

    @Bean
    @Scope("prototype")
    SaleEnterController newSaleEnterController() {
        return new SaleEnterController(this);
    }

    @Bean
    @Scope("prototype")
    SalesListController newSalesListController() {
        return new SalesListController(this);
    }

    @Bean
    @Scope("prototype")
    SaleCancelController newSaleCancelController() {
        return new SaleCancelController(this);
    }

    public Stage getStage() {
        return stage;
    }

    @Override
    public void update(Observable o, Object arg) {
        loginController();
    }

}
