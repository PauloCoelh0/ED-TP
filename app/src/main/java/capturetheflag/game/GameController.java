package capturetheflag.game;

import capturetheflag.exceptions.EmptyCollectionException;
import java.util.Scanner;

public class GameController {
    private static Scanner scanner;

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
                    break;
                case 2:
                    break;
                case 3:
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
}



