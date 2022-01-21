package hangman;

public class StructTreeNode {
    public boolean[] o;
    public int i;

    public StructTreeNode(int len, int i) {
        o = new boolean[len];
        this.i = i;
    }
    public StructTreeNode(boolean[] o, int i){
        this.o = o.clone();
        this.i = i;
    }
}
