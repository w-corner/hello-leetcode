package com.x.sudoku.resolver;

import com.google.common.collect.Sets;
import com.x.sudoku.Resolver;
import com.x.sudoku.SudokuGame;
import com.x.sudoku.data.SudokuNode;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;
import java.util.Set;

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
                .filter(Objects::nonNull)
                .collect(toSet());
        Set<Integer> before = node.getPossibleNumbers();
        Sets.SetView<Integer> possible = Sets.difference(node.getPossibleNumbers(), existed);
        node.setPossibleNumbers(Sets.newHashSet(possible));

        if (!before.equals(possible)) {
            log.info("({},{}) possible before: {}, exist: {}, after: {}", node.getX(), node.getY(), before, existed, possible);
        }
        if (node.getPossibleNumbers().size() == 1) {
            Integer exacted = node.getPossibleNumbers().stream().findFirst().get();
            node.fillNumber(exacted);
            context.solved(node);
        }
    }
}
