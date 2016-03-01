/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rcs.shoe.shop.common.exceptions;

import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author Rajko
 */
public class ShoeShopException extends Exception {
    
    private final ShoeShopExceptionsEnum exceptionEnum;
    
    public ShoeShopException (ShoeShopExceptionsEnum exceptionEnum) {
        this.exceptionEnum = exceptionEnum;
    }

    @Override
    public synchronized Throwable getCause() {
        if(exceptionEnum.getCause() != null) {
            return exceptionEnum.getCause();
        }
        return super.getCause();
    }

    @Override
    public String getMessage() {
        if(StringUtils.isNotBlank(exceptionEnum.getSourceMessage())) {
            return exceptionEnum.getSourceMessage();
        }
        if(StringUtils.isNotBlank(exceptionEnum.getMessage())) {
            return exceptionEnum.getMessage();
        }
        return super.getMessage();
    }
}
