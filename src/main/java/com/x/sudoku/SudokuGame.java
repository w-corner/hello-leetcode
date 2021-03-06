package com.x.sudoku;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.x.sudoku.data.PairChain;
import com.x.sudoku.data.SudokuNode;
import com.x.sudoku.resolver.*;
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

        int[][] arr = {
//                {0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 1, 0, 6, 0, 5, 2},
                {7, 0, 0, 0, 0, 0, 0, 6, 0},
                {0, 4, 0, 0, 0, 0, 0, 0, 0},

                {0, 0, 0, 2, 5, 0, 0, 0, 0},
                {0, 9, 0, 0, 0, 0, 3, 0, 0},
                {0, 0, 0, 0, 7, 0, 0, 0, 0},

                {0, 0, 1, 0, 0, 9, 8, 0, 0},
                {5, 0, 0, 0, 0, 0, 0, 0, 0},
                {2, 0, 0, 0, 0, 0, 0, 0, 0},

        };
        game.initNumber(arr);

        game.registerResolver(new PossibleCheckResolver(game));
        game.registerResolver(new BlockImpossibleResolver(game));
//        game.registerResolver(new PairChainResolver(game));
        game.registerResolver(new LineImpossibleResolver(game));
        game.registerResolver(new UnitCheckResolver(game));

        game.start();
    }

    private void start() {
        for (int round = 0; round < 20; round++) {
            log.info("================ round {} ======================", round);
            allNodes.forEach(node -> resolvers.forEach(resolver -> resolver.resolve(node)));
            log.info("------ inited: {}, solved: {} --------------", inited, solved);
            if (inited + solved == allNodes.size()) {
                log.info("================ complete@{} ======================", round);
                break;
            }
        }
        printBoard();
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

        rows = unmodifiableMap(allNodes.stream().collect(Collectors.groupingBy(SudokuNode::getY, toSet())));
        cols = unmodifiableMap(allNodes.stream().collect(Collectors.groupingBy(SudokuNode::getX, toSet())));
        blocks = unmodifiableMap(allNodes.stream().collect(Collectors.groupingBy(SudokuNode::getBlockKey, toSet())));
    }

    public void initNumber(int[][] arr) {
        init();
        allNodes.stream()
                .filter(node -> arrValue(arr, node) != 0)
                .forEach(node -> {
                    Integer number = arrValue(arr, node);
                    node.setNumber(number);
                    node.setNotInit(false);
                    node.setNotFilled(false);
                    node.setPossibleNumbers(Collections.singleton(number));
                    inited++;

                    getAffectNodeStream(node)
                            .forEach(n -> {
                                n.removeImpossible(number);
                            });
                });
    }

    private void registerResolver(Resolver resolver) {
        resolvers.add(resolver);
    }

    private Integer arrValue(int[][] arr, SudokuNode node) {
        return arr[node.getY()][node.getX()];
    }

    public SudokuNode getNode(int x, int y) {
        return allNodes.stream()
                .filter(n -> n.getX() == x && n.getY() == y)
                .findFirst()
                .orElse(null);
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
        chains.forEach(pairChain -> {
            HashSet<Integer> before = Sets.newHashSet(pairChain.getPossibleNumbers());
            Optional<SudokuNode> theOtherNode = pairChain.destroyBy(node);
            theOtherNode.ifPresent(node1 -> {
                Integer number = pairChain.getPossibleNumbers().stream().findFirst().get();
                log.info("PairChain {} destory by {}, fill {} with: {}", before, node, node1, number);

                if (node1.isNotFilled()) {
                    fillNumber(node1, number);
                }

                pairChains.getOrDefault(node1, Sets.newHashSet()).removeIf(pairChain1 -> pairChain1.equals(pairChain));
            });

        });
    }

    public boolean existPairChain(SudokuNode node, Set<SudokuNode> toCreateChain) {
        return pairChains.getOrDefault(node, Sets.newHashSet()).stream()
                .anyMatch(pairChain -> pairChain.getNodes().equals(toCreateChain));
    }

    public void solved(SudokuNode node) {
        solved++;
        destroyPairChain(node);
    }

    public void printBoard() {
        StringBuilder sb = new StringBuilder();
        allNodes.forEach(node -> {
            if (node.getX() % 9 == 0) {
                sb.append("\n");
            }
            sb.append(String.format("%4s", node.getNumber() == 0 ? node.getPossibleNumbers() : node.getNumber()));
        });
        log.info(sb.toString());
    }

    public SudokuNode getNode(SudokuNode node) {
        return getNode(node.getX(), node.getY());
    }

    public void fillNumber(SudokuNode copyNode, int number) {
        SudokuNode node = getNode(copyNode);
        if (!node.isNotInit() || !node.isNotFilled()) {
            throw new IllegalStateException("error fill: " + copyNode + " with: " + number);
        }

        node.setNumber(number);
        node.setPossibleNumbers(Collections.singleton(number));
        node.setNotFilled(false);
        log.info("({},{})={}", node.getX(), node.getY(), number);

        solved(copyNode);

        getAffectNodeStream(copyNode)
                .forEach(n -> {
                    n.removeImpossible(number);
                    if (n.getPossibleNumbers().size() == 1 && n.isNotFilled()) {
                        fillNumber(n, n.getPossibleNumbers().stream().findFirst().orElse(0));
                    }
                });

    }

    public Stream<SudokuNode> getAffectNodeStream(SudokuNode node) {
        return Stream.of(getRows().get(node.getY()), getCols().get(node.getX()), getBlocks().get(node.getBlockKey()))
                .flatMap(Collection::stream)
                .filter(n -> !n.equals(node))
                .distinct();
    }
}
