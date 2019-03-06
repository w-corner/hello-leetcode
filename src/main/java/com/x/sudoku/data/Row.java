package com.x.sudoku.data;

import com.google.common.collect.Sets;
import lombok.Builder;
import lombok.Data;

import java.util.Set;

import static com.x.sudoku.SudokuGame.ALL_ELEMENTS;

@Data
public class Row {

    private Set<SudokuNode> nodes;
    @Builder.Default
    private Set<Integer> possibleNumbers = Sets.newHashSet(ALL_ELEMENTS);
}
