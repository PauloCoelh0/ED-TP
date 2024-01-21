package capturetheflag.game;

import capturetheflag.exceptions.EmptyCollectionException;
import capturetheflag.structures.Network;

public class GameMap {
    private final Network<Integer> network;
    private final int numLocations;
    private double density;

    public GameMap(int numLocations, boolean isBidirectional, double density) throws EmptyCollectionException {
        this.numLocations = numLocations;
        this.density = density;
        this.network = new Network<>(isBidirectional);

        generateMap();
    }

    public GameMap(Network<Integer> importedNetwork) {
        this.network = importedNetwork;
        this.numLocations = importedNetwork.size();
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

    public Network<Integer> getNetwork() {
        return network;
    }
}
