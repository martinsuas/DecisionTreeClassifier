import java.io.*;
import java.util.Scanner;
import java.util.HashSet;
import java.util.Set;

public class DTC_Main {
    public static void main(String args[]) throws Exception {
        if (args.length == 2 || args.length == 3) {
            String output_fs;
            if (args.length == 2) {
                output_fs = "data/test_results.txt";
            } else {
                output_fs = "data/" + args[2];
            }
            String train_fs = "data/" + args[0];
            String test_fs = "data/" + args[1];
            File train_f = new File(train_fs);
            File test_f = new File(test_fs);

            System.out.println("Decision Tree Classifier -- Version 1.0");
            double[][] training_data;
            double[][] testing_data;

            Set<Integer> clases = new HashSet<Integer>();

            if (train_f.exists() && test_f.exists()) {
                if (train_f.isFile() && train_f.canRead() && test_f.isFile() && test_f.canRead()) {
                    /*
                     * Get line count for both files
                     */
                    LineNumberReader reader  = new LineNumberReader(new FileReader(train_fs));
                    int cnt_t = 0;
                    String lineRead = "";
                    while ((lineRead = reader.readLine()) != null) {}
                    cnt_t = reader.getLineNumber();
                    reader  = new LineNumberReader(new FileReader(test_fs));
                    int cnt_c = 0;
                    lineRead = "";
                    while ((lineRead = reader.readLine()) != null) {}
                    cnt_c = reader.getLineNumber();
                    reader.close();
                    training_data = new double[cnt_t][];
                    testing_data = new double[cnt_c][];

                    /*
                     * Open up files again to process columns;
                     */
                    Scanner train = null;
                    Scanner test = null;
                    try {
                        train = new Scanner(train_f);
                        test = new Scanner(test_f);
                        // Get training data
                        assert(train.hasNextLine());
                        int i = 0;
                        while (train.hasNextLine()) {
                            String[] line = train.nextLine().split(",");
                            training_data[i] = new double[line.length];
                            for(int j = 0; j < line.length; j++) {
                                training_data[i][j] = Double.parseDouble(line[j]);
                                if (j == line.length - 1) {
                                    if (!clases.contains((int)training_data[i][j])) {
                                        clases.add((int)training_data[i][j]);
                                    }
                                }
                            }
                            i++;
                        }
                        // Get testing data
                        assert(test.hasNextLine());
                        i = 0;
                        while (test.hasNextLine()) {
                            String[] line = test.nextLine().split(",");
                            testing_data[i] = new double[line.length];
                            for(int j = 0; j < line.length; j++) {
                                testing_data[i][j] = Double.parseDouble(line[j]);
                            }
                            i++;
                        }

                    } catch (FileNotFoundException fnfe) {
                        fnfe.printStackTrace();
                    }
                    /*
                     * Create Gardener to grow up tree.
                     */
                    Gardener gardener = new Gardener(clases.size());
                    DecisionTree dt = new DecisionTree(gardener.growTree(training_data));
                    PrintWriter out_te = new PrintWriter(output_fs);

                    // Write test results
                    int total = 0;
                    int correct = 0;
                    out_te.println("- Testing results -");
                    for (double[] r : testing_data) {
                        int result = dt.Classify(r);
                        if (result == (int)r[r.length-1]) {
                            correct++;
                            //System.out.println("E:" + result + " C:" + (int)r[r.length-1]);
                            out_te.println("Estimated:" + result + " Correct:" + (int)r[r.length-1]);
                        } else {
                            //System.out.println("!! E:" + result + " C:" + (int)r[r.length-1]);
                            out_te.println("!! Estimated:" + result + " Correct:" + (int)r[r.length-1]);
                        }
                        total++;
                    }
                    System.out.println("Accuracy: " + ((double)correct/total));
                    System.out.println("Please, check file " + output_fs + " for full results.");
                    out_te.println("Accuracy: " + ((double)correct/total));

                    out_te.close();
                } else {
                    System.out.println("Cannot read one of the files. Please, check for an appropiate format" +
                            " on README.md or one of the supplied files.");
                }
            } else {
                System.out.println("Cannot find one of the supplied file names. Please, check they are" +
                " located in the 'data' folder.");
            }
        } else {
            System.out.println("Usage: DCT_Main training_file testing_file [output_file]");
        }
    }
}
