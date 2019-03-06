package com.x.sudoku;

public class ImpossibleSetResolver implements Resolver {

    @Override
    public void resolve(SudokuGame context, SudokuNode currentCheck) {
        if (currentCheck.isNotInit() || currentCheck.isNotFilled()) {
            return;
        }

        context.getAffectNodeStream(currentCheck).forEach(node -> {
            node.removeImpossible(currentCheck.getNumber());
            if (node.isNotInit() && node.isNotFilled() && node.getPossibleNumbers().size() == 1) {
                node.fillNumber(node.getPossibleNumbers().stream().findFirst().get());
            }
        });
    }
}
