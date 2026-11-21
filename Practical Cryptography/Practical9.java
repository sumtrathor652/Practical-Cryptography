
import java.security.*;
import javax.crypto.*;
import javax.crypto.spec.*;
import java.util.*;

public class Practical9 {

    public static void main(String[] args) throws Exception {

        Scanner sc = new Scanner(System.in);

        System.out.print("Enter Password: ");
        String password = sc.nextLine();

        // Hash without salt
        MessageDigest md = MessageDigest.getInstance("SHA-256");

        byte[] unsaltedHash = md.digest(
                password.getBytes(java.nio.charset.StandardCharsets.UTF_8));

        System.out.println("\nWithout Salt:");
        System.out.println(HexFormat.of().formatHex(unsaltedHash));

        // Generate random salt
        byte[] salt = new byte[16];

        new SecureRandom().nextBytes(salt);

        // Hash with PBKDF2 and salt
        byte[] saltedHash = hashPassword(
                password.toCharArray(), salt);

        System.out.println("\nWith Salt:");

        System.out.println("Salt: "
                + HexFormat.of().formatHex(salt));

        System.out.println("Salted Hash: "
                + HexFormat.of().formatHex(saltedHash));

        // Verify password
        System.out.print("\nRe-enter Password: ");

        String entered = sc.nextLine();

        byte[] enteredHash = hashPassword(
                entered.toCharArray(), salt);

        boolean result =
                MessageDigest.isEqual(saltedHash, enteredHash);

        System.out.println("Password Verification: " + result);

        System.out.println("\nSecurity Comparison:");
        System.out.println("Without salt: Same passwords produce same hashes.");
        System.out.println("With salt: Different salts produce different hashes.");
        System.out.println("PBKDF2: Makes password guessing more expensive.");
    }

    static byte[] hashPassword(char[] password, byte[] salt)
            throws Exception {

        PBEKeySpec spec = new PBEKeySpec(
                password, salt, 210000, 256);

        try {
            SecretKeyFactory factory =
                    SecretKeyFactory.getInstance(
                            "PBKDF2WithHmacSHA256");

            return factory.generateSecret(spec).getEncoded();

        } finally {
            spec.clearPassword();
            Arrays.fill(password, '\0');
        }
    }
}
