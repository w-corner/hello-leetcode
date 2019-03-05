package com.x.sudoku;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class SudokuResolver {

    public static void main(String[] args) {
        SudokuResolver resolver = new SudokuResolver();

        Integer[][] arr = {
                {null, 7, null, 2, 3, null, 6, null, 4},
                {null, null, null, null, null, 9, null, null, null},
                {3, 6, 4, null, null, 7, null, null, null},

                {4, null, null, null, null, null, 3, null, null},
                {null, null, 7, 3, 2, 1, 8, null, null},
                {null, null, 3, null, null, null, null, null, 1},

                {null, null, null, 7, null, null, 4, 1, 3},
                {null, null, null, 5, null, null, null, null, null},
                {2, null, 8, null, 1, 4, null, 9, null}
        };
        resolver.initNumber(arr);

        for (int i = 0; i < 10; i++) {
            System.out.println("======================================");
            resolver.round();
        }
    }

    private List<SudokuNode> allNodes = new ArrayList<>(81);
    private static final Set<Integer> ALL_ELEMENTS = ImmutableSet.of(1, 2, 3, 4, 5, 6, 7, 8, 9);

    private void init() {

        IntStream.range(0, 9).forEach(x -> IntStream.range(0, 9).forEach(y -> allNodes.add(SudokuNode.builder().x(x).y(y).build())));

        Map<Integer, List<SudokuNode>> rows = allNodes.stream().collect(Collectors.groupingBy(SudokuNode::getX));
        Map<Integer, List<SudokuNode>> cols = allNodes.stream().collect(Collectors.groupingBy(SudokuNode::getY));
        Map<Integer, List<SudokuNode>> blocks = allNodes.stream().collect(Collectors.groupingBy(node -> node.getY() / 3 * 10 + node.getX() / 3));

        allNodes.forEach(node -> {
            node.setRows(rows.get(node.getX()));
            node.setCols(cols.get(node.getY()));
            node.setBlocks(blocks.get(node.getY() / 3 * 10 + node.getX() / 3));
        });
    }

    public void initNumber(Integer[][] arr) {
        init();
        //TODO: consume
        allNodes.stream()
                .filter(node -> arrValue(arr, node) != null)
                .forEach(node -> {
                    node.setNumber(arrValue(arr, node));
                    node.setNotInit(false);
                });
    }

    private Integer arrValue(Integer[][] arr, SudokuNode node) {
        return arr[node.getY()][node.getX()];
    }

    public void round() {
        allNodes.stream()
                .filter(SudokuNode::isNotInit)
                .forEach(node -> {
                    Set<Integer> existed = Stream.of(node.getRows(), node.getCols(), node.getBlocks())
                            .flatMap(Collection::stream)
                            .map(SudokuNode::getNumber)
                            .filter(Objects::nonNull)
                            .collect(Collectors.toSet());
                    Sets.SetView<Integer> possible = Sets.difference(ALL_ELEMENTS, existed);
                    node.setPossibleNumbers(possible);

                    if (node.getPossibleNumbers().size() == 1) {
                        Integer exacted = (Integer) node.getPossibleNumbers().toArray()[0];
                        node.setNumber(exacted);
                        node.setNumberFilled(true);
                        System.out.printf("(%d, %d)=%d%n", node.getX(), node.getY(), exacted);
                    }
                });
    }
}
