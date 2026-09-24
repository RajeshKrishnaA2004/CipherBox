package com.cipherbox.cipherbox;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class XorCipher {

    public String encrypt(String message, String key) {

         byte[] bytes = message.getBytes(StandardCharsets.UTF_8);

        for (int i = 0; i < bytes.length; i++) {
            bytes[i] = (byte) (bytes[i] ^ key.charAt(i % key.length()));
        }

        return Base64.getEncoder().encodeToString(bytes);
    }

    public String decrypt(String message, String key) {

         byte[] bytes = Base64.getDecoder().decode(message);

        for (int i = 0; i < bytes.length; i++) {
            bytes[i] = (byte) (bytes[i] ^ key.charAt(i % key.length()));
        }

        return new String(bytes, StandardCharsets.UTF_8);
    }
}

