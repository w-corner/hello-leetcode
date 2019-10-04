package com.x.algorithm.huffman;

class QuickSort {
    /**
     * @description: 交换两个数组元素
     */
    private static void exchange(Node[] a, int i, int j) {
        Node temp = a[i];
        a[i] = a[j];
        a[j] = temp;
    }

    /**
     * @description: 切分函数
     */
    private static int partition(Node[] a, int low, int high) {
        int i = low, j = high + 1;      // i, j为左右扫描指针
        int pivotkey = a[low].getWeight();  // pivotkey 为选取的基准元素（头元素）
        while (true) {
            while (a[--j].getWeight() > pivotkey) {
                if (j == low) break;
            }  // 右游标左移
            while (a[++i].getWeight() < pivotkey) {
                if (i == high) break;
            }  // 左游标右移
            if (i >= j) break;    // 左右游标相遇时候停止， 所以跳出外部while循环
            else exchange(a, i, j);  // 左右游标未相遇时停止, 交换各自所指元素，循环继续
        }
        exchange(a, low, j); // 基准元素和游标相遇时所指元素交换，为最后一次交换
        return j;  // 一趟排序完成， 返回基准元素位置
    }

    /**
     * @description: 根据给定的权值对数组进行排序
     */
    private static void sort(Node[] a, int low, int high) {
        if (high <= low) {
            return;
        } // 当high == low, 此时已是单元素子数组，自然有序， 故终止递归
        int j = partition(a, low, high);  // 调用partition进行切分
        sort(a, low, j - 1);   // 对上一轮排序(切分)时，基准元素左边的子数组进行递归
        sort(a, j + 1, high); // 对上一轮排序(切分)时，基准元素右边的子数组进行递归
    }

    static void sort(Node[] a) { //sort函数重载， 只向外暴露一个数组参数
        sort(a, 0, a.length - 1);
    }
}
