package tp_ed.capturetheflag.game;

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

    //TODO Mudar o nome dos bots
    public String getInfo() {
        String algorithmName;
        switch (algorithm.getType()) {
            case SHORTEST_PATH:
                algorithmName = "Sonic";
                break;
            case RANDOM_MOVE:
                algorithmName = "Toninho";
                break;
            case GUARD:
                algorithmName = "GNR";
                break;
            default:
                algorithmName = "Desconhecido";
        }
        return "O Bot " + botNumber + " está na posição " + location + " e é o " + algorithmName;
    }
}