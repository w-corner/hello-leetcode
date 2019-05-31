package com.x.sudoku;

public class Su {

    public static void main(String[] args) {
        int[][] arr = {
//                {0, 0, 0,  0, 0, 0,  0, 0, 0,},
                {0, 7, 0,  4, 8, 0,  0, 0, 0,},
                {0, 0, 9,  0, 0, 0,  5, 0, 0,},
                {8, 0, 4,  0, 0, 0,  0, 2, 9,},

                {7, 0, 0,  0, 1, 5,  0, 0, 0,},
                {9, 0, 0,  7, 0, 2,  1, 0, 0,},
                {0, 0, 1,  0, 9, 0,  0, 0, 0,},

                {0, 0, 0,  0, 5, 0,  0, 0, 3,},
                {2, 1, 0,  0, 0, 3,  6, 0, 4,},
                {0, 4, 0,  1, 0, 0,  0, 0, 0,},
        };
        resolve(arr, 0, 0);
    }

    private static void resolve(int[][] arr, int y, int x) {
        // TODO: break
        if (x == 9) {
            if (y == 8) {
                System.out.println("eureka");
                printBoard(arr);
                return;
            }

            y += 1;
            x = 0;
        }

        if (arr[y][x] == 0) {
            for (int v = 1; v <= 9; v++) {
                if (check(arr, y, x, v)) {
                    arr[y][x] = v;
                    resolve(arr, y, x + 1);

                    arr[y][x] = 0;
                }
            }
        } else {
            resolve(arr, y, x + 1);
        }
    }

    private static boolean check(int[][] arr, int y, int x, int v) {
        for (int i = 0; i < 9; i++) {
            if (arr[y][i] == v || arr[i][x] == v) {
                return false;
            }
        }
        for (int a = y / 3 * 3; a < y / 3 * 3 + 3; a++) {
            for (int b = x / 3 * 3; b < x / 3 * 3 + 3; b++) {
                if (arr[a][b] == v) {
                    return false;
                }
            }
        }
        return true;
    }

    private static void printBoard(int[][] arr) {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                System.out.print(arr[i][j] + ", ");
            }
            System.out.println();
        }
    }
}
