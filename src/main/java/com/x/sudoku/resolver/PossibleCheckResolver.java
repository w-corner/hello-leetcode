package com.x.sudoku.resolver;

import com.google.common.collect.Sets;
import com.x.sudoku.Resolver;
import com.x.sudoku.SudokuGame;
import com.x.sudoku.data.SudokuNode;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Collection;
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

        Set<Integer> existed = getAffectNodeStream(node)
                .map(SudokuNode::getNumber)
                .filter(Objects::nonNull)
                .collect(toSet());
        Set<Integer> before = Sets.newHashSet(node.getPossibleNumbers());
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

    private Stream<SudokuNode> getAffectNodeStream(SudokuNode node) {
        return Stream.of(context.getRows().get(node.getY()), context.getCols().get(node.getX()), context.getBlocks().get(node.getBlockKey()))
                .flatMap(Collection::stream)
                .filter(n -> !n.equals(node))
                .distinct();
    }
}
