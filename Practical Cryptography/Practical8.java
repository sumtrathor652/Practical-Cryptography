import javax.crypto.*;
import javax.crypto.spec.*;
import java.security.*;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HexFormat;

public class Practical8 {

    public static void main(String[] args) throws Exception {

        String message = "This is my secure message.";

        byte[] data = message.getBytes(StandardCharsets.UTF_8);

        // Generate AES key
        KeyGenerator generator = KeyGenerator.getInstance("AES");

        generator.init(256);

        SecretKey key = generator.generateKey();

        // Generate random IV
        byte[] iv = new byte[12];

        new SecureRandom().nextBytes(iv);

        // AES encryption
        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");

        cipher.init(Cipher.ENCRYPT_MODE, key,
                new GCMParameterSpec(128, iv));

        byte[] encrypted = cipher.doFinal(data);

        System.out.println("Encrypted Message: "
                + Base64.getEncoder().encodeToString(encrypted));

        // AES decryption
        cipher.init(Cipher.DECRYPT_MODE, key,
                new GCMParameterSpec(128, iv));

        byte[] decrypted = cipher.doFinal(encrypted);

        System.out.println("Decrypted Message: "
                + new String(decrypted, StandardCharsets.UTF_8));

        // SHA-256 hashing
        MessageDigest md = MessageDigest.getInstance("SHA-256");

        byte[] hash = md.digest(data);

        System.out.println("SHA-256 Hash: "
                + HexFormat.of().formatHex(hash));
    }
}
