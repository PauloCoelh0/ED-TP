package capturetheflag.game;

import capturetheflag.exceptions.EmptyCollectionException;
import capturetheflag.structures.ArrayUnorderedList;

import java.util.Iterator;
import java.util.Scanner;

import static capturetheflag.game.GameRules.isLocationOccupied;

public class GameUtils {
    public static boolean executeBotTurn(Player player, Bot bot, int flagLocation, GameMap gameMap, Player enemy) throws EmptyCollectionException {
        int oldLocation = bot.getLocation(); // Localização anterior para registro
        Iterator<Integer> pathIterator = bot.getAlgorithm().findShortestPath(bot.getLocation(), flagLocation, null);

        // Verifica se há um caminho disponível
        if (pathIterator != null && pathIterator.hasNext()) {
            int nextStep = pathIterator.next(); // O próximo passo no caminho

            // Se a próxima posição não estiver ocupada, move o bot
            if (!isLocationOccupied(nextStep, player, enemy, flagLocation)) {
                bot.setLocation(nextStep); // Move o bot para o próximo passo
                System.out.println(player.getName() + " Bot " + bot.getBotNumber() + " na posição " + oldLocation + " moveu-se para a posição " + nextStep);
            } else {
                // Se a posição estiver ocupada, tenta encontrar um novo caminho
                System.out.println(player.getName() + " Bot " + bot.getBotNumber() + " encontrou uma posição ocupada na posição " + nextStep);
                ArrayUnorderedList<Integer> locationsToAvoid = new ArrayUnorderedList<>();
                locationsToAvoid.addToRear(nextStep);

                Iterator<Integer> newPathIterator = bot.getAlgorithm().findShortestPath(bot.getLocation(), flagLocation, locationsToAvoid);

                if (newPathIterator != null && newPathIterator.hasNext()) {
                    int newNextStep = newPathIterator.next();
                    if (!isLocationOccupied(newNextStep, player, enemy, flagLocation)) {
                        bot.setLocation(newNextStep);
                        System.out.println(player.getName() + " Bot " + bot.getBotNumber() + " recalculou e moveu-se para a posição " + newNextStep);
                    } else {
                        System.out.println(player.getName() + " Bot " + bot.getBotNumber() + " ainda encontrou uma posição ocupada após recalcular.");
                    }
                } else {
                    System.out.println(player.getName() + " Bot " + bot.getBotNumber() + " não conseguiu encontrar um novo caminho válido.");
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
        for (int i = 0; i < numberOfBots; i++) {
            System.out.println(playerName + ", escolha um algoritmo para o bot " + (i + 1) + ":");
            Algorithm algorithm = GameUtils.chooseAlgorithm(scanner, gameMap);
            Bot bot = new Bot(player ,player.getFlag().getLocation(), algorithm, (i + 1));
            player.addBot(bot);
        }
    }

    public static Algorithm chooseAlgorithm(Scanner scanner, GameMap gameMap) {
        System.out.println("Escolha um algoritmo para o bot:");
        System.out.println("1. Caminho mais curto (ShortestPath)");
        int choice = scanner.nextInt();
        switch (choice) {
            case 1:
                return new Algorithm(gameMap.getNetwork());
            default:
                System.out.println("Opção inválida. Escolhendo 'Caminho mais curto' por padrão.");
                return new Algorithm(gameMap.getNetwork());
        }
    }

}