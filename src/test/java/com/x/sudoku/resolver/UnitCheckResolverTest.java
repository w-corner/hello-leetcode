package com.x.sudoku.resolver;

import com.x.sudoku.SudokuGame;
import com.x.sudoku.data.SudokuNode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UnitCheckResolverTest {

    @BeforeEach
    void setUp() {

    }

    @Test
    void test_check_line() {


        // [3, 8, 9][3, 8][3, 8]   1[3, 4, 8, 9]   6[4, 7, 9]   5   2
        int[][] arr = {
//                {0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 1, 0, 6, 0, 5, 2},
                {7, 0, 0, 0, 0, 0, 0, 6, 0},
                {0, 4, 0, 0, 0, 0, 0, 0, 0},

                {0, 0, 0, 2, 5, 0, 0, 0, 0},
                {0, 9, 0, 0, 0, 0, 3, 0, 0},
                {0, 0, 0, 0, 7, 0, 0, 0, 0},

                {0, 0, 1, 0, 0, 9, 8, 0, 0},
                {5, 0, 0, 0, 0, 0, 0, 0, 0},
                {2, 0, 0, 0, 0, 0, 0, 0, 0},
        };
        SudokuGame context = new SudokuGame();
        context.initNumber(arr);

        UnitCheckResolver resolver = new UnitCheckResolver(context);
        resolver.resolve(SudokuNode.builder().x(0).y(1).build());

        assertEquals(7, context.getNode(6, 0).getNumber());

        resolver.resolve(SudokuNode.builder().x(0).y(1).build());
        assertEquals(4, context.getNode(4, 0).getNumber());
    }
}