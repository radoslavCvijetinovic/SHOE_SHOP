/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rcs.shoe.shop.fx.controller;

import com.rcs.shoe.shop.fx.model.LanguageModel;
import com.rcs.shoe.shop.fx.model.LanguageModel.Language;

/**
 *
 * @author Rajko
 */
public class LanguageController {

    private final LanguageModel model;

    public LanguageController(LanguageModel model) {
        this.model = model;
        toSerbian();
    }

    public void toEnglish() {
        model.setBundle(Language.EN);
    }
    
    public final void toSerbian() {
        model.setBundle(Language.SR);
    }

    public Language getLanguage() {
        return model.getLanguage();
    }

}
