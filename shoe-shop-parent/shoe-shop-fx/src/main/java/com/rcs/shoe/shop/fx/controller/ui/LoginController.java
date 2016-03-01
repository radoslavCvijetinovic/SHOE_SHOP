package com.rcs.shoe.shop.fx.controller.ui;

import com.rcs.shoe.shop.common.enums.AuthoritiesEnum;
import com.rcs.shoe.shop.core.service.UserService;
import com.rcs.shoe.shop.fx.config.ScreensConfig;
import com.rcs.shoe.shop.fx.model.LanguageModel;
import com.rcs.shoe.shop.fx.model.LanguageModel.TranslationKeys;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

public class LoginController extends Controller implements Initializable {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoginController.class);

    @FXML
    private TextField usernameField;
    @FXML
    private TextField passwordField;
    @FXML
    private Label messageLabel;

    @Autowired
    private UserService userService;

    @Autowired
    private LanguageModel languageModel;

    public LoginController(ScreensConfig uIConfig) {
        super(uIConfig);
    }

    public void tryLogin() {

        String username = usernameField.getText();
        String password = passwordField.getText();

        UserDetails userDetails = userService.authenticate(username, password);

        if (userDetails != null) {
            LOGGER.debug("Succesfuly logged in.");
            uIConfig.loadMain();
        } else {
            LOGGER.debug("User not found.");
            messageLabel.setText(languageModel.translate(TranslationKeys.USER_NOT_FOUND));
        }
    }

    public void initialize(URL url, ResourceBundle rb) {

        if (!userService.userExists("rajkofon")) {
            LOGGER.info("Admin not in database.");

            List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
            authorities.add(new SimpleGrantedAuthority(AuthoritiesEnum.ADMIN.name()));
            authorities.add(new SimpleGrantedAuthority(AuthoritiesEnum.USER.name()));

            UserDetails user = new User("rajkofon", "123456", authorities);

            userService.save(user);
        } else {
            LOGGER.debug("Admin FOUND in database.");
        }

    }

}
