package tp_ed.capturetheflag.game;

import tp_ed.structures.exceptions.ElementNotFoundException;
import tp_ed.structures.exceptions.EmptyCollectionException;
import tp_ed.structures.Network;
import tp_ed.capturetheflag.utils.JsonUtil;

import java.io.IOException;
import java.util.Random;
import java.util.Scanner;

import static tp_ed.capturetheflag.game.GameRules.*;
import static tp_ed.capturetheflag.game.GameUtils.executeBotTurn;
import static tp_ed.capturetheflag.game.GameUtils.initializeBots;

import java.util.Timer;
import java.util.TimerTask;

public class GameController {
    private static GameMap gameMap;
    private Player player1;
    private Player player2;
    public static Scanner scanner;
    private static boolean isGameRunning = false;

    public GameController() {
        scanner = new Scanner(System.in);
        this.player1 = new Player("Player 1");
        this.player2 = new Player("Player 2");
    }

    public void start() {
        System.out.println("\n\n=================================");
        System.out.println("| Bem-vindo ao Capture the Flag! |");
        System.out.println("=================================\n");
        boolean running = true;

        while (running) {
            if (!isGameRunning) {
                System.out.println("1. Gerar novo mapa");
                System.out.println("2. Importar mapa existente");
                System.out.println("3. Iniciar jogo");
                System.out.println("4. Regras");
                System.out.println("5. Sair");
                System.out.print("Escolha uma opção: ");
                int choice = readIntSafely();

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
                            System.out.println("\n[ERRO]: Por favor, gere ou importe um mapa antes de iniciar o jogo.\n");
                        }
                        break;
                    case 4:
                        displayRules();
                        break;
                    case 5:
                        running = false;
                        System.out.println("\n[MESSAGEM]: Saindo...\n");
                        break;
                    default:
                        System.out.println("\n[ERRO]: Opção Inválida.\n");
                }
            }

            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                System.out.println("Erro: " + e.getMessage());
            }
        }
    }

    private void setupGame() {
        int numberOfBots;

        setFlagsMenu(player1);
        setFlagsMenu(player2);

        do {
            System.out.print("Escolha o número de bots (3-5): ");
            numberOfBots = readIntSafely();
            if (numberOfBots < 3 || numberOfBots > 5) {
                System.out.println("[ERRO]: Número inválido. Por favor, escolha um número entre 3 e 5.");
            }
        } while (numberOfBots < 3 || numberOfBots > 5);

        initializeBots(player1, numberOfBots, player1.getName(), gameMap);
        initializeBots(player2, numberOfBots, player2.getName(), gameMap);

        player1.printBotsInfo();
        player2.printBotsInfo();
    }

    private void startGame() {
        System.out.println("\n[MESSAGEM]: O jogo começou!\n");
        isGameRunning = true;

        Timer timer = new Timer();
        int delay = 1000; // 1000 ms = 1 segundo entre turnos

        // Use um objeto Random para escolher aleatoriamente o primeiro jogador
        Random random = new Random();
        int firstPlayerIndex = random.nextInt(2); // 0 ou 1 (Player 1 ou Player 2)
        Player firstPlayer = (firstPlayerIndex == 0) ? player1 : player2;
        Player secondPlayer = (firstPlayer == player1) ? player2 : player1;
        final int[] round = {1};

        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                if (round[0] <= 100) { // Limitar o número de rodadas, por exemplo, a 10
                    System.out.println("Ronda " + round[0] + ":");

                    // Executar turno do primeiro jogador
                    boolean gameEndedPlayer1 = false;
                    try {
                        gameEndedPlayer1 = executeBotTurn(firstPlayer, firstPlayer.getBots().get((round[0] - 1) % firstPlayer.getBots().size()),
                                secondPlayer.getFlag().getLocation(), gameMap, secondPlayer);
                    } catch (EmptyCollectionException | ElementNotFoundException e) {
                        e.printStackTrace();
                    }

                    // Executar turno do segundo jogador se o jogo não tiver terminado
                    boolean gameEndedPlayer2 = false;
                    if (!gameEndedPlayer1) {
                        try {
                            gameEndedPlayer2 = executeBotTurn(secondPlayer, secondPlayer.getBots().get((round[0] - 1) % secondPlayer.getBots().size()),
                                    firstPlayer.getFlag().getLocation(), gameMap, firstPlayer);
                        } catch (EmptyCollectionException | ElementNotFoundException e) {
                            e.printStackTrace();
                        }
                    }

                    // Verificar se o jogo terminou
                    if (gameEndedPlayer1 || gameEndedPlayer2) {
                        String arteAscii =
                                """
                                        __/\\\\\\________/\\\\\\__/\\\\\\\\\\\\\\\\\\\\\\__/\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\_______/\\\\\\\\\\_________/\\\\\\\\\\\\\\\\\\______/\\\\\\\\\\\\\\\\\\\\\\_____/\\\\\\\\\\\\\\\\\\____       \s
                                         _\\/\\\\\\_______\\/\\\\\\_\\/////\\\\\\///__\\///////\\\\\\/////______/\\\\\\///\\\\\\_____/\\\\\\///////\\\\\\___\\/////\\\\\\///____/\\\\\\\\\\\\\\\\\\\\\\\\\\__      \s
                                          _\\//\\\\\\______/\\\\\\______\\/\\\\\\___________\\/\\\\\\_________/\\\\\\/__\\///\\\\\\__\\/\\\\\\_____\\/\\\\\\_______\\/\\\\\\______/\\\\\\/////////\\\\\\_     \s
                                           __\\//\\\\\\____/\\\\\\_______\\/\\\\\\___________\\/\\\\\\________/\\\\\\______\\//\\\\\\_\\/\\\\\\\\\\\\\\\\\\\\\\/________\\/\\\\\\_____\\/\\\\\\_______\\/\\\\\\_    \s
                                            ___\\//\\\\\\__/\\\\\\________\\/\\\\\\___________\\/\\\\\\_______\\/\\\\\\_______\\/\\\\\\_\\/\\\\\\//////\\\\\\________\\/\\\\\\_____\\/\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\_   \s
                                             ____\\//\\\\\\/\\\\\\_________\\/\\\\\\___________\\/\\\\\\_______\\//\\\\\\______/\\\\\\__\\/\\\\\\____\\//\\\\\\_______\\/\\\\\\_____\\/\\\\\\/////////\\\\\\_  \s
                                              _____\\//\\\\\\\\\\__________\\/\\\\\\___________\\/\\\\\\________\\///\\\\\\__/\\\\\\____\\/\\\\\\_____\\//\\\\\\______\\/\\\\\\_____\\/\\\\\\_______\\/\\\\\\_ \s
                                               ______\\//\\\\\\________/\\\\\\\\\\\\\\\\\\\\\\_______\\/\\\\\\__________\\///\\\\\\\\\\/_____\\/\\\\\\______\\//\\\\\\__/\\\\\\\\\\\\\\\\\\\\\\_\\/\\\\\\_______\\/\\\\\\_\s
                                                _______\\///________\\///////////________\\///_____________\\/////_______\\///________\\///__\\///////////__\\///________\\///__""";

                        System.out.println("\n" + arteAscii);
                        System.out.println("\n["+(gameEndedPlayer1 ? firstPlayer.getName() : secondPlayer.getName())+"]" + " VENCEU O JOGO!!!\n");
                        Player winningPlayer = gameEndedPlayer1 ? firstPlayer : secondPlayer;
                        Bot winningBot = winningPlayer.getBots().get((round[0] - 1) % winningPlayer.getBots().size());
                        gameMap.highlightWinningBot(winningPlayer.getName(), winningBot.getBotNumber());
                        timer.cancel(); // Cancelar o temporizador
                        resetGameState(); // Resetar o estado do jogo
                        isGameRunning = false; // O jogo terminou
                        return;
                    }

                    round[0]++;
                } else {
                    System.out.println("O jogo terminou após 100 rodadas sem um vencedor.");
                    timer.cancel();
                    resetGameState();
                    isGameRunning = false;
                }
            }
        };

        timer.scheduleAtFixedRate(task, 0, delay * 2); // Agendar a tarefa para executar a cada 2 segundos
    }

    public void setFlagsMenu(Player player) {
        int flagLocation;

        do {
            System.out.print(player.getName() + ", escolha a localização da sua bandeira [0-" + (gameMap.getNetwork().size() - 1) + "]: ");
            flagLocation = readIntSafely();

            if (flagLocation < 0 || flagLocation >= gameMap.getNetwork().size()) {
                System.out.println("\n[ERRO]: Localização inválida.");
            } else if (!GameRules.hasMinimumAdjacentVertices(gameMap.getNetwork(), flagLocation, 2)) {
                System.out.println("\n[ERRO]: Escolha uma localização que tenha pelo menos dois vértices adjacentes.");
            } else if ((player1.getFlag() != null && player1.getFlag().getLocation() == flagLocation) || (player2.getFlag() != null && player2.getFlag().getLocation() == flagLocation)) {
                System.out.println("\n[ERRO]: Localização já escolhida pelo outro jogador. Escolha outra localização.");
            } else {
                break;
            }
        } while (true);

        player.setFlag(new Flag(flagLocation));
        System.out.println(player.getName() + " definiu a bandeira na localização: " + flagLocation);
        if (player == player1) {
            gameMap.setFlagLocationPlayer1(flagLocation);
        } else if (player == player2) {
            gameMap.setFlagLocationPlayer2(flagLocation);
        }
        gameMap.updateVisualMap();
    }

    private static void generateMapMenu() {
        int numLocations;
        boolean isBidirectional;
        double density;

        try {
            do {
                System.out.print("Insira a quantidade de localizações [10-100]: ");
                numLocations = readIntSafely();
            } while (numLocations < 10 || numLocations > 100);

            System.out.print("Caminhos bidirecionais ? [true/false]: ");
            isBidirectional = readBooleanSafely();

            do {
                System.out.print("Insira a densidade das arestas [0-1] (Ex: 0,2): ");
                density = readDoubleSafely();
            } while (density < 0 || density > 1);

            gameMap = new GameMap(numLocations, isBidirectional, density);

            System.out.println("[MENSSAGEM]: O mapa foi criado com sucesso!");

            System.out.print("Deseja exportar o mapa para um arquivo? [true/false]: ");
            boolean export = readBooleanSafely();
            if (export) {
                String filename;
                do {
                    System.out.print("Insira o nome do ficheiro para guardar o mapa (ex: mapa.json): ");
                    filename = scanner.next();
                    if (!filename.endsWith(".json")) {
                        System.out.println("[ERRO]: O nome do ficheiro deve terminar em '.json'.");
                    }
                } while (!filename.endsWith(".json"));

                JsonUtil.exportNetworkToJson(gameMap.getNetwork(), filename);
                System.out.println("Mapa exportado com sucesso para " + filename);
            }
        } catch (EmptyCollectionException e) {
            System.err.println("Erro ao gerar o mapa: " + e.getMessage());
        }
    }

    private static void importMapMenu() {
        String filename;

        do {
            System.out.print("Digite o nome do arquivo para importar o mapa (ex: mapa.json): ");
            filename = scanner.next();
            if (!filename.endsWith(".json")) {
                System.out.println("[ERRO]: O nome do arquivo deve terminar com '.json'.");
            }
        } while (!filename.endsWith(".json"));

        try {
            Network<Integer> importedNetwork = JsonUtil.importNetworkFromJson(filename);
            System.out.println("Mapa importado com sucesso!");

            gameMap = new GameMap(importedNetwork);
        } catch (IOException e) {
            System.err.println("Erro ao importar o mapa: " + e.getMessage());
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

    private void displayRules() {
        System.out.println("\n============================ REGRAS DO JOGO ===========================");
        System.out.println("| 1. Cada jogador posiciona a sua bandeira num local do mapa.         |");
        System.out.println("| 2. Cada jogador tem um conjunto de bots ([3] Min & [5] Max).        |");
        System.out.println("| 3. Existem [3] tipos de BOTs: [ATAQUE] [DEFESA] [SUPORTE].          |");
        System.out.println("| 4. Cada jogador tem de possuir no minimo um bot de cada tipo.       |");
        System.out.println("| 5. O jogo termina quando um bot captura a bandeira do adversário.   |");
        System.out.println("=======================================================================\n");
    }
}


