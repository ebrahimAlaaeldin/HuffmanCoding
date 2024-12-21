import java.io.*;
import java.util.BitSet;
import java.util.HashMap;

public class Decompress {
    private HashMap<Byte, BitSet> map = new HashMap<>();
    private HashMap<Byte, Integer> codeLengths = new HashMap<>();
    private String filePath;
    // Map for decoding Huffman codes directly
    private HashMap<String, Byte> decodingMap = new HashMap<>();

    private HashMap<String,String> decodingNMap = new HashMap<>();
    private String decodedFilePath;
    private long originalFileLength = -1;

    private int n;
    private HashMap<String, BitSet> nMap;
    private HashMap<String, Integer> nCodeLengths;

    public Decompress(String filePath) {
        this.filePath = filePath;
        String directory = filePath.substring(0, filePath.lastIndexOf(File.separator)); // Extract the directory path
        String originalFileName = filePath.substring(filePath.lastIndexOf(File.separator) + 1, filePath.lastIndexOf('.')); // Extract the file name without extension
        this.decodedFilePath = directory + File.separator + "extracted." + originalFileName ; // Construct the new file path
    }

    public void decompress() throws IOException {
        try (BufferedInputStream inputStream = new BufferedInputStream(new FileInputStream(filePath))) {
            readHeaderAndBuildDecodingMap(inputStream);
            try (BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(decodedFilePath))) {
                if(this.n == 1)
                    decodeData(inputStream, outputStream);
                else
                    decodeNData(inputStream, outputStream);
            }
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
                if(line.startsWith("N:")) {
                    this.n = Integer.parseInt(line.substring("N:".length()));
                }
                else if (line.startsWith("LENGTH:")) {
                    originalFileLength = Long.parseLong(line.substring("LENGTH:".length()));
                } else {
                    if(this.n ==1) {
                        String[] parts = line.split(":");
                        byte key = Byte.parseByte(parts[0]);
                        String huffmanCode = parts[1];
                        decodingMap.put(huffmanCode, key);
                    }
                    else {
                        String[] parts = line.split(":");
                        String key = parts[0];
                        String huffmanCode = parts[1];
                        decodingNMap.put(huffmanCode, key);
                    }
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
    private void decodeNData(BufferedInputStream inputStream, BufferedOutputStream outputStream) throws IOException {
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
                String decodedNGram = decodingNMap.get(currentCode.toString());
                if (decodedNGram != null) {
                    // Convert the decoded string to bytes
                    String[] byteStrings = decodedNGram.replace("[", "").replace("]", "").split(", ");
                    for (String byteString : byteStrings) {
                        outputStream.write(Byte.parseByte(byteString));
                    }
                    currentCode.setLength(0);
                    bytesDecoded += decodedNGram.split(",").length;

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
