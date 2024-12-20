import java.io.*;
import java.util.BitSet;
import java.util.HashMap;

public class Decompress {
    private HashMap<Byte, BitSet> map = new HashMap<>();
    private HashMap<Byte, Integer> codeLengths = new HashMap<>();
    private String filePath;
    private String decodedFilePath;
    private long originalFileLength = -1;

    public Decompress(String filePath) {
        this.filePath = filePath;
        this.decodedFilePath = filePath.substring(0, filePath.lastIndexOf('.')) + ".pdf";
    }

    public void decompress() throws IOException {
        try (BufferedInputStream inputStream = new BufferedInputStream(new FileInputStream(filePath));
             BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(decodedFilePath))) {

            readHeader(inputStream);
            decodeData(inputStream, outputStream);
        }
    }

    private void readHeader(BufferedInputStream inputStream) throws IOException {
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

                    BitSet bitSet = new BitSet(huffmanCode.length());
                    for (int i = 0; i < huffmanCode.length(); i++) {
                        if (huffmanCode.charAt(i) == '1') {
                            bitSet.set(i);
                        }
                    }

                    map.put(key, bitSet);
                    codeLengths.put(key, huffmanCode.length());
                }
                headerBuilder.setLength(0);
            } else {
                headerBuilder.append((char) currentByte);
            }
        }
    }

    private void decodeData(BufferedInputStream inputStream, BufferedOutputStream outputStream) throws IOException {
        HashMap<String, Byte> invertedMap = new HashMap<>();
        for (Byte key : map.keySet()) {
            BitSet bitSet = map.get(key);
            int length = codeLengths.get(key);
            StringBuilder binaryCode = new StringBuilder();
            for (int i = 0; i < length; i++) {
                binaryCode.append(bitSet.get(i) ? "1" : "0");
            }
            invertedMap.put(binaryCode.toString(), key);
        }

        StringBuilder currentCode = new StringBuilder();
        int currentByte;
        long bytesDecoded = 0;

        while ((currentByte = inputStream.read()) != -1) {
            for (int i = 7; i >= 0; i--) {
                currentCode.append(((currentByte >> i) & 1) == 1 ? "1" : "0");

                if (invertedMap.containsKey(currentCode.toString())) {
                    outputStream.write(invertedMap.get(currentCode.toString()));
                    currentCode.setLength(0);
                    bytesDecoded++;

                    // Stop if we've reached the original file length
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
