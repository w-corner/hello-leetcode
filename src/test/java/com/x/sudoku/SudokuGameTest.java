package com.x.sudoku;

import com.x.sudoku.data.SudokuNode;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class SudokuGameTest {

    private SudokuGame sudokuGame = new SudokuGame();

    @Test
    void should_throw_error_with_blank_array() {
        Assertions.assertThrows(ArrayIndexOutOfBoundsException.class, () -> sudokuGame.initNumber(new Integer[][]{}));
    }

    @Test
    void should_be_null_if_no_set_value() {
        sudokuGame.initNumber(new Integer[9][9]);
        SudokuNode node = sudokuGame.getNode(1, 1);
        Assertions.assertNull(node.getNumber());
    }

    @Test
    void should_be_init() {

        Integer[][] arr = new Integer[][]{
                {1, 2, 3, 4, 5, 6, 7, 8, 9},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {9, 8, 7, 6, 5, 4, 3, 2, 1}
        };
        sudokuGame.initNumber(arr);
        Assertions.assertNull(sudokuGame.getNode(1, 1).getNumber());
        Assertions.assertTrue(sudokuGame.getNode(1, 1).isNotInit());

        Assertions.assertEquals(2, sudokuGame.getNode(1, 0).getNumber());
        Assertions.assertFalse(sudokuGame.getNode(1, 0).isNotInit());

        Assertions.assertEquals(2, sudokuGame.getNode(7, 8).getNumber());
        Assertions.assertFalse(sudokuGame.getNode(7, 8).isNotInit());
    }
}
