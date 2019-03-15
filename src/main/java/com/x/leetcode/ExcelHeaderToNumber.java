package com.x.leetcode;

public class ExcelHeaderToNumber {

    public int titleToNumber(String s) {
        if (s == null || s.trim().length() == 0) {
            throw new IllegalArgumentException("illegal arguement: " + s);
        }
        int result = 0;
        int index = 0;
        for (int i = s.length() - 1; i >= 0; i--) {
            double v = (s.charAt(i) - 'A' + 1) * Math.pow(26, index++);
            result += v;
        }
        return result;
    }
}
