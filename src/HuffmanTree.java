import java.util.BitSet;
import java.util.Comparator;
import java.util.HashMap;
import java.util.PriorityQueue;

public class HuffmanTree {

    private PriorityQueue<HuffmanNode> minHeap = new PriorityQueue<>(new Comparator<HuffmanNode>() {
        @Override
        public int compare(HuffmanNode node1, HuffmanNode node2) {
            int frequencyComparison = node1.getFrequency() - node2.getFrequency();
            if (frequencyComparison == 0) {
                return node1.getData().compareTo(node2.getData());
            }
            return frequencyComparison;
        }
    });

    private HashMap<Byte, BitSet> map = new HashMap<>();
    private HashMap<Byte, Integer> codeLengths = new HashMap<>();

    private HuffmanNode root;

    public HuffmanTree(PriorityQueue<HuffmanNode> minHeap) {
        this.minHeap = minHeap;
    }

    public HashMap<Byte, BitSet> getMap() {
        return map;
    }

    public HuffmanNode getRoot() {
        return root;
    }
    public HashMap<Byte, Integer> getCodeLengths() {
        return codeLengths;
    }

    public void buildHuffmanTree() {
        if (minHeap.isEmpty()) {
            return;
        }
        while (minHeap.size() > 1) {
            HuffmanNode node1 = minHeap.poll();
            HuffmanNode node2 = minHeap.poll();
            HuffmanNode newNode = new HuffmanNode(node1.getFrequency() + node2.getFrequency());
            newNode.setLeft(node1);
            newNode.setRight(node2);
            minHeap.add(newNode);
        }

        this.root = minHeap.poll();
        storeCodes();
    }

    private void storeCodes() {
        if (root != null) {
            storeCodesRecursive(root, new BitSet(), 0);
        }
    }
    private void storeCodesRecursive(HuffmanNode node, BitSet bitSet, int bitIndex) {
        if (node.getLeft() == null && node.getRight() == null) {
            if (node.getData() != null) {
                map.put(node.getData(), (BitSet) bitSet.clone());
                codeLengths.put(node.getData(), bitIndex); // Store the length of the code
            }
            return;
        }
        if (node.getLeft() != null) {
            bitSet.clear(bitIndex); // Set bit to 0 for the left branch
            storeCodesRecursive(node.getLeft(), bitSet, bitIndex + 1);
        }

        if (node.getRight() != null) {
            bitSet.set(bitIndex); // Set bit to 1 for the right branch
            storeCodesRecursive(node.getRight(), bitSet, bitIndex + 1);
        }
    }


    public void printTree() {
        if (root != null) {
            printTreeRecursive(root, new BitSet(), 0);
        }
    }

    private void printTreeRecursive(HuffmanNode node, BitSet bitSet, int bitIndex) {
        if (node.getLeft() == null && node.getRight() == null) {
            if (node.getData() != null) {
                char character = (char) (byte) node.getData();
                System.out.print("Character: " + character + " (" + node.getData() + "): ");
                for (int i = 0; i < bitIndex; i++) {
                    System.out.print(bitSet.get(i) ? "1" : "0");
                }
                System.out.println();
            }
            return;
        }

        if (node.getLeft() != null) {
            bitSet.clear(bitIndex); // Set bit to 0 for the left branch
            printTreeRecursive(node.getLeft(), bitSet, bitIndex + 1);
        }

        if (node.getRight() != null) {
            bitSet.set(bitIndex); // Set bit to 1 for the right branch
            printTreeRecursive(node.getRight(), bitSet, bitIndex + 1);
        }
    }

    public void printHashmap() {
        for (Byte key : map.keySet()) {
            BitSet bitSet = map.get(key);
            int length = codeLengths.getOrDefault(key, 0); // Get the stored length

            StringBuilder binaryCode = new StringBuilder();

            // Convert BitSet to binary string using the correct length
            for (int i = 0; i < length; i++) {
                binaryCode.append(bitSet.get(i) ? "1" : "0");
            }

            System.out.println("Character: " + (char) (byte) key + " (" + key + "), Huffman code: " + binaryCode);
        }
    }



}
