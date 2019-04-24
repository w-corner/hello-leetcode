package com.x.sudoku;

import com.x.sudoku.data.SudokuNode;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class SudokuGameTest {

    private SudokuGame sudokuGame = new SudokuGame();

    @Test
    void should_throw_error_with_blank_array() {
        Assertions.assertThrows(ArrayIndexOutOfBoundsException.class, () -> sudokuGame.initNumber(new int[][]{}));
    }

    @Test
    void should_be_null_if_no_set_value() {
        sudokuGame.initNumber(new int[9][9]);
        SudokuNode node = sudokuGame.getNode(1, 1);
        assertEquals(0, node.getNumber());
    }

    @Test
    void should_be_init() {

        int[][] arr = {
                {1, 2, 3, 4, 5, 6, 7, 8, 9},
                {0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0},
                {9, 8, 7, 6, 4, 5, 3, 2, 1}
        };
        sudokuGame.initNumber(arr);
        assertEquals(0, sudokuGame.getNode(1, 1).getNumber());
        assertTrue(sudokuGame.getNode(1, 1).isNotInit());

        assertEquals(2, sudokuGame.getNode(1, 0).getNumber());
        assertFalse(sudokuGame.getNode(1, 0).isNotInit());

        assertEquals(2, sudokuGame.getNode(7, 8).getNumber());
        assertFalse(sudokuGame.getNode(7, 8).isNotInit());

        assertEquals(sudokuGame.getNode(0, 1).getPossibleNumbers(), Set.of(4, 5, 6, 7, 8));
    }

    @Test
    void hard_sudoku() {// OK
        int[][] arr = {
                {8, 0, 0, 0, 0, 0, 1, 5, 6},
                {1, 0, 0, 6, 0, 0, 0, 8, 2},
                {2, 6, 5, 1, 0, 8, 4, 0, 0},

                {4, 0, 0, 0, 6, 0, 0, 0, 0},
                {0, 9, 0, 4, 0, 2, 0, 1, 0},
                {3, 0, 0, 0, 7, 0, 0, 0, 4},

                {9, 4, 8, 7, 1, 6, 5, 2, 3},
                {0, 0, 0, 0, 0, 0, 0, 0, 0},
                {6, 0, 2, 0, 0, 0, 8, 0, 1}
        };
        sudokuGame.initNumber(arr);
    }

    @Test
    void hard_sudoku_2() {
        int[][] arr = {
                {2, 0, 4, 0, 5, 0, 9, 0, 3},
                {0, 9, 0, 3, 0, 2, 0, 6, 0},
                {0, 0, 8, 0, 7, 9, 2, 0, 0},

                {0, 0, 0, 2, 0, 0, 0, 0, 0},
                {0, 0, 0, 7, 1, 5, 0, 2, 0},
                {0, 0, 2, 8, 0, 6, 5, 0, 0},

                {0, 1, 0, 0, 2, 0, 0, 3, 9},
                {4, 0, 0, 9, 0, 7, 0, 0, 2},
                {9, 2, 0, 0, 0, 0, 0, 0, 7},
        };
        sudokuGame.initNumber(arr);
    }
}
