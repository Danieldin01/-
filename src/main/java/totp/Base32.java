package totp;

import java.io.ByteArrayOutputStream;

/**
 * Base32 - способ записать байты обычными буквами и цифрами.
 * Используются только символы A-Z и 2-7, поэтому ключ удобно печатать,
 * вводить вручную и вставлять в QR-код.
 *
 * Идея: берём биты подряд и режем на группы по 5 бит.
 * Каждая группа (число от 0 до 31) заменяется символом из алфавита.
 */
public class Base32 {

    // 32 символа: номер символа в строке = число от 0 до 31
    private static final String ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZ234567";

    /** Байты -> текст Base32 */
    public static String encode(byte[] data) {
        StringBuilder result = new StringBuilder();
        int buffer = 0;        // сюда складываем биты
        int bitsInBuffer = 0;  // сколько битов сейчас в buffer

        for (byte b : data) {
            // добавляем 8 новых битов (b & 0xFF делает байт числом от 0 до 255)
            buffer = (buffer << 8) | (b & 0xFF);
            bitsInBuffer += 8;

            // пока набралось хотя бы 5 битов - берём из них один символ
            while (bitsInBuffer >= 5) {
                int index = (buffer >> (bitsInBuffer - 5)) & 31;
                result.append(ALPHABET.charAt(index));
                bitsInBuffer -= 5;
            }
        }

        // если остались лишние биты (меньше 5), дополняем нулями и берём последний символ
        if (bitsInBuffer > 0) {
            int index = (buffer << (5 - bitsInBuffer)) & 31;
            result.append(ALPHABET.charAt(index));
        }

        // по стандарту длина текста должна делиться на 8, добавляем знаки '='
        while (result.length() % 8 != 0) {
            result.append('=');
        }
        return result.toString();
    }

    /** Текст Base32 -> байты */
    public static byte[] decode(String text) {
        // знаки '=' не нужны, регистр не важен
        text = text.replace("=", "").toUpperCase();

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int buffer = 0;
        int bitsInBuffer = 0;

        for (char c : text.toCharArray()) {
            int value = ALPHABET.indexOf(c);  // номер символа в алфавите (0-31)
            if (value < 0) {
                throw new IllegalArgumentException("Недопустимый символ: " + c);
            }

            // добавляем 5 новых битов
            buffer = (buffer << 5) | value;
            bitsInBuffer += 5;

            // как только набралось 8 битов - получился один байт
            if (bitsInBuffer >= 8) {
                out.write((buffer >> (bitsInBuffer - 8)) & 0xFF);
                bitsInBuffer -= 8;
            }
        }
        return out.toByteArray();
    }
}
