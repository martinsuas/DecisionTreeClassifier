import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.HashSet;
import java.util.Set;
import java.io.File;
import java.io.FileReader;
import java.io.LineNumberReader;
public class DTC_Main {
    public static void main(String args[]) throws Exception {
        System.out.println("Decision Tree Classifier -- Version 1.0");
        double[][] training_data;
        double[][] testing_data;

        for(;;) {
            Scanner in = new Scanner(System.in);
            System.out.println("Please type the name of the training file, or type 'exit' to end the program:");
            String filename_t = in.nextLine().trim();
            if (filename_t.equalsIgnoreCase("quit")) {
                break;
            }
            System.out.println("Please type the name of the testing file:");
            String filename_c = in.nextLine().trim();


            // DEBUG
            //String filename_t = "bears_train.csv";
            //String filename_c = "bears_test.csv";
            //String filename_t = "k_test.csv";
            //String filename_c = "k_train.csv";

            File train_f = new File(filename_t);
            File test_f = new File(filename_c);

            Set<Integer> clases = new HashSet<Integer>();

            if (train_f.exists() && test_f.exists()) {
                if (train_f.isFile() && train_f.canRead() && test_f.isFile() && test_f.canRead()) {
                    /*
                     * Get line count for both files
                     */
                    LineNumberReader reader  = new LineNumberReader(new FileReader(filename_t));
                    int cnt_t = 0;
                    String lineRead = "";
                    while ((lineRead = reader.readLine()) != null) {}
                    cnt_t = reader.getLineNumber();
                    reader  = new LineNumberReader(new FileReader(filename_c));
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
                            String[] line = train.nextLine().split(";");
                            training_data[i] = new double[line.length];
                            for(int j = 0; j < line.length; j++) {
                                training_data[i][j] = Double.parseDouble(line[j].replace(',','.'));
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
                            String[] line = test.nextLine().split(";");
                            testing_data[i] = new double[line.length];
                            for(int j = 0; j < line.length; j++) {
                                testing_data[i][j] = Double.parseDouble(line[j].replace(',','.'));
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
                    int total = 0;
                    int correct = 0;

                    for (double[] r : testing_data) {
                        int result = dt.Classify(r);
                        if (result == (int)r[r.length-1]) {
                            correct++;
                        }
                        total++;
                        System.out.println("R:" + result + " E:" + (int)r[r.length-1]);
                    }

                    System.out.println("Accuracy: " + ((double)correct/total));
                    break;
                }
            }
        }
    }
}
