package com.x.sudoku;

import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.Set;

@Data
@Builder
public class SudokuNode {
    private int x;
    private int y;
    private Integer number;

    @Builder.Default
    private boolean notInit = true;
    private Set<Integer> possibleNumbers;
    private boolean numberFilled;

    private List<SudokuNode> rows;
    private List<SudokuNode> cols;
    private List<SudokuNode> blocks;

    public SudokuNode getNode(int x, int y) {
        return this.x == x && this.y == y ? this : null;
    }
}
