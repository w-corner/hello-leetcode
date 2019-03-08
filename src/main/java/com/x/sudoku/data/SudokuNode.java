package com.x.sudoku.data;

import com.google.common.collect.Sets;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;

import java.util.Collections;
import java.util.Set;

import static com.x.sudoku.SudokuGame.ALL_ELEMENTS;

@Data
@Builder
@Slf4j
@EqualsAndHashCode(of = {"x", "y"})
public class SudokuNode {
    private int x;
    private int y;
    private Integer number;

    @Builder.Default
    private boolean notInit = true;
    @Builder.Default
    private boolean notFilled = true;
    @Builder.Default
    private Set<Integer> possibleNumbers = Sets.newHashSet(ALL_ELEMENTS);

    public int getBlockKey() {
        return y / 3 * 10 + x / 3;
    }

    public void fillNumber(int number) {
        if (isNotInit() && isNotFilled()) {
            this.number = number;
            this.possibleNumbers = Collections.singleton(number);
            this.notFilled = false;
            log.info("({},{})={}", x, y, number);
        } else {
            throw new IllegalStateException("error fill: " + this + " with: " + number);
        }
    }

    public void removeImpossible(int number) {
        removeImpossible(Collections.singleton(number));
    }

    public void removeImpossible(Set<Integer> numbers) {
        if (Collections.disjoint(possibleNumbers, numbers)) {
            return;
        }

        Set<Integer> before = Sets.newHashSet(this.possibleNumbers);
        this.possibleNumbers.removeAll(numbers);
        if (this.getPossibleNumbers().size() == 0) {
            String msg = String.format("(%d, %d) error remove, before: %s, to be removed: %s", x, y, before, numbers);
            throw new IllegalStateException(msg);
        }
        log.info("({},{}) possible before: {}, impossible: {}, after: {}", x, y, before, numbers, this.possibleNumbers);
    }

    @Override
    public String toString() {
        return String.format("SudokuNode(%d,%d)=%s", x, y, number == null ? possibleNumbers : number);
    }
}
