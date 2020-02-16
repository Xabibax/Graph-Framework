package GraphAlgorithms;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;

import Abstraction.IGraph;
import AdjacencyList.DirectedGraph;
import Nodes.AbstractNode;
import Nodes.DirectedNode;
import Nodes.UndirectedNode;
import Collection.*;

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

    public static Map<Integer, Triple<DirectedNode, DirectedNode, Integer>> algoDijkstra(DirectedGraph<DirectedNode> graph, DirectedNode start) {
        Map<Integer, DirectedNode> nodesSettled = new HashMap<>(),
                                 nodesUnsettled = new HashMap<>();
        // Le résultat est une map qui contiendra en clé le label du noeud et un triple de :
        // - Le noeud en question
        // - Son prédecesseur
        // - La valeur du chemin vers ce noeud
        Map<Integer, Triple<DirectedNode, DirectedNode, Integer>> result = new HashMap<>();
        for (DirectedNode directedNode : graph.getNodes()) {
            result.put(directedNode.getLabel(), new Triple<>(directedNode, null, Integer.MAX_VALUE));
        }
        nodesUnsettled.put(start.getLabel(), start);
        DirectedNode currentNode = start;
        result.replace(start.getLabel(), new Triple<>(start, start, 0));
        while (!nodesUnsettled.isEmpty()) {
            DirectedNode finalCurrentNode = currentNode;
            currentNode.getSuccs().forEach((node, pathValue) -> {
                if (!nodesSettled.containsKey(node.getLabel())) {
                    nodesUnsettled.put(node.getLabel(), node);
                    if (result.get(node.getLabel()).getThird() > result.get(finalCurrentNode.getLabel()).getThird() + pathValue)
                        result.replace(node.getLabel(), new Triple<>(node, finalCurrentNode, result.get(finalCurrentNode.getLabel()).getThird() + pathValue));
                }
            });
            nodesSettled.put(currentNode.getLabel(), currentNode);
            nodesUnsettled.remove(currentNode.getLabel());
            final DirectedNode[] min = new DirectedNode[1];
            result.forEach((nodeLabel, tripleNode_PathValue) -> {
                if (nodesUnsettled.containsKey(nodeLabel))
                    if (min[0] == null)
                        min[0] = tripleNode_PathValue.getFirst();
                    else
                        if (min[0].getWeight() > tripleNode_PathValue.getThird())
                        min[0] = tripleNode_PathValue.getFirst();
            });
            currentNode = min[0];
        }

        return result;
    }

    /**
     * Method to explore every nodes in a graph G
     * @return an array of the time where every successor of the node were explored
     */
    public static int[] explorerGrapheG(IGraph graph) {
        int startTime =  (int)(System.nanoTime());
        Set<Integer> atteint = new HashSet<>();
        int[] result = new int[graph.toAdjacencyMatrix().length];
        // J'initialise le resultat avec la valeur max des long
        Arrays.fill(result, Integer.MAX_VALUE);
        Triple<Integer, Set<Integer>, int[]> coupleAtteinResult;
        for (int i = 0; i < graph.toAdjacencyMatrix().length ; i++) {
            // Si le sommet n'a pas déjà été exploré je l'explore
            if (!atteint.contains(i)) {
                coupleAtteinResult = explorerSommet(graph, new Triple<>(i, atteint, result));
                atteint = coupleAtteinResult.getSecond();
                result = coupleAtteinResult.getThird();
            }
        }
        int endTime = result[0];
        for (int i = 1; i < result.length ; i++) {
            if (endTime < result[i])
                endTime = result[i];
        }
        System.out.println("L'exploration du graphe complémentaire à duré " + (endTime-startTime) + " nanosecondes");
        return result;
    }

    /**
     * 905351600
     * 60684588109219
     * Method to explore every nodes in a graph G-1
     * @param sortedFinNode, an array containing the node index by decreasing eplored time
     * @return an array of the time where every successor of the node were explored
     */
    public static int[] explorerGrapheComplG(IGraph graph, int[] sortedFinNode) {
        int startTime =  (int)(System.nanoTime());
        Set<Integer> atteint = new HashSet<>();
        int[] result = new int[graph.toAdjacencyMatrix().length];
        // J'initialise le resultat avec la valeur max des long
        Arrays.fill(result, Integer.MAX_VALUE);
        Triple<Integer, Set<Integer>, int[]> coupleAtteinResult;
        for (int i = 0; i < graph.toAdjacencyMatrix().length ; i++) {
            // Si le sommet n'a pas déjà été exploré je l'explore
            if (!atteint.contains(sortedFinNode[i])) {
                coupleAtteinResult = explorerSommet(graph, new Triple<>(sortedFinNode[i], atteint, result));
                atteint = coupleAtteinResult.getSecond();
                result = coupleAtteinResult.getThird();
            }
        }
        int endTime = result[0];
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
     * @param graph, the graph where the exploration takes place
     * @param params, a Triple containing the node to start the exploration, a set of explored nodes and a int array
     *                of the time when a node and its successor have been explored
     * @return a Pair of a set containing every node already explored and an array of the time when every successor
     *         of a node was explored
     */
    public static Triple<Integer, Set<Integer>, int[]> explorerSommet(IGraph graph, Triple<Integer, Set<Integer>, int[]> params) {
        int sommet = params.getFirst();
        Set<Integer> atteint = params.getSecond();
        int[] fin = params.getThird();
        // J'ajoute le sommet à la liste des sommets explorés
        atteint.add(sommet);
        // J'initialise le resultat
        Triple<Integer, Set<Integer>, int[]> result = new Triple<>(sommet, atteint, fin);
        for (int i = 0; i < graph.toAdjacencyMatrix()[sommet].length; i++) {
            // Si la valeur de matrice d'ajacence est supérieur à 0 alors il y a un arc
            if (graph.toAdjacencyMatrix()[sommet][i] > 0)
                // Si le sommet courant n'a pas déjà était exploré, je l'exlore
                if (!atteint.contains(i)) {
                    params = new Triple<>(i, atteint, fin);
                    result = explorerSommet(graph, params);
                }
        }
        // Tous les successeurs de ce sommet ont été exploré, je peux donc noté ce moment à partir du temps système
        if (fin[sommet] == Integer.MAX_VALUE) {
            fin[sommet] = (int)(System.nanoTime());
            // Je fais attendre le programme pour bien différencier les temps entre les sommets exploré
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return result;
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
        int[] fin = explorerGrapheG(al);
        for (int i = 0; i < fin.length ; i++) {
            System.out.println("fin[" + i + "] => " + fin[i]);
        }

        System.out.println(System.lineSeparator() + "Question 22");
        System.out.println("\t 2)");
        System.out.println(System.lineSeparator() + "inversion du graphe");
        DirectedGraph<DirectedNode> alCompl = new DirectedGraph<>(al);
        alCompl.computeInverse();
        System.out.println(alCompl);

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
            fin[max] = Integer.MIN_VALUE;
        }

        System.out.println(System.lineSeparator() + "Question 22");
        System.out.println("\t 3)");
        System.out.println(System.lineSeparator() + "exploration du graphe complémentaire");
        int[] finComplG = explorerGrapheComplG(alCompl, sortedFinNode);
        for (int i = 0; i < fin.length ; i++) {
            System.out.println("sortedFin[" + i + "] => " + finComplG[i]);
        }


        System.out.println(System.lineSeparator() + "Question 23");
        System.out.println("On constate que l'exploration du graphe complémentaire est plus rapide.");

        System.out.println(System.lineSeparator() + "Algorithme de dijkstra");
        // Le graphe d'origine a des chemins entre les noeuds de zéro
        // Pour que l'algo de Dijkstra ai du sens j'inverse deux fois le graphe pour retrouver les mêmes arc
        // mais avec un valeur de 1
        al.computeInverse();
        al.computeInverse();
        Map<Integer, Triple<DirectedNode, DirectedNode, Integer>> ad = GraphToolsList.algoDijkstra(al, al.getNodes().get(0));
        ad.forEach((label, tripleNode_PathValue) -> {
            System.out.println(tripleNode_PathValue.getFirst().getLabel() + "  " +
                    (tripleNode_PathValue.getSecond() == null ? "_" :  tripleNode_PathValue.getSecond().getLabel()) + "  " +
                    tripleNode_PathValue.getThird());
        });
    }
}