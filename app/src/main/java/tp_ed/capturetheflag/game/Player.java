package tp_ed.capturetheflag.game;

import tp_ed.structures.exceptions.EmptyCollectionException;
import tp_ed.structures.CircularArrayQueue;

public class Player {
    private String name;
    private Flag flag;
    private CircularArrayQueue<Bot> bots;

    public Player(String name) {
        this.name = name;
        this.bots = new CircularArrayQueue<>();
    }

    public void clearBots() {
        while (!bots.isEmpty()) {
            try {
                bots.dequeue(); // Isso remove e descarta o bot da frente da fila
            } catch (EmptyCollectionException e) {
                // Isso não deve acontecer, pois estamos verificando se a fila está vazia
                System.err.println("Erro ao limpar os bots: " + e.getMessage());
            }
        }
    }

    public CircularArrayQueue<Bot> getBots() {
        return bots;
    }

    public String getName() {
        return name;
    }

    public void setFlag(Flag flag) {
        this.flag = flag;
    }

    public Flag getFlag() {
        return flag;
    }

    public void addBot(Bot bot) {
        bots.enqueue(bot);
    }

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