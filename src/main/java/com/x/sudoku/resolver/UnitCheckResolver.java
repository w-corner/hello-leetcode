package com.x.sudoku.resolver;

import com.x.sudoku.Resolver;
import com.x.sudoku.SudokuGame;
import com.x.sudoku.data.SudokuNode;
import lombok.extern.slf4j.Slf4j;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.counting;

@Slf4j
public class UnitCheckResolver implements Resolver {

    private SudokuGame context;

    public UnitCheckResolver(SudokuGame context) {
        this.context = context;
    }

    @Override
    public void resolve(SudokuNode node) {
        if (node.getX() == 0) {
            int row = (node.getY() - 1 + 9) % 9;
            checkLine(context.getRows().get(row));
        }
        if (node.getY() == 8) {
            int col = (node.getX() - 1 + 9) % 9;
            checkLine(context.getCols().get(col));
        }
    }

    private void checkLine(Set<SudokuNode> col) {
        Map<Integer, Long> distribute = col.stream()
                .filter(node -> node.isNotInit() && node.isNotFilled())
                .map(SudokuNode::getPossibleNumbers)
                .flatMap(Collection::stream)
                .collect(Collectors.groupingBy(Function.identity(), counting()));
        Set<Integer> confirm = distribute.keySet().stream()
                .filter(i -> distribute.get(i) == 1)
                .collect(Collectors.toSet());
        if (confirm.isEmpty()) {
            return;
        }

        log.info("line: {}\nonly number: {}", col, confirm);
        confirm.forEach(cf -> {

            col.stream()
                    .filter(node -> node.isNotInit() && node.isNotFilled())
                    .filter(node -> node.getPossibleNumbers().contains(cf))
                    .findFirst()
                    .ifPresent(node -> context.fillNumber(node, cf));
        });
    }

}
