package MachineLearning.NeuralNetwork.ANN.Layers;

import LinearAlgebra.Execution.Parallel;
import LinearAlgebra.Types.Matrices.Matrix;
import LinearAlgebra.Types.Matrices.MatrixBuilder;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SoftmaxLayer implements Layer {
    // Fields:
    private Matrix netMatrix;           // The net input matrix (Before activation)
    private Matrix outMatrix;           // The output matrix (After activation)
    private Matrix netExpMatrix;        // Equals Math.exp(this.netMatrix) (If this was legal code... But still)
    private int layerSize;

    // Constructor:
    public SoftmaxLayer(int layerSize) {
        this.layerSize = layerSize;
    }

    // Package protected
    SoftmaxLayer() {
        // Only used by import layer from bytes.
    }

    @Override
    public void updateLayer(Matrix outMatrix) {
        // Set the net matrix equal to the outMatrix
        this.netMatrix = outMatrix;

        // Calculate the out probabilities
        this.outMatrix = this.softmax(this.netMatrix);
    }

    @Override
    public void updateLayer(Layer lastLayer) {
        this.updateLayer(lastLayer.getOutMatrix());
    }

    @Override
    public Matrix backpropagate(Matrix prevOutMatrix, Matrix effectMatrix) {
        Matrix bottom = this.calculateBottomMatrix();

        MatrixBuilder builder = new MatrixBuilder(this.netMatrix.getRows(), this.netMatrix.getColumns(), true);

        final int cols = this.netMatrix.getColumns();
        final int rows = this.netMatrix.getRows();
        Parallel.forIteration(rows, row -> {
            for (int from = 0; from < cols; from++) {
                double sum = 0;
                double fromEffect = effectMatrix.getEntry(row, from);

                for (int to = 0; to < cols; to++) {
                    if (from == to) {
                        continue;
                    }

                    double toEffect = effectMatrix.getEntry(row, to);

                    sum += (fromEffect - toEffect) * this.netExpMatrix.getEntry(row, to);
                }

                sum *= this.netExpMatrix.getEntry(row, from);
                sum /= bottom.getEntry(row, 0);

                builder.setEntry(row, from, sum);
            }
        });

        return builder.build();
    }

    private Matrix calculateBottomMatrix() {
        MatrixBuilder matrixBuilder = new MatrixBuilder(this.getNetMatrix().getRows(), this.getNetMatrix().getColumns(), true);

        this.getNetMatrix().compLoop((row, col, value) -> matrixBuilder.additionToEntry(row, 0, this.netExpMatrix.getEntry(row, col))); // Sum all values in exp

        matrixBuilder.compLoop((row, col, value) -> matrixBuilder.setEntry(row, col, Math.pow(value, 2))); // Square the sums

        return matrixBuilder.build();
    }

    private Matrix calculateNetExpMatrix() {
        final int rows = this.netMatrix.getRows();
        final int cols = this.netMatrix.getColumns();

        MatrixBuilder builder = new MatrixBuilder(rows, cols, true);

        Parallel.forIteration(rows, rows * cols > 100, row -> {
            for (int col = 0; col < cols; col++) {
                double value = this.netMatrix.getEntry(row, col);
                builder.setEntry(row, col, Math.exp(value));
            }
        });

        return builder.build();
    }

    private Matrix softmax(Matrix netMatrix) {
        double[] sums = new double[netMatrix.getRows()];
        this.netExpMatrix = calculateNetExpMatrix();

        // Calculate the sum of each row (e^x)
        //netMatrix.compLoop((row, col, value) -> sums[row] += Math.exp(value));
        final int cols = this.netMatrix.getColumns();
        final int rows = this.netMatrix.getRows();
        Parallel.forIteration(rows, row -> {
            for (int col = 0; col < cols; col++) {
                sums[row] += this.netExpMatrix.getEntry(row, col);
            }
        });

        // Calculate the out probabilities
        //Matrix outMatrix = MatrixBuilder.buildEmpty(netMatrix.getRows(), netMatrix.getColumns());//new DenseMatrix(netMatrix.getRows(), netMatrix.getColumns());

        MatrixBuilder outMatrix = new MatrixBuilder(netMatrix.getRows(), netMatrix.getColumns(), true);

        //netMatrix.compLoop((row, col, value) -> outMatrix.setEntry(row, col, Math.exp(value) / sums[row]));

        Parallel.forIteration(rows, row -> {
            for (int col = 0; col < cols; col++) {
                double value = this.netMatrix.getEntry(row, col);
                outMatrix.setEntry(row, col, Math.exp(value) / sums[row]);
            }
        });

        return outMatrix.build();
    }

    @Override
    public void updateWeights(double learningRate) {
        // Do nothing
    }

    @Override
    public Matrix getOutMatrix() {
        return this.outMatrix;
    }

    @Override
    public Matrix getNetMatrix() {
        return this.netMatrix;
    }

    @Override
    public int getNodesInLayer() {
        return this.layerSize;
    }

    @Override
    public void clear() {
        // Do nothing
    }

    @Override
    public List<Byte> getByteRepresentation() {
        ByteBuffer byteBuffer = ByteBuffer.allocate(2 * 4);

        byteBuffer.putInt(3);
        byteBuffer.putInt(this.getNodesInLayer());

        // Export
        ArrayList<Byte> out = new ArrayList<>();
        for (byte b : byteBuffer.array())
            out.add(b);
        return out;
    }

    @Override
    public Layer bytesToLayer(ByteBuffer byteBuffer) {
        this.layerSize = byteBuffer.getInt();
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SoftmaxLayer that = (SoftmaxLayer) o;
        return layerSize == that.layerSize;
    }

    @Override
    public int hashCode() {
        return Objects.hash(layerSize);
    }
}
