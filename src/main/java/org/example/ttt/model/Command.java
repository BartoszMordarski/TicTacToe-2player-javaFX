package org.example.ttt.model;

import java.io.Serializable;
import java.util.ArrayList;

public class Command implements Serializable {
    private CommandType commandType;
    private String message;
    private String oponentName;
    private ArrayList<String> gameResults;

    public Command(CommandType commandType, String message) {
        this.commandType = commandType;
        this.message = message;
    }

    public Command(CommandType commandType, String message, String oponentName) {
        this.commandType = commandType;
        this.message = message;
        this.oponentName = oponentName;
    }

    public Command(CommandType commandType, ArrayList<String> gameResults) {
        this.commandType = commandType;
        this.gameResults = gameResults;
    }

    public Command(CommandType commandType) {
        this.commandType = commandType;
    }

    public Command(CommandType commandType, String message, ArrayList<String> gameResults) {
        this.commandType = commandType;
        this.message = message;
        this.gameResults = gameResults;
    }

    public String getOponentName() {
        return oponentName;
    }

    public CommandType getCommandType() {
        return commandType;
    }

    public String getMessage() {
        return message;
    }

    public ArrayList<String> getGameResults() {
        return gameResults;
    }

    @Override
    public String toString() {
        return "Command{" +
                "commandType=" + commandType +
                ", message='" + message + '\'' +
                ", gameResults=" + gameResults +
                '}';
    }
}
