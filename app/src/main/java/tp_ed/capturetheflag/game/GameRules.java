package tp_ed.capturetheflag.game;

import tp_ed.structures.Network;

import static tp_ed.capturetheflag.game.GameController.scanner;

public class GameRules {
    public static boolean isLocationOccupied(int location, Player player1, Player player2, int enemyFlagLocation) {
        // Pode ser ocupada se for a localização da bandeira inimiga
        if (location == enemyFlagLocation) {
            return false;
        }

        // Verificar bots do player1
        for (int i = 0; i < player1.getBots().size(); i++) {
            Bot bot = player1.getBots().get(i);
            if (bot.getLocation() == location) {
                return true;
            }
        }

        // Verificar bots do player2
        for (int i = 0; i < player2.getBots().size(); i++) {
            Bot bot = player2.getBots().get(i);
            if (bot.getLocation() == location) {
                return true;
            }
        }

        return false;
    }

    public static boolean hasMinimumAdjacentVertices(Network<Integer> network, int location, int minAdjacent) {
        int count = 0;
        for (int i = 0; i < network.size(); i++) {
            if (network.edgeExists(location, i)) {
                count++;
            }
        }
        return count >= minAdjacent;
    }

    public static int readIntSafely() {
        while (true) {
            if (scanner.hasNextInt()) {
                return scanner.nextInt();
            } else {
                System.out.println("\n[ERRO]: Insira um número inteiro: ");
                scanner.next();
            }
        }
    }

    public static boolean readBooleanSafely() {
        while (true) {
            String input = scanner.next();
            if (input.equalsIgnoreCase("true") || input.equalsIgnoreCase("false")) {
                return Boolean.parseBoolean(input);
            } else {
                System.out.println("\n[ERRO]: Insira 'true' ou 'false': ");
            }
        }
    }

    public static double readDoubleSafely() {
        while (!scanner.hasNextDouble()) {
            System.out.println("\n[ERRO]: Insira um número válido: ");
            scanner.next();
        }
        return scanner.nextDouble();
    }
}
