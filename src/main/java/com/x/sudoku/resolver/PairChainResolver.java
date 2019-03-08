package com.x.sudoku.resolver;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.x.sudoku.Resolver;
import com.x.sudoku.SudokuGame;
import com.x.sudoku.data.PairChain;
import com.x.sudoku.data.SudokuNode;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Set;
import java.util.stream.Collectors;

@AllArgsConstructor
@Slf4j
public class PairChainResolver implements Resolver {

    private SudokuGame context;

    @Override
    public void resolve(SudokuNode node) {
//        if (!node.isNotInit() || !node.isNotFilled()) {
//            return;
//        }

        if (node.getPossibleNumbers().size() != 2) {
            return;
        }

        makeColumnPairChain(node);
        makeRowPairChain(node);
        makeBlockPairChain(node);
    }

    private void makeBlockPairChain(SudokuNode node) {
        Set<SudokuNode> notResolved = context.getBlocks().get(node.getBlockKey()).stream()
                .filter(n -> n.isNotFilled() && n.isNotInit())
                .collect(Collectors.toSet());
        if (notResolved.size() != 2 || context.existPairChain(node, notResolved)) {
            return;
        }

        PairChain chain = PairChain.of(notResolved, Sets.newHashSet(node.getPossibleNumbers()));
        log.info("{} in block create PairChain {}", node, chain);
        context.registerPariChain(chain);

        ArrayList<SudokuNode> nodes = Lists.newArrayList(chain.getNodes());
        SudokuNode first = nodes.get(0);
        Set<SudokuNode> affectNodes = Sets.newHashSet();
        if (first.getX() == nodes.get(1).getX()) {
            affectNodes = context.getCols().get(first.getX());
        } else if (first.getY() == nodes.get(1).getY()) {
            affectNodes = context.getRows().get(first.getY());
        }
        Set<SudokuNode> blockNodes = context.getBlocks().get(first.getBlockKey());
        Sets.difference(affectNodes, blockNodes).stream()
                .filter(node1 -> node1.isNotInit() && node1.isNotFilled())
                .forEach(node1 -> node1.removeImpossible(chain.getPossibleNumbers()));
    }

    private void makeRowPairChain(SudokuNode node) {
        Set<SudokuNode> notResolved = context.getRows().get(node.getY()).stream()
                .filter(n -> n.isNotFilled() && n.isNotInit())
                .collect(Collectors.toSet());
        if (notResolved.size() != 2 || context.existPairChain(node, notResolved)) {
            return;
        }
        PairChain chain = PairChain.of(notResolved, Sets.newHashSet(node.getPossibleNumbers()));
        log.info("{} in row create PairChain {}", node, chain);
        context.registerPariChain(chain);

    }

    private void makeColumnPairChain(SudokuNode node) {
        Set<SudokuNode> notResolved = context.getCols().get(node.getX()).stream()
                .filter(n -> n.isNotFilled() && n.isNotInit())
                .collect(Collectors.toSet());
        if (notResolved.size() != 2 || context.existPairChain(node, notResolved)) {
            return;
        }
        PairChain chain = PairChain.of(notResolved, Sets.newHashSet(node.getPossibleNumbers()));
        log.info("{} in column create PairChain {}", node, chain);
        context.registerPariChain(chain);
    }
}
