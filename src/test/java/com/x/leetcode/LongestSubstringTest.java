package com.x.leetcode;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LongestSubstringTest {

    private LongestSubstring longestSubstring = new LongestSubstring();

    @Test
    void testLengthOfSubString() {
        int i = longestSubstring.lengthOfLongestSubstring("");
        assertEquals(0, i);

        i = longestSubstring.lengthOfLongestSubstring("a");
        assertEquals(1, i);

        i = longestSubstring.lengthOfLongestSubstring("abcabcbb");
        assertEquals(3, i);

        i = longestSubstring.lengthOfLongestSubstring("bbbbb");
        assertEquals(1, i);

        i = longestSubstring.lengthOfLongestSubstring("pwwkew");
        assertEquals(3, i);

        i = longestSubstring.lengthOfLongestSubstring("bbbccc");
        assertEquals(2, i);
    }
}