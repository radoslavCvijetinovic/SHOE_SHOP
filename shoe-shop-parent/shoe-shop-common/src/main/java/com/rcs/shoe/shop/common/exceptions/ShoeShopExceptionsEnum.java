/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rcs.shoe.shop.common.exceptions;

/**
 *
 * @author Rajko
 */
public enum ShoeShopExceptionsEnum {
    
    DB_EXCEPTION(1, "Databse exception!");
    
    private final int code;
    
    private final String message; 
    
    private Throwable cause;
    
    private ShoeShopExceptionsEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }
    
    public int getCode() {
        return this.code;
    }
    
    public String getMessage() {
        return this.message;
    }
    
    public ShoeShopExceptionsEnum setCause(Throwable source) {
        this.cause = source;
        return this;
    }
    
    public Throwable getCause() {
        return cause;
    }
    
    public String getSourceMessage() {
        return this.cause != null ? this.cause.getMessage() : "";
    }
}
