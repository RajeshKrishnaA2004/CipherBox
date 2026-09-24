package com.cipherbox.cipherbox;

public class CaesarCipher {

    public String encrypt(String message) {

        String cipher = "";

        for (int i = 0; i < message.length(); i++) {

            char ch = message.charAt(i);

            if (ch >= 'a' && ch <= 'z') {
                cipher += (char) ('a' + (ch - 'a' + 3) % 26);
            }
            else if (ch >= 'A' && ch <= 'Z') {
                cipher += (char) ('A' + (ch - 'A' + 3) % 26);
            }
            else {
                cipher += ch;
            }
        }

        return cipher;
    }

    public String decrypt(String message) {

        String decipher = "";

        for (int i = 0; i < message.length(); i++) {

            char ch = message.charAt(i);

            if (ch >= 'a' && ch <= 'z') {
                decipher += (char) ('a' + (ch - 'a' - 3 + 26) % 26);
            }
            else if (ch >= 'A' && ch <= 'Z') {
                decipher += (char) ('A' + (ch - 'A' - 3 + 26) % 26);
            }
            else {
                decipher += ch;
            }
        }

        return decipher;
    }
}

