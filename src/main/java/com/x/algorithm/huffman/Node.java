package com.x.algorithm.huffman;

import lombok.Data;

@Data
class Node {
    private char data;
    private int weight;
    private int left, right, parent; // index

    Node(char data, int weight) {
        this.data = data;
        this.weight = weight;
    }

    public Node(int weight) {
        this.weight = weight;
    }
}
