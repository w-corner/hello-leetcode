package com.x.leetcode;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ExcelHeaderToNumberTest {

    private ExcelHeaderToNumber handler = new ExcelHeaderToNumber();

    @Test
    void titleToNumber_with_A() {
        String s = "A";
        int number = handler.titleToNumber(s);
        Assertions.assertEquals(1, number);
    }

    @Test
    void titleToNumber_with_AA() {
        String s = "AA";
        int number = handler.titleToNumber(s);
        Assertions.assertEquals(27, number);
    }

    @Test
    void titleToNumber_with_ZY() {
        String s = "ZY";
        int number = handler.titleToNumber(s);
        Assertions.assertEquals(701, number);
    }
}