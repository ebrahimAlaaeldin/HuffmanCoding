//import com.sun.source.tree.ParenthesizedPatternTree;
//
//import java.nio.ByteBuffer;
//import java.util.*;
//import java.io.*;
//
//public class commented {
//
//    public static HashMap<Object, Integer> map = new HashMap<>();
//
//    public static void main(String[] args) {
//
//        String filePath = "gbbct10.seq";
//        BufferedInputStream bufferedInputStream = null;
//        Scanner input = new Scanner(System.in);
//        System.out.println("Enter the number of characters: ");
//        int n = input.nextInt();
//        try {
//            bufferedInputStream = new BufferedInputStream(new FileInputStream(filePath));
//            byte[] buffer = new byte[1024 * 1024 * 200]; //read 1MB at a time
//            int bytesRead = 0;
//            int i = 1;
//            while (((bytesRead = bufferedInputStream.read(buffer)) != -1)) {
////                System.out.println(new String(buffer, 0, bytesRead));
//                printCodes(buffer, bytesRead, n);
//
//            }
//            printQueue();
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//
//    }
//
//    public static void printCodes(byte[] buffer, int bytesRead, int n) {
//
//
//        // usage of java.nio.ByteBuffer
//        //https://docs.oracle.com/javase/8/docs/api/java/nio/ByteBuffer.html
////    HashMap<Object, Integer> map = new HashMap<>();
//        // apply N-gram model construction according to this published paper i use sliding window approach
//        // https://link.springer.com/article/10.1007/s10791-024-09431-y?utm_source=chatgpt.com
//        // part 4.1  N‑gram model and N‑gram construction
//        if (n == 1) {
//            for (int i = 0; i < bytesRead; i++) {
//                ByteBuffer key = ByteBuffer.wrap(new byte[]{buffer[i]});
//                if (map.containsKey(key)) {
//                    map.put(key, map.get(key) + 1);
//                } else {
//                    map.put(key, 1);
//                }
//
//            }
//        } else if (n > 1) {
//
//            for (int i = 0; i <= bytesRead - n; i++) {
//                // Create a ByteBuffer for the current n-gram
//                ByteBuffer key = ByteBuffer.wrap(buffer, i, n);
//                map.put(key, map.getOrDefault(key, 0) + 1);
//            }
//
//
//        }
//
//
////    if(n==1) {
////        for (int i = 0; i < bytesRead; i++) {
////            char c = (char) buffer[i];
////            System.out.println(c);
////            if (map.containsKey(c)) {
////                map.put(c, map.get(c) + 1);
////            } else {
////                map.put(c, 1);
////            }
////
////        }
////    }
////    else if(n>1){
////        StringBuilder Ngram;
////
////        for (int i = 0; i < bytesRead; i++) {
////            Ngram = new StringBuilder();
////            for (int j = 0; j < n; j++) {
////                if(i+j<bytesRead) {
////                    Ngram.append((char) buffer[i + j]);
////                }
////            }
////            System.out.println(Ngram);
////            String s = Ngram.toString();
////            if (map.containsKey(s)) {
////                map.put(s, map.get(s) + 1);
////            } else {
////                map.put(s, 1);
////            }
////
////        }
////
////
////    }
//
//
////    // Max heap - to prioritize higher frequencies
////    PriorityQueue<HuffmanNode> MinHeap = new PriorityQueue<>(new Comparator<HuffmanNode>() {
////        @Override
////        public int compare(HuffmanNode node1, HuffmanNode node2) {
////            // MinHeap
////            return node1.getFrequency() - node2.getFrequency();
////        }
////    });
////
////    for (Object key : map.keySet()) {
////        HuffmanNode node = new HuffmanNode(key, map.get(key));
////        MinHeap.add(node);
////    }
////
////    // Store all nodes from the heap in a list
////    List<HuffmanNode> nodesList = new ArrayList<>();
////    while (!MinHeap.isEmpty()) {
////        nodesList.add(MinHeap.poll());
////    }
////
//////        // Sort the list based on frequency (ascending order)
//////        nodesList.sort(Comparator.comparingInt(HuffmanNode::getFrequency));
////
////    // Print the nodes in sorted order
////    for (HuffmanNode node : nodesList) {
////
////        System.out.println("Character: " + node.getData() + ", Frequency: " + node.getFrequency());
////   }
//
//    }
//
//    public static void printQueue() {
//
//        PriorityQueue<HuffmanNode> MinHeap = new PriorityQueue<>(new Comparator<HuffmanNode>() {
//            @Override
//            public int compare(HuffmanNode node1, HuffmanNode node2) {
//                return node1.getFrequency() - node2.getFrequency();
//            }
//        });
//
//        for (Object key : map.keySet()) {
//            HuffmanNode node = new HuffmanNode(key, map.get(key));
//            MinHeap.add(node);
//        }
//
//        // Store all nodes from the heap in a list
//        List<HuffmanNode> nodesList = new ArrayList<>();
//        while (!MinHeap.isEmpty()) {
//            nodesList.add(MinHeap.poll());
//        }
//
////        // Sort the list based on frequency (ascending order)
////        nodesList.sort(Comparator.comparingInt(HuffmanNode::getFrequency));
//
//        // Print the nodes in sorted order
//        for (HuffmanNode node : nodesList) {
//
//            System.out.println("Character: " + node.getData() + ", Frequency: " + node.getFrequency());
//        }
//
//
//    }
//}
//
////
////
////    if(n==1) {
////            for (int i = 0; i < bytesRead; i++) {
////        char c = (char) buffer[i];
////        System.out.println(c);
////        if (map.containsKey(c)) {
////        map.put(c, map.get(c) + 1);
////        } else {
////        map.put(c, 1);
////        }
////
////        }
////        }
////        else if(n>1){
////        StringBuilder Ngram;
////
////        for (int i = 0; i < bytesRead; i++) {
////        Ngram = new StringBuilder();
////        for (int j = 0; j < n; j++) {
////        if(i+j<bytesRead) {
////        Ngram.append((char) buffer[i + j]);
////        }
////        }
////        System.out.println(Ngram);
////        String s = Ngram.toString();
////        if (map.containsKey(s)) {
////        map.put(s, map.get(s) + 1);
////        } else {
////        map.put(s, 1);
////        }
////
////        }
////
////
////        }


//lasst
//
//public static void printQueue() {
//
//        PriorityQueue<HuffmanNode> MinHeap = new PriorityQueue<>(new Comparator<HuffmanNode>() {
//@Override
//public int compare(HuffmanNode node1, HuffmanNode node2) {
//        return node1.getFrequency() - node2.getFrequency();
//        }
//        });
//
//        for (Object key : map.keySet()) {
//        HuffmanNode node = new HuffmanNode(key, map.get(key));
//        MinHeap.add(node);
//        }
//
//        // Store all nodes from the heap in a list
//        List<HuffmanNode> nodesList = new ArrayList<>();
//        while (!MinHeap.isEmpty()) {
//        nodesList.add(MinHeap.poll());
//        }
//
////        // Sort the list based on frequency (ascending order)
////        nodesList.sort(Comparator.comparingInt(HuffmanNode::getFrequency));
//
//        // Print the nodes in sorted order
//        for (HuffmanNode node : nodesList) {
//
//        System.out.println("Character: " + node.getData() + ", Frequency: " + node.getFrequency());
//        }
//
//
//        }