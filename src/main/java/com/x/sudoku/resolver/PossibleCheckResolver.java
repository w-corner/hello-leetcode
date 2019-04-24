package com.x.sudoku.resolver;

import com.x.sudoku.Resolver;
import com.x.sudoku.SudokuGame;
import com.x.sudoku.data.SudokuNode;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Collection;
import java.util.Collections;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toSet;

@Slf4j
@AllArgsConstructor
public class PossibleCheckResolver implements Resolver {

    private SudokuGame context;

    @Override
    public void resolve(SudokuNode node) {
        if (!node.isNotInit() || !node.isNotFilled()) {
            return;
        }

        Set<Integer> existed = context.getAffectNodeStream(node)
                .map(SudokuNode::getNumber)
                .collect(toSet());
        if (Collections.disjoint(node.getPossibleNumbers(), existed)) {
            return;
        }
        node.removeImpossible(existed);
        if (node.getPossibleNumbers().size() == 1) {
            Integer exacted = node.getPossibleNumbers().stream().findFirst().get();
            context.fillNumber(node, exacted);
        }
    }


}
