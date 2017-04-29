package aivanov.games.minesweeper.start;

import aivanov.games.minesweeper.gui.MainWindow;
import javax.swing.*;
import java.awt.EventQueue;

/**
 * Created by aivanov on 4/28/2017.
 */
public class GameRunner {
    public static void main(String... args) {

        EventQueue.invokeLater(() -> {
                JFrame frame = MainWindow.createMainWindow();
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setVisible(true);
        });
    }
}