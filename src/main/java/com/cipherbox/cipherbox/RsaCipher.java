package com.cipherbox.cipherbox;

import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.MGF1ParameterSpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.OAEPParameterSpec;
import javax.crypto.spec.PSource;

public class RsaCipher {

    private final KeyPair keyPair;

    public RsaCipher() throws Exception {

        KeyPairGenerator generator =
                KeyPairGenerator.getInstance("RSA");

        generator.initialize(2048);

        keyPair = generator.generateKeyPair();
    }

    public String getPublicKey() {

        return Base64
                .getEncoder()
                .encodeToString(
                        keyPair.getPublic().getEncoded()
                );
    }

    public String getPrivateKey() {

        return Base64
                .getEncoder()
                .encodeToString(
                        keyPair.getPrivate().getEncoded()
                );
    }

    public String encrypt(
            String message,
            String publicKey
    ) throws Exception {

        byte[] keyBytes =
                Base64
                        .getDecoder()
                        .decode(publicKey);

        PublicKey key =
                KeyFactory
                        .getInstance("RSA")
                        .generatePublic(
                                new X509EncodedKeySpec(keyBytes)
                        );

        Cipher cipher =
                Cipher.getInstance(
                        "RSA/ECB/OAEPPadding"
                );

        OAEPParameterSpec spec =
                new OAEPParameterSpec(
                        "SHA-256",
                        "MGF1",
                        MGF1ParameterSpec.SHA256,
                        PSource.PSpecified.DEFAULT
                );

        cipher.init(
                Cipher.ENCRYPT_MODE,
                key,
                spec
        );

        byte[] encrypted =
                cipher.doFinal(
                        message.getBytes(
                                StandardCharsets.UTF_8
                        )
                );

        return Base64
                .getEncoder()
                .encodeToString(encrypted);
    }

    public String decrypt(
            String message,
            String privateKey
    ) throws Exception {

        byte[] keyBytes =
                Base64
                        .getDecoder()
                        .decode(privateKey);

        PrivateKey key =
                KeyFactory
                        .getInstance("RSA")
                        .generatePrivate(
                                new PKCS8EncodedKeySpec(keyBytes)
                        );

        Cipher cipher =
                Cipher.getInstance(
                        "RSA/ECB/OAEPPadding"
                );

        OAEPParameterSpec spec =
                new OAEPParameterSpec(
                        "SHA-256",
                        "MGF1",
                        MGF1ParameterSpec.SHA256,
                        PSource.PSpecified.DEFAULT
                );

        cipher.init(
                Cipher.DECRYPT_MODE,
                key,
                spec
        );

        byte[] decrypted =
                cipher.doFinal(
                        Base64
                                .getDecoder()
                                .decode(message)
                );

        return new String(
                decrypted,
                StandardCharsets.UTF_8
        );
    }
}