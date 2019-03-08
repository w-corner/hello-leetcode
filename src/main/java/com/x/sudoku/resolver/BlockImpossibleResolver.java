package com.x.sudoku.resolver;

import com.google.common.collect.Sets;
import com.x.sudoku.Resolver;
import com.x.sudoku.SudokuGame;
import com.x.sudoku.data.SudokuNode;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;

import static com.x.sudoku.SudokuGame.ALL_ELEMENTS;
import static java.util.stream.Collectors.toSet;

@Slf4j
@AllArgsConstructor
public class BlockImpossibleResolver implements Resolver {

    private SudokuGame context;

    @Override
    public void resolve(SudokuNode node) {
        if (!node.isNotInit() || !node.isNotFilled()) {
            return;
        }

        Sets.SetView<SudokuNode> otherNodesInSameBlock = Sets.difference(context.getBlocks().get(node.getBlockKey()), Collections.singleton(node));
        Set<Integer> possibleOfOtherNode = otherNodesInSameBlock.stream()
                .map(SudokuNode::getPossibleNumbers)
                .flatMap(Collection::stream)
                .collect(toSet());
        Sets.SetView<Integer> othersImpossible = Sets.difference(ALL_ELEMENTS, possibleOfOtherNode);
        if (othersImpossible.size() == 0) {
            return;
        }
        Sets.SetView<Integer> selfPossible = Sets.intersection(node.getPossibleNumbers(), othersImpossible);
        log.info("({},{}) possible before: {}, othersImpossible: {}", node.getX(), node.getY(), node.getPossibleNumbers(), othersImpossible);
        if (selfPossible.size() == 1) {
            node.fillNumber(selfPossible.stream().findFirst().get());
            context.solved(node);
        }
    }
}
