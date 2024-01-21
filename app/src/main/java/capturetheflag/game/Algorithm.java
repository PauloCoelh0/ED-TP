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

    public Iterator<Integer> execute(int currentLocation, int enemyFlagLocation, int ourFlagLocation, ArrayUnorderedList<Integer> locationsToAvoid) throws EmptyCollectionException {
        switch (type) {
            case RANDOM_MOVE:
                return moveRandomly(currentLocation, locationsToAvoid).iterator();
            case GUARD:
                return guardFlag(currentLocation, ourFlagLocation, locationsToAvoid).iterator(); // Inclui locationsToAvoid
            case SHORTEST_PATH:
            default:
                return findShortestPath(currentLocation, enemyFlagLocation, locationsToAvoid);
        }
    }

    public Iterator<Integer> findShortestPath(int startVertex, int endVertex, ArrayUnorderedList<Integer> locationsToAvoid) throws EmptyCollectionException {
        return network.findShortestPath(startVertex, endVertex, locationsToAvoid, network);
    }

    public ArrayUnorderedList<Integer> moveRandomly(int currentLocation, ArrayUnorderedList<Integer> locationsToAvoid) {
        ArrayUnorderedList<Integer> adjacentVertices = new ArrayUnorderedList<>();
        ArrayUnorderedList<Integer> nextStep = new ArrayUnorderedList<>();

        // Encontrar todos os vértices adjacentes não presentes em locationsToAvoid
        for (int i = 0; i < network.size(); i++) {
            if (network.edgeExists(currentLocation, i) && (locationsToAvoid == null || !locationsToAvoid.contains(i))) {
                adjacentVertices.addToFront(i);
            }
        }

        // Escolher um vértice adjacente aleatoriamente
        if (!adjacentVertices.isEmpty()) {
            nextStep.addToRear(adjacentVertices.getIndex(random.nextInt(adjacentVertices.size())));
        } else {
            nextStep.addToRear(currentLocation); // Nenhuma mudança se não houver vértices adjacentes válidos
        }

        return nextStep;
    }

    public ArrayUnorderedList<Integer> guardFlag(int currentLocation, int ourFlagLocation, ArrayUnorderedList<Integer> locationsToAvoid) {
        ArrayUnorderedList<Integer> nextStep = new ArrayUnorderedList<>();

        // Verifica se o bot já está em uma localização adjacente à bandeira
        if (network.edgeExists(currentLocation, ourFlagLocation)) {
            nextStep.addToRear(currentLocation); // Permanecer na posição atual se já estiver protegendo a bandeira
        } else {
            // Encontrar localizações adjacentes à bandeira que não estão em locationsToAvoid
            ArrayUnorderedList<Integer> adjacentLocations = new ArrayUnorderedList<>();
            for (int i = 0; i < network.size(); i++) {
                if (network.edgeExists(ourFlagLocation, i) && (locationsToAvoid == null || !locationsToAvoid.contains(i))) {
                    adjacentLocations.addToRear(i);
                }
            }

            // Escolher uma das localizações adjacentes para se mover
            if (!adjacentLocations.isEmpty()) {
                int moveToLocation = adjacentLocations.getIndex(random.nextInt(adjacentLocations.size()));
                nextStep.addToRear(moveToLocation);
            } else {
                nextStep.addToRear(currentLocation); // Se não houver locais adjacentes válidos, permanecer na posição atual
            }
        }

        return nextStep;
    }


}