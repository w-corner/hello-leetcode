package com.x.sudoku.data;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Optional;
import java.util.Set;

@Data(staticConstructor = "of")
@EqualsAndHashCode(of = "nodes")
public class PairChain {

    private final Set<SudokuNode> nodes;
    private final Set<Integer> possibleNumbers;

    public Optional<SudokuNode> destroyBy(SudokuNode node) {
        nodes.remove(node);
        this.possibleNumbers.remove(node.getNumber());
        return nodes.stream().findFirst();
    }
}
