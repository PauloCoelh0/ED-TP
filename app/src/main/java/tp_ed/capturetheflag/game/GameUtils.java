package tp_ed.capturetheflag.game;

import tp_ed.structures.exceptions.ElementNotFoundException;
import tp_ed.structures.exceptions.EmptyCollectionException;
import tp_ed.structures.ArrayUnorderedList;

import java.util.Iterator;
import java.util.Scanner;

import static tp_ed.capturetheflag.game.GameRules.isLocationOccupied;
import static tp_ed.capturetheflag.game.GameRules.readIntSafely;

/**
 * The GameUtils class provides utility methods for gameplay mechanics in the game.
 * It includes methods for executing bot turns, initializing bots, and choosing algorithms for bots.
 */
public class GameUtils {

    /**
     * Executes a turn for a given bot, determining its next move based on the current game state.
     *
     * @param player The player to whom the bot belongs.
     * @param bot The bot that is taking the turn.
     * @param flagLocation The location of the enemy's flag.
     * @param gameMap The game map.
     * @param enemy The enemy player.
     * @return True if the bot reaches the enemy's flag, false otherwise.
     * @throws EmptyCollectionException If a required collection is empty.
     * @throws ElementNotFoundException If a required element is not found.
     */
    public static boolean executeBotTurn(Player player, Bot bot, int flagLocation, GameMap gameMap, Player enemy) throws EmptyCollectionException, ElementNotFoundException {
        int oldLocation = bot.getLocation(); // Localização anterior para registro
        int teamFlagLocation = player.getFlag().getLocation();
        Iterator<Integer> pathIterator = bot.getAlgorithm().execute(bot.getLocation(), flagLocation, teamFlagLocation,null);

        // Verifica se há um caminho disponível
        if (pathIterator != null && pathIterator.hasNext()) {
            int nextStep = pathIterator.next(); // O próximo passo no caminho

            if (nextStep == oldLocation) {
                System.out.println(player.getName() + " Bot " + bot.getBotNumber() + " permaneceu na posição " + oldLocation);
            } else if (!isLocationOccupied(nextStep, player, enemy, flagLocation)) {
                bot.setLocation(nextStep); // Move o bot para o próximo passo
                gameMap.updateBotLocation(player.getName(), bot.getBotNumber(), bot.getLocation());
                gameMap.updateVisualMap();
                System.out.println(player.getName() + " Bot " + bot.getBotNumber() + " na posição " + oldLocation + " moveu-se para a posição " + nextStep);
            } else {
                // Se a posição estiver ocupada, tenta encontrar um novo caminho
                System.out.println(player.getName() + " Bot " + bot.getBotNumber() + " encontrou a posição ocupada " + nextStep);
                ArrayUnorderedList<Integer> locationsToAvoid = new ArrayUnorderedList<>();
                locationsToAvoid.addToRear(nextStep);

                // Recalcular o movimento com base no algoritmo do bot
                pathIterator = bot.getAlgorithm().execute(bot.getLocation(), flagLocation, teamFlagLocation,  locationsToAvoid);

                if (pathIterator != null && pathIterator.hasNext()) {
                    int newNextStep = pathIterator.next();
                    if (!isLocationOccupied(newNextStep, player, enemy, flagLocation)) {
                        bot.setLocation(newNextStep);
                        gameMap.updateBotLocation(player.getName(), bot.getBotNumber(), bot.getLocation());
                        gameMap.updateVisualMap();
                        System.out.println(player.getName() + " Bot " + bot.getBotNumber() + " recalculou e moveu-se para a posição " + newNextStep);
                    } else {
                        System.out.println(player.getName() + " Bot " + bot.getBotNumber() + " ainda encontrou uma posição ocupada após recalcular.");
                    }
                } else {
                    System.out.println(player.getName() + " Bot " + bot.getBotNumber() + " não conseguiu encontrar um novo movimento válido.");
                }
            }
        } else {
            System.out.println(player.getName() + " Bot " + bot.getBotNumber() + " não conseguiu encontrar um caminho válido.");
        }

        // Verifica se o bot atingiu a localização da bandeira adversária
        boolean reachedFlag = bot.getLocation() == flagLocation;
        if (reachedFlag) {
            System.out.println(player.getName() + " Bot " + bot.getBotNumber() + " atingiu a bandeira na posição " + flagLocation);
        }
        return reachedFlag;
    }

    /**
     * Initializes bots for a player, setting their algorithms and positions.
     *
     * @param player The player for whom the bots are being initialized.
     * @param numberOfBots The number of bots to initialize.
     * @param playerName The name of the player.
     * @param gameMap The game map.
     */
    public static void initializeBots(Player player, int numberOfBots, String playerName, GameMap gameMap) {
        ArrayUnorderedList<AlgorithmType> chosenAlgorithms = new ArrayUnorderedList<>();

        for (int i = 0; i < numberOfBots; i++) {
            System.out.println(playerName + ", escolha um algoritmo para o bot " + (i + 1) + ":");
            Algorithm algorithm = GameUtils.chooseAlgorithm(gameMap, chosenAlgorithms);
            Bot bot = new Bot(player, player.getFlag().getLocation(), algorithm, (i + 1));
            player.addBot(bot);
        }
    }

    /**
     * Allows a player to choose an algorithm for a bot based on available options.
     *
     * @param gameMap The game map.
     * @param chosenAlgorithms The list of already chosen algorithms to ensure diversity.
     * @return The selected algorithm.
     */
    public static Algorithm chooseAlgorithm(GameMap gameMap, ArrayUnorderedList<AlgorithmType> chosenAlgorithms) {
        int choice;
        AlgorithmType chosenType = null;

        do {
            printAvailableAlgorithms(chosenAlgorithms);
            choice = readIntSafely();

            switch (choice) {
                case 1:
                    if (!chosenAlgorithms.contains(AlgorithmType.SHORTEST_PATH) || chosenAlgorithms.size() >= 3) {
                        chosenType = AlgorithmType.SHORTEST_PATH;
                    } else {
                        System.out.println("\n[ERRO]: Bot [FLASH] já foi escolhido. Escolha outro!");
                        continue;
                    }
                    break;
                case 2:
                    if (!chosenAlgorithms.contains(AlgorithmType.RANDOM_MOVE) || chosenAlgorithms.size() >= 3) {
                        chosenType = AlgorithmType.RANDOM_MOVE;
                    } else {
                        System.out.println("\n[ERRO]: Bot [LOKI] já foi escolhido. Escolha outro!");
                        continue;
                    }
                    break;
                case 3:
                    if (!chosenAlgorithms.contains(AlgorithmType.GUARD)) {
                        chosenType = AlgorithmType.GUARD;
                    } else {
                        System.out.println("\n[ERRO]: Só pode existir um Bot [DEFESA]");
                        continue;
                    }
                    break;
                default:
                    System.out.println("\n[ERRO]: Opção inválida!");
            }
        } while (chosenType == null);

        chosenAlgorithms.addToRear(chosenType);
        return new Algorithm(gameMap.getNetwork(), chosenType);
    }

    /**
     * Prints the available algorithms for bots to the console, indicating the choices left.
     *
     * @param chosenAlgorithms The list of already chosen algorithms.
     */
    public static void printAvailableAlgorithms(ArrayUnorderedList<AlgorithmType> chosenAlgorithms) {
        boolean canChooseGuard = !chosenAlgorithms.contains(AlgorithmType.GUARD);


        if (!chosenAlgorithms.contains(AlgorithmType.SHORTEST_PATH) || chosenAlgorithms.size() >= 3) {
            System.out.println("\n1. FLASH [ATAQUE] - Procura o caminho mais curto para a base inimiga, e recalcula o seu caminho se for encurralado.");
        }
        if (!chosenAlgorithms.contains(AlgorithmType.RANDOM_MOVE) || chosenAlgorithms.size() >= 3) {
            System.out.println("2. LOKI [SUPORTE] - Anda pelo mapa de maneira aleatoria, distrai e encurrala.");
        }
        if (canChooseGuard) {
            System.out.println("3. HULK [DEFESA] - Desloca-se para uma posicao adjacente a base e fica de guarda bloqueando um possivel caminho de vitoria.");
        }
        System.out.print("\nEscolha um algoritmo para o bot: ");
    }
}
