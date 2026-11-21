
import javax.crypto.*;
import javax.crypto.spec.*;
import java.nio.ByteBuffer;
import java.nio.file.*;
import java.security.*;
import java.security.spec.*;
import java.util.Arrays;

public class Practical4 {
    public static void main(String[] args) throws Exception {

        // Generate RSA key pair
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
        keyGen.initialize(2048);
        KeyPair pair = keyGen.generateKeyPair();

        Files.write(Path.of("public.key"), pair.getPublic().getEncoded());
        Files.write(Path.of("private.key"), pair.getPrivate().getEncoded());

        System.out.println("RSA keys generated successfully.");

        // Create a sample file
        String message = "This is a secure confidential file.";
        Files.writeString(Path.of("original.txt"), message);

        // Generate AES key for file encryption
        KeyGenerator aesGen = KeyGenerator.getInstance("AES");
        aesGen.init(256);
        SecretKey aesKey = aesGen.generateKey();

        byte[] iv = new byte[12];
        new SecureRandom().nextBytes(iv);

        // Encrypt file using AES-GCM
        Cipher aes = Cipher.getInstance("AES/GCM/NoPadding");
        aes.init(Cipher.ENCRYPT_MODE, aesKey,
                new GCMParameterSpec(128, iv));

        byte[] encryptedData = aes.doFinal(
                Files.readAllBytes(Path.of("original.txt")));

        // Encrypt AES key using RSA-OAEP
        Cipher rsa = Cipher.getInstance(
                "RSA/ECB/OAEPWithSHA-256AndMGF1Padding");

        OAEPParameterSpec oaep = new OAEPParameterSpec(
                "SHA-256", "MGF1", MGF1ParameterSpec.SHA256,
                PSource.PSpecified.DEFAULT);

        rsa.init(Cipher.ENCRYPT_MODE, pair.getPublic(), oaep);
        byte[] encryptedKey = rsa.doFinal(aesKey.getEncoded());

        // Save encrypted key, IV and encrypted file data
        ByteBuffer buffer = ByteBuffer.allocate(
                4 + encryptedKey.length + iv.length + encryptedData.length);

        buffer.putInt(encryptedKey.length);
        buffer.put(encryptedKey);
        buffer.put(iv);
        buffer.put(encryptedData);

        Files.write(Path.of("encrypted.dat"), buffer.array());

        // Read encrypted file
        ByteBuffer input = ByteBuffer.wrap(
                Files.readAllBytes(Path.of("encrypted.dat")));

        int keyLength = input.getInt();
        byte[] wrappedKey = new byte[keyLength];
        input.get(wrappedKey);

        byte[] storedIV = new byte[12];
        input.get(storedIV);

        byte[] cipherText = new byte[input.remaining()];
        input.get(cipherText);

        // Decrypt AES key using RSA private key
        rsa.init(Cipher.DECRYPT_MODE, pair.getPrivate(), oaep);
        byte[] decryptedKey = rsa.doFinal(wrappedKey);

        SecretKey recoveredKey =
                new SecretKeySpec(decryptedKey, "AES");

        // Decrypt file
        aes.init(Cipher.DECRYPT_MODE, recoveredKey,
                new GCMParameterSpec(128, storedIV));

        byte[] decrypted = aes.doFinal(cipherText);

        Files.write(Path.of("decrypted.txt"), decrypted);

        System.out.println("File encrypted successfully.");
        System.out.println("File decrypted successfully.");
        System.out.println("Decrypted content: "
                + Files.readString(Path.of("decrypted.txt")));

        Arrays.fill(decryptedKey, (byte) 0);
    }
}
