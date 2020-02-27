package LinearAlgebra.Types.Vectors;

public class OnehotVector extends DefaultVectorBehavior {

    // Fields:
    private int oneIndex;
    private int size;

    // Constructor:
    public OnehotVector(int oneIndex, int size) {
        this.size = size;
        this.oneIndex = oneIndex;
        if (oneIndex < 0 || oneIndex >= size)
            throw new IndexOutOfBoundsException("OneIndex can not be outside of vector!");
    }

    /*
    public DenseVector toDenseVector() {
        return new DenseVector(toArray());
    }
    */

    @Override
    public double[] toArray() {
        double[] array = new double[this.size];
        array[this.oneIndex] = 1;
        return array;
    }

    @Override
    public double dotProduct(Vector B) {
        Vector A = this;

        if (A.size() != B.size()) {
            throw new IllegalArgumentException("Vector A and B has to have the same size");
        }

        return B.getEntry(this.oneIndex);
    }


    @Override
    public Vector mult(double scalar) {
        return new VectorBuilder(this)
                .multiply(scalar)
                .build();
    }

    @Override
    public Vector div(double scalar) {
        return this.mult(1 / scalar);
    }


    @Override
    public double norm(int p) {
        if (p <= 0) {
            throw new IllegalArgumentException("A norm cant be negative nor zero.");
        }
        return 1;
    }

    @Override
    public double normMax() {
        return 1;
    }

    @Override
    public double getEntry(int index) {
        if (index < 0 || index >= size)
            throw new IndexOutOfBoundsException();

        if (index == oneIndex)
            return 1;
        else
            return 0;
    }

    @Override
    public int size() {
        return this.size;
    }

    @Override
    public boolean isNull() {
        return false; //Can never be a nullVector due to constructor implementation :)
    }


    @Override
    public Vector clone() {
        return new OnehotVector(this.oneIndex, this.size);
    }

    @Override
    public boolean isOnehot() {
        return true;
    }


    @Override
    public boolean isDense() {
        return false;
    }


    public int getOneIndex() {
        return this.oneIndex;
    }

}
