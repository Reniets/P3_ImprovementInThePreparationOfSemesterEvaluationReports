package LinearAlgebra.Types.Matrices;

import LinearAlgebra.Types.Matrices.InternalStorageTypes.SparseDynamicMatrixStorage;
import javafx.util.Pair;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class SparseMatrix extends DefaultMatrixBehavior {
    private SparseDynamicMatrixStorage internalStorage;

    public SparseMatrix(SparseDynamicMatrixStorage internalStorage) {
        this.internalStorage = internalStorage;
    }

    public Set<Map.Entry<Pair<Integer, Integer>, Double>> getEntrySet() {
        return internalStorage.getEntrySet();
    }

    @Override
    public int getRows() {
        return this.internalStorage.getRows();
    }

    @Override
    public int getColumns() {
        return this.internalStorage.getCols();
    }

    @Override
    public double getEntry(int row, int col) {
        return this.internalStorage.getEntry(row, col);
    }

    //Exports a byte sequence containing all numbers
    @Override
    public List<Byte> getByteRepresentation() {
        // get rows and cols

        int matrixType = 2; //supposed to be different for oneohot (or potentially others)

        int rows = this.getRows();
        int cols = this.getColumns();
        int entries = this.internalStorage.getTotalEntries();

        //Add random but constant byte sequences to allow verifying the file format is correct.

        // Get byteBuffer
        ByteBuffer byteBuffer = ByteBuffer.allocate((entries * 2) * 8 + 4 * 4);

        //Brand as type
        byteBuffer.putInt(matrixType);

        // Get bytes
        byteBuffer.putInt(rows);    // First add rows
        byteBuffer.putInt(cols);    // Then add cols
        byteBuffer.putInt(entries);

        // Then add all values
        this.compLoop((row, col, value) -> {
            if (value != 0d) {
                byteBuffer.putInt(row);
                byteBuffer.putInt(col);
                byteBuffer.putDouble(value);
            }
        });

        // Export
        ArrayList<Byte> out = new ArrayList<>();
        for (byte b : byteBuffer.array())
            out.add(b);

        return out;
    }
}
