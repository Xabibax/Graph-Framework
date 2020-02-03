package GraphAlgorithms;

import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.function.BiConsumer;

import Abstraction.IGraph;
import AdjacencyList.DirectedGraph;
import AdjacencyList.DirectedValuedGraph;
import AdjacencyList.UndirectedValuedGraph;
import Collection.Triple;
import Nodes.AbstractNode;
import Nodes.DirectedNode;
import Nodes.UndirectedNode;

public class GraphToolsList  extends GraphTools {

	private static int _DEBBUG =0;

	private static int[] visite;
	private static int[] debut;
	private static int[] fin;
	private static List<Integer> order_CC;
	private static int cpt=0;

	//--------------------------------------------------
	// 				Constructors
	//--------------------------------------------------

	public GraphToolsList(){
		super();
	}

	// ------------------------------------------
	// 				Accessors
	// ------------------------------------------



	// ------------------------------------------
	// 				Methods
	// ------------------------------------------

	/**
	 * @param graph, a graph
	 * @param start, start node
	 */
	public static ArrayList<Boolean> parcourProfond(IGraph graph, AbstractNode start) throws Exception {
		int n = graph.getNbNodes();
		ArrayList<Boolean> mark = new ArrayList<>();
		for (int i = 0; i < n; i++) {
			mark.add(false);
		}
		mark.set(start.getLabel(), true);
		Stack<AbstractNode> toVisit = new Stack<AbstractNode>();
		toVisit.push(start);
		while (!toVisit.empty()) {
			AbstractNode v = toVisit.pop();
			ArrayList<AbstractNode> m = new ArrayList<>();
			if (v.getClass() == UndirectedNode.class) {
				for (Map.Entry<UndirectedNode, Integer> entry : ((UndirectedNode) v).getNeighbours().entrySet()) {
					m.add(entry.getKey());
				}
			}else if (v.getClass() == DirectedNode.class)  {
				for (Map.Entry<DirectedNode, Integer> entry : ((DirectedNode) v).getSuccs().entrySet()) {
					m.add(entry.getKey());
				}
			} else {
				throw new Exception("Unknow node type");
			}
			m.forEach((node) -> {
				if (!mark.get(node.getLabel())) {
					mark.set(node.getLabel(), true);
					toVisit.push(node);
				}
			});
		}
		return mark;
	}
	/**
	 * @param graph, a graph
	 * @param start, start node
	 */
	public static ArrayList<Boolean> parcourEnLargeur(IGraph graph, AbstractNode start) throws Exception {
		int n = graph.getNbNodes();
		ArrayList<Boolean> mark = new ArrayList<>();
		for (int i = 0; i < n; i++) {
			mark.add(false);
		}
		mark.set(start.getLabel(), true);
		ConcurrentLinkedQueue<AbstractNode> toVisit = new ConcurrentLinkedQueue<>();
		toVisit.add(start);
		while (!toVisit.isEmpty()) {
			AbstractNode v = toVisit.remove();
			ArrayList<AbstractNode> m = new ArrayList<>();
			if (v.getClass() == UndirectedNode.class) {
				for (Map.Entry<UndirectedNode, Integer> entry : ((UndirectedNode) v).getNeighbours().entrySet()) {
					m.add(entry.getKey());
				}
			}else if (v.getClass() == DirectedNode.class) {
				for (Map.Entry<DirectedNode, Integer> entry : ((DirectedNode) v).getSuccs().entrySet()) {
					m.add(entry.getKey());
				}
			} else {
				throw new Exception("Unknow node type");
			}
			for (AbstractNode node: m) {
				if (!mark.get(node.getLabel())) {
					mark.set(node.getLabel(), true);
					toVisit.add(node);
				}
			}
		}
		return mark;
	}

	public static void main(String[] args) throws Exception {
		int[][] Matrix = GraphTools.generateGraphData(10, 20, false, false, true, 100001);
		GraphTools.afficherMatrix(Matrix);
		DirectedGraph<DirectedNode> al = new DirectedGraph<>(Matrix);
		System.out.println(al);

		for (Boolean aBoolean : parcourProfond(al, al.getNodeOfList(al.makeNode(1)))) {
			System.out.print(parcourProfond(al, al.getNodeOfList(al.makeNode(1))).indexOf(aBoolean));
			System.out.print(" ");
			System.out.println(aBoolean);
		}
		for (Boolean r : parcourEnLargeur(al, al.getNodeOfList(al.makeNode(1)))) {
			System.out.print(parcourProfond(al, al.getNodeOfList(al.makeNode(1))).indexOf(r));
			System.out.print(" ");
			System.out.println(r);
		}

	}
}
