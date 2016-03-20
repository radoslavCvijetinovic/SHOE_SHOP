/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rcs.shoe.shop.fx.utils;

import java.time.Instant;
import java.util.Date;
import java.time.LocalDate;
import java.time.ZoneId;

/**
 *
 * @author Rajko
 */
public class DateUtils extends org.apache.commons.lang3.time.DateUtils {

    public static Date convert(LocalDate localDate) {
        Instant instant = Instant.from(localDate.atStartOfDay(ZoneId.systemDefault()));
        return Date.from(instant);
    }

    public static LocalDate convert(Date date) {
        Instant instant = date.toInstant();
        return instant.atZone(ZoneId.systemDefault()).toLocalDate();
    }

}
