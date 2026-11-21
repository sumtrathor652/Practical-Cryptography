
import java.io.*;
import java.nio.file.*;
import java.security.cert.*;

public class Practical6 {

    public static void main(String[] args) throws Exception {

        // Generate self-signed certificate and keystore
        ProcessBuilder generate = new ProcessBuilder(
                "keytool",
                "-genkeypair",
                "-alias", "mycertificate",
                "-keyalg", "RSA",
                "-keysize", "2048",
                "-sigalg", "SHA256withRSA",
                "-validity", "365",
                "-dname", "CN=Cryptography Practical, OU=CSE, O=College, C=IN",
                "-keystore", "mykeystore.p12",
                "-storetype", "PKCS12",
                "-storepass", "changeit",
                "-keypass", "changeit",
                "-noprompt"
        );

        generate.inheritIO();

        Process p = generate.start();

        if (p.waitFor() != 0) {
            throw new RuntimeException("Certificate generation failed");
        }

        // Export certificate
        ProcessBuilder export = new ProcessBuilder(
                "keytool",
                "-exportcert",
                "-rfc",
                "-alias", "mycertificate",
                "-keystore", "mykeystore.p12",
                "-storepass", "changeit",
                "-file", "certificate.pem"
        );

        export.inheritIO();

        if (export.start().waitFor() != 0) {
            throw new RuntimeException("Certificate export failed");
        }

        // Read certificate
        CertificateFactory factory =
                CertificateFactory.getInstance("X.509");

        try (InputStream in =
                     Files.newInputStream(Path.of("certificate.pem"))) {

            X509Certificate cert =
                    (X509Certificate) factory.generateCertificate(in);

            System.out.println("\nCertificate Details");

            System.out.println("Subject: "
                    + cert.getSubjectX500Principal());

            System.out.println("Issuer: "
                    + cert.getIssuerX500Principal());

            System.out.println("Serial Number: "
                    + cert.getSerialNumber());

            System.out.println("Signature Algorithm: "
                    + cert.getSigAlgName());

            System.out.println("Public Key Algorithm: "
                    + cert.getPublicKey().getAlgorithm());

            System.out.println("Valid From: "
                    + cert.getNotBefore());

            System.out.println("Valid Until: "
                    + cert.getNotAfter());

            cert.checkValidity();

            // Verify self-signature
            cert.verify(cert.getPublicKey());

            System.out.println("Certificate is valid.");
            System.out.println("Self-signature verified.");
        }
    }
}
