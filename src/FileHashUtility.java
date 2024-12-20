import java.io.*;
import java.security.MessageDigest;
import java.security.DigestInputStream;

public class FileHashUtility {

    public static String calculateHash(String filePath, String algorithm) throws Exception {
        MessageDigest digest = MessageDigest.getInstance(algorithm);

        try (InputStream fis = new BufferedInputStream(new FileInputStream(filePath));
             DigestInputStream dis = new DigestInputStream(fis, digest)) {

            byte[] buffer = new byte[8192];
            while (dis.read(buffer) != -1) {
                // The DigestInputStream updates the digest internally
            }
        }

        // Convert the digest into a hexadecimal string
        StringBuilder hashString = new StringBuilder();
        for (byte b : digest.digest()) {
            hashString.append(String.format("%02x", b));
        }

        return hashString.toString();
    }

    public static void main(String[] args) {
        try {
            String file1 = "E:\\CSE\\CSE 3-1\\Algorithm\\Project\\HuffmanCoding\\src\\Lecture 4.0 - Greedy Algorithms_encoded.pdf";
            String file2 = "E:\\CSE\\CSE 3-1\\Algorithm\\Project\\HuffmanCoding\\src\\Lecture 4.0 - Greedy Algorithms.pdf";

            String hash1 = calculateHash(file1, "SHA-256");
            String hash2 = calculateHash(file2, "SHA-256");

            System.out.println("Hash of File 1: " + hash1);
            System.out.println("Hash of File 2: " + hash2);

            if (hash1.equals(hash2)) {
                System.out.println("Files are identical.");
            } else {
                System.out.println("Files are different.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
