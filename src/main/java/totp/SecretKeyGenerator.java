package totp;

import java.security.SecureRandom;

public class SecretKeyGenerator {
    private static final int KEY_LENGTH = 20;

    public static byte[] generate() {
        SecureRandom random = new SecureRandom();

        byte[] key = new byte[KEY_LENGTH];
        random.nextBytes(key);
        return key;
    }
}
