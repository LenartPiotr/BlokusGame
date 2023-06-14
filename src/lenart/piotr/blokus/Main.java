package lenart.piotr.blokus;

import lenart.piotr.blokus.basic.Vector2i;
import lenart.piotr.blokus.console.Console;
import lenart.piotr.blokus.engine.client.LocalClientAdapter;
import lenart.piotr.blokus.engine.exceptions.WrongActionException;
import lenart.piotr.blokus.engine.game.GameService;
import lenart.piotr.blokus.engine.game.IGameService;
import lenart.piotr.blokus.network.Server;
import lenart.piotr.blokus.view.GameWindow;

public class Main {

    public static void main(String[] args) throws WrongActionException {
        switch (Console.select(new String[]{ "Host new game", "Join to game" })) {
            case 0 -> server();
            case 1 -> client();
        }
    }

    public static void server() {
        int maxPlayers = Console.getInt("Enter room slots (1-4)");
        maxPlayers = Math.min(4, Math.max(1, maxPlayers));

        String nickname = Console.getText("Enter your nickname");

        IGameService gameService = new GameService();
        Server networkServer = new Server(gameService);
        try {
            gameService.setBoardSize(new Vector2i(20, 20));
            gameService.setPlayersCount(maxPlayers);

            networkServer.run();
            LocalClientAdapter localClient = new LocalClientAdapter(nickname, gameService);

            GameWindow window = new GameWindow(localClient);

            boolean endWhile = false;
            while (!endWhile) {
                try {
                    switch (Console.select(new String[]{ "Start game", "Exit" })) {
                        case 0 -> gameService.init();
                        case 1 -> endWhile = true;
                    }
                }catch (WrongActionException e) {
                    System.out.println(e.getTextToDisplay());
                }
            }
            networkServer.stop();
        }
        catch (WrongActionException e) {
            System.out.println(e.getTextToDisplay());
            networkServer.stop();
        }
    }

    public static void client() {
        //
    }
}
