package totp;

import static org.junit.jupiter.api.Assertions.*;

import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.Test;

class TotpGeneratorTest {
    private final byte[] key = "12345678901234567890".getBytes(StandardCharsets.US_ASCII);

    @Test
    void rfcTime59() {
        assertEquals("287082", TotpGenerator.generate(key, 59));
    }

    @Test
    void rfcTime1111111109() {
        assertEquals("081804", TotpGenerator.generate(key, 1111111109L));
    }

    @Test
    void rfcTime1111111111() {
        assertEquals("050471", TotpGenerator.generate(key, 1111111111L));
    }

    @Test
    void rfcTime1234567890() {
        assertEquals("005924", TotpGenerator.generate(key, 1234567890L));
    }

    @Test
    void rfcTime2000000000() {
        assertEquals("279037", TotpGenerator.generate(key, 2000000000L));
    }

    @Test
    void codeKeepsLeadingZero() {
        assertEquals(6, TotpGenerator.generate(key, 1234567890L).length());
    }

    @Test
    void sameStepGivesSameCode() {
        assertEquals(TotpGenerator.generate(key, 30), TotpGenerator.generate(key, 59));
    }

    @Test
    void nextStepGivesOtherCode() {
        assertNotEquals(TotpGenerator.generate(key, 0), TotpGenerator.generate(key, 30));
    }

    @Test
    void secondsLeftIsBetween1And30() {
        assertEquals(30, TotpGenerator.secondsLeft(60));
        assertEquals(1, TotpGenerator.secondsLeft(89));
    }
}
