package com.hra.hra.config;

import java.time.LocalDate;

public class FiscalYearUtil {
    /**
     * returns "YYYY-YYYY" representing fiscal year Apr1..Mar31 that contains given date.
     */
    public static String fiscalYearFor(LocalDate date) {
        int year = date.getYear();
        if (date.getMonthValue() >= 4) {
            return String.format("%d-%d", year, year + 1);
        } else {
            return String.format("%d-%d", year - 1, year);
        }
    }
}
