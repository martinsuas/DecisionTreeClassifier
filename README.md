# Decision Tree Classifier
A program that creates a decision tree classifier that predicts, given some training data, 
the classification of some testing data. 
## Running
Run the main file, DTC_Main, and it will prompt you for the name of the file you want to load.
This file must be located in the 'data' folder with the rest of the supplied csv files.

javac *.java

java DTC_Main train_file test_file [$output_file]

e.g.: java DCT_Main bears_train.csv bears_test.csv bears_result.txt

If no output file is specified, the results will be written to the
default file 'test_results.txt'.

## Format
To use:
All csv files must be located in the 'data' folder and must follow this pattern:

At1,At2,...,Atn,Class

(See supplied datasets.)
