package LinearAlgebra.Types.Vectors;

import java.util.HashMap;

public class SparseVector extends DefaultVectorBehavior {

    // Fields:
    private HashMap<Integer, Double> storage = new HashMap<>();
    private int size;

    // Constructor:
    public SparseVector(int size, int[] indices, double[] values) {
        this.size = size;

        if (indices.length == values.length) {
            for (int i = 0; i < indices.length; i++) {
                this.storage.put(indices[i], values[i]);
            }
        } else {
            throw new IllegalArgumentException();
        }
    }

    // Methods:
    @Override
    public double[] toArray() {
        return new double[0];
    }

    @Override
    public double getEntry(int index) {
        if (index < this.size()) {
            return this.storage.getOrDefault(index, 0d);
        } else {
            throw new IndexOutOfBoundsException();
        }
    }

    @Override
    public int size() {
        return this.size;
    }

    @Override
    public boolean isNull() {
        return this.storage.isEmpty();
    }

    @Override
    public boolean isDense() {
        return false;
    }

    @Override
    public boolean isOnehot() {
        if (this.storage.entrySet().size() == 1) {
            int index = 0;

            for (Integer indices : this.storage.keySet()) {
                index = indices;
            }

            return this.storage.get(index) == 1;
        } else {
            return false;
        }
    }
}
