package com.cipherbox.cipherbox;

import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

public class AesCipher {

    public String encrypt(String message, String key) throws Exception {

        // Generate random salt
        byte[] salt = new byte[16];

        SecureRandom random = new SecureRandom();
        random.nextBytes(salt);


        // Derive AES-256 key using PBKDF2
        PBEKeySpec spec =
                new PBEKeySpec(
                        key.toCharArray(),
                        salt,
                        65536,
                        256
                );

        SecretKeyFactory factory =
                SecretKeyFactory.getInstance(
                        "PBKDF2WithHmacSHA256"
                );

        byte[] keyBytes =
                factory.generateSecret(spec).getEncoded();


        // Convert derived bytes into AES key
        SecretKeySpec secretKey =
                new SecretKeySpec(keyBytes, "AES");


        // Generate random GCM nonce
        byte[] iv = new byte[12];
        random.nextBytes(iv);


        // Configure GCM
        GCMParameterSpec gcmSpec =
                new GCMParameterSpec(128, iv);


        // Create AES-GCM cipher
        Cipher cipher =
                Cipher.getInstance("AES/GCM/NoPadding");


        // Initialize encryption
        cipher.init(
                Cipher.ENCRYPT_MODE,
                secretKey,
                gcmSpec
        );


        // Encrypt message
        byte[] encrypted =
                cipher.doFinal(
                        message.getBytes(StandardCharsets.UTF_8)
                );


        // Combine:
        // salt + IV + ciphertext + authentication tag
        byte[] result =
                new byte[
                        salt.length
                        + iv.length
                        + encrypted.length
                ];

        System.arraycopy(
                salt,
                0,
                result,
                0,
                salt.length
        );

        System.arraycopy(
                iv,
                0,
                result,
                salt.length,
                iv.length
        );

        System.arraycopy(
                encrypted,
                0,
                result,
                salt.length + iv.length,
                encrypted.length
        );


        // Convert to Base64
        return Base64
                .getEncoder()
                .encodeToString(result);
    }


    public String decrypt(String encryptedMessage, String key)
            throws Exception {

        // Convert Base64 back to bytes
        byte[] data =
                Base64
                        .getDecoder()
                        .decode(encryptedMessage);


        // Extract salt
        byte[] salt = new byte[16];

        System.arraycopy(
                data,
                0,
                salt,
                0,
                16
        );


        // Extract IV / nonce
        byte[] iv = new byte[12];

        System.arraycopy(
                data,
                16,
                iv,
                0,
                12
        );


        // Extract ciphertext + authentication tag
        byte[] encrypted =
                new byte[data.length - 28];

        System.arraycopy(
                data,
                28,
                encrypted,
                0,
                encrypted.length
        );


        // Derive the same AES-256 key
        // using the password and extracted salt
        PBEKeySpec spec =
                new PBEKeySpec(
                        key.toCharArray(),
                        salt,
                        65536,
                        256
                );

        SecretKeyFactory factory =
                SecretKeyFactory.getInstance(
                        "PBKDF2WithHmacSHA256"
                );

        byte[] keyBytes =
                factory.generateSecret(spec).getEncoded();


        // Convert derived bytes into AES key
        SecretKeySpec secretKey =
                new SecretKeySpec(keyBytes, "AES");


        // Configure GCM using extracted IV
        GCMParameterSpec gcmSpec =
                new GCMParameterSpec(128, iv);


        // Create AES-GCM cipher
        Cipher cipher =
                Cipher.getInstance("AES/GCM/NoPadding");


        // Initialize decryption
        cipher.init(
                Cipher.DECRYPT_MODE,
                secretKey,
                gcmSpec
        );


        // Decrypt
        byte[] decrypted =
                cipher.doFinal(encrypted);


        // Convert bytes back to text
        return new String(
                decrypted,
                StandardCharsets.UTF_8
        );
    }
}