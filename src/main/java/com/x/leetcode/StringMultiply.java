package com.x.leetcode;

import java.math.BigInteger;

public class StringMultiply {

    public static void main(String[] args) {
        BigInteger a = BigInteger.valueOf(112233);
        BigInteger b = new BigInteger("44556677889900112234778899065754564");
        BigInteger multiply = a.multiply(b);
        System.out.println(multiply);
    }
}
