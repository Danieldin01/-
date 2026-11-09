package totp;

import static org.junit.jupiter.api.Assertions.*;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import org.junit.jupiter.api.Test;

/**
 * Тесты. Каждый тест проверяет одну вещь.
 * Контрольные значения для Base32 взяты из стандарта RFC 4648.
 */
class Base32Test {

    // Вспомогательный метод: текст -> байты
    private byte[] bytes(String s) {
        return s.getBytes(StandardCharsets.US_ASCII);
    }

    @Test
    void encodeF() {
        assertEquals("MY======", Base32.encode(bytes("f")));
    }

    @Test
    void encodeFoo() {
        assertEquals("MZXW6===", Base32.encode(bytes("foo")));
    }

    @Test
    void encodeFooba() {
        assertEquals("MZXW6YTB", Base32.encode(bytes("fooba")));
    }

    @Test
    void encodeFoobar() {
        assertEquals("MZXW6YTBOI======", Base32.encode(bytes("foobar")));
    }

    @Test
    void decodeFoobar() {
        assertEquals("foobar", new String(Base32.decode("MZXW6YTBOI======"), StandardCharsets.US_ASCII));
    }

    @Test
    void decodeWorksWithLowerCase() {
        assertEquals("foo", new String(Base32.decode("mzxw6==="), StandardCharsets.US_ASCII));
    }

    @Test
    void decodeBadSymbolThrowsError() {
        assertThrows(IllegalArgumentException.class, () -> Base32.decode("ABC1"));
    }

    @Test
    void encodeThenDecodeGivesSameBytes() {
        byte[] key = SecretKeyGenerator.generate();
        assertArrayEquals(key, Base32.decode(Base32.encode(key)));
    }

    @Test
    void keyHas20Bytes() {
        assertEquals(20, SecretKeyGenerator.generate().length);
    }

    @Test
    void twoKeysAreDifferent() {
        byte[] first = SecretKeyGenerator.generate();
        byte[] second = SecretKeyGenerator.generate();
        assertFalse(Arrays.equals(first, second));
    }
}
