/**
 * Class used to represent a node in the decision tree.
 */
class Node {
    // Classification class
    private int clas;
    // Threshold
    private Threshold th;
    // Left and right nodes
    private Node l, r;

    Node(Threshold th, int clas) {
        this.l = null;
        this.r = null;
        this.th = th;
        this.clas = clas;
    }

    void addLeft(Node left) {
        this.l = left;
    }

    void addRight(Node right) {
        this.r = right;
    }

    Node left() {
        return l;
    }

    Node right() {
        return  r;
    }

    int clas() {
        return clas;
    }

    Threshold th() {
        return th;
    }
}
