/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rcs.shoe.shop.fx.controller.ui;

import com.rcs.shoe.shop.fx.config.ScreensConfig;

/**
 *
 * @author Rajko
 */
public abstract class Controller {
    
    protected ScreensConfig uIConfig;

    public Controller(ScreensConfig uIConfig) {
        this.uIConfig = uIConfig;
    }
    
}
