package LinearAlgebra.Types.Matrices;

public class TransposedMatrix extends DefaultMatrixBehavior {

    // Field:
    private final Matrix parent;

    // Constructor:
    public TransposedMatrix(Matrix parent) {
        this.parent = parent;
    }

    // Methods:
    @Override
    public int getRows() {
        return parent.getColumns();
    }

    @Override
    public int getColumns() {
        return parent.getRows();
    }

    @Override
    public double getEntry(int row, int col) {
        return parent.getEntry(col, row);
    }

    @Override
    public Matrix transposeSoft() {
        return parent;
    }
}
