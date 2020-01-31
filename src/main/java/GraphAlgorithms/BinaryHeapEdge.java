package GraphAlgorithms;

import java.util.ArrayList;
import java.util.List;

import Abstraction.IGraph;
import Collection.Triple;
import Nodes.AbstractNode;
import Nodes.DirectedNode;

public class BinaryHeapEdge<A> {

	/**
	 * A list structure for a faster management of the heap by indexing
	 * 
	 */
	private  List<Triple<A,A,Integer>> binh;

    public BinaryHeapEdge() {
        this.binh = new ArrayList<>();
    }

    public boolean isEmpty() {
        return binh.isEmpty();
    }

    /**
	 * Insert a new edge in the binary heap
	 * 
	 * @param from one node of the edge
	 * @param to one node of the edge
	 * @param val the edge weight
	 */
    public void insert(A from, A to, int val) {
    	Triple<A,A,Integer> element = new Triple(from, to, val);
		if (this.binh.size() == 0)
			this.binh.add(element);
		else if (this.binh.size() > 0) {
			this.binh.add(element);
			int i = this.binh.size()-1;
			while (i > 0) {
				int father = new Double(Math.floor((i - 1) / 2)).intValue();
				if (this.binh.get(father).getThird() > element.getThird()) {
					this.swap(father, i);
					i = father;
					element = this.binh.get(i);
				}
				else
					break;
			}
		}
    }

    
    /**
	 * Removes the root edge in the binary heap, and swap the edges to keep a valid binary heap
	 * 
	 * @return the edge with the minimal value (root of the binary heap)
	 * 
	 */
    public Triple<A,A,Integer> remove() throws Exception {
		if (this.binh.size() != 0) {
			Triple<A,A,Integer> res = this.binh.get(0);
			if (this.binh.size() == 1) {
				res = this.binh.remove(0);
			}
			else {
				this.swap(0, this.binh.size()-1);
				this.binh.remove(this.binh.size()-1);
				int currentNodeIndex = 0;
				while (currentNodeIndex < this.binh.size()-1) {
					int sonIndex = this.getBestChildPos(currentNodeIndex);
					if (sonIndex != Integer.MAX_VALUE) {
						Triple<A,A,Integer> sonValue = this.binh.get(sonIndex);
						if (this.binh.get(currentNodeIndex).getThird() > sonValue.getThird()) {
							this.swap(currentNodeIndex, sonIndex);
							currentNodeIndex = sonIndex;
						}
					} else
						break;
				}
			}
			return res;
		}
		throw new Exception("No element in the BinaryHeap.");
	}
    

    /**
	 * From an edge indexed by src, find the child having the least weight and return it
	 * 
	 * @param src an index of the list edges
	 * @return the index of the child edge with the least weight
	 */
    private int getBestChildPos(int src) {
    	int lastIndex = binh.size()-1;
		if (isLeaf(src)) { // the leaf is a stopping case, then we return a default value
			return Integer.MAX_VALUE;
		} else {
			if (2*src+1 < this.binh.size())
				return this.binh.get(2*src +1).getThird() < this.binh.get(2*src + 2).getThird() ?
						2*src + 1 :
						2*src + 2;
			else
				return this.binh.get(2*src+1).getThird();
		}
    }

    private boolean isLeaf(int src) {
		return 2*src+1 >= this.binh.size();
    }

    
    /**
	 * Swap two edges in the binary heap
	 * 
	 * @param father an index of the list edges
	 * @param child an index of the list edges
	 */
    private void swap(int father, int child) {         
    	Triple<A,A,Integer> temp = new Triple<>(binh.get(father).getFirst(), binh.get(father).getSecond(), binh.get(father).getThird());
    	binh.get(father).setTriple(binh.get(child));
    	binh.get(child).setTriple(temp);
    }


	/**
	 * Create the string of the visualisation of a binary heap
	 *
	 * @return the string of the binary heap
	 */
	public String toString() {
		StringBuilder s = new StringBuilder();
		for (Triple<A,A,Integer> no: binh) {
			s.append(no.getThird()).append(", ");
		}
		return s.toString();
	}

	/**
	 * Create the string of the visualisation of a binary heap
	 *
	 * @return the string of the binary heap
	 */
	public String toStringNode() {
		StringBuilder s = new StringBuilder();
		for (Triple<A,A,Integer> no: binh) {
			s.append(no).append(", ");
		}
		return s.toString();
	}


	private String space(int x) {
		StringBuilder res = new StringBuilder();
		for (int i=0; i<x; i++) {
			res.append(" ");
		}
		return res.toString();
	}

	public void algoPRIM(List<DirectedNode> nodes, List<Triple<DirectedNode, DirectedNode, Integer>> listEdge, int start) throws Exception {
		BinaryHeapEdge<A> tree = new BinaryHeapEdge<>();
		List<DirectedNode> restingNodes = nodes;
		int currentNodeIndex = start;
		do {
			for (int i = 0; i < listEdge.size(); i++) {
				if (listEdge.get(i).getFirst().equals(restingNodes.get(currentNodeIndex))) {
					Triple<A, A, Integer> edge = (Triple<A, A, Integer>) listEdge.get(i);
					tree.insert(edge.getFirst(), edge.getSecond(), edge.getThird());
				}
			}
			if (!tree.isEmpty()) {
				Triple<A, A, Integer> minimumEdge = tree.remove();
				restingNodes.remove(currentNodeIndex);
				if (listEdge.contains(minimumEdge.getSecond()))
					currentNodeIndex = listEdge.indexOf(minimumEdge.getSecond());
				this.insert(minimumEdge.getFirst(),minimumEdge.getSecond(),minimumEdge.getThird());
			}
		} while (!restingNodes.isEmpty());
	}
	
	/**
	 * Print a nice visualisation of the binary heap as a hierarchy tree
	 * 
	 */	
	public void lovelyPrinting(){
		int nodeWidth = this.binh.get(0).toString().length();
		int depth = 1+(int)(Math.log(this.binh.size())/Math.log(2));
		int index=0;
		
		for(int h = 1; h<=depth; h++){
			int left = ((int) (Math.pow(2, depth-h-1)))*nodeWidth - nodeWidth/2;
			int between = ((int) (Math.pow(2, depth-h))-1)*nodeWidth;
			int i =0;
			System.out.print(space(left));
			while(i<Math.pow(2, h-1) && index<binh.size()){
				System.out.print(binh.get(index) + space(between));
				index++;
				i++;
			}
			System.out.println("");
		}
		System.out.println("");
	}
	
	// ------------------------------------
    // 					TEST
	// ------------------------------------

	/**
	 * Recursive test to check the validity of the binary heap
	 * 
	 * @return a boolean equal to True if the binary tree is compact from left to right
	 * 
	 */
    private boolean test() {
        return this.isEmpty() || testRec(0);
    }

    private boolean testRec(int root) {
    	int lastIndex = binh.size()-1; 
        if (isLeaf(root)) {
            return true;
        } else {
            int left = 2 * root + 1;
            int right = 2 * root + 2;
            if (right >= lastIndex) {
                return binh.get(left).getThird() >= binh.get(root).getThird() && testRec(left);
            } else {
                return binh.get(left).getThird() >= binh.get(root).getThird() && testRec(left)
                    && binh.get(right).getThird() >= binh.get(root).getThird() && testRec(right);
            }
        }
    }

    public static void main(String[] args) throws Exception {
        BinaryHeapEdge<DirectedNode> jarjarBin = new BinaryHeapEdge<>();
        System.out.println(jarjarBin.isEmpty()+"\n");
        int k = 10;
        int m = k;
        int min = 2;
        int max = 20;
		while (k > 0) {
			int rand = min + (int) (Math.random() * ((max - min) + 1));
			System.out.print("insert " + rand + " | ");
			jarjarBin.insert(new DirectedNode(k), new DirectedNode(k+30), rand);
			k--;
		}
		System.out.println("");

		System.out.println(jarjarBin);
		jarjarBin.lovelyPrinting();

		jarjarBin.remove();
		jarjarBin.lovelyPrinting();
		k = 10;
		List<DirectedNode> jarjarBinList = new ArrayList<>();
		List<Triple<DirectedNode, DirectedNode, Integer>> jarjarBinListTriple = new ArrayList<>();
		while (k > 0) {
			int rand = min + (int) (Math.random() * ((max - min) + 1));
			System.out.print("insert " + rand + " | ");
			jarjarBinList.add(new DirectedNode(k));
			jarjarBinList.add(new DirectedNode(k));
			jarjarBinListTriple.add(new Triple<>(jarjarBinList.get(jarjarBinList.size()-2), jarjarBinList.get(jarjarBinList.size()-1), rand));
			k--;
		}
		BinaryHeapEdge<DirectedNode> lol = new BinaryHeapEdge<>();
		lol.algoPRIM(jarjarBinList, jarjarBinListTriple, 0);
		System.out.println("");

		System.out.println(lol);
		lol.lovelyPrinting();

		lol.remove();
		lol.lovelyPrinting();



		System.out.println(lol.test());
	}

}

