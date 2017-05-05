import java.util.Arrays;
import java.util.stream.*;

/**
 * Class responsible to grow the decision tree.
 */
class Gardener {
    // Number of possible classes for a given dataset
    private int num_classes;

    Gardener(int num_classes) {
        this.num_classes = num_classes;
    }

    /**
     * Grows a tree with the given dataset
     * @param dataset a double array representing a list of objects
     * @return the root of the tree
     */
    Node growTree(double[][] dataset) {
        return growTree(dataset, 0, false);
    }

    /**
     * Grows a tree with the given partition
     * @param partition a double array representing a list of objects
     * @param level the current tree level, used only for debugging
     * @param debug whether to print the tree
     * @return the root of the tree
     */
    private Node growTree(double[][] partition, int level, boolean debug) {
        if (stopping_condition(partition)) {
            Node leaf = new Node(null, (int)partition[0][partition[0].length-1]);
            return leaf;
        }
        // Split current partition into two partitions
        Threshold th = findBestSplit(partition);
        //System.out.println(th);
        double[][] P1_buffer = new double[partition.length][];
        int P1_c = 0;
        double[][] P2_buffer = new double[partition.length][];
        int P2_c = 0;
        for (double[] r : partition) {
            if ( r[th.index()] <= th.value() ) {
                P1_buffer[P1_c] = new double[r.length];
                System.arraycopy(r, 0, P1_buffer[P1_c], 0, r.length);
                P1_c++;
            } else {
                P2_buffer[P2_c] = new double[r.length];
                System.arraycopy(r, 0, P2_buffer[P2_c], 0, r.length);
                P2_c++;
            }
        }
        double[][] P1 = new double[P1_c][];
        double[][] P2 = new double[P2_c][];
        for (int i = 0; i < P1_c; i++) {
            P1[i] = new double[P1_buffer[i].length];
            System.arraycopy(P1_buffer[i], 0, P1[i], 0, P1_buffer[i].length);
        }
        for (int i = 0; i < P2_c; i++) {
            P2[i] = new double[P2_buffer[i].length];
            System.arraycopy(P2_buffer[i], 0, P2[i], 0, P2_buffer[i].length);
        }

        // With the new partitions, determine left and right nodes.
        Node root = new Node(th, -1);
        if (debug) {
            for (int i = 0; i < level; i++) {
                System.out.print(" ");
            }
            System.out.print("A[ ");
            for (double[] r : P1) {
                System.out.print(r[th.index()]+"|"+(int)r[r.length-1]+" ");
            }
            System.out.println("]");
        }
        root.addLeft(growTree(P1, level + 1, debug));

        if (debug) {
            for (int i = 0; i < level; i++) {
                System.out.print(" ");
            }
            System.out.print("B[ ");
            for (double[] r : P2) {
                System.out.print(r[th.index()]+"|"+(int)r[r.length-1]+" ");
            }
            System.out.println("]");
        }
        root.addRight(growTree(P2, level + 1, debug));
        return root;
    }

    /**
     * Determines when the tree should stop growing
     * @param partition a double array representing a list of objects
     * @return true if it should stop
     */
    private boolean stopping_condition(double[][] partition) {
        assert(partition.length != 0);
        int clas_i = partition[0].length - 1;
        int clas = (int)partition[0][clas_i];
        for (double[] r : partition) {
            if ((int)r[clas_i] != clas) {
                return false;
            }
        }
        //System.out.println("STAHP!");
        return true;
    }

    /**
     * Determines the best split using the gini coefficient.
     * @param partition a double array representing a list of objects
     * @return a Threshold object containing the best split.
     */
    private Threshold findBestSplit(double[][] partition)  {
        double best_gini = 1.0f;
        Threshold th = null;

        // The following loops (ti and tj) around the datasets are used to iterate over possibles thresholds
        for (int ti = 0; ti < partition.length; ti++) {
            // Ignore last line, which holds class
            int c = partition[0].length - 1;
            for (int tj = 0; tj < partition[ti].length - 1; tj++) {
                int less_than[] = new int[num_classes];
                int more_than[] = new int[num_classes];
                // The following loops around the datasets to estimate gini values.
                for (int i = 0; i < partition.length; i++) {
                    // Find the current's row class
                    int index = (int)partition[i][c];
                    if (partition[i][tj] <=  partition[ti][tj]) {
                        less_than[index-1] += 1;
                    } else {
                        more_than[index-1] += 1;
                    }
                }
                // Estimate gini for current threshold
                double gini = calculate_gini(less_than, more_than);
                if (gini < best_gini) {
                    best_gini = gini;
                    th = new Threshold(partition[ti][tj], tj);
                }
            }
        }
        //System.out.println("Best gini: " + best_gini);
        return th;
    }

    /**
     * Estimates gini value.
     * @param less_than array of counting results
     * @param more_than array of counting results
     * @return gini
     */
    private double calculate_gini(int[] less_than, int[] more_than) {
        int less_total = IntStream.of(less_than).sum();
        int more_total = IntStream.of(more_than).sum();
        double gini = 1.0;
        double less_value, more_value;

        // Estimate Less
        less_value = 1;
        for (double less : less_than) {
            less_value -= (less/(less_total))*(less/(less_total));
        }
        double less_weight = (double)less_total / (less_total + more_total);
        less_value *= less_weight;

        // Estimate More
        more_value = 1;
        for (double more : more_than) {
            more_value -= (more/(more_total))*(more/(more_total));
        }
        double more_weight = (double)more_total / (less_total + more_total);
        more_value *= more_weight;

        gini = less_value + more_value;
        return gini;
    }


}
