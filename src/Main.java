import java.nio.ByteBuffer;
import java.util.*;
import java.io.*;

public class Main {

    public static HashMap<Byte, Integer> map = new HashMap<>();
    static Byte[] key2 = new Byte[1];

    public static PriorityQueue<HuffmanNode> MinHeap = new PriorityQueue<>(new Comparator<HuffmanNode>() {
        @Override
        public int compare(HuffmanNode node1, HuffmanNode node2) {
            return node1.getFrequency() - node2.getFrequency();
        }
    });

    public static void main(String[] args) throws IOException {
        if(args.length>1) {
            // Ensure at least 3 arguments are passed
            String choice = args[0]; // First argument: 'c' or 'd'
            String filePath = args[1]; // Second argument: file path
            if (choice.equals("c")) {
                if (args.length < 3) {
                    System.out.println("Missing parameter: n (number of characters) for compression.");
                    return;
                }
                int n;
                try {
                    n = Integer.parseInt(args[2]); // Third argument: n (number of characters)
                } catch (NumberFormatException e) {
                    System.out.println("Invalid number format for 'n'. It should be an integer.");
                    return;
                }
                compress(filePath, n);
            } else if (choice.equals("d")) {
                decompress(filePath);
            } else {
                System.out.println("Invalid choice. Use 'c' for compression or 'd' for decompression.");
            }
        } else {
            String filePath="";
            Scanner input = new Scanner(System.in);
            System.out.println("Enter Whether you want to compress or decompress: ");
            String choice = input.nextLine();
            int n=0;
            if(choice.equals(("c"))) {
                System.out.println("please enter the file path: ");
                filePath = input.nextLine();
                System.out.println("Enter the number of characters: ");
                n = input.nextInt();
                compress(filePath, n);
            }
            else if(choice.equals("d")) {
                System.out.println("please enter the file path: ");
                filePath = input.nextLine();
                decompress(filePath);
            }
            else
                System.out.println("Invalid choice");


        }
    }


    public static void compress(String filePath, int n){
        BufferedInputStream bufferedInputStream = null;
        long startTime = System.currentTimeMillis();
        try {
            bufferedInputStream = new BufferedInputStream(new FileInputStream(filePath));
            byte[] buffer = new byte[1024]; // 1KB buffer
            int bytesRead = 0;
            int i = 2;
            while (((bytesRead = bufferedInputStream.read(buffer)) != -1)) {
                updateFreq(buffer, bytesRead, n);
            }
            bufferedInputStream.close();
            addingToHeap();
            HuffmanTree huffmanTree = new HuffmanTree( MinHeap);
            huffmanTree.buildHuffmanTree();
            CompressFile compressFile = new CompressFile(huffmanTree.getMap(), huffmanTree.getCodeLengths(), filePath,n);
            compressFile.encodeAndWrite();
            long endTime = System.currentTimeMillis();
            System.out.println("Time taken to compress: " + (endTime - startTime) + "ms");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void decompress(String filePath){
        long startTime = System.currentTimeMillis();
        try {
            Decompress decompressFile = new Decompress(filePath);
            decompressFile.decompress();
            long endTime = System.currentTimeMillis();
            System.out.println("Time taken to decompress: " + (endTime - startTime) + "ms");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void updateFreq(byte[] buffer, int bytesRead, int n) {
        // usage of java.nio.ByteBuffer
        //https://docs.oracle.com/javase/8/docs/api/java/nio/ByteBuffer.html
        // apply N-gram model construction according to this published paper i use sliding window approach
        // https://link.springer.com/article/10.1007/s10791-024-09431-y?utm_source=chatgpt.com
        // part 4.1  N‑gram model and N‑gram construction
        if (n == 1) {
            for (int i = 0; i < bytesRead; i++) {
                key2[0] = buffer[i];
                if (map.containsKey(key2[0])) {
                    map.put(key2[0], map.get(key2[0]) + 1);
                } else {
                    map.put(key2[0], 1);
                }
                key2[0] = null;
            }
        } else if (n > 1) {
            for (int i = 0; i <= bytesRead - n; i++) {
                byte[] key = Arrays.copyOfRange(buffer, i, i + n);
                ByteBuffer byteBuffer = ByteBuffer.wrap(key);
                if (map.containsKey(byteBuffer)) {
                    map.put(byteBuffer.get(), map.get(byteBuffer) + 1);
                } else {
                    map.put(byteBuffer.get(), 1);

                }

            }
        }
    }

    public static void addingToHeap() {
            for (Byte key : map.keySet()) {
                HuffmanNode node = new HuffmanNode(key, map.get(key));
                MinHeap.add(node);
            }

    }
}
