package com.epiis.apicitasmedicas.helper;

import java.security.SecureRandom;

public class GenericHelper {
    private GenericHelper() {}

    private static final SecureRandom RANDOM = new SecureRandom();

    public static String followCodeGeneration() {
        String characters = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 7; i++) {
            sb.append(characters.charAt(RANDOM.nextInt(characters.length())));
        }
        return sb.toString();
    }
}
