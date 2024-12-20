public class HuffmanNode {

    private int frequency;
    private HuffmanNode left;
    private HuffmanNode right;

    public HuffmanNode getLeft() {
        return left;
    }

    public void setFrequency(int frequency) {
        this.frequency = frequency;
    }

    public void setLeft(HuffmanNode left) {
        this.left = left;
    }

    public void setRight(HuffmanNode right) {
        this.right = right;
    }

    public void setData(Byte data) {
        this.data = data;
    }

    public HuffmanNode getRight() {
        return right;
    }

    private Byte data;

    public HuffmanNode(int frequency){
        this.frequency = frequency;
    }
    public HuffmanNode(Byte data,int frequency){
        this.frequency = frequency;
        this.data = data;
    }
    public int  getFrequency(){
        return this.frequency;
    }
    public Object getdata(){
        return this.data;
    }
    public void incrementfrequency(){
        this.frequency++;
    }


    public Byte getData() {
        return data;
    }
}
