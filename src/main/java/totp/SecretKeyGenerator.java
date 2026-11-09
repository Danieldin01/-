package totp;

import java.security.SecureRandom;

/**
 * Создаёт случайный секретный ключ.
 * Этот ключ знает и программа, и телефон (Google Authenticator).
 */
public class SecretKeyGenerator {

    // Длина ключа в байтах: 20 байт = 160 бит.
    // Столько рекомендует стандарт для алгоритма HMAC-SHA1.
    private static final int KEY_LENGTH = 20;

    public static byte[] generate() {
        // SecureRandom - безопасный генератор случайных чисел (для криптографии).
        // Обычный Random для ключей использовать нельзя.
        SecureRandom random = new SecureRandom();

        byte[] key = new byte[KEY_LENGTH];  // пустой массив из 20 байт
        random.nextBytes(key);              // заполняем его случайными байтами
        return key;
    }
}
