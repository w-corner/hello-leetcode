package com.x.sudoku;

import java.util.Arrays;

public class S {
    private int[][] matrix = new int[9][9];

    public S(int[][] arr) {
        matrix = arr;
    }

    public static void main(String[] args) {
        int[][] arr = {
                {0, 4, 6,  7, 0, 0,  0, 0, 0},
                {0, 2, 0,  0, 8, 0,  7, 0, 0},
                {7, 0, 0,  0, 0, 9,  4, 0, 0},

                {5, 0, 0,  2, 0, 0,  0, 0, 0},
                {0, 3, 0,  0, 9, 0,  0, 2, 0},
                {0, 0, 0,  0, 0, 3,  0, 0, 1},

                {0, 0, 2,  6, 0, 0,  0, 0, 4},
                {0, 0, 8,  0, 7, 0,  0, 6, 0},
                {0, 0, 0,  0, 0, 1,  5, 9, 0},
        };
        S s = new S(arr);
        s.detect(0, 0);
    }

    private void detect(int x, int y) {
        if (x == 9) {
            if (y == 8) {
                System.out.println("eureka");
                printBoard();
                return;
            }
            x = 0;
            y += 1;
        }

        if (matrix[y][x] == 0) {
            for (int v = 1; v <= 9; v++) {
                if (check(x, y, v)) {
                    matrix[y][x] = v;
                    detect(x + 1, y);

                    matrix[y][x] = 0;
                }
            }
        } else {
            detect(x + 1, y);
        }
    }

    private boolean check(int x, int y, int v) {
        for (int i = 0; i < 9; i++) {
            if (matrix[y][i] == v || matrix[i][x] == v) {
                return false;
            }
        }

        // TODO: readable
        for (int a = x / 3 * 3; a < x / 3 * 3 + 3; a++) {
            for (int b = y / 3 * 3; b < y / 3 * 3 + 3; b++) {
                if (matrix[b][a] == v) {
                    return false;
                }
            }
        }

        return true;
    }

    private void printBoard() {
        for (int i = 0; i < 9; i++) {
            System.out.println(Arrays.toString(matrix[i]));
        }
    }
}
