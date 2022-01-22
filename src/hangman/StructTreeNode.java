package hangman;

public class StructTreeNode {
    public boolean[] o;
    public int i;

    public StructTreeNode(boolean[] o, int i){
        this.o = o.clone();
        this.i = i;
    }
}
