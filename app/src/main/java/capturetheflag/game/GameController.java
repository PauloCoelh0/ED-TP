package capturetheflag.game;

import capturetheflag.exceptions.EmptyCollectionException;
import capturetheflag.structures.Network;
import capturetheflag.utils.JsonUtil;

import java.io.IOException;
import java.util.Random;
import java.util.Scanner;

import static capturetheflag.game.GameUtils.executeBotTurn;
import static capturetheflag.game.GameUtils.initializeBots;

public class GameController {
    private static GameMap gameMap;
    private Player player1;
    private Player player2;
    private static Scanner scanner;

    public GameController() {
        this.scanner = new Scanner(System.in);
        this.player1 = new Player("Player 1");
        this.player2 = new Player("Player 2");
    }

    public void start() throws EmptyCollectionException {
        System.out.println("=================================");
        System.out.println("| Bem-vindo ao Capture the Flag! |");
        System.out.println("=================================");
        boolean running = true;

        while (running) {
            System.out.println("1. Gerar novo mapa");
            System.out.println("2. Importar mapa existente");
            System.out.println("3. Iniciar jogo");
            System.out.println("4. Sair");
            System.out.print("Escolha uma opção: ");
            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    generateMapMenu();
                    break;
                case 2:
                    importMapMenu();
                    break;
                case 3:
                    if (gameMap != null) {
                        setupGame();
                        startGame();
                    } else {
                        System.out.println("Por favor, gere ou importe um mapa antes de iniciar o jogo.");
                    }
                    break;
                case 4:
                    running = false;
                    System.out.println("Saindo...");
                    break;
                default:
                    System.out.println("Opção inválida.");
            }
        }
    }

    private void setupGame() {
        setFlagsMenu(player1);
        setFlagsMenu(player2);

        System.out.println("Quantos bots cada jogador terá?");
        int numberOfBots = scanner.nextInt();

        initializeBots(player1, numberOfBots, player1.getName(), scanner, gameMap);
        initializeBots(player2, numberOfBots, player2.getName(), scanner, gameMap);

        player1.printBotsInfo();
        player2.printBotsInfo();
    }

    private void startGame() throws EmptyCollectionException {
        System.out.println("O jogo começou!");

        boolean gameEnded = false;
        int round = 1;

        // Use a Random object to randomly choose the first player
        Random random = new Random();
        int firstPlayerIndex = random.nextInt(2); // 0 or 1 (Player 1 or Player 2)
        Player firstPlayer = (firstPlayerIndex == 0) ? player1 : player2;
        Player secondPlayer = (firstPlayer == player1) ? player2 : player1;

        while (!gameEnded) {
            System.out.println("Ronda " + round + ":");
            // Determina qual bot joga com base na ronda atual
            int botIndexPlayer1 = (round - 1) % firstPlayer.getBots().size(); // Ciclo pelos bots do player 1
            int botIndexPlayer2 = (round - 1) % secondPlayer.getBots().size(); // Ciclo pelos bots do player 2

            // Execute the turn for the first player
            gameEnded = executeBotTurn(firstPlayer, firstPlayer.getBots().get(botIndexPlayer1), secondPlayer.getFlag().getLocation(), gameMap, secondPlayer);
            if (gameEnded) {
                System.out.println(firstPlayer.getName() + " venceu o jogo!");
                resetGameState();
                break;
            }

            // Execute the turn for the second player
            gameEnded = executeBotTurn(secondPlayer, secondPlayer.getBots().get(botIndexPlayer2), firstPlayer.getFlag().getLocation(), gameMap, firstPlayer);
            if (gameEnded) {
                System.out.println(secondPlayer.getName() + " venceu o jogo!");
                resetGameState();
                break;
            }

            round++;
        }
    }

    public  void setFlagsMenu(Player player) {
        System.out.println(player.getName() + ", escolha a localização da sua bandeira [0-" + (gameMap.getNetwork().size() - 1) + "]: ");
        int flagLocation = scanner.nextInt();

        // Verificação da validade da localização e se ela já foi escolhida
        while (flagLocation < 0 || flagLocation >= gameMap.getNetwork().size() || (player1.getFlag() != null && player1.getFlag().getLocation() == flagLocation) || (player2.getFlag() != null && player2.getFlag().getLocation() == flagLocation)) {
            if (flagLocation < 0 || flagLocation >= gameMap.getNetwork().size()) {
                System.out.println("Localização inválida. Por favor, escolha um número entre 0 e " + (gameMap.getNetwork().size() - 1) + ".");
            } else {
                System.out.println("Localização já escolhida pelo outro jogador. Escolha outra localização.");
            }
            flagLocation = scanner.nextInt();
        }

        player.setFlag(new Flag(flagLocation));
        System.out.println(player.getName() + " definiu a bandeira na localização: " + flagLocation);
    }

    private static void generateMapMenu() {
        try {
            System.out.println("Insira a quantidade de localizações [10-100]: ");
            int numLocations = scanner.nextInt();

            System.out.println("Caminhos bidirecionais ? [true/false]: ");
            boolean isBidirectional = scanner.nextBoolean();

            System.out.println("Insira a densidade das arestas [0-1]: ");
            double density = scanner.nextDouble();

            gameMap = new GameMap(numLocations, isBidirectional, density);
            gameMap.printMap();

            System.out.println("O mapa foi criado com sucesso!");
            // Exportação opcional do mapa
            System.out.println("Deseja exportar o mapa para um arquivo? [true/false]: ");
            boolean export = scanner.nextBoolean();
            if (export) {
                System.out.println("Insira o nome do arquivo para salvar o mapa (ex: map.json): ");
                String filename = scanner.next();
                JsonUtil.exportNetworkToJson(gameMap.getNetwork(), filename);
                System.out.println("Mapa exportado com sucesso para " + filename);
            }
        } catch (EmptyCollectionException e) {
            System.err.println("Erro ao gerar o mapa: " + e.getMessage());
        }
    }

    private static void importMapMenu() {
        System.out.println("Digite o nome do arquivo para importar o mapa: ");
        String filename = scanner.next();

        boolean isBidirectional = false;

        try {
            Network<Integer> importedNetwork = JsonUtil.importNetworkFromJson(filename, isBidirectional);
            System.out.println("Grafo importado com sucesso!");

            // Aqui você precisa atualizar o gameMap com o grafo importado
            gameMap = new GameMap(importedNetwork);
            gameMap.printNetwork(importedNetwork);
        } catch (IOException e) {
            System.err.println("Erro ao importar o grafo: " + e.getMessage());
        }
    }

    private void resetGameState() {
        // Limpar as bandeiras dos jogadores
        player1.setFlag(null);
        player2.setFlag(null);

        // Limpar os bots dos jogadores
        player1.clearBots();
        player2.clearBots();

        // Reset dos jogadores
        player1 = new Player("Player 1");
        player2 = new Player("Player 2");

        // Reset do GameMap para garantir que um novo mapa seja gerado ou importado
        gameMap = null;
    }
}


