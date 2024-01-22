package capturetheflag.game;

import capturetheflag.exceptions.EmptyCollectionException;
import capturetheflag.structures.ArrayUnorderedList;

import java.util.Iterator;
import java.util.Scanner;

import static capturetheflag.game.GameRules.isLocationOccupied;

public class GameUtils {
    public static boolean executeBotTurn(Player player, Bot bot, int flagLocation, GameMap gameMap, Player enemy) throws EmptyCollectionException {
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


    public static void initializeBots(Player player, int numberOfBots, String playerName, Scanner scanner, GameMap gameMap) {
        ArrayUnorderedList<AlgorithmType> chosenAlgorithms = new ArrayUnorderedList<>();

        for (int i = 0; i < numberOfBots; i++) {
            System.out.println(playerName + ", escolha um algoritmo para o bot " + (i + 1) + ":");
            Algorithm algorithm = GameUtils.chooseAlgorithm(scanner, gameMap, chosenAlgorithms);
            Bot bot = new Bot(player, player.getFlag().getLocation(), algorithm, (i + 1));
            player.addBot(bot);
        }
    }

    public static Algorithm chooseAlgorithm(Scanner scanner, GameMap gameMap, ArrayUnorderedList<AlgorithmType> chosenAlgorithms) {
        int choice;
        AlgorithmType chosenType = null;

        do {
            printAvailableAlgorithms(chosenAlgorithms);
            choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    if (!chosenAlgorithms.contains(AlgorithmType.SHORTEST_PATH) || chosenAlgorithms.size() >= 3) {
                        chosenType = AlgorithmType.SHORTEST_PATH;
                    } else {
                        System.out.println("\n[ERRO]: Algoritmo 'Caminho mais curto' já foi escolhido. Escolha outro.");
                        continue;
                    }
                    break;
                case 2:
                    if (!chosenAlgorithms.contains(AlgorithmType.RANDOM_MOVE) || chosenAlgorithms.size() >= 3) {
                        chosenType = AlgorithmType.RANDOM_MOVE;
                    } else {
                        System.out.println("\n[ERRO]: Algoritmo 'Movimento aleatório' já foi escolhido. Escolha outro.");
                        continue;
                    }
                    break;
                case 3:
                    if (!chosenAlgorithms.contains(AlgorithmType.GUARD)) {
                        chosenType = AlgorithmType.GUARD;
                    } else {
                        System.out.println("\n[ERRO]: Só pode existir um Bot Guarda.");
                        continue;
                    }
                    break;
                default:
                    System.out.println("\n[ERRO]: Opção inválida.");
                    chosenType = null;
            }
        } while (chosenType == null);

        chosenAlgorithms.addToRear(chosenType);
        return new Algorithm(gameMap.getNetwork(), chosenType);
    }


    public static void printAvailableAlgorithms(ArrayUnorderedList<AlgorithmType> chosenAlgorithms) {
        boolean canChooseGuard = !chosenAlgorithms.contains(AlgorithmType.GUARD);

        System.out.println("Escolha um algoritmo para o bot:");
        if (!chosenAlgorithms.contains(AlgorithmType.SHORTEST_PATH) || chosenAlgorithms.size() >= 3) {
            System.out.println("1. Caminho mais curto (ShortestPath)");
        }
        if (!chosenAlgorithms.contains(AlgorithmType.RANDOM_MOVE) || chosenAlgorithms.size() >= 3) {
            System.out.println("2. Movimento aleatório");
        }
        if (canChooseGuard) {
            System.out.println("3. Guarda (Guard)");
        }
    }
}