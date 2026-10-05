import java.nio.charset.StandardCharsets;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.util.Base64;

public class DigitalSignatureDemo {

    public static void main(String[] args) throws Exception {
        KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
        generator.initialize(2048);
        KeyPair keyPair = generator.generateKeyPair();
        PrivateKey privateKey = keyPair.getPrivate();
        PublicKey publicKey = keyPair.getPublic();

        String document = "Келісімшарт №1: тараптар шарттарға келісті.";
        byte[] data = document.getBytes(StandardCharsets.UTF_8);

        Signature signer = Signature.getInstance("SHA256withRSA");
        signer.initSign(privateKey);
        signer.update(data);
        byte[] signature = signer.sign();

        System.out.println("Құжат:     " + document);
        System.out.println("Қолтаңба:  " + Base64.getEncoder().encodeToString(signature).substring(0, 60) + "...");

        System.out.println("\nТүпнұсқа құжат тексерілуде...");
        System.out.println("Қолтаңба жарамды ма? " + verify(publicKey, data, signature));

        String tampered = "Келісімшарт №1: тараптар шарттарға КЕЛІСПЕДІ.";
        System.out.println("\nӨзгертілген құжат тексерілуде...");
        System.out.println("Қолтаңба жарамды ма? "
                + verify(publicKey, tampered.getBytes(StandardCharsets.UTF_8), signature));
    }

    static boolean verify(PublicKey publicKey, byte[] data, byte[] signature) throws Exception {
        Signature verifier = Signature.getInstance("SHA256withRSA");
        verifier.initVerify(publicKey);
        verifier.update(data);
        return verifier.verify(signature);
    }
}
