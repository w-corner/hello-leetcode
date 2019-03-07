package com.x.sudoku;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.x.sudoku.data.PairChain;
import com.x.sudoku.data.SudokuNode;
import com.x.sudoku.resolver.BlockImpossibleResolver;
import com.x.sudoku.resolver.PairChainResolver;
import com.x.sudoku.resolver.PossibleCheckResolver;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.util.Collections.unmodifiableMap;
import static java.util.stream.Collectors.toSet;

@Slf4j
@Data
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

        game.registerResolver(new PossibleCheckResolver(game));
        game.registerResolver(new BlockImpossibleResolver(game));
        game.registerResolver(new PairChainResolver(game));

        game.start();
    }

    private void start() {
        for (int i = 0; i < 20; i++) {
            log.info("================ round {} ======================", i);
            round();
        }
    }

    private int inited = 0;
    private int solved = 0;

    public static final Set<Integer> ALL_ELEMENTS = ImmutableSet.of(1, 2, 3, 4, 5, 6, 7, 8, 9);
    private List<SudokuNode> allNodes = new ArrayList<>(81);
    private Map<Integer, Set<SudokuNode>> rows;
    private Map<Integer, Set<SudokuNode>> cols;
    private Map<Integer, Set<SudokuNode>> blocks;
    private Map<SudokuNode, Set<PairChain>> pairChains = Maps.newHashMap();
    private List<Resolver> resolvers = Lists.newArrayList();

    private void init() {

        IntStream.range(0, 9).forEach(y -> IntStream.range(0, 9).forEach(x -> allNodes.add(SudokuNode.builder().x(x).y(y).build())));

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
                    inited ++;
                });
    }

    private void registerResolver(Resolver resolver) {
        resolvers.add(resolver);
    }

    private Integer arrValue(Integer[][] arr, SudokuNode node) {
        return arr[node.getY()][node.getX()];
    }

    public void round() {
        allNodes.forEach(node -> resolvers.forEach(resolver -> resolver.resolve(node)));
        log.info("------ inited: {}, solved: {} --------------", inited, solved);
        if (inited + solved == allNodes.size()) {
            log.info("================ complete ======================");
        }
    }

    public Stream<SudokuNode> getAffectNodeStream(SudokuNode node) {
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

    public void showCurrent() {
        StringBuilder sb = new StringBuilder();
        allNodes.forEach(node -> {
            if (node.getX() % 9 == 0) {
                sb.append("\n");
            }
            if (node.getNumber() == null) {
                sb.append(String.format("%10s", node.getPossibleNumbers()));
            } else {
                sb.append(String.format("%10d", node.getNumber()));
            }
        });
        log.info(sb.toString());
    }

    public void registerPariChain(PairChain pair) {
        pair.getNodes().forEach(node -> {
            Set<PairChain> chains = pairChains.getOrDefault(node, Sets.newHashSet());
            chains.add(pair);
            pairChains.put(node, chains);
        });
    }

    private void destroyPairChain(SudokuNode node) {
        Set<PairChain> chains = pairChains.remove(node);
        if (chains == null) {
            return;
        }
        log.info("node {} 's chain {}", node, chains);
        chains.forEach(pairChain -> pairChain.destroyBy(node));
    }

    public boolean existPairChain(SudokuNode node, Set<SudokuNode> toCreateChain) {
        return pairChains.getOrDefault(node, Sets.newHashSet()).stream()
                .anyMatch(pairChain -> pairChain.getNodes().equals(toCreateChain));
    }

    public void solved(SudokuNode node) {
        solved ++;
        destroyPairChain(node);
    }
}
