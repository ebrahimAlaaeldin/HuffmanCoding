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


//        Decompress decompressFile = new Decompress("gbbct10_encoded.txt");
//        decompressFile.decompressFile();

        String filePath = "E:\\CSE\\CSE 3-1\\Algorithm\\Project\\HuffmanCoding\\gbbct10.seq";
        BufferedInputStream bufferedInputStream = null;
        Scanner input = new Scanner(System.in);
        System.out.println("Enter the number of characters: ");
        int n = input.nextInt();
        long startTime = System.currentTimeMillis();

        try {
            bufferedInputStream = new BufferedInputStream(new FileInputStream(filePath));
            byte[] buffer = new byte[1024]; //read 1MB at a time
            int bytesRead = 0;
            int i = 2;
            while (((bytesRead = bufferedInputStream.read(buffer)) != -1)) {
//                System.out.println(new String(buffer, 0, bytesRead));
                updateFreq(buffer, bytesRead, n);
            }
            bufferedInputStream.close();
            addingToHeap();
            HuffmanTree huffmanTree = new HuffmanTree( MinHeap);
            huffmanTree.buildHuffmanTree();
            CompressFile compressFile = new CompressFile(huffmanTree.getMap(), huffmanTree.getCodeLengths(), filePath);
            compressFile.encodeAndWrite();
            String encodedFilePath = compressFile.getEncodedFilePath();
            Decompress decompressFile = new Decompress(encodedFilePath);
            decompressFile.decompress();
            long endTime = System.currentTimeMillis();
            System.out.println("Time taken: " + (endTime - startTime) + "ms");

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

//        // Store all nodes from the heap in a list
//        List<HuffmanNode> nodesList = new ArrayList<>();
//        while (!MinHeap.isEmpty()) {
//            nodesList.add(MinHeap.poll());
//        }

//        // Sort the list based on frequency (ascending order)
//        nodesList.sort(Comparator.comparingInt(HuffmanNode::getFrequency));

        // Print the nodes in sorted order
//        for (HuffmanNode node : nodesList) {
//
//            System.out.println("Character: " + node.getData() + ", Frequency: " + node.getFrequency());
//        }


    }
}
