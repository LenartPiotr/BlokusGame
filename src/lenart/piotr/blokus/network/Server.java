package lenart.piotr.blokus.network;

import lenart.piotr.blokus.engine.exceptions.WrongActionException;
import lenart.piotr.blokus.engine.game.IGameService;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server {
    Thread thread;
    ServerSocket serverSocket;

    List<Socket> sockets;

    public Server(IGameService gameService){
        sockets = new ArrayList<>();
        thread = new Thread(() -> {
            try {
                int port = 8080;
                serverSocket = new ServerSocket(port);
                System.out.println("Server start to listen on port " + port);
                try {
                    while (!thread.isInterrupted()) {
                        Socket socket = serverSocket.accept();
                        Client gameClient = new Client(socket);
                        gameClient.listen();
                        try {
                            gameService.registerClient(gameClient);
                        } catch (WrongActionException e) {
                            gameClient.invoke("wrongAction", e.getTextToDisplay());
                            gameClient.stop();
                        }
                    }
                } catch (IOException ignored) { }
                System.out.println("Server stopped");
            } catch (IOException ignored) {
                System.out.println("Cannot create server");
            }
        });
    }

    public void run() {
        thread.start();
    }

    public void stop() {
        thread.interrupt();
        try {
            serverSocket.close();
        } catch (IOException ignored) { }
        for (Socket s : sockets) {
            try {
                s.close();
            } catch (Exception ignored) { }
        }
    }
}
