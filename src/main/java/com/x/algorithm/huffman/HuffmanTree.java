package com.x.algorithm.huffman;

import java.util.Arrays;

public class HuffmanTree {

    private int selectStart = 0;

    private static class HuffmanCode {
        char data; // 存放字符，例如 'C'
        String bit; // 存放编码后的字符串, 例如"111"

        HuffmanCode(char data, String bit) {
            this.data = data;
            this.bit = bit;
        }
    }

    /**
     * @description: 返回权值排名为rank的结点对象在nodes中的下标（按权值从小到大排）
     */
    private int select(Node[] HT, int range, int rank) {
        Node[] copyNodes = Arrays.copyOf(HT, range);// 将HT[0]~HT[range]拷贝到copyNodes中
        QuickSort.sort(copyNodes); // 对copyNodes进行从小到大的快速排序
        Node target = copyNodes[rank + selectStart]; // 取得“删除”后权值排名为rank的结点对象
        for (int j = 0; j < HT.length; j++) {
            if (target == HT[j]) return j; // 返回该结点对象在数组HT中的下标
        }
        return -1;
    }

    /**
     * @description: 构建赫夫曼树
     */
    private Node[] buildTree(Node[] nodes) {
        int s1, s2, p;
        int n = nodes.length; // 外结点的数量
        int m = 2 * n - 1; // 内结点 + 外结点的总数量
        Node[] HT = new Node[m]; // 存储结点对象的HT数组
        for (int i = 0; i < m; i++) HT[i] = new Node(0); // 初始化HT数组元素
        for (int i = 0; i < n; i++) {
            HT[i].setData(nodes[i].getData());
            HT[i].setWeight(nodes[i].getWeight()); //将给定的权值列表赋给外结点对象
        }
        for (int i = n; i < m; i++) {
            s1 = select(HT, i, 0); // 取得HT数组中权值最小的结点对象的下标
            s2 = select(HT, i, 1); // 取得HT数组中权值次小的结点对象的下标
            HT[i].setLeft(s1); // 建立链接
            HT[i].setRight(s2);
            HT[s1].setParent(i);
            HT[s2].setParent(i);
            HT[i].setWeight(HT[s1].getWeight() + HT[s2].getWeight());// 计算当前外结点的权值
            selectStart += 2; // 这个变量表示之前“被删除”的最小结点的数量和
        }
        return HT; // 将处理后的HT数组返回
    }

    /**
     * @description: 进行赫夫曼编码
     */
    public HuffmanCode[] encode(Node[] nodes) {
        Node[] HT = buildTree(nodes); // 根据输入的nodes数组构造赫夫曼树
        int n = nodes.length;
        HuffmanCode[] HC = new HuffmanCode[n];
        StringBuilder bit;
        for (int i = 0; i < n; i++) { // 遍历各个叶子结点
            bit = new StringBuilder();
            for (int c = i, f = HT[i].getParent(); f != 0; c = f, f = HT[f].getParent()) { // 从叶子结点上溯到根结点
                if (HT[f].getLeft() == c) bit.insert(0, "0"); // 反向编码
                else bit.insert(0, "1");
            }
            HC[i] = new HuffmanCode(HT[i].getData(), bit.toString()); // 将字符和对应的编码存储起来
        }
        return HC;
    }

    /**
     * @description: 进行赫夫曼译码
     */
    private String decode(Node[] nodes, String code) {
        StringBuilder str = new StringBuilder();
        Node[] HT = buildTree(nodes);
        int n = HT.length - 1;
        for (int i = 0; i < code.length(); i++) {
            char c = code.charAt(i);
            if (c == '1') {
                n = HT[n].getRight();
            } else {
                n = HT[n].getLeft();
            }
            if (HT[n].getLeft() == 0) {
                str.append(HT[n].getData());
                n = HT.length - 1;
            }
        }
        return str.toString();
    }

    /**
     * @description: buildTree方法的用例
     */
    public static void main(String[] args) {
        Node[] nodes = new Node[4];
        nodes[0] = new Node('A', 7);
        nodes[1] = new Node('B', 5);
        nodes[2] = new Node('C', 2);
        nodes[3] = new Node('D', 4);
        HuffmanTree ht = new HuffmanTree();
        System.out.println(ht.decode(nodes, "010110111"));
    }
}
