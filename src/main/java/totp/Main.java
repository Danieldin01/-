package totp;

import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        byte[] key;
        if (args.length > 0) {
            key = Base32.decode(args[0]);
        } else {
            key = SecretKeyGenerator.generate();
        }

        String secret = Base32.encode(key);
        System.out.println("Secret key (Base32): " + secret);

        byte[] back = Base32.decode(secret);
        System.out.println("Decoded key is the same: " + Arrays.equals(key, back));

        long now = System.currentTimeMillis() / 1000;
        System.out.println("TOTP code: " + TotpGenerator.generate(key, now));
        System.out.println("Valid for (seconds): " + TotpGenerator.secondsLeft(now));
    }
}
