
import java.security.*;
import java.nio.charset.StandardCharsets;
import java.util.HexFormat;

public class Practical5 {
    public static void main(String[] args) throws Exception {

        String message = "Hello Cryptography";

        byte[] data = message.getBytes(StandardCharsets.UTF_8);

        // Generate SHA-256 hash
        MessageDigest md = MessageDigest.getInstance("SHA-256");

        byte[] hash = md.digest(data);

        System.out.println("SHA-256 Hash:");
        System.out.println(HexFormat.of().formatHex(hash));

        // Generate RSA keys
        KeyPairGenerator generator =
                KeyPairGenerator.getInstance("RSA");

        generator.initialize(2048);

        KeyPair pair = generator.generateKeyPair();

        // Create digital signature
        Signature sign = Signature.getInstance("SHA256withRSA");

        sign.initSign(pair.getPrivate());
        sign.update(data);

        byte[] signature = sign.sign();

        System.out.println("\nDigital Signature Generated.");

        // Verify digital signature
        sign.initVerify(pair.getPublic());
        sign.update(data);

        boolean result = sign.verify(signature);

        System.out.println("Signature Verification: " + result);

        // Check modified message
        byte[] modified = "Modified Message".getBytes(
                StandardCharsets.UTF_8);

        sign.initVerify(pair.getPublic());
        sign.update(modified);

        System.out.println("Modified Message Valid: "
                + sign.verify(signature));
    }
}
