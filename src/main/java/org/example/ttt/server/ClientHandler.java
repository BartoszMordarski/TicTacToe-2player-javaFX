package org.example.ttt.server;

import org.example.ttt.model.Command;
import org.example.ttt.model.CommandType;
import org.example.ttt.model.Game;

import java.io.*;
import java.net.Socket;
import java.util.Date;

import static org.example.ttt.client.ClientSocketConnection.close;


public class ClientHandler implements Runnable {

    private Socket socket;
    private Server server;
    private BufferedWriter bufferedWriter;
    private BufferedReader bufferedReader;
    private String username;
    private ObjectInputStream objectInputStream;
    private ObjectOutputStream objectOutputStream;


    private Game game;

    public ClientHandler(Socket socket, Server server, Game game) {
        try {
            this.game = game;
            this.socket = socket;
            this.server = server;

            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.objectInputStream = new ObjectInputStream(socket.getInputStream());
            this.objectOutputStream = new ObjectOutputStream(socket.getOutputStream());

            this.username = null;


        } catch (IOException e) {
            closeEverything();
        }
    }

    public void sendCommand(Command command) {
        try {
            objectOutputStream.writeObject(command);
            objectOutputStream.reset();
        } catch (IOException e) {
            System.out.println("exception while sending command");
            close();
        }
    }

    private void gameLogic() throws IOException, InterruptedException, ClassNotFoundException {
        boolean firstMoveInTheGame = true;
        while (socket.isConnected()) {
            firstMoveInTheGame = waitUntilPlayerTurn();
            if (!firstMoveInTheGame) {
                handleGameStatus();
            }
            if (game.isGameOver()) {
                game.reset();
                break;
            } else {
                handlePlayerMove();
                if (game.isGameOver()) {
                    break;
                }
            }
        }
    }

    private boolean waitUntilPlayerTurn() throws InterruptedException {
        boolean hadToWait = false;
        while (!isPlayerTurn() && !game.isGameOver() && !game.isUserDisconnected()) {
            System.out.println("Waiting for turn: " + username + " - " + game.getUserMarks().get(username) + " - " + new Date().getTime());
            Thread.sleep(500);
            hadToWait = true;
        }
        return !hadToWait;
    }

    private boolean isPlayerTurn() {
        return game.getTurn().equals(game.getUserMarks().get(username));
    }

    private void handleGameStatus() {
        System.out.println("Game status before sending to user: " + username + " - " + game);

        if (game.isGameOver()) {
            handleGameWon();
            updateDatabase();
        } else if (game.isUserDisconnected()) {
            sendCommand(new Command(CommandType.GAME_USER_LEFT));
        } else {
            sendCommand(new Command(CommandType.GAME_STATUS, game.getButtons()));
        }

    }

    private void updateDatabase(){
        if(!DatabaseManager.doesUserExist(game.getUserX())) DatabaseManager.addPlayer(game.getUserX());
        if(!DatabaseManager.doesUserExist(game.getUserO())) DatabaseManager.addPlayer(game.getUserO());

        if(game.getWinnerSymbol().equalsIgnoreCase("X")){
            DatabaseManager.incrementGamesWon(game.getUserX());
            DatabaseManager.incrementGamesLost(game.getUserO());
        } else {
            DatabaseManager.incrementGamesWon(game.getUserO());
            DatabaseManager.incrementGamesLost(game.getUserX());
        }
    }

    private void handleGameWon() {
        System.out.println("Game won by " + game.getWinnerSymbol());
        sendCommand(new Command(CommandType.GAME_OVER, game.getWinnerSymbol(), game.getButtons()));
    }

    private void handlePlayerMove() throws IOException, ClassNotFoundException {
        Command receivedCommand = (Command) objectInputStream.readObject();
        System.out.println("MOVE from client: " + username + " - " + receivedCommand);
        int cellId = Integer.parseInt(receivedCommand.getMessage());

        updateGameAfterPlayerMove(cellId);
        checkAndHandleGameResult();
    }

    private void updateGameAfterPlayerMove(int cellId) {
        game.getButtons().set(cellId - 1, game.getUserMarks().get(username));
        game.setTurn("X".equalsIgnoreCase(game.getUserMarks().get(username)) ? "O" : "X");
    }

    private void checkAndHandleGameResult() {
        GameChecker.checkIfGameIsOver(game);
        if (game.isGameOver()) {
            handleGameWon();
        } else if (game.isUserDisconnected()) {
            sendCommand(new Command(CommandType.GAME_USER_LEFT));
        }
    }


    private void playerAssignLogic() throws IOException, InterruptedException, ClassNotFoundException {
        if (socket.isConnected()) {
            Command receivedCommand = (Command) objectInputStream.readObject();
            username = receivedCommand.getMessage();
            System.out.println("INIT message received: " + receivedCommand);
            System.out.println("State of game: " + game);

            if (isGameFull()) {
                sendCommand(new Command(CommandType.ERROR, "game is full"));
            } else {
                assignUserToGame();
            }
        }
    }

    private boolean isGameFull() {
        return game.getUserX() != null && game.getUserO() != null;
    }

    private void assignUserToGame() throws InterruptedException {
        if (game.getUserX() == null) {
            assignUserToX();
        } else {
            assignUserToO();
        }
    }

    private void assignUserToX() throws InterruptedException {
        game.setUserX(username);
        game.getUserMarks().put(username, "X");
        game.setTurn("O");
        sendCommand(new Command(CommandType.WAIT));

        while (game.getUserO() == null) {
            Thread.sleep(500);
        }

        if (game.getTurn().equals("X")) {
            sendCommand(new Command(CommandType.OK_GO, "X", game.getUserO()));
        } else {
            sendCommand(new Command(CommandType.OK_WAIT, "X", game.getUserO()));
        }
    }

    private void assignUserToO() {
        game.setUserO(username);
        game.getUserMarks().put(username, "O");

        if (game.getTurn().equals("O")) {
            sendCommand(new Command(CommandType.OK_GO, "O", game.getUserX()));
        } else {
            sendCommand(new Command(CommandType.OK_WAIT, "O", game.getUserX()));
        }
    }


    public void closeEverything() {
        System.out.println("Closing socket");
        try {
            if (this.bufferedReader != null) {
                this.bufferedReader.close();
            }
            if (this.bufferedWriter != null) {
                this.bufferedWriter.close();
            }
            if (this.objectInputStream != null) {
                this.objectInputStream.close();
            }
            if (this.objectOutputStream != null) {
                this.objectOutputStream.close();
            }
            if (this.socket != null) {
                this.socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            playerAssignLogic();
            gameLogic();
            System.out.println("Stopped the game for user: " + username);
            closeEverything();

        } catch (IOException e) {
            System.out.println("**** Client has disconnected due to IO Error ****");
            if (!game.isUserDisconnected()) {
                game.setUserDisconnected(true);
            } else {
                game.reset();
            }
            closeEverything();
            throw new RuntimeException(e);
        } catch (InterruptedException | ClassNotFoundException e) {
            System.out.println("****Client has disconnected****");
            throw new RuntimeException(e);
        }
    }
}


