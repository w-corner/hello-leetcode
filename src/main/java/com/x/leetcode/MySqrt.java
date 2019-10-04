package com.x.leetcode;

public class MySqrt {

    private int mySqrt(int x) {
        if (x < 0) throw new IllegalArgumentException("");
        if (x <= 1) return x;
        for (int i = 1; i <= x; i++) {
            int v = i * i;
            if (v <= x && (v + 2 * i + 1 > x || 2 * i > Integer.MAX_VALUE - 1 - v)) {
                return i;
            }
        }
        throw new IllegalArgumentException("");
    }

    public static int mySqrt2(int x) {
        if (x < 0) {
            throw new IllegalArgumentException("Argument x can not be negative.");
        }
        if (x == 0 || x == 1) {
            return x;
        }

        for (int i = 1; i <= x / 2; i++) {
            int iSquare = i * i;
            if (iSquare <= x
                    && (Integer.MAX_VALUE - iSquare < 2 * i + 1 || (iSquare + 2 * i + 1) > x)) {
                return i;
            }
        }
        throw new IllegalArgumentException("no solution");
    }

    public int mySqrt3(int x) {
        return (int) Math.sqrt(x);
    }

    public static void main(String[] args) {
        MySqrt mySqrt = new MySqrt();

//        System.out.println(1 << 30);
        long start = System.nanoTime();
        int sqrt = mySqrt.mySqrt(3609);
        long end = System.nanoTime();
        System.out.println((end - start) + "ns: " + sqrt);

    }
}
