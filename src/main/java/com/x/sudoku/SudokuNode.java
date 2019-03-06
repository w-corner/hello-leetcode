package com.x.sudoku;

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
        this.number = number;
        this.possibleNumbers = Collections.singleton(number);
        this.notFilled = false;
        log.info("({},{})={}", x + 1, y + 1, number);
    }

    public void removeImpossible(int number) {
        log.debug("({},{})~=({}) remove {}", x, y, this.possibleNumbers, number);
        this.getPossibleNumbers().remove(number);
    }
}
