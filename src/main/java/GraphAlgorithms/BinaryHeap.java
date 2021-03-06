package GraphAlgorithms;


public class BinaryHeap {

    private int[] nodes;
    private int pos;

    public BinaryHeap() {
        this.nodes = new int[32];
        for (int i = 0; i < nodes.length; i++) {
            this.nodes[i] = Integer.MAX_VALUE;
        }
        this.pos = 0;
    }

    public void resize() {
        int[] tab = new int[this.nodes.length + 32];
        for (int i = 0; i < nodes.length; i++) {
            tab[i] = Integer.MAX_VALUE;
        }
        System.arraycopy(this.nodes, 0, tab, 0, this.nodes.length);
        this.nodes = tab;
    }

    public boolean isEmpty() {
        return pos == 0;
    }

    public void insert(int element) {
        if (this.pos == 0)
            this.nodes[pos ++] = element;
        else if (this.pos < this.nodes.length) {
            this.nodes[this.pos] = element;
            int i = pos;
            while (i > 0) {
                int father = new Double(Math.floor((i - 1) / 2)).intValue();
                if (this.nodes[father] > element) {
                    this.swap(father, i);
                    i = father;
                }
                else
                    break;
            }
            pos ++;
        }
    }

    public int remove() throws Exception {
        if (this.pos != 0) {
            int res = this.nodes[0];
            if (this.pos == 1) {
                this.nodes = new int[32];
                for (int i = 0; i < nodes.length; i++) {
                    this.nodes[i] = Integer.MAX_VALUE;
                }
                this.pos = 0;
            }
            else {
                this.swap(0, --this.pos);
                this.nodes[pos] = Integer.MAX_VALUE;
                int currentNode = 0;
                while (currentNode < this.pos) {
                    int sonIndex = this.getBestChildPos(currentNode);
                    if (sonIndex != Integer.MAX_VALUE) {
                        int sonValue = this.nodes[sonIndex];
                        if (this.nodes[currentNode] > sonValue) {
                            this.swap(currentNode, sonIndex);
                            currentNode = sonIndex;
                        }
                    } else
                        break;
                }
            }
            return res;
        }
        throw new Exception("No element in the BinaryHeap.");
    }

    private int getBestChildPos(int src) {
        if (isLeaf(src)) { // the leaf is a stopping case, then we return a default value
            return Integer.MAX_VALUE;
        } else {
            return this.nodes[2*src +1] < this.nodes[2*src + 2] ?
                    2*src + 1 :
                    2*src + 2;
        }
    }

    
    /**
	 * Test if the node is a leaf in the binary heap
	 * 
	 * @returns true if it's a leaf or false else
	 * 
	 */	
    private boolean isLeaf(int src) {
        return 2*src+1 >= this.pos;
    }

    private void swap(int father, int child) {
        int temp = nodes[father];
        nodes[father] = nodes[child];
        nodes[child] = temp;
    }

    public String toStringTree() {

        StringBuilder s = new StringBuilder();
        for (int i = 0; i < pos; i++) {
            if (i == 0 || i == 1 || i == 3 || i == 7 || i == 15 || i == 31)
                s.append(("\n"));
            s.append(nodes[i]).append(", ");
        }
        return s.toString();
    }
    public String toString() {

        StringBuilder s = new StringBuilder();
        for (int i = 0; i < pos; i++) {
            s.append(nodes[i]).append(", ");
        }
        return s.toString();
    }
    public String tab() {
        StringBuilder s = new StringBuilder();
        for (int i = 0; i < pos; i++) {
        s.append(i).append("\t\t| ");
    }
        s.append("\n");
        for (int i = 0; i < pos; i++) {
        s.append(nodes[i]).append("\t\t, ");
    }
        return s.toString();

}

    /**
	 * Recursive test to check the validity of the binary heap
	 * 
	 * @returns a boolean equal to True if the binary tree is compact from left to right
	 * 
	 */
    public boolean test() {
        return this.isEmpty() || testRec(0);
    }

    private boolean testRec(int root) {
        if (isLeaf(root)) {
            return true;
        } else {
            int left = 2 * root + 1;
            int right = 2 * root + 2;
            if (right >= pos) {
                return nodes[left] >= nodes[root] && testRec(left);
            } else {
                return nodes[left] >= nodes[root] && testRec(left) && nodes[right] >= nodes[root] && testRec(right);
            }
        }
    }

    public static void main(String[] args) throws Exception {
        BinaryHeap jarjarBin = new BinaryHeap();
        System.out.println("jarjarBin is empty ? : ");
        System.out.println(jarjarBin.isEmpty()+"\n");
        int k = 20;
        int m = k;
        int min = 2;
        int max = 20;
        while (k > 0) {
            int rand = min + (int) (Math.random() * ((max - min) + 1));
            System.out.print("insert " + rand + " | ");
            jarjarBin.insert(rand);            
            k--;
        }
     // A completer
        System.out.println("\n" + jarjarBin.toStringTree());

        jarjarBin.remove();
        System.out.println("\n" + jarjarBin.toStringTree());

        System.out.println("jarjarBin test : ");
        System.out.println(jarjarBin.test());
    }

}
