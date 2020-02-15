package Abstraction;

import Collection.Pair;
import Nodes.AbstractNode;
import Nodes.DirectedNode;
import Nodes.UndirectedNode;
import org.omg.PortableInterceptor.INACTIVE;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.Timer;

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
     * Method to explore every nodes
	 * @return an array of the time where every successor of the node were explored
     */
    default Long[] explorerGraphe() {
    	Set<Integer> atteint = new HashSet<>();
		Long[] result = new Long[this.toAdjacencyMatrix().length];
		// J'initialise le resultat avec la valeur max des long
		for (int i = 0; i < result.length ; i++) {
			result[i] = Long.MAX_VALUE;
		}
		Pair<Set<Integer>, Long[]> coupleAtteinResult = new Pair<>(atteint, result);
		for (int i = 0; i < this.toAdjacencyMatrix().length ; i++) {
			// Si le sommet n'a pas déjà été exploré je l'explore
			if (!atteint.contains(i)) {
				coupleAtteinResult = explorerSommet(i, atteint, result);
				atteint = coupleAtteinResult.getLeft();
				result = coupleAtteinResult.getRight();
			}
		}
		return result;
    }

	/**
	 * Method to explore a node
	 * @param s, a node index in the adjencyMatrix
	 * @param a, a Set containing all previously explored nodes
	 * @return a Pair of a set containing every node already explored and an array of the time when every successor
	 * of a node was explored
	 */
	default Pair<Set<Integer>, Long[]> explorerSommet(Integer s, Set<Integer>  a, Long[] fin) {
		// J'ajoute le sommet à la liste des sommet exploré
		a.add(s);
		// J'initialise le resultat
		Pair<Set<Integer>, Long[]> result = new Pair<>(a, fin);
		for (int i = 0; i < this.toAdjacencyMatrix()[s].length; i++) {
			// Si la valeur de matrice d'ajacence est supérieur à 0 alors il y a un arc
			if (this.toAdjacencyMatrix()[s][i] > 0)
				// Si le sommet courant n'a pas déjà était exploré, je l'exlore
				if (!a.contains(i)) {
					result = explorerSommet(i, a, fin);
				}
		}
		// Tous les successeurs de ce sommet ont été exploré, je peux donc noté ce moment à partir du temps système
		if (fin[s] == Long.MAX_VALUE) {
			fin[s] = System.currentTimeMillis();
			// Je fais attendre le programme pour bien différencier les temps entre les sommets exploré
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	/**
	 * Method to get the adjency matrix of the complement graph
	 */
	default int[][] complGraphAdjMatrix() {
		int[][] result = new int[this.toAdjacencyMatrix().length][this.toAdjacencyMatrix().length];;
		for (int i = 0; i < this.toAdjacencyMatrix().length; i++)
			for (int j = 0; j < this.toAdjacencyMatrix()[i].length; j++)
				result[i][j] = this.toAdjacencyMatrix()[i][j] != 0 ? 0 : 1;
		return result;
	}
}
