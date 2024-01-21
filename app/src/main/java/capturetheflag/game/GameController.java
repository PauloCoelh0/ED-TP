package capturetheflag.game;

import capturetheflag.exceptions.EmptyCollectionException;
import capturetheflag.structures.Network;
import capturetheflag.utils.JsonUtil;

import java.io.IOException;
import java.util.Scanner;

public class GameController {
    private static GameMap gameMap;
    private static Scanner scanner;

    public GameController() {
        this.scanner = new Scanner(System.in);
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
                    System.out.println("Iniciar jogo");
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
}



