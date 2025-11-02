package com.service.desk.utils;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class FuxoCaixaUtils {
    private static final DateTimeFormatter BR_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final Locale BRASIL = new Locale("pt", "BR");

    public static String formatarData(LocalDate date) {
        if (date == null) return "";
        return date.format(BR_FORMATTER);
    }

    public static String formatarValorBR(BigDecimal valor) {
        if (valor == null) return "";
        NumberFormat nf = NumberFormat.getCurrencyInstance(BRASIL);
        nf.setMinimumFractionDigits(2);
        nf.setMaximumFractionDigits(2);
        return nf.format(valor);
    }
}
