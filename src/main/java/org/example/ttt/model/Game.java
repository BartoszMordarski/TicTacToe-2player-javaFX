package org.example.ttt.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Game {
    private String userX;
    private String userO;

    private ArrayList<String> buttons = new ArrayList<>();

    private Map<String, String> userMarks = new HashMap<>();

    private String turn;

    private boolean isGameOver;

    private String winnerSymbol;

    private boolean userDisconnected = false;

    public boolean isUserDisconnected() {
        return userDisconnected;
    }

    public void setUserDisconnected(boolean userDisconnected) {
        this.userDisconnected = userDisconnected;
    }

    public String getWinnerSymbol() {
        return winnerSymbol;
    }

    public void setWinnerSymbol(String winnerSymbol) {
        this.winnerSymbol = winnerSymbol;
    }

    public boolean isGameOver() {
        return isGameOver;
    }

    public void setGameOver(boolean gameOver) {
        isGameOver = gameOver;
    }

    public Map<String, String> getUserMarks() {
        return userMarks;
    }

    public void setTurn(String turn) {
        this.turn = turn;
    }

    public String getTurn() {
        return turn;
    }

    public Game() {
        for (int i = 0; i < 16; i++) {
            buttons.add("");
        }
    }

    public void reset() {
        buttons = new ArrayList<>();
        for (int i = 0; i < 16; i++) {
            buttons.add("");
        }
        userX = null;
        userO = null;
        userMarks.clear();
        turn = null;
        isGameOver = false;
        winnerSymbol = null;
        userDisconnected = false;
    }

    public String getUserX() {
        return userX;
    }

    public String getUserO() {
        return userO;
    }

    public ArrayList<String> getButtons() {
        return buttons;
    }

    public void setUserX(String userX) {
        this.userX = userX;
    }

    public void setUserO(String userO) {
        this.userO = userO;
    }

    @Override
    public String toString() {
        return "Game{" +
                "userX='" + userX + '\'' +
                ", userY='" + userO + '\'' +
                ", buttons=" + buttons +
                ", userMarks=" + userMarks +
                ", turn='" + turn + '\'' +
                '}';
    }
}
