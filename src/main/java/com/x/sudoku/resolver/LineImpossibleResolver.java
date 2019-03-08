package com.x.sudoku.resolver;

import com.google.common.collect.Sets;
import com.x.sudoku.Resolver;
import com.x.sudoku.SudokuGame;
import com.x.sudoku.data.SudokuNode;
import lombok.extern.slf4j.Slf4j;

import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toSet;

@Slf4j
public class LineImpossibleResolver implements Resolver {

    private SudokuGame context;

    private int counter = 0;

    public LineImpossibleResolver(SudokuGame context) {
        this.context = context;
    }

    private int maxNotResolve() {
        counter++;
        int max = 2 + counter / 300;
        return Math.max(max, 5);
    }

    @Override
    public void resolve(SudokuNode node) {
        Set<SudokuNode> blockNodes = context.getBlocks().get(node.getBlockKey());
        long notResolved = blockNodes.stream()
                .filter(node1 -> node1.isNotInit() && node1.isNotFilled())
                .count();
        if (notResolved > maxNotResolve()) {
            return;
        }

        Map<Integer, Set<SudokuNode>> columns = blockNodes.stream().collect(Collectors.groupingBy(SudokuNode::getX, toSet()));
        Map<Integer, Set<SudokuNode>> rows = blockNodes.stream().collect(Collectors.groupingBy(SudokuNode::getY, toSet()));

        columns.forEach((index, nodes) -> {
            Set<SudokuNode> currentColumnNodes = Sets.newHashSet(nodes);
            Sets.SetView<SudokuNode> otherNodesInSameBlock = Sets.difference(blockNodes, currentColumnNodes);

            Set<Integer> currentColumnMayBe = currentColumnNodes.stream()
                    .filter(node1 -> node1.isNotInit() && node1.isNotFilled())
                    .map(SudokuNode::getPossibleNumbers)
                    .reduce(SudokuGame.ALL_ELEMENTS, Sets::intersection);
            if (currentColumnMayBe.size() == SudokuGame.ALL_ELEMENTS.size()) {
                return;
            }
            Set<Integer> currentColumnMustBe = otherNodesInSameBlock.stream()
                    .map(SudokuNode::getPossibleNumbers)
                    .reduce(currentColumnMayBe, Sets::difference);
            if (currentColumnMustBe.size() == 0) {
                return;
            }
            Sets.SetView<SudokuNode> otherNodesInSameColumnDiffBlock = Sets.difference(context.getCols().get(nodes.stream().findAny().get().getX()), nodes);
            otherNodesInSameColumnDiffBlock.stream()
                    .filter(node1 -> node1.isNotInit() && node1.isNotFilled())
                    .forEach(node1 -> {
                        if (Collections.disjoint(node1.getPossibleNumbers(), currentColumnMustBe)) {
                            return;
                        }
                        log.info("{} column must be {}", nodes, currentColumnMustBe);
                        node1.removeImpossible(currentColumnMustBe);
                    });
        });

        rows.forEach((index, nodes) -> {
            Set<SudokuNode> currentRowNodes = Sets.newHashSet(nodes);
            Sets.SetView<SudokuNode> otherNodesInSameBlock = Sets.difference(blockNodes, currentRowNodes);

            Set<Integer> currentRowMayBe = currentRowNodes.stream()
                    .filter(node1 -> node1.isNotInit() && node1.isNotFilled())
                    .map(SudokuNode::getPossibleNumbers)
                    .reduce(SudokuGame.ALL_ELEMENTS, Sets::intersection);
            if (currentRowMayBe.size() == SudokuGame.ALL_ELEMENTS.size()) {
                return;
            }
            Set<Integer> currentRowMustBe = otherNodesInSameBlock.stream()
                    .map(SudokuNode::getPossibleNumbers)
                    .reduce(currentRowMayBe, Sets::difference);
            if (currentRowMustBe.size() == 0) {
                return;
            }
            Sets.SetView<SudokuNode> otherNodesInSameRowDiffBlock = Sets.difference(context.getRows().get(nodes.stream().findAny().get().getY()), nodes);
            otherNodesInSameRowDiffBlock.stream()
                    .filter(node1 -> node1.isNotInit() && node1.isNotFilled())
                    .forEach(node1 -> {
                        if (Collections.disjoint(node1.getPossibleNumbers(), currentRowMustBe)) {
                            return;
                        }
                        log.info("{} row must be {}", nodes, currentRowMustBe);
                        node1.removeImpossible(currentRowMustBe);
                    });
        });
    }
}
