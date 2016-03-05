package com.rcs.shoe.shop.fx;

import com.sun.javafx.application.LauncherImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {

    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) throws Exception {
        try {
            LauncherImpl.launchApplication(App.class, AppPreloader.class, args);
        } catch (Throwable e) {
            if (e instanceof Exception) {
                Exception ex = (Exception) e;
                LOGGER.error("Exception: ", ex);
                throw ex;
            } else {
                LOGGER.error("Throwable: ", e);
            }
        }

    }
}
