package tp_ed.capturetheflag.game;

/**
 * The Bot class represents a bot in the game.
 * It stores information the bot's current location, the player it is associated with,
 * the movement algorithm it uses, the bot number and a unique identifier.
 */
public class Bot {
    private int location;
    private Player player;
    private Algorithm algorithm;
    private int botNumber;

    private String botIdentifier;

    /**
     * Constructs a new bot associated with a player, specified location, algorithm, and bot number.
     *
     * @param player The player the bot is associated with.
     * @param location The initial location.
     * @param algorithm The movement algorithm.
     * @param botNumber The bot number.
     */
    public Bot(Player player, int location, Algorithm algorithm, int botNumber) {
        this.player = player;
        this.location = location;
        this.algorithm = algorithm;
        this.botNumber = botNumber;
    }

    /**
     * Constructs a new bot with a specified location and unique identifier.
     *
     * @param location The initial location of the bot.
     * @param botIdentifier The unique identifier.
     */
    public Bot(int location, String botIdentifier) {
        this.location = location;
        this.botIdentifier = botIdentifier;
    }

    /**
     * Returns the bot's unique identifier.
     *
     * @return The unique identifier of the bot.
     */
    public String getBotIdentifier() {
        return botIdentifier;
    }

    /**
     * Returns the bot's number.
     *
     * @return The number of the bot.
     */
    public int getBotNumber() {
        return botNumber;
    }

    /**
     * Returns the bot's movement algorithm.
     *
     * @return The movement algorithm of the bot.
     */
    public Algorithm getAlgorithm() {
        return algorithm;
    }

    /**
     * Returns the bot location in the map.
     *
     * @return The bot's location.
     */
    public int getLocation() {
        return location;
    }

    /**
     * Sets the bot's location to the specified location.
     *
     * @param location The new location of the bot.
     */
    public void setLocation(int location) {
        this.location = location;
    }

    /**
     * Returns a string with information about the bot, including its location and movement algorithm.
     *
     * @return A string with the bot's information.
     */
    public String getInfo() {
        String algorithmName;
        switch (algorithm.getType()) {
            case SHORTEST_PATH:
                algorithmName = "FLASH";
                break;
            case RANDOM_MOVE:
                algorithmName = "LOKI";
                break;
            case GUARD:
                algorithmName = "HULK";
                break;
            default:
                algorithmName = "Desconhecido";
        }
        return "O Bot " + botNumber + " está na posição " + location + " e é o " + algorithmName;
    }
}