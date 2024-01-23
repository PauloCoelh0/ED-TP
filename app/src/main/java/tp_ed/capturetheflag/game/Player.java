package tp_ed.capturetheflag.game;

import tp_ed.structures.exceptions.EmptyCollectionException;
import tp_ed.structures.CircularArrayQueue;

/**
 * The Player class represents a player in the game.
 * It stores information about the player's name, the associated flag, and a circular array queue of bots.
 */
public class Player {
    private String name; // The name of the player
    private Flag flag; // The flag associated with the player
    private CircularArrayQueue<Bot> bots; // The player's circular queue of bots

    /**
     * Constructs a new player with the specified name.
     *
     * @param name The name of the player.
     */
    public Player(String name) {
        this.name = name;
        this.bots = new CircularArrayQueue<>();
    }

    /**
     * Returns the circular queue of bots belonging to the player.
     *
     * @return The circular queue of bots.
     */
    public CircularArrayQueue<Bot> getBots() {
        return bots;
    }

    /**
     * Returns the name of the player.
     *
     * @return The name of the player.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the flag associated with the player.
     *
     * @param flag The flag to be associated with the player.
     */
    public void setFlag(Flag flag) {
        this.flag = flag;
    }

    /**
     * Returns the flag associated with the player.
     *
     * @return The associated flag.
     */
    public Flag getFlag() {
        return flag;
    }

    /**
     * Adds a bot to the player's circular queue of bots.
     *
     * @param bot The bot to be added.
     */
    public void addBot(Bot bot) {
        bots.enqueue(bot);
    }

    /**
     * Clears all bots associated with the player.
     * All bots in the queue are removed.
     */
    public void clearBots() {
        while (!bots.isEmpty()) {
            try {
                bots.dequeue();
            } catch (EmptyCollectionException e) {
                System.err.println("Erro ao limpar os bots: " + e.getMessage());
            }
        }
    }

    /**
     * Prints information about all bots associated with the player.
     * Iterates through the circular queue and prints information for each bot.
     */
    public void printBotsInfo() {
        System.out.println("Bots de " + this.name + ":");

        int initialSize = this.bots.size();
        for (int i = 0; i < initialSize; i++) {
            try {
                Bot bot = this.bots.dequeue();
                System.out.println(bot.getInfo());
                this.bots.enqueue(bot);
            } catch (EmptyCollectionException e) {
                System.err.println("Erro: " + e.getMessage());
            }
        }
    }
}
