package lenart.piotr.blokus.network;

import lenart.piotr.blokus.basic.Vector2i;
import lenart.piotr.blokus.engine.client.IGameClient;
import lenart.piotr.blokus.engine.exceptions.WrongActionException;
import lenart.piotr.blokus.engine.game.IGameService;
import lenart.piotr.blokus.engine.game.endgame.EndgameData;
import lenart.piotr.blokus.engine.puzzle.IPuzzle;

import java.io.*;
import java.net.Socket;
import java.util.List;

public class NetGameClient {

    private final Socket socket;
    private ObjectInputStream input;
    private ObjectOutputStream output;

    private int index;
    private String name;
    private IGameService gameService;
    private Thread listenThread;

    public NetGameClient(Socket socket, IGameService gameService) {
        this.socket = socket;
        /*try {
            index = gameService.registerClient(this);
        } catch (WrongActionException e) {
            try {
                socket.close();
            } catch (IOException ignored) { }
            return;
        }
        try {
            output = new ObjectOutputStream(socket.getOutputStream());
            input = new ObjectInputStream(socket.getInputStream());
        } catch (IOException ignored) {
            try {
                socket.close();
            } catch (IOException ignored2) { }
            return;
        }

        createListenThread();
        listenThread.start();*/
    }

    private void createListenThread() {
        listenThread = new Thread(() -> {
            //
        });
    }
}
