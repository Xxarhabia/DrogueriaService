package com.msara.servicio.utils;

import java.util.Random;

public class MethodsUtils {

    public static String referenceNumber() {
        Random random = new Random();
        int result = 100000 + random.nextInt(900000);
        return String.valueOf(result);
    }
}
