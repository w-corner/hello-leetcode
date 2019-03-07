package com.x.sudoku.data;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;

import java.util.Set;

@Data(staticConstructor = "of")
@Slf4j
@EqualsAndHashCode(of = "nodes")
public class PairChain {

    private final Set<SudokuNode> nodes;
    private final Set<Integer> possibleNumbers;

    public void destroyBy(SudokuNode node) {
        final Set<Integer> before = this.possibleNumbers;
        nodes.remove(node);
        this.possibleNumbers.remove(node.getNumber());
        nodes.stream()
//                .filter(node1 -> node1.isNotInit() && node1.isNotFilled())
                .findFirst()
                .ifPresent(node1 -> {
                    Integer number = this.possibleNumbers.stream().findFirst().get();
                    log.info("PairChain {} destory by {}, fill {} with: {}", before, node, node1, number);
                    node1.fillNumber(number);
                });
    }

}
