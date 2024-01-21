package capturetheflag;

import capturetheflag.exceptions.EmptyCollectionException;
import capturetheflag.game.GameController;

public class
DemoCaptureTheFlag {
    public static void main(String[] args) throws EmptyCollectionException {
        GameController gameController = new GameController();
        gameController.start();
    }
}
