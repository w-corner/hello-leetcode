package com.x.sudoku.resolver;

import com.google.common.collect.Sets;
import com.x.sudoku.Resolver;
import com.x.sudoku.SudokuGame;
import com.x.sudoku.data.SudokuNode;
import lombok.extern.slf4j.Slf4j;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;

import static com.x.sudoku.SudokuGame.ALL_ELEMENTS;
import static java.util.stream.Collectors.toSet;

@Slf4j
public class BlockImpossibleResolver implements Resolver {

    @Override
    public void resolve(SudokuGame context, SudokuNode node) {
        Sets.SetView<SudokuNode> otherNodesInSameBlock = Sets.difference(context.getBlocks().get(node.getBlockKey()), Collections.singleton(node));
        Set<Integer> possibleOfOtherNode = otherNodesInSameBlock.stream()
                .map(SudokuNode::getPossibleNumbers)
                .flatMap(Collection::stream)
                .collect(toSet());
        Sets.SetView<Integer> othersImpossible = Sets.difference(ALL_ELEMENTS, possibleOfOtherNode);
        Sets.SetView<Integer> selfPossible = Sets.intersection(node.getPossibleNumbers(), othersImpossible);
        if (node.isNotInit() && node.isNotFilled() && selfPossible.size() == 1) {
            node.fillNumber(selfPossible.stream().findFirst().get());
            context.setSolved(context.getSolved() + 1);
        }
    }
}
