package totp;

import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        byte[] key = SecretKeyGenerator.generate();

        String secret = Base32.encode(key);
        System.out.println("Secret key (Base32): " + secret);

        byte[] back = Base32.decode(secret);
        System.out.println("Decoded key is the same: " + Arrays.equals(key, back));
    }
}
