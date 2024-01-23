package tp_ed.client;

import tp_ed.structures.exceptions.EmptyCollectionException;
import tp_ed.capturetheflag.game.GameController;

public class
DemoCaptureTheFlag {
    public static void main(String[] args) throws EmptyCollectionException {
        GameController gameController = new GameController();
        gameController.start();
    }
}
