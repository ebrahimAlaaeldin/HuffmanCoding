import java.io.*;
import java.util.BitSet;
import java.util.HashMap;

public class Decompress {
    private HashMap<Byte, BitSet> map = new HashMap<>();
    private HashMap<Byte, Integer> codeLengths = new HashMap<>();
    private String filePath;
    // Map for decoding Huffman codes directly
    private HashMap<String, Byte> decodingMap = new HashMap<>();
    private String decodedFilePath;
    private long originalFileLength = -1;

    public Decompress(String filePath) {
        this.filePath = filePath;
        this.decodedFilePath = filePath.substring(0, filePath.lastIndexOf('.')) + ".pdf";
    }

    public void decompress() throws IOException {
        try (BufferedInputStream inputStream = new BufferedInputStream(new FileInputStream(filePath));
             BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(decodedFilePath))) {
            readHeaderAndBuildDecodingMap(inputStream);
            decodeData(inputStream, outputStream);
        }
    }

    private void readHeaderAndBuildDecodingMap(BufferedInputStream inputStream) throws IOException {
        StringBuilder headerBuilder = new StringBuilder();
        int currentByte;

        while ((currentByte = inputStream.read()) != -1) {
            if (currentByte == '\n') {
                String line = headerBuilder.toString().trim();
                if (line.isEmpty()) {
                    break;
                }
                if (line.startsWith("LENGTH:")) {
                    // Parse the length of the original file
                    originalFileLength = Long.parseLong(line.substring("LENGTH:".length()));
                } else {
                    // Parse Huffman map line: Byte:HuffmanCode
                    String[] parts = line.split(":");
                    byte key = Byte.parseByte(parts[0]);
                    String huffmanCode = parts[1];
                    decodingMap.put(huffmanCode, key);
                }
                headerBuilder.setLength(0);
            } else {
                headerBuilder.append((char) currentByte);
            }
        }

        this.decodingMap = decodingMap; // Store the decoding map for later use
    }
    private void decodeData(BufferedInputStream inputStream, BufferedOutputStream outputStream) throws IOException {
        StringBuilder currentCode = new StringBuilder();
        int currentByte;
        long bytesDecoded = 0;

        while ((currentByte = inputStream.read()) != -1) {
            for (int i = 7; i >= 0; i--) {
                if (((currentByte >> i) & 1) == 1) {
                    currentCode.append("1");
                } else {
                    currentCode.append("0");
                }

                Byte decodedByte = decodingMap.get(currentCode.toString());
                if (decodedByte != null) {
                    outputStream.write(decodedByte);
                    currentCode.setLength(0);
                    bytesDecoded++;

                    if (originalFileLength != -1 && bytesDecoded == originalFileLength) {
                        return;
                    }
                }
            }
        }
    }


    public String getDecodedFilePath() {
        return decodedFilePath;
    }
}
