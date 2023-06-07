package lenart.piotr.blokus;

import lenart.piotr.blokus.basic.Vector2i;
import lenart.piotr.blokus.engine.client.LocalClientAdapter;
import lenart.piotr.blokus.engine.exceptions.WrongActionException;
import lenart.piotr.blokus.engine.game.GameService;
import lenart.piotr.blokus.engine.game.IGameService;
import lenart.piotr.blokus.view.GameWindow;

import javax.swing.*;

public class Main {

    public static void main(String[] args) throws WrongActionException {
        IGameService gameService = new GameService();
        gameService.setBoardSize(new Vector2i(20, 20));
        gameService.setPlayersCount(4);

        LocalClientAdapter localClient = new LocalClientAdapter("Gracz 1", gameService);

        GameWindow window = new GameWindow(localClient);
        window.setTitle("Title");
        window.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        window.pack();
        // window.setLocationRelativeTo(null);
        window.setVisible(true);
        window.setSize(400, 300);
    }
}
