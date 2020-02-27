package LinearAlgebra.Types.Vectors;

import java.util.Arrays;
import java.util.Collection;

public class DenseVector extends DefaultVectorBehavior {

    // Field:
    private double[] vector;

    // Constructors:
    public DenseVector(double[] vector) {
        this.vector = vector;
    }

    public DenseVector(Collection<Double> vector) {
        int i = 0;
        this.vector = new double[vector.size()];
        for (Double entry : vector)
            this.vector[i++] = entry;
    }

    /*
    public DenseVector(Vector vector) {
        this.vector = new double[vector.size()];
        vector.compLoop((i,val) -> this.vector[i] = val);
    }
    */

    public DenseVector(int size) {
        this.vector = new double[size];
    }

    // Methods:
    @Override
    public double[] toArray() {
        return Arrays.copyOf(this.vector, this.vector.length);
    }

    @Override
    public double getEntry(int index) {
        return this.vector[index];
    }

    @Override
    public int size() {
        return this.vector.length;
    }

    @Override
    public boolean isNull() {
        boolean isNull = true;

        for (int i = 0; (i < this.size()) && isNull; i++) {
            isNull = this.getEntry(i) == 0;
        }

        return isNull;
    }

    @Override
    public boolean isDense() {
        return !(isNull() || isOnehot());
    }

    /*
    @Override
    public String toString() {
        return Arrays.toString(this.toArray());
    }
    */

    // Returns a deep copy of the vector.
    /*
    @Override
    public Vector clone() {
        double[] newVector = this.toArray().clone();
        return new DenseVector(newVector);
    }
    */

    /*
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vector vector1 = (Vector) o;
        return Arrays.equals(vector, vector1.toArray());
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(vector);
    }
    */

    @Override
    @SuppressWarnings("Duplicates") //IntelliJ thinks this is the same code as in matrixBuilder
    public boolean isOnehot() {
        boolean hasEncountedOne = false;
        for (double entry : vector) {
            if (entry != 0) {
                if (entry == 1) {
                    if (hasEncountedOne) {
                        return false;
                    } else {
                        hasEncountedOne = true;
                    }
                } else return false;
            }
        }
        return hasEncountedOne;
    }

}
