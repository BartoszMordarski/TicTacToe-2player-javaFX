package org.example.ttt.server;

import org.example.ttt.model.Game;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private final ServerSocket serverSocket;
    private final Game game;

    public Server(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
        game = new Game();
    }

    public void stopServer() {
        try {
            if (serverSocket != null) {
                serverSocket.close();
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void startServer() {
        try {
            while (!serverSocket.isClosed()) {
                Socket socket = serverSocket.accept();
                System.out.println("****New client has connected****");
                ClientHandler clientHandler = new ClientHandler(socket, this, game);

                Thread thread = new Thread(clientHandler);
                thread.start();
            }
        } catch (IOException e) {
            stopServer();
        }
    }

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(2222);
        Server server = new Server(serverSocket);
        server.startServer();

    }





}