package com.x.sudoku.data;

import com.google.common.collect.Sets;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static com.x.sudoku.SudokuGame.ALL_ELEMENTS;

@Data
@Builder
@Slf4j
@EqualsAndHashCode(of = {"x", "y"})
public class SudokuNode {
    private int x;
    private int y;
    private int number;

    @Builder.Default
    private boolean notInit = true;
    @Builder.Default
    private boolean notFilled = true;
    @Builder.Default
    private Set<Integer> possibleNumbers = new HashSet<>(ALL_ELEMENTS);

    public int getBlockKey() {
        return y / 3 * 10 + x / 3;
    }

    public void removeImpossible(int number) {
        removeImpossible(Collections.singleton(number));
    }

    public void removeImpossible(Set<Integer> numbers) {
        if (Collections.disjoint(possibleNumbers, numbers)) {
            return;
        }

        log.debug("{} remove {}", this, numbers);
        Set<Integer> before = Sets.newHashSet(possibleNumbers);
        possibleNumbers.removeAll(numbers);
        if (getPossibleNumbers().size() == 0) {
            String msg = String.format("(%d, %d) error remove, before: %s, to be removed: %s", x, y, before, numbers);
            throw new IllegalStateException(msg);
        }
        log.info("({},{}) possible before: {}, impossible: {}, after: {}", x, y, before, numbers, possibleNumbers);
    }

    @Override
    public String toString() {
        return String.format("SudokuNode(%d,%d)=%s", x, y, number == 0 ? possibleNumbers : number);
    }
}
