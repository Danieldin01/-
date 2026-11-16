package totp;

import java.nio.ByteBuffer;
import java.security.GeneralSecurityException;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class TotpGenerator {
    private static final int STEP = 30;
    private static final int DIGITS = 6;

    public static String generate(byte[] key, long timeSeconds) {
        long counter = timeSeconds / STEP;
        byte[] hash = hmacSha1(key, counter);
        int number = truncate(hash) % 1_000_000;
        return String.format("%0" + DIGITS + "d", number);
    }

    public static int secondsLeft(long timeSeconds) {
        return STEP - (int) (timeSeconds % STEP);
    }

    private static byte[] hmacSha1(byte[] key, long counter) {
        try {
            Mac mac = Mac.getInstance("HmacSHA1");
            mac.init(new SecretKeySpec(key, "HmacSHA1"));
            byte[] message = ByteBuffer.allocate(8).putLong(counter).array();
            return mac.doFinal(message);
        } catch (GeneralSecurityException e) {
            throw new IllegalStateException(e);
        }
    }

    private static int truncate(byte[] hash) {
        int offset = hash[hash.length - 1] & 0x0F;
        return ((hash[offset] & 0x7F) << 24)
                | ((hash[offset + 1] & 0xFF) << 16)
                | ((hash[offset + 2] & 0xFF) << 8)
                | (hash[offset + 3] & 0xFF);
    }
}
