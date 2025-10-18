package core;

import ui.GameUI;

public class Main {
    public static void main(String[] args) {

        try (GameUI gameUI = new GameUI("en")) {
            gameUI.startGame();
        }
    }
}