package capturetheflag.game;

public class Bot {
    private int location;
    private Player player;
    private Algorithm algorithm;
    private int botNumber;

    public Bot(Player player, int location, Algorithm algorithm, int botNumber) {
        this.player = player;
        this.location = location;
        this.algorithm = algorithm;
        this.botNumber = botNumber;
    }

    public int getBotNumber() {
        return botNumber;
    }

    public Algorithm getAlgorithm() {
        return algorithm;
    }

    public int getLocation() {
        return location;
    }

    public void setLocation(int location) {
        this.location = location;
    }
    @Override
    public String toString() {
        return "Bot{" +
                "location=" + location +
                ", player=" + player.getName() +
                '}';
    }
}