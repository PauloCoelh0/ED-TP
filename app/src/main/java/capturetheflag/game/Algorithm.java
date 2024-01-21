package capturetheflag.game;

import capturetheflag.exceptions.EmptyCollectionException;
import capturetheflag.structures.ArrayUnorderedList;
import capturetheflag.structures.Network;

import java.util.Iterator;
import java.util.Random;

public class Algorithm {
    private Network<Integer> network;
    private Random random;
    private AlgorithmType type;

    public Algorithm(Network<Integer> network, AlgorithmType type) {
        this.network = network;
        this.random = new Random();
        this.type = type;
    }

    public Iterator<Integer> execute(int currentLocation, int flagLocation, ArrayUnorderedList<Integer> locationsToAvoid) throws EmptyCollectionException {
        switch (type) {
            case RANDOM_MOVE:
                return moveRandomly(currentLocation).iterator();
            case SHORTEST_PATH:
            default:
                return findShortestPath(currentLocation, flagLocation, locationsToAvoid);
        }
    }

    public Iterator<Integer> findShortestPath(int startVertex, int endVertex, ArrayUnorderedList<Integer> locationsToAvoid) throws EmptyCollectionException {
        return network.findShortestPath(startVertex, endVertex, locationsToAvoid, network);
    }

    public ArrayUnorderedList<Integer> moveRandomly(int currentLocation) {
        ArrayUnorderedList<Integer> adjacentVertices = new ArrayUnorderedList<>();
        ArrayUnorderedList<Integer> nextStep = new ArrayUnorderedList<>();

        // Encontrar todos os vértices adjacentes
        for (int i = 0; i < network.size(); i++) {
            if (network.edgeExists(currentLocation, i)) {
                adjacentVertices.addToFront(i);
            }
        }

        // Escolher um vértice adjacente aleatoriamente
        if (!adjacentVertices.isEmpty()) {
            nextStep.addToRear(adjacentVertices.getIndex(random.nextInt(adjacentVertices.size())));
        } else {
            nextStep.addToRear(currentLocation); // Nenhuma mudança se não houver vértices adjacentes
        }

        return nextStep;
    }

}