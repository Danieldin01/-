package totp;

import java.util.Arrays;

/**
 * Главная программа (неделя 2): создаёт ключ и показывает его в Base32.
 */
public class Main {

    public static void main(String[] args) {
        // 1. Создаём случайный ключ (20 байт)
        byte[] key = SecretKeyGenerator.generate();

        // 2. Превращаем его в текст Base32
        String secret = Base32.encode(key);
        System.out.println("Secret key (Base32): " + secret);

        // 3. Проверяем: если декодировать обратно, получится тот же ключ?
        byte[] back = Base32.decode(secret);
        System.out.println("Decoded key is the same: " + Arrays.equals(key, back));
    }
}
