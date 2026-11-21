
import javax.crypto.*;
import javax.crypto.spec.*;
import java.security.*;
import java.security.spec.*;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class Practical7 {

    public static void main(String[] args) throws Exception {

        // RSA Algorithm
        KeyPairGenerator rsaGenerator =
                KeyPairGenerator.getInstance("RSA");

        rsaGenerator.initialize(2048);

        KeyPair rsaPair = rsaGenerator.generateKeyPair();

        String message = "Hello RSA Algorithm";

        Cipher rsa = Cipher.getInstance(
                "RSA/ECB/OAEPWithSHA-256AndMGF1Padding");

        OAEPParameterSpec oaep = new OAEPParameterSpec(
                "SHA-256", "MGF1", MGF1ParameterSpec.SHA256,
                PSource.PSpecified.DEFAULT);

        rsa.init(Cipher.ENCRYPT_MODE, rsaPair.getPublic(), oaep);

        byte[] encrypted = rsa.doFinal(
                message.getBytes(StandardCharsets.UTF_8));

        rsa.init(Cipher.DECRYPT_MODE, rsaPair.getPrivate(), oaep);

        byte[] decrypted = rsa.doFinal(encrypted);

        System.out.println("RSA Encrypted: "
                + Base64.getEncoder().encodeToString(encrypted));

        System.out.println("RSA Decrypted: "
                + new String(decrypted, StandardCharsets.UTF_8));

        // Diffie-Hellman Algorithm
        AlgorithmParameterGenerator parameterGenerator =
                AlgorithmParameterGenerator.getInstance("DH");

        parameterGenerator.init(2048);

        AlgorithmParameters parameters =
                parameterGenerator.generateParameters();

        DHParameterSpec dhSpec =
                parameters.getParameterSpec(DHParameterSpec.class);

        KeyPairGenerator dhGenerator =
                KeyPairGenerator.getInstance("DH");

        dhGenerator.initialize(dhSpec);

        // Alice and Bob generate key pairs
        KeyPair alice = dhGenerator.generateKeyPair();
        KeyPair bob = dhGenerator.generateKeyPair();

        // Alice generates shared secret
        KeyAgreement aliceAgreement =
                KeyAgreement.getInstance("DH");

        aliceAgreement.init(alice.getPrivate());
        aliceAgreement.doPhase(bob.getPublic(), true);

        byte[] aliceSecret = aliceAgreement.generateSecret();

        // Bob generates shared secret
        KeyAgreement bobAgreement =
                KeyAgreement.getInstance("DH");

        bobAgreement.init(bob.getPrivate());
        bobAgreement.doPhase(alice.getPublic(), true);

        byte[] bobSecret = bobAgreement.generateSecret();

        System.out.println("\nDiffie-Hellman");

        System.out.println("Alice Secret: "
                + Base64.getEncoder().encodeToString(aliceSecret));

        System.out.println("Bob Secret: "
                + Base64.getEncoder().encodeToString(bobSecret));

        System.out.println("Shared Secret Match: "
                + MessageDigest.isEqual(aliceSecret, bobSecret));
    }
}
