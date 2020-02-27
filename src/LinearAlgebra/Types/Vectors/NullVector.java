package LinearAlgebra.Types.Vectors;

import LinearAlgebra.Types.Matrices.Matrix;
import LinearAlgebra.Types.Matrices.MatrixBuilder;

public class NullVector extends DefaultVectorBehavior {

    // Field
    private int size;

    // Constructor
    public NullVector(int size) {
        this.size = size;
    }

    @Override
    public double[] toArray() {
        return new double[this.size];
    }

    @Override
    public double dotProduct(Vector B) {
        Vector A = this;

        if (A.size() != B.size()) {
            throw new IllegalArgumentException("Vector A and B has to have the same size");
        }

        return 0;
    }

    @Override
    public Matrix mult(Matrix B) {
        if (B.getRows() != this.size)
            throw new IllegalArgumentException("Amount of rows in matrix must be equals the size of the vector.");

        return MatrixBuilder.buildEmpty(B.getRows(), B.getColumns());

    }

    @Override
    public Vector mult(double scalar) {
        return this.clone();
    }

    @Override
    public Vector div(double scalar) {
        return this.clone();
    }

    @Override
    public Vector sub(Vector B) {

        if (B.isNull())
            return this.clone();

        return new VectorBuilder(B)
                .negate()
                .build();
    }

    @Override
    public Vector add(Vector B) {
        return B.clone();
    }

    @Override
    public double norm(int p) {
        return 0;
    }

    @Override
    public double normMax() {
        return 0;
    }

    @Override
    public double getEntry(int index) {
        if (index < 0 || index >= size)
            throw new IndexOutOfBoundsException();
        return 0;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isNull() {
        return true;
    }

    @Override
    public Matrix toRowMatrix() {
        return new MatrixBuilder(1, this.size, true)
                //.addColumns(this.size)
                .build();
    }

    @Override
    public Matrix toColumnMatrix() {
        return new MatrixBuilder(this.size, 1, true)
                //.addEmptyRows(this.size)
                .build();
    }

    @Override
    public boolean isOnehot() {
        return false;
    }

    @Override
    public boolean isDense() {
        return false;
    }

    @Override
    public Vector clone() {
        return new NullVector(this.size);
    }

}
