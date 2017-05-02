package aivanov.games.minesweeper.model;

import org.junit.Ignore;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.*;

/**
 * Created by AIvanov on 5/2/2017.
 */

/*

public class MapGeneratorTest {
    @Ignore
    @Test
    public void testIfSetMinesCellsWorkAsExpected() throws Exception {
        byte[][] src = MapGenerator.initEmptyMinefield(4, 4);
        int count = 0;
        int mineCount = 4;

        MapGenerator.setMinesCells(src, mineCount);

        for (byte[] a : src) {
            for (byte b : a) {
                if (b == -1) {
                    count++;
                }
            }
        }

        assertEquals(count, mineCount);
    }

    @Ignore
    @Test
    public void testIfSetNonMineCellsWorkAsExpected() throws Exception {
        byte[][] src =      {{-1, 0, 0},
                             {0, 0, 0},
                             {0, 0, -1}};

        byte[][] dest =     {{-1, 1, 0},
                             {1, 2, 1},
                             {0, 1, -1}};

        MapGenerator.setNonMineCells(src);

        assertTrue(Arrays.deepEquals(src, dest));

    }

    @Ignore
    @Test
    public void testInitEmptyMinefieldWorksAsExpected() {
        byte[][] src =
                        {{0,0,0},
                        {0,0,0},
                        {0,0,0}};

        byte[][] a = MapGenerator.initEmptyMinefield(3,3);

        assertTrue(Arrays.deepEquals(src, a));
    }

}*/
