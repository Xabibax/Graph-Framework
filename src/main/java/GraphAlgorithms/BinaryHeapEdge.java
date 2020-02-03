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
				res = this.binh.remove(this.binh.size()-1);
				int currentNodeIndex = 0;
				while (currentNodeIndex < this.binh.size()-1) {
					int sonIndex = this.getBestChildPos(currentNodeIndex);
					if (sonIndex != Integer.MAX_VALUE) {
						Triple<A,A,Integer> sonValue = this.binh.get(sonIndex);
						if (this.binh.get(currentNodeIndex).getThird() > sonValue.getThird()) {
							this.swap(currentNodeIndex, sonIndex);
							currentNodeIndex = sonIndex;
						} else
							break;
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
			if (2*src+2 < this.binh.size())
				return this.binh.get(2*src +1).getThird() < this.binh.get(2*src + 2).getThird() ?
						2*src + 1 :
						2*src + 2;
			else
				return 2*src+1;
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

	/**
	 * Transform this BinaryHeapEdge with the PRIM algorithm
	 *
	 * @param nodes the list of all the nodes in the graph
	 * @param listEdge the list of all the edges in the graph
	 * @param start the index of the starting node in nodes
	 */
	public void algoPRIM(List<A> nodes, List<Triple<A, A, Integer>> listEdge, int start) throws Exception {
		// I create a tree to stock my Edges //
		BinaryHeapEdge<A> tree = new BinaryHeapEdge<>();

		// I add every edge from the current node into the tree
		for (int i = 0; i < listEdge.size(); i++) {
			if (listEdge.get(i).getFirst().equals(nodes.get(start))) {
				Triple<A, A, Integer> edge = (Triple<A, A, Integer>) listEdge.get(i);
				tree.insert(edge.getFirst(), edge.getSecond(), edge.getThird());
			}
		}

		// I remove the starting node from the list of nodes
		nodes.remove(nodes.get(start));

		while (!nodes.isEmpty()) {
			// I retrieve the minumum edge from the tree
			if (!tree.binh.isEmpty()) {
				Triple<A, A, Integer> minimumEdge = tree.remove();
				if (nodes.contains(minimumEdge.getSecond())) {
					// I add every edge of the next node
					for (int i = 0; i < listEdge.size(); i++) {
						if (listEdge.get(i).getFirst().equals(minimumEdge.getSecond())) {
							Triple<A, A, Integer> edge = (Triple<A, A, Integer>) listEdge.get(i);
							tree.insert(edge.getFirst(), edge.getSecond(), edge.getThird());
						}
					}
					// I remove the next node from the list of nodes
					nodes.remove(minimumEdge.getSecond());
				}
				// I insert the minimum Edge to this BinaryHeapEdge
				this.insert(minimumEdge.getFirst(),minimumEdge.getSecond(),minimumEdge.getThird());
			} else {
				break;
			}
		}
	}
	
	/**
	 * Print a nice visualisation of the binary heap as a hierarchy tree
	 * 
	 */	
	public void lovelyPrinting(){
		if (!this.binh.isEmpty()) {
			int nodeWidth = this.binh.get(0).toString().length();
			int depth = 1 + (int) (Math.log(this.binh.size()) / Math.log(2));
			int index = 0;

			for (int h = 1; h <= depth; h++) {
				int left = ((int) (Math.pow(2, depth - h - 1))) * nodeWidth - nodeWidth / 2;
				int between = ((int) (Math.pow(2, depth - h)) - 1) * nodeWidth;
				int i = 0;
				System.out.print(space(left));
				while (i < Math.pow(2, h - 1) && index < binh.size()) {
					System.out.print(binh.get(index) + space(between));
					index++;
					i++;
				}
				System.out.println("");
			}
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

		List<DirectedNode> jarjarBinList = new ArrayList<>();
		List<Triple<DirectedNode, DirectedNode, Integer>> jarjarBinListTriple = new ArrayList<>();

		jarjarBinList.add(new DirectedNode(0));
		jarjarBinList.add(new DirectedNode(1));
		jarjarBinList.add(new DirectedNode(2));
		jarjarBinList.add(new DirectedNode(3));
		jarjarBinList.add(new DirectedNode(4));
		jarjarBinListTriple.add(new Triple<>(jarjarBinList.get(0), jarjarBinList.get(1), 3));
		jarjarBinListTriple.add(new Triple<>(jarjarBinList.get(0), jarjarBinList.get(2), 1));
		jarjarBinListTriple.add(new Triple<>(jarjarBinList.get(1), jarjarBinList.get(3), 1));
		jarjarBinListTriple.add(new Triple<>(jarjarBinList.get(2), jarjarBinList.get(1), 1));
		jarjarBinListTriple.add(new Triple<>(jarjarBinList.get(2), jarjarBinList.get(3), 5));
		jarjarBinListTriple.add(new Triple<>(jarjarBinList.get(2), jarjarBinList.get(4), 4));
		jarjarBinListTriple.add(new Triple<>(jarjarBinList.get(3), jarjarBinList.get(4), 4));

		BinaryHeapEdge<DirectedNode> lol = new BinaryHeapEdge<>();
		lol.algoPRIM(jarjarBinList, jarjarBinListTriple, 0);
		System.out.println("");

		System.out.println(lol);
		lol.lovelyPrinting();

		lol.lovelyPrinting();



		System.out.println(lol.test());
	}

}

