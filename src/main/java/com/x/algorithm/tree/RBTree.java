package com.x.algorithm.tree;

public class RBTree {

    enum Color {
        BLACK, RED;
    }

    class Node {

        int v;
        Color color;

        Node parent;
        Node left;
        Node right;

        public Node(int v, Color color) {
            this.v = v;
            this.color = color;
        }

        Node grandPa() {
            if (parent == null) {
                return null;
            }
            return parent.parent;
        }

        Node uncle() {
            Node grandPa = grandPa();
            if (grandPa == null) {
                return null;
            }
            if (grandPa.left == parent) {
                return grandPa.right;
            } else
                return grandPa.left;
        }

        Node sibling() {
            if (parent.left == this) {
                return parent.right;
            } else {
                return parent.left;
            }
        }

        boolean isRightSon() {
            if (parent.right == this) {
                return true;
            }
            return false;
        }

        @Override
        public String toString() {
            return color.name() + v;
        }
    }

    int size = 0;
    Node[] tree;
    Node root;

    public RBTree(int cap) {
        tree = new Node[cap];
    }

    public String leftFirstIterator() {
        StringBuilder sb = new StringBuilder();
        leftCon(sb, root);
        if (sb.length() > 0) {
            sb.deleteCharAt(sb.length() - 1);
        }
        return sb.toString();
    }

    private void leftCon(StringBuilder sb, Node node) {
        if (node == null) {
            return;
        }
        leftCon(sb, node.left);
        sb.append(node.v).append(",");
        leftCon(sb, node.right);
    }

    public boolean add(int ele) {
        if (size < tree.length) {
            Node newNode = new Node(ele, Color.RED);
            putVal(root, newNode);

            tree[size] = newNode;
            size++;

            return true;
        }
        return false;
    }

    public boolean delete(int v) {
        if (root == null) return false;
        return delete(root, v);
    }

    private boolean delete(Node node, int v) {
        if (node == null) {
            return false;
        }
        if (node.v == v) {
            if (node.right == null) {
                deleteNode(node);
                return true;
            }
            Node smallestChild = getSmallestChild(node.right);
            node.v = smallestChild.v;
            deleteNode(smallestChild);
            return true;
        } else if (node.v > v) {
            if (node.left == null) {
                return false;
            }
            return delete(node.left, v);
        } else {
            if (node.right == null) {
                return false;
            }
            return delete(node.right, v);
        }
    }

    private void deleteNode(Node node) {
        Node child = node.left == null ? node.right : node.left;
        if (node.parent == null && node.left == null && node.right == null) {
            node = null;
            root = node;
            return;
        }
        if (node.parent == null) {
            child.parent = null;
            root = child;
            child.color = Color.BLACK;

            node = null;
            return;
        }
        if (node.isRightSon()) {
            node.parent.right = child;
        } else {
            node.parent.left = child;
        }
        if (child != null)
            child.parent = node.parent;

        if (node.color == Color.BLACK) {
            if (child != null && child.color == Color.RED) {
                child.color = Color.BLACK;
            } else {
                balanceTreeAfterDelete(child);
            }
        }
        node = null;
    }

    public Node getSmallestChild(Node node) {
        if (node.left == null) {
            return node;
        }
        return getSmallestChild(node.left);
    }

    private void putVal(Node node, Node ele) {
        if (node == null) {
            ele.color = Color.BLACK;
            root = ele;
            return;
        }
        if (node.v == ele.v) {

        } else if (node.v > ele.v) {
            if (node.left == null) {
                node.left = ele;
                ele.parent = node;

                balanceTreeAfterInsert(ele);
            } else {
                putVal(node.left, ele);
            }
        } else {
            if (node.right == null) {
                node.right = ele;
                ele.parent = node;

                balanceTreeAfterInsert(ele);
            } else {
                putVal(node.right, ele);
            }
        }
    }

    private void rotateLeft(Node node) {
        if (node.parent == null) {
            root = node;
            return;
        }
        System.out.println("rotateLeft -> " + node);
        Node gp = node.grandPa();
        Node p = node.parent;
        Node ls = node.left;

        p.right = ls;
        if (ls != null) {
            ls.parent = p;
        }
        node.left = p;
        p.parent = node;

        if (root == p) {
            root = node;
        }
        node.parent = gp;

        if (gp != null) {
            if (gp.left == p) {
                gp.left = node;
            } else {
                gp.right = node;
            }
        }
    }

    private void rotateRight(Node node) {
        if (node.parent == null) {
            root = node;
            return;
        }
        System.out.println("rotateRight -> " + node);

        Node gp = node.grandPa();
        Node p = node.parent;
        Node rs = node.right;

        p.left = rs;
        if (rs != null) {
            rs.parent = p;
        }
        node.right = p;
        p.parent = node;
        if (root == p) {
            root = node;
        }
        node.parent = gp;
        if (gp != null) {
            if (gp.left == p) {
                gp.left = node;
            } else {
                gp.right = node;
            }
        }
    }

    private void balanceTreeAfterDelete(Node node) {
        if (node.parent == null) {
            node.color = Color.BLACK;
            return;
        }
        if (node.sibling() != null && node.sibling().color == Color.RED) {
            node.sibling().color = Color.BLACK;
            node.parent.color = Color.RED;
            if (node.sibling().isRightSon()) {
                rotateLeft(node.sibling());
            } else {
                rotateRight(node.sibling());
            }
        }
        if (node.parent.color == Color.RED && node.sibling().color == Color.BLACK
                && node.sibling().left.color == Color.BLACK && node.sibling().right.color == Color.BLACK) {
            node.sibling().color = Color.RED;
            balanceTreeAfterDelete(node.parent);
        } else if (node.parent.color == Color.BLACK && node.sibling().color == Color.BLACK
                && node.sibling().left.color == Color.BLACK && node.sibling().right.color == Color.BLACK) {
            node.sibling().color = Color.RED;
            node.parent.color = Color.BLACK;
        } else {
            if (node.sibling().color == Color.BLACK) {
                if (!node.isRightSon() && node.sibling().right.color == Color.BLACK && node.sibling().left.color == Color.RED) {
                    node.sibling().color = Color.RED;
                    node.sibling().left.color = Color.BLACK;
                } else if (node.isRightSon() && node.sibling().right.color == Color.RED && node.sibling().left.color == Color.BLACK) {
                    node.sibling().color = Color.RED;
                    node.sibling().right.color = Color.BLACK;
                }
            }
            node.sibling().color = node.parent.color;
            node.parent.color = Color.BLACK;
            if (!node.isRightSon()) {
                node.sibling().right.color = Color.BLACK;
                rotateLeft(node.sibling());
            } else {
                node.sibling().left.color = Color.BLACK;
                rotateRight(node.sibling());
            }
        }
    }

    private void balanceTreeAfterInsert(Node node) {
        if (node.parent == null) {
            node.color = Color.BLACK;
            return;
        }
        if (node.parent.color == Color.RED) {
            if (node.uncle() != null && node.uncle().color == Color.RED) {
                node.parent.color = Color.BLACK;
                node.uncle().color = Color.BLACK;
                node.grandPa().color = Color.RED;
                balanceTreeAfterInsert(node.grandPa());
            } else {
                if (node.isRightSon() && !node.parent.isRightSon()) {
                    rotateLeft(node);
                    rotateRight(node);
                    node.color = Color.BLACK;
                    node.left.color = Color.RED;
                    node.right.color = Color.RED;
                } else if (!node.isRightSon() && node.parent.isRightSon()) {
                    rotateRight(node);
                    rotateLeft(node);
                    node.color = Color.BLACK;
                    node.left.color = Color.RED;
                    node.right.color = Color.RED;
                } else if (!node.isRightSon() && !node.parent.isRightSon()) {
                    node.parent.color = Color.BLACK;
                    node.grandPa().color = Color.RED;
                    rotateRight(node.parent);
                } else if (node.isRightSon() && node.parent.isRightSon()) {
                    node.parent.color = Color.BLACK;
                    node.grandPa().color = Color.RED;
                    rotateLeft(node.parent);
                }
            }
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        con(sb, root);
        return "RBTree{tree=[" + sb + "]}";
    }

    private void con(StringBuilder sb, Node node) {
        if (node != null) {
            sb.append(node).append(", ");
            con(sb, node.left);
            con(sb, node.right);
        } else {
            sb.append("null, ");
        }
    }

    public static void main(String[] args) {
        RBTree rbTree = new RBTree(10);

        System.out.println(rbTree);
    }
}
