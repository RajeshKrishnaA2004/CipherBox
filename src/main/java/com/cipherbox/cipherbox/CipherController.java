package com.cipherbox.cipherbox;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CipherController {

    CaesarCipher caesarCipher = new CaesarCipher();
    XorCipher xorCipher = new XorCipher();

    @GetMapping("/encrypt")
    public String encrypt(  @RequestParam String message, 
                            @RequestParam String method,
                            @RequestParam(required = false) String key) {

        if (method.equals("caesar")) {
            return caesarCipher.encrypt(message);
        } else if (method.equals("xor")) {
            return xorCipher.encrypt(message, key);
        }
        return "Invalid method";
    }

    @GetMapping("/decrypt")
    public String decrypt(  @RequestParam String message, 
                            @RequestParam String method, 
                            @RequestParam(required = false) String key) {

        if (method.equals("caesar")) {
            return caesarCipher.decrypt(message);
        } else if (method.equals("xor")) {
            return xorCipher.decrypt(message, key);
        }
        return "Invalid method";
    }
}