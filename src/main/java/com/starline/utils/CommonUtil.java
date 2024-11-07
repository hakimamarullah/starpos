package com.starline.utils;
/*
@Author hakim a.k.a. Hakim Amarullah
Java Developer
Created on 11/6/2024 12:55 PM
@Last Modified 11/6/2024 12:55 PM
Version 1.0
*/

import java.text.DecimalFormat;

public class CommonUtil {

    private CommonUtil() {

    }

    public static String formatRupiah(double number) {
        DecimalFormat decimalFormat = new DecimalFormat("#,###");
        return decimalFormat.format(number);
    }

}
