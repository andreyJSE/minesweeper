package aivanov.games.minesweeper.model;

/**
 * Created by AIvanov on 4/29/2017.
 */
public class GameOptions {

    private final int rowCount;
    private final int columnCount;
    private final int mineCount;
    private boolean safeMove;
    private boolean openRemaining;

    private GameOptions(int rowCount, int columnCount, int mineCount) {
        this.rowCount = rowCount;
        this.columnCount = columnCount;
        this.mineCount = mineCount;
        this.safeMove = false;
        this.openRemaining = false;
    };

    public int getRowCount() {
        return rowCount;
    }

    public int getColumnCount() {
        return columnCount;
    }

    public int getMineCount() {
        return mineCount;
    }

    public static GameOptions beginner() {
        GameOptions game = new GameOptions(9, 9,4);
        return game;
    }

    public static GameOptions intermediate() {
        GameOptions game = new GameOptions(16, 16,40);
        return game;
    }

    public static GameOptions expert() {
        GameOptions game = new GameOptions(40, 60,256);
        return game;
    }

    public static GameOptions custom(int rows, int columns, int mines) {
        GameOptions game = new GameOptions(rows, columns, mines);
        return game;
    }

    public boolean isSafeMove() {
        return safeMove;
    }

    public boolean isOpenRemaining() {
        return openRemaining;
    }

    public void setSafeMove(boolean safeMove) {
        this.safeMove = safeMove;
    }
    public void setOpenRemaining(boolean openRemaining) {
        this.openRemaining = openRemaining;
    }
}
