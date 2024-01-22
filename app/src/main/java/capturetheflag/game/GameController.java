package capturetheflag.game;

import capturetheflag.exceptions.EmptyCollectionException;
import capturetheflag.structures.Network;
import capturetheflag.utils.JsonUtil;

import java.io.IOException;
import java.util.Random;
import java.util.Scanner;

import static capturetheflag.game.GameUtils.executeBotTurn;
import static capturetheflag.game.GameUtils.initializeBots;

import java.util.Timer;
import java.util.TimerTask;

public class GameController {
    private static GameMap gameMap;
    private Player player1;
    private Player player2;
    private static Scanner scanner;
    private static boolean isGameRunning = false;

    public GameController() {
        this.scanner = new Scanner(System.in);
        this.player1 = new Player("Player 1");
        this.player2 = new Player("Player 2");
    }

    private int readIntSafely() {
        while (true) {
            if (scanner.hasNextInt()) {
                return scanner.nextInt();
            } else {
                System.out.println("\n[ERRO]: Entrada inválida. Por favor, insira um número inteiro: ");
                scanner.next();
            }
        }
    }

    public void start() throws EmptyCollectionException {
        System.out.println("=================================");
        System.out.println("| Bem-vindo ao Capture the Flag! |");
        System.out.println("=================================");
        boolean running = true;

        while (running) {
            if (!isGameRunning) {
                System.out.println("1. Gerar novo mapa");
                System.out.println("2. Importar mapa existente");
                System.out.println("3. Iniciar jogo");
                System.out.println("4. Sair");
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
        setFlagsMenu(player1);
        setFlagsMenu(player2);

        int numberOfBots;

        do {
            System.out.print("Escolha o número de bots (3-5): ");
            numberOfBots = scanner.nextInt();
        } while (numberOfBots < 3 || numberOfBots > 5);

        initializeBots(player1, numberOfBots, player1.getName(), scanner, gameMap);
        initializeBots(player2, numberOfBots, player2.getName(), scanner, gameMap);

        System.out.println("aqui crl");
        player1.printBotsInfo();
        player2.printBotsInfo();
    }

    private void startGame() throws EmptyCollectionException {
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
                    } catch (EmptyCollectionException e) {
                        e.printStackTrace();
                    }

                    // Executar turno do segundo jogador se o jogo não tiver terminado
                    boolean gameEndedPlayer2 = false;
                    if (!gameEndedPlayer1) {
                        try {
                            gameEndedPlayer2 = executeBotTurn(secondPlayer, secondPlayer.getBots().get((round[0] - 1) % secondPlayer.getBots().size()),
                                    firstPlayer.getFlag().getLocation(), gameMap, firstPlayer);
                        } catch (EmptyCollectionException e) {
                            e.printStackTrace();
                        }
                    }

                    // Verificar se o jogo terminou
                    if (gameEndedPlayer1 || gameEndedPlayer2) {
                        System.out.println("\n"+(gameEndedPlayer1 ? firstPlayer.getName() : secondPlayer.getName()) + " venceu o jogo!\n");
                        timer.cancel(); // Cancelar o temporizador
                        resetGameState(); // Resetar o estado do jogo
                        isGameRunning = false; // O jogo terminou
                        return;
                    }

                    round[0]++;
                } else {
                    System.out.println("O jogo terminou após 10 rodadas sem um vencedor.");
                    timer.cancel();
                    resetGameState();
                    isGameRunning = false; // O jogo terminou
                }
            }
        };

        timer.scheduleAtFixedRate(task, 0, delay * 2); // Agendar a tarefa para executar a cada 2 segundos
    }

    public  void setFlagsMenu(Player player) {
        int flagLocation;

        do {
            System.out.print(player.getName() + ", escolha a localização da sua bandeira [0-" + (gameMap.getNetwork().size() - 1) + "]: ");
            flagLocation = scanner.nextInt();

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
        try {
            //TODO colocar limite se for para utilizar.
            System.out.print("Insira a quantidade de localizações [10-100]: ");
            int numLocations = scanner.nextInt();

            System.out.print("Caminhos bidirecionais ? [true/false]: ");
            boolean isBidirectional = scanner.nextBoolean();

            System.out.print("Insira a densidade das arestas [0-1]: ");
            double density = scanner.nextDouble();

            gameMap = new GameMap(numLocations, isBidirectional, density);
//            gameMap.printVisualMap();

            System.out.println("[MESSAGEM]: O mapa foi criado com sucesso!");
            // Exportação opcional do mapa
            System.out.print("Deseja exportar o mapa para um arquivo? [true/false]: ");
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

            gameMap = new GameMap(importedNetwork);
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


