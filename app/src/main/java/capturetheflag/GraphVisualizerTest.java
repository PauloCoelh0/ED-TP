package capturetheflag;

import org.graphstream.graph.Graph;
import org.graphstream.graph.implementations.SingleGraph;
public class GraphVisualizerTest {
    public static void main(String args[]) {
        Graph graph = new SingleGraph("Graph Visualizer");
        graph.display();

        // Adicionando vértices
        graph.addNode("A");
        graph.addNode("B");
        graph.addNode("C");

        // Adicionando arestas
        graph.addEdge("AB", "A", "B");
        graph.addEdge("BC", "B", "C");
        graph.addEdge("CA", "C", "A");
    }
}