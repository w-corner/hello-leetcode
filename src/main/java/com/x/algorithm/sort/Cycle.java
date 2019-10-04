package com.x.algorithm.sort;

import java.util.Arrays;

public class Cycle {

    private Node[] eles;
    private int count;
    private Node first;

    private Cycle(int count) {
        this.count = count;
        eles = new Node[count];
        for (int i = 1; i <= count; i++) {
            eles[i - 1] = new Node(i);
        }

        for (int i = 0; i < eles.length; i++) {
            eles[i].next = eles[(i + 1) % eles.length];
        }

        first = eles[0];

        System.out.println(Arrays.toString(eles));
    }

    public static void main(String[] args) {
        Cycle cycle = new Cycle(20);
        int ccc = 0;
        for (Node node = cycle.first; node != null; ) {
            if (cycle.count == 1) {
                break;
            }
            ccc++;
            Node tmp = node.next;
            if (ccc % 3 == 0) {
                cycle.remove(node);
            }
            node = tmp;
        }
        System.out.println(cycle);
    }

    private void remove(Node node) {
        int i = (node.index - 1 + eles.length) % eles.length;

        int x = findPre(node);
        if (first.index == node.index) {
            first = first.next;
        }

        eles[x].next = eles[i].next;
        eles[i].next = null;

        count--;
    }

    private int findPre(Node node) {

        for (Node ite = first; ite != null; ite = ite.next) {

            if (ite.next != null && ite.next.index == node.index) {
                return (ite.index - 1 + eles.length) % eles.length;
            }
        }
        throw new IllegalStateException(node + " have no pre node");
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Node ele : eles) {
            if (ele.next != null) {
                sb.append(ele).append(", ");
            }
        }
        return sb.toString();
    }

    private static class Node {
        int index;
        Node next;

        Node(int index) {
            this.index = index;
        }

        public Node next() {
            return next;
        }

        public String toString() {
            return index + " -> " + (next == null ? "" : next.index);
        }
    }
}
