package GraphAlgorithms;

import java.lang.reflect.Array;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;

import Abstraction.IGraph;
import AdjacencyList.DirectedGraph;
import Collection.Pair;
import Nodes.AbstractNode;
import Nodes.DirectedNode;
import Nodes.UndirectedNode;

public class GraphToolsList extends GraphTools {

    private static int _DEBBUG = 0;

    private static int[] visite;
    private static int[] debut;
    private static int[] fin;
    private static List<Integer> order_CC;
    private static int cpt = 0;

    //--------------------------------------------------
    // 				Constructors
    //--------------------------------------------------

    public GraphToolsList() {
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
        for (int i = 0; i < n; i++)
            mark.add(false);
        mark.set(start.getLabel(), true);
        Stack<AbstractNode> toVisit = new Stack<AbstractNode>();
        toVisit.push(start);
        while (!toVisit.empty()) {
            AbstractNode v = toVisit.pop();
            ArrayList<AbstractNode> m = new ArrayList<>();
            if (v.getClass() == UndirectedNode.class) {
                for (Map.Entry<UndirectedNode, Integer> entry : ((UndirectedNode) v).getNeighbours().entrySet())
                    m.add(entry.getKey());
            } else if (v.getClass() == DirectedNode.class) {
                for (Map.Entry<DirectedNode, Integer> entry : ((DirectedNode) v).getSuccs().entrySet())
                    m.add(entry.getKey());
            } else
                throw new Exception("Unknow node type");
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
        for (int i = 0; i < n; i++)
            mark.add(false);
        mark.set(start.getLabel(), true);
        ConcurrentLinkedQueue<AbstractNode> toVisit = new ConcurrentLinkedQueue<>();
        toVisit.add(start);
        while (!toVisit.isEmpty()) {
            AbstractNode v = toVisit.remove();
            ArrayList<AbstractNode> m = new ArrayList<>();
            if (v.getClass() == UndirectedNode.class) {
                for (Map.Entry<UndirectedNode, Integer> entry : ((UndirectedNode) v).getNeighbours().entrySet())
                    m.add(entry.getKey());
            } else if (v.getClass() == DirectedNode.class) {
                for (Map.Entry<DirectedNode, Integer> entry : ((DirectedNode) v).getSuccs().entrySet())
                    m.add(entry.getKey());
            } else {
                throw new Exception("Unknow node type");
            }
            for (AbstractNode node : m) {
                if (!mark.get(node.getLabel())) {
                    mark.set(node.getLabel(), true);
                    toVisit.add(node);
                }
            }
        }
        return mark;
    }

    public static IGraph inversionGraphe(IGraph graph) {
        for (int i = 0; i < graph.toAdjacencyMatrix().length; i++)
            for (int j = 0; j < graph.toAdjacencyMatrix()[i].length; j++)
                graph.toAdjacencyMatrix()[i][j] = graph.toAdjacencyMatrix()[i][j] != 0 ? 0 : 1;
        return graph;
    }


    public static void main(String[] args) throws Exception {
        int[][] Matrix = GraphTools.generateGraphData(10, 20, false, false, true, 100001);
        GraphTools.afficherMatrix(Matrix);
        DirectedGraph<DirectedNode> al = new DirectedGraph<>(Matrix);
        System.out.println(al);

        int node = 1;
        System.out.println(System.lineSeparator() + "Question 20");
        System.out.println(System.lineSeparator() + "Parours en profondeur du Graphe depuis le sommet " + node);
        ArrayList<Boolean> bl = parcourProfond(al, al.getNodeOfList(al.makeNode(node)));
        for (int i = 0; i < bl.size(); i++) {
            System.out.print(i);
            System.out.print(" ");
            System.out.println(bl.get(i) ? "Parcouru" : "Non parcouru");
        }

        System.out.println(System.lineSeparator() + "Question 21");
        System.out.println(System.lineSeparator() + "Parours en largeur du Graphe depuis le sommet " + node);
        bl = parcourEnLargeur(al, al.getNodeOfList(al.makeNode(node)));
        for (int i = 0; i < bl.size(); i++) {
            System.out.print(i);
            System.out.print(" ");
            System.out.println(bl.get(i) ? "Parcouru" : "Non parcouru");
        }

        System.out.println(System.lineSeparator() + "Question 22");
        System.out.println("\t 1)");
        System.out.println(System.lineSeparator() + "exploration du graphe");
        Long[] fin = al.explorerGrapheG();
        for (int i = 0; i < fin.length ; i++) {
            System.out.println("fin[" + i + "] => " + fin[i]);
        }

        System.out.println(System.lineSeparator() + "Question 22");
        System.out.println("\t 2)");
        System.out.println(System.lineSeparator() + "inversion du graphe");
        al = (DirectedGraph<DirectedNode>) al.computeInverse();
        System.out.println(al);

        // je crée un tableau contenant l'ordre des noeuds de fin dans l'ordre décroissant
        int[] sortedFinNode = new int[fin.length];
        for (int i = 0; i < fin.length ; i++) {
            int max = i;
            for (int j = 0; j < fin.length ; j++) {
                if (j != i)
                    if (fin[j] > fin[i])
                        max = j;
            }
            sortedFinNode[i] = max;
            fin[max] = Long.MIN_VALUE;
        }

        System.out.println(System.lineSeparator() + "Question 22");
        System.out.println("\t 3)");
        System.out.println(System.lineSeparator() + "exploration du graphe complémentaire");
        Long[] finComplG = al.explorerGrapheComplG(sortedFinNode);
        for (int i = 0; i < fin.length ; i++) {
            System.out.println("sortedFin[" + i + "] => " + finComplG[i]);
        }


        System.out.println(System.lineSeparator() + "Question 23");
        System.out.println("On constate que l'exploration du graphe complémentaire est plus rapide.");
    }
}
