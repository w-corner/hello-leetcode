package com.x.sudoku;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.util.Collections.unmodifiableMap;
import static java.util.stream.Collectors.toSet;

@Slf4j
public class SudokuGame {

    public static void main(String[] args) {
        SudokuGame game = new SudokuGame();

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
        game.initNumber(arr);

        for (int i = 0; i < 10; i++) {
            log.info("================ round {} ======================", i);
            game.round();
        }
    }

    private List<SudokuNode> allNodes = new ArrayList<>(81);
    private Map<Integer, Set<SudokuNode>> rows;
    private Map<Integer, Set<SudokuNode>> cols;
    private Map<Integer, Set<SudokuNode>> blocks;
    public static final Set<Integer> ALL_ELEMENTS = ImmutableSet.of(1, 2, 3, 4, 5, 6, 7, 8, 9);

    private void init() {

        IntStream.range(0, 9).forEach(x -> IntStream.range(0, 9).forEach(y -> allNodes.add(SudokuNode.builder().x(x).y(y).build())));

        rows = unmodifiableMap(allNodes.stream().collect(Collectors.groupingBy(SudokuNode::getX, toSet())));
        cols = unmodifiableMap(allNodes.stream().collect(Collectors.groupingBy(SudokuNode::getY, toSet())));
        blocks = unmodifiableMap(allNodes.stream().collect(Collectors.groupingBy(SudokuNode::getBlockKey, toSet())));
    }

    public void initNumber(Integer[][] arr) {
        init();
        allNodes.stream()
                .filter(node -> arrValue(arr, node) != null)
                .forEach(node -> {
                    Integer number = arrValue(arr, node);
                    node.setNumber(number);
                    node.setNotInit(false);
                    node.setPossibleNumbers(Collections.singleton(number));
                });
    }

    private Integer arrValue(Integer[][] arr, SudokuNode node) {
        return arr[node.getY()][node.getX()];
    }

    public void round() {
        allNodes.forEach(node -> {
            if (node.isNotInit() && node.isNotFilled()) {
                possibleCheck(node);
            } else {
                impossibleSet(node);
            }
        });
    }

    private void impossibleSet(SudokuNode currentCheck) {
        getAffectNodeStream(currentCheck).forEach(node -> {
            node.removeImpossible(currentCheck.getNumber());
            if (node.isNotInit() && node.isNotFilled() && node.getPossibleNumbers().size() == 1) {
                node.fillNumber(node.getPossibleNumbers().stream().findFirst().get());
            }
        });
    }

    private void possibleCheck(SudokuNode node) {
        Set<Integer> existed = getAffectNodeStream(node)
                .map(SudokuNode::getNumber)
                .filter(Objects::nonNull)
                .collect(toSet());
        Sets.SetView<Integer> possible = Sets.difference(node.getPossibleNumbers(), existed);
        node.setPossibleNumbers(Sets.newHashSet(possible));

        if (node.getPossibleNumbers().size() == 1) {
            Integer exacted = node.getPossibleNumbers().stream().findFirst().get();
            node.fillNumber(exacted);
            return;
        }

        Sets.SetView<SudokuNode> otherNodesInSameBlock = Sets.difference(blocks.get(node.getBlockKey()), Collections.singleton(node));
        Set<Integer> possibleOfOtherNode = otherNodesInSameBlock.stream()
                .map(SudokuNode::getPossibleNumbers)
                .flatMap(Collection::stream)
                .collect(toSet());
        Sets.SetView<Integer> othersImpossible = Sets.difference(ALL_ELEMENTS, possibleOfOtherNode);
        Sets.SetView<Integer> selfPossible = Sets.intersection(node.getPossibleNumbers(), othersImpossible);
        if (selfPossible.size() == 1) {
            node.fillNumber(selfPossible.stream().findFirst().get());
            return;
        }
    }

    private Stream<SudokuNode> getAffectNodeStream(SudokuNode node) {
        return Stream.of(rows.get(node.getX()), cols.get(node.getY()), blocks.get(node.getBlockKey()))
                .flatMap(Collection::stream)
                .filter(n -> !n.equals(node))
                .distinct();
    }

    public SudokuNode getNode(int x, int y) {
        return allNodes.stream()
                .filter(n -> n.getX() == x && n.getY() == y)
                .findFirst()
                .orElse(null);
    }
}
