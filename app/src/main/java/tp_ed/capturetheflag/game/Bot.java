package tp_ed.capturetheflag.game;

public class Bot {
    private int location;
    private Player player;
    private Algorithm algorithm;
    private int botNumber;

    private String botIdentifier;

    public Bot(Player player, int location, Algorithm algorithm, int botNumber) {
        this.player = player;
        this.location = location;
        this.algorithm = algorithm;
        this.botNumber = botNumber;
    }

    public Bot(int location, String botIdentifier) {
        this.location = location;
        this.botIdentifier = botIdentifier;
    }
    public String getBotIdentifier() {
        return botIdentifier;
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