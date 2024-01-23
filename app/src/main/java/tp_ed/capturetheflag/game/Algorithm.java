package tp_ed.capturetheflag.game;

import tp_ed.structures.ArrayUnorderedList;
import tp_ed.structures.Network;

import java.util.Iterator;
import java.util.Random;

/**
 * Represents an algorithm used by a bot in the game.
 * It defines the strategy a bot uses to move, either randomly, guarding the flag, or finding the shortest path to the enemy's flag location.
 */
public class Algorithm {
    private final Network<Integer> network; // The network (map) of the game
    private final Random random; // Random instance for making random moves
    private final AlgorithmType type; // The type of algorithm

    /**
     * Constructs an Algorithm with the specified network and algorithm type.
     *
     * @param network The game's network (map).
     * @param type    The type of algorithm to be used.
     */
    public Algorithm(Network<Integer> network, AlgorithmType type) {
        this.network = network;
        this.random = new Random();
        this.type = type;
    }

    /**
     * Returns the type of the algorithm.
     *
     * @return The algorithm type.
     */
    public AlgorithmType getType() {
        return type;
    }

    /**
     * Executes the algorithm based on its type.
     *
     * @param currentLocation      The current location of the bot.
     * @param enemyFlagLocation    The location of the enemy's flag.
     * @param teamFlagLocation      The location of the team's flag.
     * @param locationsToAvoid     Locations to avoid during movement.
     * @return                     An iterator over the path decided by the algorithm.
     */
    public Iterator<Integer> execute(int currentLocation, int enemyFlagLocation, int teamFlagLocation, ArrayUnorderedList<Integer> locationsToAvoid){
        switch (type) {
            case RANDOM_MOVE:
                return moveRandomly(currentLocation, locationsToAvoid);
            case GUARD:
                return guardFlag(currentLocation, teamFlagLocation, locationsToAvoid);
            case SHORTEST_PATH:
            default:
                return findShortestPath(currentLocation, enemyFlagLocation, locationsToAvoid);
        }
    }

    /**
     * Finds the shortest path from a start vertex to an end vertex, avoiding occupied locations.
     * This method utilizes the network's shortest path algorithm, considering the locations to avoid.
     *
     * @param startVertex       The starting vertex in the network (bot's location).
     * @param endVertex         The end vertex for the path (enemy's flag location).
     * @param locationsToAvoid  A list of vertices to avoid in the path.
     * @return                  An iterator over the sequence of vertices in the shortest path.
     */
    public Iterator<Integer> findShortestPath(int startVertex, int endVertex, ArrayUnorderedList<Integer> locationsToAvoid) {
        return network.findShortestPath(startVertex, endVertex, locationsToAvoid, network);
    }

    /**
     * Moves randomly from the current location, avoiding occupied locations.
     * This method selects a random adjacent vertex that is not in the locations to avoid.
     *
     * @param currentLocation   The current location of the bot.
     * @param locationsToAvoid  A list of locations to avoid.
     * @return                  An iterator over the next location chosen randomly.
     */
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

    /**
     * Guards the flag by staying close to it, moving to an adjacent location if not already in one.
     * This method aims to move the bot to a location adjacent to the flag, avoiding occupied locations.
     *
     * @param currentLocation   The current location of the bot.
     * @param teamFlagLocation   The location of team's flag.
     * @param locationsToAvoid  A list of locations to avoid.
     * @return                  An iterator over the next location chosen to guard the flag.
     */
    public Iterator<Integer> guardFlag(int currentLocation, int teamFlagLocation, ArrayUnorderedList<Integer> locationsToAvoid) {
        int moveToLocation = currentLocation; // Padrão para permanecer na mesma localização

        // Verifica se o bot já está numa localização adjacente à bandeira
        if (!network.edgeExists(currentLocation, teamFlagLocation)) {
            // Encontrar localizações adjacentes à bandeira que não estão em locationsToAvoid
            ArrayUnorderedList<Integer> adjacentLocations = new ArrayUnorderedList<>();
            for (int i = 0; i < network.size(); i++) {
                if (network.edgeExists(teamFlagLocation, i) && (locationsToAvoid == null || !locationsToAvoid.contains(i))) {
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
