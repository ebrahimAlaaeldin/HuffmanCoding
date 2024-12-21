import java.io.*;
import java.util.BitSet;
import java.util.HashMap;

public class CompressFile {
    private HashMap<Byte, BitSet> map;
    private HashMap<Byte, Integer> codeLengths;
    private String filePath;
    private String encodedFilePath;
    private long originalFileLength;

    public String getEncodedFilePath() {
        return encodedFilePath;
    }

    public CompressFile(HashMap<Byte, BitSet> map, HashMap<Byte, Integer> codeLengths, String filePath,int n) {
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
        this.encodedFilePath = directory + File.separator + "21010017." + n + "." + originalFileName+".hc";

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
}
