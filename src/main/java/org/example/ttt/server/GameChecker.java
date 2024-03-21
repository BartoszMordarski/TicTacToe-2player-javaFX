package org.example.ttt.server;

import org.example.ttt.model.Game;

import java.util.ArrayList;

public class GameChecker {

    public static void checkIfGameIsOver(Game game) {
        ArrayList<String> buttons = game.getButtons();

        for (int a = 0; a <= 18; a++) {
            String line = switch (a) {
                //poziomo
                case 0 -> buttons.get(0) + buttons.get(1) + buttons.get(2) + buttons.get(3);
                case 1 -> buttons.get(4) + buttons.get(5) + buttons.get(6) + buttons.get(7);
                case 2 -> buttons.get(8) + buttons.get(9) + buttons.get(10) + buttons.get(11);
                case 3 -> buttons.get(12) + buttons.get(13) + buttons.get(14) + buttons.get(15);

                //pionowo
                case 4 -> buttons.get(0) + buttons.get(4) + buttons.get(8) + buttons.get(12);
                case 5 -> buttons.get(1) + buttons.get(5) + buttons.get(9) + buttons.get(13);
                case 6 -> buttons.get(2) + buttons.get(6) + buttons.get(10) + buttons.get(14);
                case 7 -> buttons.get(3) + buttons.get(7) + buttons.get(11) + buttons.get(15);


                //po skosie
                case 8 -> buttons.get(0) + buttons.get(5) + buttons.get(10) + buttons.get(15);
                case 9 -> buttons.get(3) + buttons.get(6) + buttons.get(9) + buttons.get(12);


                //kwadraty
                case 10 -> buttons.get(0) + buttons.get(1) + buttons.get(4) + buttons.get(5);
                case 11 -> buttons.get(1) + buttons.get(2) + buttons.get(5) + buttons.get(6);
                case 12 -> buttons.get(2) + buttons.get(3) + buttons.get(6) + buttons.get(7);

                case 13 -> buttons.get(4) + buttons.get(5) + buttons.get(8) + buttons.get(9);
                case 14 -> buttons.get(5) + buttons.get(6) + buttons.get(9) + buttons.get(10);
                case 15 -> buttons.get(6) + buttons.get(7) + buttons.get(10) + buttons.get(11);

                case 16 -> buttons.get(8) + buttons.get(9) + buttons.get(12) + buttons.get(13);
                case 17 -> buttons.get(9) + buttons.get(10) + buttons.get(13) + buttons.get(14);
                case 18 -> buttons.get(10) + buttons.get(11) + buttons.get(14) + buttons.get(15);

                default -> null;
            };

            if (line.equals("XXXX")) {
                game.setGameOver(true);
                game.setWinnerSymbol("X");
            }

            else if (line.equals("OOOO")) {
                game.setGameOver(true);
                game.setWinnerSymbol("O");
            }
        }
    }
}
