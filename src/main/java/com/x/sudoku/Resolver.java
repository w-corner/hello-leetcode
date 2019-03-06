package com.x.sudoku;

import com.x.sudoku.data.SudokuNode;

public interface Resolver {

    public void resolve(SudokuGame context, SudokuNode node);
}
