package com.x.leetcode;

public class LongestCommonPrefix {
    public String longestCommonPrefix(String[] strs) {
        if (strs.length == 0) {
            return "";
        }
        String res = strs[0];
        do {

            if (startedWith(strs, res)) {
                return res;
            }
            res = res.substring(0, res.length() - 1);
        } while (res.length() > 0);
        return "";
    }

    private boolean startedWith(String[] strs, String res) {
        for (int i = 1; i < strs.length; i++) {
            if (!strs[i].startsWith(res)) {
                return false;
            }
        }
        return true;
    }
}
