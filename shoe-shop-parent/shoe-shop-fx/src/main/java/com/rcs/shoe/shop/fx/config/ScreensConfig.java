package com.rcs.shoe.shop.fx.config;

import com.rcs.shoe.shop.fx.controller.ui.Controller;
import com.rcs.shoe.shop.fx.controller.ui.LoginController;
import com.rcs.shoe.shop.fx.controller.ui.MainController;
import com.rcs.shoe.shop.fx.controller.ui.NotImplementedController;
import com.rcs.shoe.shop.fx.controller.ui.ProductsController;
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

    public static final int WIDTH = 800;
    public static final int HEIGHT = 600;
    public static final String STYLE_FILE = "/styles/styles.css";

    private Stage stage;
    private Scene scene;
    private LanguageModel lang;
    private StackPane root;
    private StackPane mainStackPane;

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
        root.getStylesheets().add(STYLE_FILE);
        stage.setTitle("Shoe Shop");
        stage.getIcons().add(new Image(getClass().getResource("/images/shoe-shop-fx.png").toExternalForm()));
        scene = new Scene(root, WIDTH, HEIGHT);
        stage.setScene(scene);
        stage.setResizable(false);

        stage.setOnHiding(new EventHandler<WindowEvent>() {
            public void handle(WindowEvent event) {
                LOGGER.debug("Closing app.");
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

    private void setNodeOnTop(Node node) {
        root.getChildren().add(node);
    }

    public void removeNode(Node node) {
        root.getChildren().remove(node);
    }

    public void removeNodeFromMain(Node node) {
        mainStackPane.getChildren().remove(node);
    }

    public void loadLogin() {
        try {
            setNode(getNode(loginController(), getClass().getResource("/fxml/login.fxml")));
        } catch (Throwable e) {
            LOGGER.error("loadLogin error.", e);
        }
    }

    public void loadMain() {
        try {
            MainController mainController = mainController();
            Node mainNode = getNode(mainController, getClass().getResource("/fxml/main.fxml"));
            mainStackPane = mainController.getMainStackPane();
            setNode(mainNode);
            loadProducts();
        } catch (Throwable e) {
            LOGGER.error("loadMain error.", e);
        }
    }

    public void loadProducts() {
        setNodeToMain(getNode(productsController(), getClass().getResource("/fxml/products.fxml")));
    }
    
    public void loadNotImplemented(){
        setNodeToMain(getNode(notImplementedController(), getClass().getResource("/fxml/not_implemented.fxml")));
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
            LOGGER.error("getNode error.", e);
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

    public Stage getStage() {
        return stage;
    }

    @Override
    public void update(Observable o, Object arg) {
        loginController();
    }

}
