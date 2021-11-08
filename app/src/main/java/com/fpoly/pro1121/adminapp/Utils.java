package com.fpoly.pro1121.adminapp;

import java.text.NumberFormat;
import java.util.Locale;

public class Utils {

    public static String getFormatNumber(int number) {
        Locale localeVN = new Locale("vi", "VN");
        NumberFormat currencyVN = NumberFormat.getCurrencyInstance(localeVN);
        return (currencyVN.format(number));
    }
}
