package tp_ed.capturetheflag.game;

/**
 * The Flag class represents a flag in the game.
 * It stores information the flag's location in the game map.
 */
public class Flag {
    private int location;

    /**
     * Constructs a new flag with the specified location.
     *
     * @param location The location of the flag in the map.
     */
    public Flag(int location) {
        this.location = location;
    }

    /**
     * Returns the location of the flag.
     *
     * @return The location of the flag in the map.
     */
    public int getLocation() {
        return location;
    }
}
