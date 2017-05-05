/**
 * Simple class used to represent a threshold.
 */
class Threshold {
    // Value of threshold
    private double value;
    // Attribute index
    private int index;

    Threshold(double value, int index) {
        this.value = value;
        this.index = index;
    }

    double value() {
        return value;
    }

    int index() {
        return index;
    }

    public String toString() {
        return "Th: " + index + " V: " + value;
    }
}
