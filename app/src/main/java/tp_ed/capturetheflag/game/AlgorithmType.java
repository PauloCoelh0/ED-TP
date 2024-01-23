package tp_ed.capturetheflag.game;

/**
 * Enumerates the types of algorithms that can be used by bots in the game.
 * Each type defines a different strategy for movement and action.
 */
public enum AlgorithmType {
    /** Strategy to find the shortest path to the enemy's flag location. */
    SHORTEST_PATH,
    /** Strategy to move randomly within the game's map. */
    RANDOM_MOVE,
    /** Strategy to guard the flag. */
    GUARD
}
