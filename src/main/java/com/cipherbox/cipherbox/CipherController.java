package com.cipherbox.cipherbox;

import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CipherController {

    CaesarCipher caesarCipher = new CaesarCipher();
    XorCipher xorCipher = new XorCipher();
    AesCipher aesCipher = new AesCipher();

    @GetMapping("/rsa/generate-keys")
    public Map<String, String> generateKeys() throws Exception {

        RsaCipher rsaCipher = new RsaCipher();

        return Map.of(
                "publicKey", rsaCipher.getPublicKey(),
                "privateKey", rsaCipher.getPrivateKey()
        );
    }

    @GetMapping("/encrypt")
    public String encrypt(
            @RequestParam String message,
            @RequestParam String method,
            @RequestParam(required = false) String key) {

        try {

            if (method.equals("caesar")) {

                return caesarCipher.encrypt(message);

            } else if (method.equals("xor")) {

                return xorCipher.encrypt(message, key);

            } else if (method.equals("aes")) {

                return aesCipher.encrypt(message, key);

            } else if (method.equals("rsa")) {

                RsaCipher rsaCipher = new RsaCipher();

                return rsaCipher.encrypt(message, key);
            }

            return "Invalid method";

        } catch (Exception e) {

            return "Encryption failed: " + e.getMessage();
        }
    }

    @GetMapping("/decrypt")
    public String decrypt(
            @RequestParam String message,
            @RequestParam String method,
            @RequestParam(required = false) String key) {

        try {

            if (method.equals("caesar")) {

                return caesarCipher.decrypt(message);

            } else if (method.equals("xor")) {

                return xorCipher.decrypt(message, key);

            } else if (method.equals("aes")) {

                return aesCipher.decrypt(message, key);

            } else if (method.equals("rsa")) {

                RsaCipher rsaCipher = new RsaCipher();

                return rsaCipher.decrypt(message, key);
            }

            return "Invalid method";

        } catch (Exception e) {

            return "Decryption failed: " + e.getMessage();
        }
    }
}