package com.x.algorithm.sort;

import java.util.Arrays;

public class BubbleSort {

    public static void main(String[] args) {
        int[] arr = {11, 2, 23, 4, 15, 6, 37, 8, 19};
        System.out.println(Arrays.toString(arr));

        BubbleSort.sort2(arr);
        System.out.println(Arrays.toString(arr));
    }

    private static void sort(int[] arr) {
        for (int i = 0; i < arr.length; i++) {
            for (int j = i + 1; j < arr.length; j++) {
                if (arr[i] > arr[j]) {
                    swap(arr, i, j);
                }
//                System.out.format("%d %d ---> %s%n", i, j, Arrays.toString(arr));
            }
        }
    }

    private static void swap(int[] arr, int i, int j) {
        int tmp = arr[i];
        arr[i] = arr[j];
        arr[j] = tmp;
    }

    private static void sort2(int[] arr) {
        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < arr.length - 1 - i; j++) {
                if (arr[j] > arr[j + 1]) {
                    swap(arr, j, j + 1);
                }
                System.out.format("%d %d ---> %s%n", i, j, Arrays.toString(arr));
            }
        }
    }


}
