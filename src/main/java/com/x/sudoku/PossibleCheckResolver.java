package com.x.sudoku;

import com.google.common.collect.Sets;

import java.util.Collection;
import java.util.Collections;
import java.util.Objects;
import java.util.Set;

import static com.x.sudoku.SudokuGame.ALL_ELEMENTS;
import static java.util.stream.Collectors.toSet;

public class PossibleCheckResolver implements Resolver {

    @Override
    public void resolve(SudokuGame context, SudokuNode node) {
        if (!node.isNotInit() || !node.isNotFilled()) {
            return;
        }

        Set<Integer> existed = context.getAffectNodeStream(node)
                .map(SudokuNode::getNumber)
                .filter(Objects::nonNull)
                .collect(toSet());
        Sets.SetView<Integer> possible = Sets.difference(node.getPossibleNumbers(), existed);
        node.setPossibleNumbers(Sets.newHashSet(possible));

        if (node.getPossibleNumbers().size() == 1) {
            Integer exacted = node.getPossibleNumbers().stream().findFirst().get();
            node.fillNumber(exacted);
            return;
        }

        Sets.SetView<SudokuNode> otherNodesInSameBlock = Sets.difference(context.getBlocks().get(node.getBlockKey()), Collections.singleton(node));
        Set<Integer> possibleOfOtherNode = otherNodesInSameBlock.stream()
                .map(SudokuNode::getPossibleNumbers)
                .flatMap(Collection::stream)
                .collect(toSet());
        Sets.SetView<Integer> othersImpossible = Sets.difference(ALL_ELEMENTS, possibleOfOtherNode);
        Sets.SetView<Integer> selfPossible = Sets.intersection(node.getPossibleNumbers(), othersImpossible);
        if (selfPossible.size() == 1) {
            node.fillNumber(selfPossible.stream().findFirst().get());
            return;
        }
    }
}
