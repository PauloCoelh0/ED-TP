package tp_ed.capturetheflag.game;

import tp_ed.structures.Network;

import static tp_ed.capturetheflag.game.GameController.scanner;

/**
 * Contains utility methods for enforcing game rules and performing safe input operations in game.
 */
public class GameRules {

    /**
     * Checks if a given location is occupied by any bot player, excluding the enemy flag location.
     *
     * @param location          The location to check.
     * @param player1           The first player.
     * @param player2           The second player.
     * @param enemyFlagLocation The location of the enemy flag.
     * @return                  {@code true} if the location is occupied by a bot, {@code false} otherwise.
     */
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

    /**
     * Checks if a location has a minimum number of adjacent vertices in the game's map.
     *
     * @param network       The game's network representing the map.
     * @param location      The location to check.
     * @param minAdjacent   The minimum number of adjacent vertices required.
     * @return              {@code true} if the location has at least the minimum number of adjacent vertices, {@code false} otherwise.
     */
    public static boolean hasMinimumAdjacentVertices(Network<Integer> network, int location, int minAdjacent) {
        int count = 0;
        for (int i = 0; i < network.size(); i++) {
            if (network.edgeExists(location, i)) {
                count++;
            }
        }
        return count >= minAdjacent;
    }

    /**
     * Reads an integer safely from the input scanner, prompting the user until a valid integer is entered.
     *
     * @return The integer entered by the user.
     */
    public static int readIntSafely() {
        while (true) {
            if (scanner.hasNextInt()) {
                return scanner.nextInt();
            } else {
                System.out.print("\n[ERRO]: Insira um número inteiro: ");
                scanner.next();
            }
        }
    }

    /**
     * Reads a boolean value safely from the input scanner, prompting the user until 'true' or 'false' is entered.
     *
     * @return The boolean value entered by the user.
     */
    public static boolean readBooleanSafely() {
        while (true) {
            String input = scanner.next();
            if (input.equalsIgnoreCase("true") || input.equalsIgnoreCase("false")) {
                return Boolean.parseBoolean(input);
            } else {
                System.out.print("\n[ERRO]: Insira 'true' ou 'false': ");
            }
        }
    }

    /**
     * Reads a double value safely from the input scanner, prompting the user until a valid number is entered.
     *
     * @return The double value entered by the user.
     */
    public static double readDoubleSafely() {
        while (!scanner.hasNextDouble()) {
            System.out.print("\n[ERRO]: Insira um número válido: ");
            scanner.next();
        }
        return scanner.nextDouble();
    }
}
