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
        minesLayout = MapGenerator.generateMap(rowNum, colNum, minesNum);
    }

    /**
     Construct a two-dimension array, there the value "-1" means mine with guarantee
     that selected cell doesn't contain mine.

     @param rowNum Number of rows
     @param colNum Number of columns
     @param minesNum Number of mines
     @param selectedRow The row index of selected cell
     @param selectedCol The column index of selected cell
     */

    public MinesModel(int rowNum, int colNum, int minesNum, int selectedRow, int selectedCol) {
        minesLayout = MapGenerator.generateMapEx(rowNum, colNum, minesNum,
                selectedRow, selectedCol);
    }

    public byte getValueAt(int rowIndex, int colIndex) {
        return minesLayout[rowIndex][colIndex];
    }

    public static void main(String... args) {
        byte[][] mg = MapGenerator.generateMapEx(9, 9, 4, 3, 3);

        for (byte[] sub : mg) {
            System.out.println(Arrays.toString(sub));
        }
    }

}

// Utility class to generate minefield

class MapGenerator {

    private MapGenerator() {}

    public static byte[][] generateMap(int N, int M, int minesNum) {

        // Auxiliary array
        byte[][] aux = new byte[N + 2][M + 2];

        // Final array
        byte[][] ans = new byte[N][M];

        int count = minesNum;
        int squares = N * M;

        double factor = 0.0;

        // Initialization
        for (int i = 0; i < N + 2; i++) {
            for (int j = 0; j < M + 2; j++) {
                aux[i][j] = 0;
            }
        }

        // Generate mines
        for (int i = 1; i < N + 1; i++) {
            for (int j = 1; j < M + 1; j++) {
                factor = count * 1.0 / squares;
                if (Math.random() <= factor) {
                    aux[i][j] = -1;
                    count--;
                }
                squares--;
            }
        }

        // Copy values from auxiliary array to final array
        for (int i = 1; i < N + 1; i++) {
            for (int j = 1; j < M + 1; j++) {
                if (aux[i][j] >= 0) {
                    for (int x = i-1; x <= i+1; x++) {
                        for (int y = j-1; y <= j+1; y++) {
                            if (aux[x][y] < 0) {
                                ans[i-1][j-1] += 1;
                            }
                        }
                    }
                } else {
                    ans[i-1][j-1] = -1;
                }

            }
        }

        return ans;
    }

    public static byte[][] generateMapEx(int N, int M, int minesNum, int aRow, int aCol) {

        // Auxiliary array
        byte[][] aux = new byte[N + 2][M + 2];

        // Final array
        byte[][] ans = new byte[N][M];
        int count = minesNum;

        // Count the number of excluded cells
        int exclNum = 9;
        if ((aRow == N-1 || aRow == 0) && (aCol == M-1 || aCol == 0)) {
            exclNum = 4;
        } else if (aRow == N-1 || aRow == 0 || aCol == M-1 || aCol == 0) {
            exclNum = 6;
        }

        int squares = (N * M) - exclNum;
        double factor = 0.0;

        // Initialization
        for (int i = 0; i < N + 2; i++) {
            for (int j = 0; j < M + 2; j++) {
                aux[i][j] = 0;
            }
        }

        // Generate mines taking into account the selected cell
        // P.S. Indexes may look a bit confusing because auxiliary array indexes have
        // been increased on one in comparison with final array

        for (int i = 1; i < N + 1; i++) {
            for (int j = 1; j < M + 1; j++) {
                if ((i > aRow + 2 || i < aRow) || (j > aCol + 2 || j < aCol)) {
                    factor = count * 1.0 / squares;
                    if (Math.random() <= factor) {
                        aux[i][j] = -1;
                        count--;
                    }
                    squares--;
                }
            }
        }

        // Copy values from auxiliary array to final array
        for (int i = 1; i < N + 1; i++) {
            for (int j = 1; j < M + 1; j++) {
                if (aux[i][j] >= 0) {
                    for (int x = i-1; x <= i+1; x++) {
                        for (int y = j-1; y <= j+1; y++) {
                            if (aux[x][y] < 0) {
                                ans[i-1][j-1] += 1;
                            }
                        }
                    }
                } else {
                    ans[i-1][j-1] = -1;
                }

            }
        }

        return ans;
    }

}


