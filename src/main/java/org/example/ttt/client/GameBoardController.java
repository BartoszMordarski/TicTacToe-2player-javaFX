package org.example.ttt.client;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.example.ttt.model.Command;
import org.example.ttt.model.CommandType;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ResourceBundle;
import java.util.concurrent.CompletableFuture;

import static org.example.ttt.client.GuiOperations.closeCurrentStage;
import static org.example.ttt.client.GuiOperations.initializeFXMLLoader;

public class GameBoardController implements Initializable {

    @FXML
    private Button button1;

    @FXML
    private Button button2;

    @FXML
    private Button button3;

    @FXML
    private Button button4;

    @FXML
    private Button button5;

    @FXML
    private Button button6;

    @FXML
    private Button button7;

    @FXML
    private Button button8;

    @FXML
    private Button button9;

    @FXML
    private Button button10;

    @FXML
    private Button button11;

    @FXML
    private Button button12;

    @FXML
    private Button button13;

    @FXML
    private Button button14;

    @FXML
    private Button button15;

    @FXML
    private Button button16;

    @FXML
    private Text whichPlayer;

    @FXML
    private Text whichTurn;

    @FXML
    private Text userX;

    @FXML
    private Text userO;

    ArrayList<Button> buttons;

    public void setUserX(String userX) {
        this.userX.setText(userX);
    }

    public void setUserO(String userO) {
        this.userO.setText(userO);
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        buttons = new ArrayList<>(Arrays.asList(button1, button2, button3, button4, button5, button6, button7, button8,
                button9, button10, button11, button12, button13, button14, button15, button16));

        buttons.forEach(button -> {
            setupButton(button);
            button.setFocusTraversable(false);
        });
    }

    @FXML
    void restartGame(ActionEvent event) {
        ClientSocketConnection.close();
        restoreWelcomeWindow();
    }

    private void restoreWelcomeWindow() {
        Stage stage = new Stage();
        FXMLLoader fxmlLoader = initializeFXMLLoader("welcome-view.fxml");
        Scene scene = new Scene(fxmlLoader.getRoot());
        WelcomeViewController controller = fxmlLoader.getController();
        controller.setUsernameText(whichPlayer.getText().equalsIgnoreCase("X") ? userX.getText() : userO.getText());

        stage.setTitle("TicTacToe");
        stage.setScene(scene);
        stage.show();

        closeCurrentStage(this.button1);

    }

    @FXML
    void quitGame(ActionEvent event) {
        Stage stage = (Stage) button1.getScene().getWindow();
        stage.close();
    }

    public void setWhichPlayer(String player) {
        whichPlayer.setText(player);
    }

    public void setWhichTurn(String player) {
        whichTurn.setText(player);
    }

    private void setupButton(Button button) {
        button.setStyle("-fx-font-size:30");
        button.setOnMouseClicked(mouseEvent -> {
            handleButtonClick(button);
        });
    }

    private void handleButtonClick(Button button) {
        ClientSocketConnection.sendCommandMessage(new Command(CommandType.GAME_MOVE, button.getId().substring(6)));
        System.out.println("Clicked button ID: " + button.getId().substring(6));

        insertPlayerSymbolIntoButton(button);

        CompletableFuture<Command> gameStatus = CompletableFuture.supplyAsync(ClientSocketConnection::readCommandMessage);
        gameStatus.thenAccept(this::handleGameStatus);
    }

    private void insertPlayerSymbolIntoButton(Button button) {
        button.setText(whichPlayer.getText());
        button.setDisable(true);
        setWhichTurn("Enemy's turn");
        disableAllButtons();
    }

    private void disableAllButtons() {
        for (Button panelButton : buttons) {
            panelButton.setDisable(true);
        }
    }

    private void handleGameStatus(Command result) {
        Platform.runLater(() -> {
            if (result.getCommandType().equals(CommandType.GAME_STATUS)) {
                updateButtonsWithGameResults(result);
            } else if (result.getCommandType().equals(CommandType.GAME_OVER)) {
                handleGameOver(result);
            } else if (result.getCommandType().equals(CommandType.GAME_USER_LEFT)) {
                handleOpponentLeftGame();
            }
        });
    }

    private void updateButtonsWithGameResults(Command result) {
        System.out.println("Update from org.example.ttt.server" + result);
        for (Button panelButton : buttons) {
            String buttonId = panelButton.getId().substring(6);
            int id = Integer.parseInt(buttonId) - 1;
            panelButton.setText(result.getGameResults().get(id));
        }

        setWhichTurn("Your turn");
        enableAvailableButtons();
    }

    private void enableAvailableButtons() {
        for (Button panelButton : buttons) {
            if ("".equalsIgnoreCase(panelButton.getText())) {
                panelButton.setDisable(false);
            }
        }
    }

    private void handleGameOver(Command result) {
        updateButtonsWithGameResults(result);
        setWhichTurn("");
        disableAllButtons();
        if (whichPlayer.getText().equalsIgnoreCase(result.getMessage())) {
            if (whichPlayer.getText().equalsIgnoreCase("X")) {
                GuiOperations.showAlert("Congratulations " + userX.getText() + "! You won!", "Information");
            } else {
                GuiOperations.showAlert("Congratulations " + userO.getText() + "! You won!", "Information");
            }
        } else {
            if (whichPlayer.getText().equalsIgnoreCase("X")) {
                GuiOperations.showAlert("I'm sorry " + userX.getText() + ". You Lost :(", "Information");
            } else {
                GuiOperations.showAlert("I'm sorry " + userO.getText() + ". You Lost :(", "Information");
            }
        }
    }

    private void handleOpponentLeftGame() {
        setWhichTurn("");
        disableAllButtons();
        GuiOperations.showAlert("Your opponent left the game", "Information");
        ClientSocketConnection.close();
        restoreWelcomeWindow();
    }

}