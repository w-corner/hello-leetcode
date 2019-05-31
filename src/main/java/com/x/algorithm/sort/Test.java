package com.x.algorithm.sort;

import java.util.Iterator;
import java.util.LinkedList;

public class Test {


    public static void main(String[] args) {
        LinkedList<Integer> list = new LinkedList<>();
        for (int i = 0; i < 20; i++) {
            list.add(i);
        }

        Integer integer = get(list);
        System.out.println(integer);
    }

    private static Integer get(LinkedList<Integer> list) {
        int count = 0;
        while (true) {
            Iterator<Integer> iterator = list.iterator();
            while (iterator.hasNext()) {
                count++;
                iterator.next();
                if (count % 3 == 0) {
                    iterator.remove();
                    if (list.size() == 1) {
                        return iterator.next();
                    }
                }
            }
        }
    }
}
