package Abstraction;

import Collection.Pair;
import Nodes.AbstractNode;
import Nodes.DirectedNode;
import Nodes.UndirectedNode;
import org.omg.PortableInterceptor.INACTIVE;

import java.util.HashSet;
import java.util.Set;

public interface IGraph {
    /**
     * @return the number of nodes in the graph (referred to as the order of the graph)
     */
    int getNbNodes();

    /**
     * @return the adjacency matrix representation int[][] of the graph
     */
    int[][] toAdjacencyMatrix();

    /**
     * Method to explore every nodes of the graph
     */
    default void explorerGraphe() {
    	Set<Integer> atteint = new HashSet<>();
		for (int i = 0; i < this.toAdjacencyMatrix().length ; i++) {
				if (!atteint.contains(i))
					explorerSommet(i, atteint);
		}
        System.out.println(atteint);
    }

	/**
	 * Method to explore a node and
	 * @param s, a node index in the adjencyMatrix
	 * @param a, a Set containing all previously explored nodes
	 */
	default void explorerSommet(Integer s, Set<Integer> a) {
		a.add(s);
		for (int i = 0; i < this.toAdjacencyMatrix()[s].length; i++) {
			if (this.toAdjacencyMatrix()[s][i] > 0)
				if (!a.contains(i))
					explorerSommet(i, a);
		}
	}
}
