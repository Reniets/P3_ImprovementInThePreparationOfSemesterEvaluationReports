package LinearAlgebra.Types.Vectors;

import LinearAlgebra.Execution.Parallel;
import LinearAlgebra.Types.Matrices.Matrix;
import LinearAlgebra.Types.Matrices.MatrixBuilder;

import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiConsumer;
import java.util.function.BiPredicate;

public abstract class DefaultVectorBehavior implements Vector {

    @Override
    public Matrix toRowMatrix() {
        return new MatrixBuilder(1, this.size(), true)
                .setRow(0, this)
                .build();
    }

    @Override
    public Matrix toColumnMatrix() {
        return new MatrixBuilder(this.size(), 1, true)
                .setColumn(0, this)
                .build();
    }

    @Override
    public Matrix mult(Matrix B) {
        return this.toRowMatrix().mult(B);
    }

    @Override
    public Vector mult(double scalar) {
        Vector A = this;

        double[] newVector = new double[this.size()];

        Parallel.forIteration(this.size(), this.size() > 100, (col) -> {
            newVector[col] = A.getEntry(col) * scalar;
        });

        return new DenseVector(newVector);
    }

    @Override
    public Vector div(double scalar) {
        double newScalar = 1 / scalar;

        return this.mult(newScalar);
    }

    @Override
    public Vector sub(Vector B) {
        //Vector A = this;
        if (this.size() != B.size()) {
            throw new IllegalArgumentException("Vector A and B has to have the same size");
        }

        return new VectorBuilder(this)
                .subtractive(B)
                .build();
    }

    @Override
    public Vector add(Vector B) {
        if (this.size() != B.size()) {
            throw new IllegalArgumentException("Vector A and B has to have the same size");
        }

        return new VectorBuilder(this)
                .additive(B)
                .build();
    }

    @Override
    public double norm(int p) {
        if (p <= 0) {
            throw new IllegalArgumentException("A norm cant be negative nor zero.");
        }

        double sumValue = 0;

        for (int i = 0; i < this.size(); i++) {
            sumValue += Math.pow(getEntry(i), p);
        }

        return Math.pow(sumValue, 1 / (double) p);
    }

    @Override
    public double normMax() {
        double maxValue = Double.MIN_VALUE;

        for (int i = 0; i < this.size(); i++) {
            maxValue = Math.max(maxValue, getEntry(i));
        }

        return maxValue;
    }


    @Override
    public double dotProduct(Vector B) {
        Vector A = this;

        if (A.size() != B.size()) {
            throw new IllegalArgumentException("Vector A and B has to have the same size");
        }

        double dotProduct = 0;

        for (int i = 0; i < A.size(); i++) {
            dotProduct += A.getEntry(i) * B.getEntry(i);
        }

        return dotProduct;
    }

    @Override
    public int getMaxValuePos() {
        return this.findPosOfType((value, currentValue) -> value > currentValue);
    }

    @Override
    public int getMinValuePos() {
        return this.findPosOfType((value, currentValue) -> value < currentValue);
    }

    @Override
    public double getMaxValue() {
        return this.findValueOfType((value, currentValue) -> value > currentValue);
    }

    @Override
    public double getMinValue() {
        return this.findValueOfType((value, currentValue) -> value < currentValue);
    }

    private int findPosOfType(BiPredicate<Double, Double> predicate) {
        final int limit = this.size();

        int pos = 0;
        double currentValue = Double.MIN_VALUE;

        for (int i = 0; i < limit; i++) {
            double value = this.getEntry(i);

            if (predicate.test(value, currentValue)) {
                currentValue = value;
                pos = i;
            }
        }

        return pos;
    }

    private double findValueOfType(BiPredicate<Double, Double> predicate) {
        final int limit = this.size();

        double currentValue = Double.MAX_VALUE;

        for (int i = 0; i < limit; i++) {
            double value = this.getEntry(i);

            if (predicate.test(value, currentValue)) {
                currentValue = value;
            }
        }

        return currentValue;
    }

    @Override
    public Vector clone() {
        VectorBuilder vb = new VectorBuilder();

        this.compLoop((i, v) -> vb.addEntries(v));

        return vb.build();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder()
                .append(this.getClass().getSimpleName())
                .append(":[");

        int len = this.size();
        for (int i = 0; i < len; i++) {
            sb.append(this.getEntry(i));
            if (i + 1 < len) sb.append(", ");
        }


        return sb.append(']').toString();
    }

    @Override
    public void compLoop(BiConsumer<Integer, Double> consumer) {
        int len = this.size();
        for (int i = 0; i < len; i++) {
            consumer.accept(i, getEntry(i));
        }
    }

    @Override
    public double cosineSimilarity(Vector b) {
        if (!this.isNull() && !b.isNull()) {
            return this.dotProduct(b) / (this.norm(2) * b.norm(2));
        } else {
            throw new IllegalArgumentException("Cosine similarity can only be found with two non-zero vectors");
        }
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Vector) {
            Vector that = (Vector) o;

            int size = this.size();

            if (size != that.size())
                return false;

            for (int i = 0; i < size; i++) {
                if (this.getEntry(i) != that.getEntry(i))
                    return false;
            }
            return true;
        }
        return false;
    }


    @Override
    public int hashCode() {
        AtomicInteger i = new AtomicInteger(0);

        this.compLoop((index, val) -> i.addAndGet(Objects.hash(index, val)));

        return Objects.hash(this.size(), i.get());
    }

    @SuppressWarnings("Duplicates")
    @Override
    public void export(Path path) {
        List<Byte> bytes = this.getByteRepresentation();

        byte[] b = new byte[bytes.size()];
        for (int i = 0; i < b.length; i++)
            b[i] = bytes.get(i);

        try {
            Files.createDirectories(path.getParent());
            Files.write(path, b);
        } catch (Exception e) {
            throw new RuntimeException("Unable to write file at " + path);
        }
    }

    @Override
    public List<Byte> getByteRepresentation() {
        // get rows and cols

        int vectorType = 0; //supposed to be different for oneohot (or potentially others)

        int entries = this.size();

        //Add random but constant byte sequences to allow verifying the file format is correct.

        // Get byteBuffer
        ByteBuffer byteBuffer = ByteBuffer.allocate(entries * 8 + 8);

        //Brand as type
        byteBuffer.putInt(vectorType);

        // Get bytes
        byteBuffer.putInt(entries);

        // Then add all values
        this.compLoop((row, value) -> byteBuffer.putDouble(value));

        // Export
        ArrayList<Byte> out = new ArrayList<>();
        for (byte b : byteBuffer.array())
            out.add(b);

        return out;
    }

}
