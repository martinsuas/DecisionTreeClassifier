/**
 * Class used to represent a formed classification tree.
 */
class DecisionTree {
    private Node root;

    DecisionTree(Node root) {
        this.root = root;
    }

    /**
     * Finds the expected class of a given row
     * @param row data
     * @return class number
     */
    int Classify(double[] row) {
        return ClassifyHelper(row, this.root);
    }

    /**
     * Finds the expected class of a given row
     * @param row data
     * @param node current node being traversed
     * @return class number
     */
    private int ClassifyHelper(double[] row, Node node) {
        if (node.th() == null) {
            assert((node.left() != null && node.right() != null));
            assert(node.clas() != -1);
            return node.clas();
        } else {
            if (row[node.th().index()] <= node.th().value()) {
                return ClassifyHelper(row, node.left());
            } else {
                return ClassifyHelper(row, node.right());
            }
        }
    }
}
