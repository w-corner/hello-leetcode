package com.x.sudoku.resolver;

import com.x.sudoku.Resolver;
import com.x.sudoku.SudokuGame;
import com.x.sudoku.data.PairChain;
import com.x.sudoku.data.SudokuNode;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

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
        if (notResolved.size() == 2 && !context.existPairChain(node, notResolved)) {
            PairChain chain = PairChain.of(notResolved, node.getPossibleNumbers());
            log.info("{} in block create PairChain {}", node, chain);
            context.registerPariChain(chain);
        }
    }

    private void makeRowPairChain(SudokuNode node) {
        Set<SudokuNode> notResolved = context.getRows().get(node.getY()).stream()
                .filter(n -> n.isNotFilled() && n.isNotInit())
                .collect(Collectors.toSet());
        if (notResolved.size() == 2) {
            context.registerPariChain(PairChain.of(notResolved, node.getPossibleNumbers()));
        }
    }

    private void makeColumnPairChain(SudokuNode node) {
        Set<SudokuNode> notResolved = context.getCols().get(node.getX()).stream()
                .filter(n -> n.isNotFilled() && n.isNotInit())
                .collect(Collectors.toSet());
        if (notResolved.size() == 2) {
            context.registerPariChain(PairChain.of(notResolved, node.getPossibleNumbers()));
        }
    }
}
