package lenart.piotr.blokus.network;

import lenart.piotr.blokus.engine.game.IGameService;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    Thread thread;
    ServerSocket serverSocket;
    public Server(IGameService gameService){
        thread = new Thread(() -> {
            try {
                int port = 8080;
                serverSocket = new ServerSocket(port);
                System.out.println("Server start to listen on port " + port);
                try {
                    while (!thread.isInterrupted()) {
                        Socket client = serverSocket.accept();
                        new NetGameClient(client, gameService);
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
    }
}
