package com.x.sudoku.resolver;

import com.google.common.collect.ImmutableSet;
import com.x.sudoku.SudokuGame;
import com.x.sudoku.SudokuNode;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class BlockImpossibleResolverTest {

    private SudokuGame game = new SudokuGame();

    private BlockImpossibleResolver resolver = new BlockImpossibleResolver();

    @Test
    void test_resolver_BlockImpossibleResolver() {
        Integer[][] arr = new Integer[][]{
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, 7, null, null, null, null, null, null, null},

                {null, null, 7, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},

                {null, null, null, 7, null, null, null, null, null},
                {null, null, null, null, null, null, 7, null, null},
                {null, null, null, null, null, null, null, null, null}
        };
        game.initNumber(arr);
        SudokuNode testNode = game.getNode(0, 8);
        game.getBlocks().get(testNode.getBlockKey()).stream()
                .filter(node -> !node.equals(testNode))
                .forEach(node -> node.setPossibleNumbers(ImmutableSet.of(1, 2, 3, 4, 5, 6, 8, 9)));
        resolver.resolve(game, testNode);

        Assertions.assertFalse(testNode.isNotFilled());
    }
}
