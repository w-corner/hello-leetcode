package com.x.sudoku;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class SudokuResolverTest {

    private SudokuResolver sudokuResolver = new SudokuResolver();

    @Test
    void should_throw_error_with_blank_array() {
        Assertions.assertThrows(ArrayIndexOutOfBoundsException.class, () -> sudokuResolver.initNumber(new Integer[][]{}));
    }

    @Test
    void should_() {
        sudokuResolver.initNumber(new Integer[9][9]);
    }
}
