package org.example.ttt.client;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Control;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.example.ttt.model.Command;
import org.example.ttt.model.CommandType;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;


public class GuiOperations {

    public static void initializeGameWindow(String fxmlFilePath, String windowTitle, TextField username, String oponentUsername, String player, CommandType commandType) {
        Stage stage = new Stage();
        FXMLLoader fxmlLoader = initializeFXMLLoader(fxmlFilePath);
        Scene scene = new Scene(fxmlLoader.getRoot(), 676, 474);

        GameBoardController controller = fxmlLoader.getController();
        updateControllerSettings(controller, commandType, player, username.getText(), oponentUsername);

        stage.setTitle(windowTitle);
        stage.setScene(scene);
        stage.show();

        closeCurrentStage(username);

        if (commandType.equals(CommandType.OK_WAIT)) {
            handleOkWaitCommand(fxmlLoader);
        }
    }

    public static FXMLLoader initializeFXMLLoader(String fxmlFilePath) {
        FXMLLoader fxmlLoader = new FXMLLoader(GameBoardController.class.getResource(fxmlFilePath));
        try {
            fxmlLoader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return fxmlLoader;
    }

    private static void updateControllerSettings(GameBoardController controller, CommandType commandType, String player, String username, String oponentUsername) {
        if(player.equals("X")){
            controller.setUserX(username);
            controller.setUserO(oponentUsername);
        } else {
            controller.setUserO(username);
            controller.setUserX(oponentUsername);

        }
        for (Button panelButton : controller.buttons) {
            if (commandType.equals(CommandType.OK_GO)) {
                panelButton.setDisable(false);
                controller.setWhichTurn("Your turn");
            } else {
                panelButton.setDisable(true);
                controller.setWhichTurn("Enemy's turn");
            }
        }
        controller.setWhichPlayer(player);
    }

    public static void closeCurrentStage(Control control) {
        Stage currentStage = (Stage) control.getScene().getWindow();
        currentStage.close();
    }

    private static void handleOkWaitCommand(FXMLLoader fxmlLoader) {
        CompletableFuture<Command> future = CompletableFuture.supplyAsync(ClientSocketConnection::readCommandMessage);
        future.thenAccept(result -> Platform.runLater(() -> {
            System.out.println("Got first result from the org.example.ttt.server");
            GameBoardController controller = fxmlLoader.getController();
            updateButtonsWithGameResults(controller, result);
            enableEmptyButtons(controller.buttons);
            controller.setWhichTurn("Your turn");
        }));
    }

    private static void updateButtonsWithGameResults(GameBoardController controller, Command result) {
        for (Button panelButton : controller.buttons) {
            String buttonId = panelButton.getId().substring(6);
            int id = Integer.parseInt(buttonId) - 1;
            panelButton.setText(result.getGameResults().get(id));
        }
    }

    private static void enableEmptyButtons(ArrayList<Button> buttons) {
        for (Button panelButton : buttons) {
            if ("".equalsIgnoreCase(panelButton.getText())) {
                panelButton.setDisable(false);
            }
        }
    }

    public static void showAlert(String message, String title) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static Alert showInformation(String message) {
        Alert alert = new Alert(Alert.AlertType.NONE);
        alert.setTitle("Information");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.show();
        return alert;
    }
}
