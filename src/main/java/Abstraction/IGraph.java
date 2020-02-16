package Abstraction;

import Collection.Pair;
import Nodes.AbstractNode;
import Nodes.DirectedNode;
import Nodes.UndirectedNode;
import org.omg.PortableInterceptor.INACTIVE;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.*;

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
	 * Method to explore every nodes in a graph G
	 * @return an array of the time where every successor of the node were explored
	 */
	default Long[] explorerGrapheG() {
		Long startTime = System.currentTimeMillis();
		Set<Integer> atteint = new HashSet<>();
		Long[] result = new Long[this.toAdjacencyMatrix().length];
		// J'initialise le resultat avec la valeur max des long
		Arrays.fill(result, Long.MAX_VALUE);
		Pair<Set<Integer>, Long[]> coupleAtteinResult = new Pair<>(atteint, result);
		for (int i = 0; i < this.toAdjacencyMatrix().length ; i++) {
			// Si le sommet n'a pas déjà été exploré je l'explore
			if (!atteint.contains(i)) {
				coupleAtteinResult = explorerSommet(i, atteint, result);
				atteint = coupleAtteinResult.getLeft();
				result = coupleAtteinResult.getRight();
			}
		}
		Long endTime = result[0];
		for (int i = 1; i < result.length ; i++) {
			if (endTime < result[i])
				endTime = result[i];
		}
		System.out.println("L'exploration du graphe complémentaire à duré " + (endTime-startTime) + " nanosecondes");
		return result;
	}

	/**
	 * Method to explore every nodes in a graph G-1
	 * @param sortedFinNode, an array containing the node index by decreasing eplored time
	 * @return an array of the time where every successor of the node were explored
	 */
	default Long[] explorerGrapheComplG(int[] sortedFinNode) {
		Long startTime = System.nanoTime();
		Set<Integer> atteint = new HashSet<>();
		Long[] result = new Long[this.toAdjacencyMatrix().length];
		// J'initialise le resultat avec la valeur max des long
		Arrays.fill(result, Long.MAX_VALUE);
		Pair<Set<Integer>, Long[]> coupleAtteinResult = new Pair<>(atteint, result);
		for (int i = 0; i < this.toAdjacencyMatrix().length ; i++) {
			// Si le sommet n'a pas déjà été exploré je l'explore
			if (!atteint.contains(sortedFinNode[i])) {
				coupleAtteinResult = explorerSommet(sortedFinNode[i], atteint, result);
				atteint = coupleAtteinResult.getLeft();
				result = coupleAtteinResult.getRight();
			}
		}
		Long endTime = result[0];
		for (int i = 1; i < result.length ; i++) {
			if (endTime < result[i])
				endTime = result[i];
		}
		System.out.println("L'exploration du graphe complémentaire à duré " + (endTime-startTime) + " nanosecondes");
		// Le résultat retourne un tableau avec le temps final d'exploration
		// dans l'ordre croissant des indice des noeuds
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
			fin[s] = System.nanoTime();
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
