package aivanov.games.minesweeper.model;

import java.util.Arrays;

/**
 A class to generate minefield.
 */

public class MinesModel {

    private final byte[][] minesLayout;

    /**
     Construct a two-dimension array, there the value "-1" means mine.
     @param rowNum Number of rows
     @param colNum Number of columns
     @param minesNum Number of mines
     */

    public MinesModel(int rowNum, int colNum, int minesNum) {
        minesLayout = MinefieldGenerator.initMinefield(rowNum, colNum, minesNum);
    }

    /**
     Construct a two-dimension array, there the value "-1" stands for mine with guarantee
     that selected cell doesn't contain mine.

     @param rowNum Number of rows
     @param colNum Number of columns
     @param minesNum Number of mines
     @param selectedRow The row index of selected cell
     @param selectedCol The column index of selected cell
     */

    public MinesModel(int rowNum, int colNum, int minesNum, int selectedRow, int selectedCol) {
        minesLayout = MinefieldGenerator.initMinefield(rowNum, colNum, minesNum,
                selectedRow, selectedCol);
    }

    public byte getValueAt(int rowIndex, int colIndex) {
        return minesLayout[rowIndex][colIndex];
    }

    static class MinefieldGenerator {

        public static byte[][] initMinefield(int rows, int cols, int mineCount) {

            byte[][] minefield = initEmptyMinefield(rows, cols);

            setMinesCells(minefield, mineCount);
            setNonMineCells(minefield);

            return minefield;
        }

        public static byte[][] initMinefield(int rows, int cols, int mines, int aRow, int aCol) {

            byte[][] minefield = initEmptyMinefield(rows, cols);

            setMinesCells(minefield, mines, aRow, aCol);
            setNonMineCells(minefield);

            return minefield;
        }

        private static byte[][] initEmptyMinefield(int rows, int cols) {

            byte[][] minefield = new byte[rows][cols];

            for (byte[] a : minefield) {
                Arrays.fill(a, (byte) 0);
            }

            return minefield;
        }

        /**
         * Return a byte array where -1 stands for mine.
         *
         * @param rows array row count
         * @param columns array column count
         * @param mineCount mine count
         * @return multidimensional array of bytes
         */
        private static void setMinesCells(byte[][] minefield, int mineCount) {

            int rows = minefield.length;
            int columns = minefield[0].length;

            int cellCount = rows * columns;
            double factor = 0.0;

            // Generate mines
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < columns; j++) {
                    factor = mineCount * 1.0 / cellCount;
                    if (Math.random() <= factor) {
                        minefield[i][j] = -1;
                        mineCount--;
                    }
                    cellCount--;
                }
            }
        }

        private static void setMinesCells(byte[][] minefield, int mineCount, int aRow, int aCol) {

            int rows = minefield.length;
            int columns = minefield[0].length;

            int cellCount = (rows * columns);
            double factor = 0.0;

            // Exclude selected and adjacent cells
            if ((aRow == rows-1 || aRow == 0) && (aCol == columns-1 || aCol == 0)) {
                cellCount -= 4; // Angle cell
            } else if (aRow == rows-1 || aRow == 0 || aCol == columns-1 || aCol == 0) {
                cellCount -= 6; // Outside cell
            } else {
                cellCount -= 9; // Default cell
            }


            // Generate mines taking into account the selected cell

            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < columns ; j++) {
                    if ((i > aRow + 1 || i < aRow - 1) || (j > aCol + 1 || j < aCol - 1)) {
                        factor = mineCount * 1.0 / cellCount;
                        if (Math.random() <= factor) {
                            minefield[i][j] = -1;
                            mineCount--;
                        }
                        cellCount--;
                    }
                }
            }
        }

        private static void setNonMineCells(byte[][] minegrid) {

            for (int i = 0; i < minegrid.length; i++) {
                for (int j = 0; j < minegrid[i].length; j++) {
                    if (minegrid[i][j] == -1) {
                        setAdjacentCells(minegrid, i, j);
                    }
                }
            }
        }

        private static void setAdjacentCells(byte[][] minefield, int row, int column) {
            for (int i = row - 1; i <= row + 1; i++) {
                if (i >= 0 && i < minefield.length) {
                    for (int j = column - 1; j <= column + 1; j++) {
                        if (j >= 0 && j < minefield[i].length && minefield[i][j] >= 0) {
                            minefield[i][j]++;
                        }
                    }
                }
            }
        }
    }

}



