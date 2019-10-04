package com.x.algorithm.tree;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RBTreeTest {


    @Test
    void test_left_first_iterator() {
        RBTree tree = new RBTree(10);
        assertEquals("", tree.leftFirstIterator());

        tree.add(15);
        tree.add(5);
        assertEquals("5,15", tree.leftFirstIterator());

        tree.add(10);
        assertEquals("5,10,15", tree.leftFirstIterator());

        tree.add(20);
        assertEquals("5,10,15,20", tree.leftFirstIterator());
    }

    @Test
    void test_add() {
        RBTree tree = new RBTree(10);
        tree.add(10);
        tree.add(15);
        assertEquals("10,15", tree.leftFirstIterator());
    }

    @Test
    void test_delete() {
        RBTree tree = new RBTree(10);
        tree.add(10);

        // delete not exist value, return false
        assertFalse(tree.delete(1));
        assertEquals("10", tree.leftFirstIterator());

        // delete root node, return empty string
        assertTrue(tree.delete(10));
        assertEquals("", tree.leftFirstIterator());
    }

    @Test
    void delete_root_node_with_children() {
        RBTree tree = new RBTree(10);
        // delete root node with child,
        tree.add(10);
        tree.add(11);
        assertTrue(tree.delete(10));
        assertEquals("11", tree.leftFirstIterator());

        tree.add(10);
        tree.add(12);
        assertTrue(tree.delete(11));
        assertEquals("10,12", tree.leftFirstIterator());
    }

    @Test
    void delete_red_children() {
        RBTree tree = new RBTree(10);
        tree.add(10);
        tree.add(11);
        tree.add(12);

        assertTrue(tree.delete(12));
        assertEquals("10,11", tree.leftFirstIterator());
    }
}