package org.example.ttt.client;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import org.example.ttt.model.Command;
import org.example.ttt.model.CommandType;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.CompletableFuture;


public class WelcomeViewController implements Initializable {

    @FXML
    private TextField usernameField;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        CompletableFuture<Boolean> future = CompletableFuture.supplyAsync(() -> {
            ClientSocketConnection.initialize();
            return true;
        });
    }

    @FXML
    void joinGame(ActionEvent event) {
        Command command = new Command(CommandType.INIT_REQUEST, usernameField.getText());
        ClientSocketConnection.sendCommandMessage(command);

        CompletableFuture<Command> future = CompletableFuture.supplyAsync(ClientSocketConnection::readCommandMessage);
        future.thenAccept(this::handleJoinGameResult);

    }


    private void handleJoinGameResult(Command result) {
        Platform.runLater(() -> {
            System.out.println("1: " + result);
            if (result.getCommandType().equals(CommandType.OK_GO) || result.getCommandType().equals(CommandType.OK_WAIT)) {
                GuiOperations.initializeGameWindow("game-board.fxml", "TTT", usernameField, result.getOponentName(), result.getMessage(), result.getCommandType());
            } else if (result.getCommandType().equals(CommandType.WAIT)) {
                handleWaitCommand(result);
            } else if (result.getCommandType().equals(CommandType.ERROR)) {
                System.out.println("Error from org.example.ttt.server: " + result);
                GuiOperations.showAlert("Error initializing game", "Error");
            }
        });
    }

    private void handleWaitCommand(Command result) {
        Alert alert = GuiOperations.showInformation("Wait for the second player");
        CompletableFuture<Command> future2 = CompletableFuture.supplyAsync(ClientSocketConnection::readCommandMessage);
        future2.thenAccept(result2 -> Platform.runLater(() -> {
            System.out.println("2: " + result2);
            alert.setResult(ButtonType.CANCEL);
            handleSecondPlayerResult(result2);
        }));
    }

    private void handleSecondPlayerResult(Command result2) {
        if (result2.getCommandType().equals(CommandType.OK_GO) || result2.getCommandType().equals(CommandType.OK_WAIT)) {
            GuiOperations.initializeGameWindow("game-board.fxml", "TTT", usernameField, result2.getOponentName(), result2.getMessage(), result2.getCommandType());
        } else {
            GuiOperations.showAlert("Unexpected error", "Error");
        }
    }

    public void setUsernameText(String username){
        this.usernameField.setText(username);
    }
}