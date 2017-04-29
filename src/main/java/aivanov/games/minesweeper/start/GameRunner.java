package aivanov.games.minesweeper.start;

import aivanov.games.minesweeper.gui.MinesFieldFrame;
import javax.swing.*;
import java.awt.EventQueue;

/**
 * Created by aivanov on 4/28/2017.
 */
public class GameRunner {
    public static void main(String... args) {

        EventQueue.invokeLater(new Runnable() {
            public void run() {
                JFrame frame = MinesFieldFrame.createMainWindow();
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setVisible(true);
            }
        });
    }
}