import java.io.*;
import java.util.Arrays;
import java.util.BitSet;
import java.util.HashMap;
import java.util.LinkedList;

public class CompressFile {
    private  int n;
    private HashMap<Byte, BitSet> map;
    private HashMap<Byte, Integer> codeLengths;
    private HashMap<String, BitSet> nMap;
    private HashMap<String, Integer> nCodeLengths;
    private String filePath;
    private String encodedFilePath;
    private long originalFileLength;

    public String getEncodedFilePath() {
        return encodedFilePath;
    }

    public CompressFile(HashMap<Byte, BitSet> map, HashMap<Byte, Integer> codeLengths, String filePath, int n) {
        this.map = map;
        this.codeLengths = codeLengths;
        this.filePath = filePath;
        File file = new File(filePath);
        this.originalFileLength = file.length();


        // Extract the directory path
        String directory = filePath.substring(0, filePath.lastIndexOf(File.separator));

        // Extract the original file name with the extension
        String originalFileName = filePath.substring(filePath.lastIndexOf(File.separator) + 1);

        // Construct the new encoded file name
        this.encodedFilePath = directory + File.separator + "21010017." + n + "." + originalFileName + ".hc";

    }

    public CompressFile(HashMap<String, BitSet> map, HashMap<String, Integer> codeLengths, String filePath, int n, int m) {
        this.nMap = map;
        this.nCodeLengths = codeLengths;
        this.filePath = filePath;
        File file = new File(filePath);
        this.originalFileLength = file.length();
        this.n = n;

        // Extract the directory path
        String directory = filePath.substring(0, filePath.lastIndexOf(File.separator));
        // Extract the original file name with the extension
        String originalFileName = filePath.substring(filePath.lastIndexOf(File.separator) + 1);
        // Construct the new encoded file name
        this.encodedFilePath = directory + File.separator + "21010017." + n + "." + originalFileName + ".hc";
    }

    public void encodeAndWrite() throws IOException {
        try (BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(filePath));
             BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(encodedFilePath))) {
            writeHeader(bufferedOutputStream);
            int bitBuffer = 0;
            int bitCount = 0;
            byte[] buffer = new byte[1024 * 1024];
            int bytesRead;

            while ((bytesRead = bufferedInputStream.read(buffer)) != -1) {
                for (int i = 0; i < bytesRead; i++) {
                    byte currentByte = buffer[i];
                    BitSet bitSet = map.get(currentByte);
                    if (bitSet == null || !codeLengths.containsKey(currentByte)) {
                        throw new IOException("No Huffman code for byte: " + currentByte);
                    }
                    int codeLength = codeLengths.get(currentByte);
                    for (int j = 0; j < codeLength; j++) {
                        bitBuffer = bitBuffer << 1;
                        if (bitSet.get(j)) {
                            bitBuffer = bitBuffer | 1;
                        } else {
                            bitBuffer = bitBuffer | 0;
                        }
                        bitCount++;
                        if (bitCount == 8) {
                            bufferedOutputStream.write(bitBuffer);
                            bitBuffer = 0;
                            bitCount = 0;
                        }
                    }
                }
            }
            if (bitCount > 0) {
                bitBuffer <<= (8 - bitCount);
                bufferedOutputStream.write(bitBuffer);
            }
        }
    }

    private void writeHeader(BufferedOutputStream outputStream) throws IOException {
        String nValueEntry = "N:" + this.n + "\n";
        outputStream.write(nValueEntry.getBytes());
        // Write the original file length
        String lengthEntry = "LENGTH:" + originalFileLength + "\n";
        outputStream.write(lengthEntry.getBytes());

        // Write the Huffman map
        for (Byte key : map.keySet()) {
            StringBuilder binaryCode = new StringBuilder();
            BitSet bitSet = map.get(key);
            int length = codeLengths.get(key);
            for (int i = 0; i < length; i++) {
                binaryCode.append(bitSet.get(i) ? "1" : "0");
            }
            String entry = key + ":" + binaryCode + "\n";
            outputStream.write(entry.getBytes());
        }
        // Add a delimiter line
        outputStream.write("\n".getBytes());
    }

    public void encodeAndWriteNgram() throws IOException {
        try (BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(filePath));
             BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(encodedFilePath))) {

            // Write the header
            writeHeaderNgram(bufferedOutputStream);

            int bitBuffer = 0;
            int bitCount = 0;
            byte[] buffer = new byte[512 * n];
            int bytesRead;

            while ((bytesRead = bufferedInputStream.read(buffer)) != -1) {
                for (int i = 0; i <= bytesRead - n; i += n) { // Process complete n-grams
                    byte[] ngram = Arrays.copyOfRange(buffer, i, i + n);
                    String key = Arrays.toString(ngram);
                    BitSet bitSet = nMap.get(key);

                    if (bitSet == null || !nCodeLengths.containsKey(key)) {
                        throw new IOException("No Huffman code for n-gram: " + key);
                    }

                    int codeLength = nCodeLengths.get(key);
                    for (int j = 0; j < codeLength; j++) {
                        bitBuffer = (bitBuffer << 1) | (bitSet.get(j) ? 1 : 0);
                        bitCount++;
                        if (bitCount == 8) { // Write full bytes
                            bufferedOutputStream.write(bitBuffer);
                            bitBuffer = 0;
                            bitCount = 0;
                        }
                    }
                }

                // Handle remaining bytes as an incomplete n-gram
                if (bytesRead % n != 0) {
                    byte[] remainingNgram = Arrays.copyOfRange(buffer, bytesRead - (bytesRead % n), bytesRead);
                    String key = Arrays.toString(remainingNgram);
                    BitSet bitSet = nMap.get(key);

                    if (bitSet == null || !nCodeLengths.containsKey(key)) {
                        throw new IOException("No Huffman code for n-gram: " + key);
                    }

                    int codeLength = nCodeLengths.get(key);
                    for (int j = 0; j < codeLength; j++) {
                        bitBuffer = (bitBuffer << 1) | (bitSet.get(j) ? 1 : 0);
                        bitCount++;
                        if (bitCount == 8) {
                            bufferedOutputStream.write(bitBuffer);
                            bitBuffer = 0;
                            bitCount = 0;
                        }
                    }
                }
            }

            // Write any remaining bits in the buffer
            if (bitCount > 0) {
                bitBuffer = bitBuffer << (8 - bitCount); // Pad remaining bits with zeros
                bufferedOutputStream.write(bitBuffer);
            }
        }
    }



    private void writeHeaderNgram(BufferedOutputStream outputStream) throws IOException {
        // Write the value of N
        String nValueEntry = "N:" + this.n + "\n";
        outputStream.write(nValueEntry.getBytes());

        // Write the original file length
        String lengthEntry = "LENGTH:" + originalFileLength + "\n";
        outputStream.write(lengthEntry.getBytes());

        // Write the Huffman N-gram map
        for (String key : nMap.keySet()) {
            StringBuilder binaryCode = new StringBuilder();
            BitSet bitSet = nMap.get(key);
            int length = nCodeLengths.get(key);

            for (int i = 0; i < length; i++) {
                binaryCode.append(bitSet.get(i) ? "1" : "0");
            }

            // Write the entry in the consistent format
            String entry = key + ":" + binaryCode + "\n";
            outputStream.write(entry.getBytes());
        }

        // Add a delimiter line
        outputStream.write("\n".getBytes());
    }



}

