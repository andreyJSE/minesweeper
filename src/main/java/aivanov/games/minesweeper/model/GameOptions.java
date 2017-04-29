package aivanov.games.minesweeper.model;

/**
 * Created by AIvanov on 4/29/2017.
 */

public final class GameOptions {

    public static int rowCount = 9;
    public static int columnCount = 9;
    public static int mineCount = 4;
    public static boolean safeMove = false;
    public static boolean openRemaining = false;

    public static void beginner() {
        setNewValues(9, 9, 4);
    }

    public static void intermediate() {
        setNewValues(16, 16, 40);
    }

    public static void expert() {
        setNewValues(40, 60 ,256);
    }

    public static void custom(int rows, int columns, int mines) {
        setNewValues(rows, columns, mines);
    }

    private static void setNewValues(int rows, int columns, int mines) {
        rowCount = rows;
        columnCount = columns;
        mineCount = mines;
    }
}