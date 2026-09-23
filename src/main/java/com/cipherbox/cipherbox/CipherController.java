package com.cipherbox.cipherbox;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CipherController {

    @GetMapping("/encrypt")
    public String encrypt(@RequestParam String message) {

        String cypher = "";

        for (int i = 0; i < message.length(); i++) {

            char ch = message.charAt(i);

            if (ch >= 'a' && ch <= 'z') {
                cypher += (char) ('a' + (ch - 'a' + 3) % 26);
            }
            else if (ch >= 'A' && ch <= 'Z') {
                cypher += (char) ('A' + (ch - 'A' + 3) % 26);
            }
            else {
                cypher += ch;
            }
        }

        return cypher;
    }


    @GetMapping("/decrypt")
    public String decrypt(@RequestParam String message) {

        String decypher = "";

        for (int i = 0; i < message.length(); i++) {

            char ch = message.charAt(i);

            if (ch >= 'a' && ch <= 'z') {
                decypher += (char) ('a' + (ch - 'a' - 3 + 26) % 26);
            }
            else if (ch >= 'A' && ch <= 'Z') {
                decypher += (char) ('A' + (ch - 'A' - 3 + 26) % 26);
            }
            else {
                decypher += ch;
            }
        }
        return decypher;
    }
}