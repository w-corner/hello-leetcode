package com.x.sudoku.resolver;

import com.google.common.collect.Sets;
import com.x.sudoku.Resolver;
import com.x.sudoku.SudokuGame;
import com.x.sudoku.SudokuNode;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;
import java.util.Set;

import static java.util.stream.Collectors.toSet;

@Slf4j
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
            context.setSolved(context.getSolved() + 1);
        }
    }
}
