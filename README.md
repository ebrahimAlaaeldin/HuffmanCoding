# 🗜️ Huffman Coding Compressor

[![Java](https://img.shields.io/badge/Java-17-blue?style=for-the-badge&logo=java)](https://www.oracle.com/java/)
[![License](https://img.shields.io/badge/License-MIT-yellow?style=for-the-badge)](./LICENSE)

A simple and efficient implementation of **Huffman Coding** for file compression and decompression using Java.

---

## 📦 Features

- ✅ Compress any file using Huffman Coding  
- ✅ Decompress `.huff` compressed files  
- ✅ Supports all file types (text, binary, etc.)  
- ✅ Displays original vs. compressed size  
- ✅ CLI-based usage with clean Java code

---

## 🛠️ Tech Stack

- **Language:** Java 17  
- **IDE:** IntelliJ IDEA / Eclipse  
- **Build Tool:** Maven (optional)

---

## 🚀 Getting Started

### 1️⃣ Clone the Repository

```bash
git clone https://github.com/ebrahimAlaaeldin/HuffmanCoding.git
cd HuffmanCoding
```

### 2️⃣ Compile the Code

```bash
javac Huffman.java
```

### 3️⃣ Run the Application

```bash
java Huffman compress input.txt output.huff
java Huffman decompress output.huff recovered.txt
```

---

## 📂 Example

```bash
# Compress a file
java Huffman compress myfile.txt myfile.huff

# Decompress it
java Huffman decompress myfile.huff restored.txt
```

---

## 📁 Project Structure

```
HuffmanCoding/
├── Huffman.java            # Main class for compression/decompression
├── README.md               # Project documentation
└── LICENSE                 # MIT License
```

---

## 🧠 How Huffman Coding Works

1. Calculate frequency of each character in the input  
2. Build a priority queue to create the Huffman Tree  
3. Generate binary codes for each character (shorter codes for frequent chars)  
4. Encode the file content into a binary stream  
5. Store the tree + encoded content to allow decompression

---

## 📄 License

This project is licensed under the MIT License. See the [LICENSE](./LICENSE) file for details.

---

## 🤝 Contributing

Pull requests are welcome! For major changes, please open an issue first to discuss.

---

## 👤 Author

**Ibrahim Alaaeldin**  
📂 GitHub: [@ebrahimAlaaeldin](https://github.com/ebrahimAlaaeldin)

---

*Let me know if you want to include a GUI or test files section later!*
