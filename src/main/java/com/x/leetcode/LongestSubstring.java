package com.x.leetcode;

import java.util.stream.IntStream;

public class LongestSubstring {

    public int lengthOfLongestSubstring(String s) {
        int len = s.length();
        if (len < 2) return len;

        return IntStream.range(0, len).map(i -> maxAt(s, i)).max().orElse(0);
    }

    private int maxAt(String s, int index) {
        char c = s.charAt(index);
        int i = s.indexOf(c, index + 1);
        if (i > -1) {
//            (s.length() - index);
        }
//        return gap.stream().max(Comparator.naturalOrder()).orElse(0);
        return 0;
    }

    public static void main(String[] args) {
        int aaabbb = new LongestSubstring().lengthOfLongestSubstring("aaabbb");

    }
}
