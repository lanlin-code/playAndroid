package com.example.playandroid.util;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Util {
    public static String encode(String text) {
        byte[] secretBytes = null;
        try {
            secretBytes = MessageDigest.getInstance("md5").digest(text.getBytes());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        String md5code = null;
        if (secretBytes != null) {
            md5code = new BigInteger(1, secretBytes).toString(16);
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < 32 - md5code.length(); i ++) {
                builder.append(0);
            }
            builder.append(md5code);
            md5code = builder.toString();
        }
        return md5code;
    }
}
