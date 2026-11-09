package totp;

import java.io.ByteArrayOutputStream;

public class Base32 {
    private static final String ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZ234567";

    public static String encode(byte[] data) {
        StringBuilder result = new StringBuilder();
        int buffer = 0;
        int bitsInBuffer = 0;

        for (byte b : data) {
            buffer = (buffer << 8) | (b & 0xFF);
            bitsInBuffer += 8;

            while (bitsInBuffer >= 5) {
                int index = (buffer >> (bitsInBuffer - 5)) & 31;
                result.append(ALPHABET.charAt(index));
                bitsInBuffer -= 5;
            }
        }

        if (bitsInBuffer > 0) {
            int index = (buffer << (5 - bitsInBuffer)) & 31;
            result.append(ALPHABET.charAt(index));
        }

        while (result.length() % 8 != 0) {
            result.append('=');
        }
        return result.toString();
    }

    public static byte[] decode(String text) {
        text = text.replace("=", "").toUpperCase();

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int buffer = 0;
        int bitsInBuffer = 0;

        for (char c : text.toCharArray()) {
            int value = ALPHABET.indexOf(c);
            if (value < 0) {
                throw new IllegalArgumentException("Invalid symbol: " + c);
            }

            buffer = (buffer << 5) | value;
            bitsInBuffer += 5;

            if (bitsInBuffer >= 8) {
                out.write((buffer >> (bitsInBuffer - 8)) & 0xFF);
                bitsInBuffer -= 8;
            }
        }
        return out.toByteArray();
    }
}
