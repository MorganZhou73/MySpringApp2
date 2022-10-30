package com.unistar.myservice4.shared;

import org.springframework.stereotype.Component;

import java.security.SecureRandom;

import java.util.Random;

@Component
public class Utils {
    private final Random RANDOM = new SecureRandom();
    private final String ALPHABET = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";

    public String generateUserId(int length){
        return generateRandomString(length);
    }

    private String generateRandomString(int length){
        StringBuilder returnValue = new StringBuilder(length);

        for(int i=0; i<length;i++){
            returnValue.append(ALPHABET.charAt(RANDOM.nextInt(ALPHABET.length())));
        }
        return new String(returnValue);
    }

    public static int floatCompare(double a, double b) {
        return floatCompare(a, b, 0.00001);
    }

    public static int floatCompare(double a, double b, double threshold) {
        if(threshold < 0)
            threshold = -threshold;

        if (Math.abs(a - b) < threshold)
            return 0;
        else
            return (a > b) ? 1 : -1;
    }
}
