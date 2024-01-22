package tp_ed.capturetheflag.game;

import tp_ed.structures.exceptions.EmptyCollectionException;
import tp_ed.structures.Network;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.SingleGraph;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class GameMap {
    private final Network<Integer> network;
    private final int numLocations;
    private double density;
    private Integer flagLocationPlayer1;
    private Integer flagLocationPlayer2;
    private Map<Integer, String> botLocations;
    private Graph graph;
    public GameMap(int numLocations, boolean isBidirectional, double density) throws EmptyCollectionException {
        this.numLocations = numLocations;
        this.density = density;
        this.network = new Network<>(isBidirectional);
        this.botLocations = new HashMap<>();
        generateMap();
        printVisualMap();
    }

    public GameMap(Network<Integer> importedNetwork) {
        this.network = importedNetwork;
        this.numLocations = importedNetwork.size();
        printVisualMap();
    }

    private void generateMap() throws EmptyCollectionException {
        // Adicionar vértices
        for (int i = 0; i < numLocations; i++) {
            network.addVertex(i);
        }

        // Adicionar arestas
        int edgesToAdd = calculateNumberOfEdges();
        while (edgesToAdd > 0) {
            edgesToAdd = addRandomEdge(edgesToAdd);
        }

        // Verificar conectividade
        if (!network.isConnected()) {
            System.out.println("[MENSAGEM]: O mapa gerado não é conectado. Gerando novamente...");
            regenerateMap();
        } else {
            System.out.println("[MESSAGEM]: Mapa conectado com sucesso!");
        }
    }

    private int calculateNumberOfEdges() {
        return (int) (density * numLocations * (numLocations - 1) / 2);
    }

    private int addRandomEdge(int edgesToAdd) {
        int location1 = (int) (Math.random() * numLocations);
        int location2 = (int) (Math.random() * numLocations);

        if (location1 != location2 && !network.edgeExists(location1, location2)) {
            double weight = 1 + (Math.random() * 14);
            network.addEdge(location1, location2, weight);
            edgesToAdd--;
        }
        return edgesToAdd;
    }

    private void regenerateMap() throws EmptyCollectionException {
        int edgesToAdd = calculateNumberOfEdges();
        while (!network.isConnected()) {
            edgesToAdd = addRandomEdge(edgesToAdd);
        }
        System.out.println("Mapa foi reconectado com sucesso!");
    }

    public void printMap() {
        System.out.println("Mapa:");
        for (int i = 0; i < numLocations; i++) {
            for (int j = 0; j < numLocations; j++) {
                if (network.edgeExists(i, j)) {
                    double weight = network.getWeight(i, j);
                    System.out.println("Aresta entre " + i + " e " + j + " com peso " + weight);
                }
            }
        }
    }

    public void printNetwork(Network<Integer> network) {
        System.out.println("[MESSAGEM]: Mapa Importado:");
        for (int i = 0; i < numLocations; i++) {
            for (int j = 0; j < numLocations; j++) {
                if (network.edgeExists(i, j)) {
                    double weight = network.getWeight(i, j);
                    System.out.println("Aresta entre " + i + " e " + j + " com peso " + weight);
                }
            }
        }
    }

    public void setFlagLocationPlayer1(Integer location) {
        this.flagLocationPlayer1 = location;
    }

    public void setFlagLocationPlayer2(Integer location) {
        this.flagLocationPlayer2 = location;
    }
    public void updateBotLocation(String playerIdentifier, int botNumber, int newLocation) {
        // Identificar o bot
        String botLabel;
        if(Objects.equals(playerIdentifier, "Player 1")){
            botLabel = "P1 B" + botNumber;
        } else {
            botLabel = "P2 B" + botNumber;
        }

        // Remover localização anterior do bot
        botLocations.values().remove(botLabel);

        // Atualizar com a nova localização
        botLocations.put(newLocation, botLabel);
    }


    public void printVisualMap() {
        if (graph == null) {
            graph = new SingleGraph("Game Map Visualization");
            // Configuração e adição de vértices e arestas
        }

        // Configuração do estilo visual do grafo
        graph.addAttribute("ui.stylesheet", "node { fill-color: red; size: 20px; text-size: 20; } edge { fill-color: grey; }");

        // Adicionando vértices
        for (int i = 0; i < numLocations; i++) {
            graph.addNode(Integer.toString(i));
            Node node = graph.getNode(Integer.toString(i));
            node.addAttribute("ui.label", Integer.toString(i));
        }

        // Adicionando arestas
        for (int i = 0; i < numLocations; i++) {
            for (int j = 0; j < numLocations; j++) {
                if (network.edgeExists(i, j)) {
                    String edgeId = i + "_" + j;
                    graph.addEdge(edgeId, Integer.toString(i), Integer.toString(j), true); // true para arestas direcionadas se necessário
                }
            }
        }

        graph.display();

    }
    public void updateVisualMap() {
        // Resetar a cor de todos os nós para o padrão
        for (Node node : graph) {
            node.addAttribute("ui.style", "fill-color: red;");
        }

        // Atualizar a cor do nó da bandeira do jogador 1
        if (flagLocationPlayer1 != null) {
            Node node = graph.getNode(Integer.toString(flagLocationPlayer1));
            if (node != null) {
                node.addAttribute("ui.style", "fill-color: blue;");
            }
        }

        // Atualizar a cor do nó da bandeira do jogador 2
        if (flagLocationPlayer2 != null) {
            Node node = graph.getNode(Integer.toString(flagLocationPlayer2));
            if (node != null) {
                node.addAttribute("ui.style", "fill-color: rgb(30, 140, 0);");
            }
        }

        for (Map.Entry<Integer, String> botLocationEntry : botLocations.entrySet()) {
            Node node = graph.getNode(Integer.toString(botLocationEntry.getKey()));
            if (node != null) {
                String botLabel = botLocationEntry.getValue();
                // Incluir o número do vértice no rótulo
                String label = node.getId() + " " + botLabel;

                // Definir a cor baseada no jogador
                String color = botLabel.startsWith("P1") ? "blue" : "rgb(30, 140, 0)";
                node.addAttribute("ui.style", "fill-color: " + color + ";");
                node.addAttribute("ui.style", "text-color: " + color + ";");
                node.addAttribute("ui.label", label);
            }
        }

        // Remover rótulos de bots de nós que não têm mais bots
        for (Node node : graph) {
            if (!botLocations.containsKey(Integer.parseInt(node.getId()))) {
                node.addAttribute("ui.label", node.getId());
                node.addAttribute("ui.style", "text-color: black;");
            }
        }
    }

    public Network<Integer> getNetwork() {
        return network;
    }
}
