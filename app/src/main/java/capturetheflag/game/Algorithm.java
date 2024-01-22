package capturetheflag.game;

import capturetheflag.structures.ArrayUnorderedList;
import capturetheflag.structures.Network;

import java.util.Iterator;
import java.util.Random;

public class Algorithm {
    private final Network<Integer> network;
    private final Random random;
    private final AlgorithmType type;

    public Algorithm(Network<Integer> network, AlgorithmType type) {
        this.network = network;
        this.random = new Random();
        this.type = type;
    }

    public Iterator<Integer> execute(int currentLocation, int enemyFlagLocation, int ourFlagLocation, ArrayUnorderedList<Integer> locationsToAvoid){
        switch (type) {
            case RANDOM_MOVE:
                return moveRandomly(currentLocation, locationsToAvoid);
            case GUARD:
                return guardFlag(currentLocation, ourFlagLocation, locationsToAvoid);
            case SHORTEST_PATH:
            default:
                return findShortestPath(currentLocation, enemyFlagLocation, locationsToAvoid);
        }
    }

    public Iterator<Integer> findShortestPath(int startVertex, int endVertex, ArrayUnorderedList<Integer> locationsToAvoid) {
        return network.findShortestPath(startVertex, endVertex, locationsToAvoid, network);
    }

    public Iterator<Integer> moveRandomly(int currentLocation, ArrayUnorderedList<Integer> locationsToAvoid) {
        ArrayUnorderedList<Integer> adjacentVertices = new ArrayUnorderedList<>();

        // Encontrar todos os vértices adjacentes não presentes em locationsToAvoid
        for (int i = 0; i < network.size(); i++) {
            if (network.edgeExists(currentLocation, i) && (locationsToAvoid == null || !locationsToAvoid.contains(i))) {
                adjacentVertices.addToFront(i);
            }
        }

        // Escolher um vértice adjacente aleatoriamente
        int nextLocation = currentLocation; // Padrão para permanecer na mesma localização
        if (!adjacentVertices.isEmpty()) {
            nextLocation = adjacentVertices.getIndex(random.nextInt(adjacentVertices.size()));
        }

        // Criar uma lista para o iterador com a próxima localização
        ArrayUnorderedList<Integer> nextStep = new ArrayUnorderedList<>();
        nextStep.addToRear(nextLocation);

        return nextStep.iterator();
    }

    public Iterator<Integer> guardFlag(int currentLocation, int ourFlagLocation, ArrayUnorderedList<Integer> locationsToAvoid) {
        int moveToLocation = currentLocation; // Padrão para permanecer na mesma localização

        // Verifica se o bot já está em uma localização adjacente à bandeira
        if (!network.edgeExists(currentLocation, ourFlagLocation)) {
            // Encontrar localizações adjacentes à bandeira que não estão em locationsToAvoid
            ArrayUnorderedList<Integer> adjacentLocations = new ArrayUnorderedList<>();
            for (int i = 0; i < network.size(); i++) {
                if (network.edgeExists(ourFlagLocation, i) && (locationsToAvoid == null || !locationsToAvoid.contains(i))) {
                    adjacentLocations.addToRear(i);
                }
            }

            // Escolher uma das localizações adjacentes para se mover
            if (!adjacentLocations.isEmpty()) {
                moveToLocation = adjacentLocations.getIndex(random.nextInt(adjacentLocations.size()));
            }
        }

        // Criar uma lista para o iterador com a próxima localização
        ArrayUnorderedList<Integer> nextStep = new ArrayUnorderedList<>();
        nextStep.addToRear(moveToLocation);

        return nextStep.iterator();
    }
}
