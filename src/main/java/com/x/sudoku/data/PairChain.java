package com.x.sudoku.data;

import lombok.Data;

import java.util.Set;

@Data(staticConstructor = "of")
public class PairChain {

    private final SudokuNode one;
    private final SudokuNode two;
    private final Set<Integer> possibleNumbers;
}
