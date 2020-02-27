package LinearAlgebra.Types;

import LinearAlgebra.Types.Matrices.DenseMatrix;
import LinearAlgebra.Types.Matrices.Matrix;
import LinearAlgebra.Types.Matrices.MatrixBuilder;
import LinearAlgebra.Types.Matrices.OnehotMatrix;
import LinearAlgebra.Types.Vectors.DenseVector;
import LinearAlgebra.Types.Vectors.Vector;
import LinearAlgebra.Types.Vectors.VectorBuilder;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MatrixBuilderTest {

    // Tests:
    @Test
    void build() {
        Matrix matrix000 = new MatrixBuilder()
                .addRow(1, 0, 5)
                .addRow(0, 1, 0)
                .build();

        assertTrue(matrix000.getRows() == 2);
        assertTrue(matrix000.getColumns() == 3);
        assertTrue(matrix000.getEntry(0, 0) == 1);
        assertTrue(matrix000.getEntry(0, 1) == 0);
        assertTrue(matrix000.getEntry(0, 2) == 5);

        assertTrue(matrix000.getEntry(1, 0) == 0);
        assertTrue(matrix000.getEntry(1, 1) == 1);
        assertTrue(matrix000.getEntry(1, 2) == 0);

        Matrix matrix001 = new MatrixBuilder()
                .addRow(1)
                .addRow(0, 1)
                .addRow(0, 0, 1)
                .build();

        assertTrue(matrix001.getRows() == 3);
        assertTrue(matrix001.getColumns() == 3);
        assertTrue(matrix001.getEntry(0, 0) == 1);
        assertTrue(matrix001.getEntry(0, 1) == 0);
        assertTrue(matrix001.getEntry(0, 2) == 0);

        assertTrue(matrix001.getEntry(1, 0) == 0);
        assertTrue(matrix001.getEntry(1, 1) == 1);
        assertTrue(matrix001.getEntry(1, 2) == 0);

        assertTrue(matrix001.getEntry(2, 0) == 0);
        assertTrue(matrix001.getEntry(2, 1) == 0);
        assertTrue(matrix001.getEntry(2, 2) == 1);

        assertTrue(matrix001 instanceof OnehotMatrix);

        Matrix matrix002 = new MatrixBuilder(matrix001, true).build();
        assertTrue(matrix002.equals(matrix001));


        Matrix matrix003 = new MatrixBuilder(matrix001, false)
                .addColumn(3, 4, 5)
                .build();


        assertTrue(matrix003.getRows() == 3);
        assertTrue(matrix003.getColumns() == 4);

        assertTrue(matrix003.getEntry(0, 0) == 1);
        assertTrue(matrix003.getEntry(0, 1) == 0);
        assertTrue(matrix003.getEntry(0, 2) == 0);

        assertTrue(matrix003.getEntry(1, 0) == 0);
        assertTrue(matrix003.getEntry(1, 1) == 1);
        assertTrue(matrix003.getEntry(1, 2) == 0);

        assertTrue(matrix003.getEntry(2, 0) == 0);
        assertTrue(matrix003.getEntry(2, 1) == 0);
        assertTrue(matrix003.getEntry(2, 2) == 1);

        assertTrue(matrix003.getEntry(0, 3) == 3);
        assertTrue(matrix003.getEntry(1, 3) == 4);
        assertTrue(matrix003.getEntry(2, 3) == 5);

        Matrix matrix004 = new MatrixBuilder()
                .setEntry(1, 1, 9d)
                .build();

        assertTrue(matrix004.getRows() == 2);
        assertTrue(matrix004.getColumns() == 2);

        assertTrue(matrix004.getEntry(0, 0) == 0);
        assertTrue(matrix004.getEntry(0, 1) == 0);
        assertTrue(matrix004.getEntry(1, 0) == 0);
        assertTrue(matrix004.getEntry(1, 1) == 9d);


    }

    @Test
    void fillEmptyEntriesWithZero() {
        Matrix matrix001 = MatrixBuilder.buildEmpty(5, 2);

        assertTrue(matrix001.getColumns() == 2);
        assertTrue(matrix001.getRows() == 5);

    }

    @Test
    void multiplyRow() {
        Matrix matrix001 = new MatrixBuilder()
                .addRow(1)
                .addRow(0, 1)
                .addRow(0, 0, 1)
                .multiplyRow(1, 5)
                .build();

        assertTrue(matrix001.getRows() == 3);
        assertTrue(matrix001.getColumns() == 3);
        assertTrue(matrix001.getEntry(0, 0) == 1);
        assertTrue(matrix001.getEntry(0, 1) == 0);
        assertTrue(matrix001.getEntry(0, 2) == 0);

        assertTrue(matrix001.getEntry(1, 0) == 0);
        assertTrue(matrix001.getEntry(1, 1) == 5);
        assertTrue(matrix001.getEntry(1, 2) == 0);

        assertTrue(matrix001.getEntry(2, 0) == 0);
        assertTrue(matrix001.getEntry(2, 1) == 0);
        assertTrue(matrix001.getEntry(2, 2) == 1);


    }

    @Test
    void addVectorAsColumn() {
        Matrix matrix000 = new MatrixBuilder()
                .constantEntry(2, 2, 'A')
                .build();

        assertTrue(matrix000.getRows() == 2);
        assertTrue(matrix000.getColumns() == 2);
        assertTrue(matrix000.getEntry(0, 0) == 'A');
        assertTrue(matrix000.getEntry(0, 1) == 'A');
        assertTrue(matrix000.getEntry(1, 0) == 'A');
        assertTrue(matrix000.getEntry(1, 1) == 'A');


        Vector vector = new VectorBuilder(1d, 2d)
                .build();

        Matrix matrix001 = new MatrixBuilder(matrix000, true)
                .addVectorToColumns(vector)
                .build();

        assertTrue(matrix001.getRows() == 2);
        assertTrue(matrix001.getColumns() == 2);
        assertTrue(matrix001.getEntry(0, 0) == 'B');
        assertTrue(matrix001.getEntry(0, 1) == 'B');
        assertTrue(matrix001.getEntry(1, 0) == 'C');
        assertTrue(matrix001.getEntry(1, 1) == 'C');


        Matrix matrix002 = new MatrixBuilder(matrix000, true)
                .addVectorToRows(vector)
                .build();

        assertTrue(matrix002.getRows() == 2);
        assertTrue(matrix002.getColumns() == 2);
        assertTrue(matrix002.getEntry(0, 0) == 'B');
        assertTrue(matrix002.getEntry(0, 1) == 'C');
        assertTrue(matrix002.getEntry(1, 0) == 'B');
        assertTrue(matrix002.getEntry(1, 1) == 'C');

    }


    @Test
    void buildTransposed() {
        Matrix A = new MatrixBuilder()
                .addRow(1, 2, 3)
                .addRow(4, 5, 6)
                .buildTransposed();

        Matrix B = new MatrixBuilder(A, false)
                .buildTransposed();

        assertEquals(B.transpose(), A);

        /*
         * 1 2 3
         * 4 5 6
         * ->
         * 1 4
         * 2 5
         * 3 6
         * */

        assertTrue(A.getEntry(0, 0) == 1);
        assertTrue(A.getEntry(0, 1) == 4);

        assertTrue(A.getEntry(1, 0) == 2);
        assertTrue(A.getEntry(1, 1) == 5);

        assertTrue(A.getEntry(2, 0) == 3);
        assertTrue(A.getEntry(2, 1) == 6);

        assertTrue(A.getRows() == 3);
        assertTrue(A.getColumns() == 2);

        assertTrue(B.getRows() == 2);
        assertTrue(B.getColumns() == 3);
    }

    @Test
    void makeNullMatrix() {
        Matrix matrix = new MatrixBuilder().build();
        assertTrue(matrix.getColumns() == 1);
        assertTrue(matrix.getRows() == 1);
        assertTrue(matrix.getEntry(0, 0) == 0);

        //assertEquals(matrix, );

    }

    @Test
    void scalarTest() {
        Matrix matrix = new MatrixBuilder()
                .addRow(1d)
                .build();

        assertTrue(matrix.getEntry(0, 0) == 1);

        matrix = new MatrixBuilder(matrix, true)
                .setScalar(100)
                .build();

        assertTrue(matrix.getEntry(0, 0) == 100);

        matrix = new MatrixBuilder(matrix, true)
                .setScalar(0)
                .build();

        assertTrue(matrix.getEntry(0, 0) == 0);

    }

    @Test
    void addColumn() {
        Matrix matrix000 = new MatrixBuilder()
                .addColumn(1, 2, 3)
                .build();

        assertTrue(matrix000.getRows() == 3);
        assertTrue(matrix000.getColumns() == 1);
        assertTrue(matrix000.getEntry(0, 0) == 1);
        assertTrue(matrix000.getEntry(1, 0) == 2);
        assertTrue(matrix000.getEntry(2, 0) == 3);

        Vector vectorCol = new VectorBuilder()
                .addEntries(4, 5, 6)
                .build();

        Matrix matrix001 = new MatrixBuilder(matrix000, false)
                .addColumn(vectorCol)
                .buildDenseMatrix();

        assertTrue(matrix001.getRows() == 3);
        assertTrue(matrix001.getColumns() == 2);
        assertTrue(matrix001.getEntry(0, 0) == 1);
        assertTrue(matrix001.getEntry(1, 0) == 2);
        assertTrue(matrix001.getEntry(2, 0) == 3);
        assertTrue(matrix001.getEntry(0, 1) == 4);
        assertTrue(matrix001.getEntry(1, 1) == 5);
        assertTrue(matrix001.getEntry(2, 1) == 6);

        Matrix matrix002 = new MatrixBuilder(matrix001, false)
                .addColumn()
                .addColumns(2)
                .build();

        assertTrue(matrix002.getRows() == 3);
        assertTrue(matrix002.getColumns() == 5);

    }

    @Test
    void addEmptyRows() {
        Matrix matrix000 = new MatrixBuilder()
                .addEmptyRows(10)
                .build();

        assertTrue(matrix000.getRows() == 10);
        assertTrue(matrix000.getColumns() == 1);

        Matrix matrix001 = new MatrixBuilder(matrix000, false)
                .addSizedRow(10)
                .build();

        assertTrue(matrix001.getRows() == 11);
        assertTrue(matrix001.getColumns() == 10);
    }

    @Test
    void buildTransposted() {
        Matrix matrix001 = new MatrixBuilder()
                .addRow(1, 2, 3)
                .build();

        Matrix matrix002 = new MatrixBuilder()
                .addColumn(1, 2, 3)
                .buildTransposed();

        assertEquals(matrix001, matrix002);
    }

    @Test
    void buildTranspostedPow() {
        Matrix matrix001 = new MatrixBuilder()
                .addRow(1, 2, 3)
                .setScalar(4)
                .setPow(2)
                .build();

        Matrix matrix002 = new MatrixBuilder()
                .addColumn(1, 2, 3)
                .setScalar(4)
                .setPow(2)
                .buildTransposed();

        assertEquals(matrix001, matrix002);
    }

    @Test
    void buildEmptySquared() {
        Matrix matrix = MatrixBuilder.buildEmpty(5);

        assertTrue(matrix.getRows() == 5);
        assertTrue(matrix.getColumns() == 5);

    }

    @Test
    void compLoop() {
        MatrixBuilder matrix000 = new MatrixBuilder()
                .addSizedRow(10)
                .addEmptyRows(9);


        matrix000.compLoop((col, row, val) -> assertTrue(val == 0 && col < 10 && row < 10));

        matrix000.compLoop((col, row, val) -> matrix000.setEntry(col, row, col + row));

        matrix000.compLoop((col, row, val) -> assertTrue(val == col + row));

        matrix000.compLoop(5, 5, (col, row, val) -> assertTrue(val < 10));

    }

    @Test
    void addArrayListAsRow() {
        ArrayList<Double> row = new ArrayList<>();

        row.add(1d);
        row.add(2d);
        row.add(3d);

        Matrix matrix = new MatrixBuilder()
                .addRow(row)
                .build();

        assertTrue(matrix.getColumns() == 3);
        assertTrue(matrix.getRows() == 1);
        assertTrue(matrix.getEntry(0, 0) == 1);
        assertTrue(matrix.getEntry(0, 1) == 2);
        assertTrue(matrix.getEntry(0, 2) == 3);
    }

    @Test
    void cantSetNegativeRow() {
        MatrixBuilder matrixBuilder = new MatrixBuilder();

        assertThrows(IndexOutOfBoundsException.class, () -> matrixBuilder.setRow(-1, 3, 4, 5));

        assertThrows(IndexOutOfBoundsException.class, () -> matrixBuilder.setRow(-1, new VectorBuilder().addEntries(3, 4, 5).build()));
    }

    @Test
    void setRowFillMissingRows() {
        Matrix matrix = new MatrixBuilder()
                .setRow(3, 1, 2, 3)
                .build();

        assertTrue(matrix.getRows() == 4);
        assertTrue(matrix.getColumns() == 3);

        assertTrue(matrix.getEntry(3, 0) == 1);
        assertTrue(matrix.getEntry(3, 1) == 2);
        assertTrue(matrix.getEntry(3, 2) == 3);
    }

    @Test
    void zeroPowGivesIdentityMatrix() {
        MatrixBuilder mb = new MatrixBuilder()
                .addRow(1, 2, 3)
                .addRow(3, 4, 5)
                .addRow(6, 7, 8)
                .setPow(0);

        Matrix matrix000 = mb.build();

        Matrix matrix001 = mb.buildTransposed();

        matrix000.compLoop((row, col, val) -> assertTrue(val == ((row.equals(col)) ? 1 : 0)));

        assertEquals(matrix000, matrix001);

    }

    @Test
    void toStringTest() {
        MatrixBuilder matrix = new MatrixBuilder()
                .addRow(1, 2, 3)
                .addRow(3, 4, 5)
                .addRow(6, 7, 8);

        String expected = "MatrixBuilder:\n" +
                "[[1.0, 2.0, 3.0],\n" +
                " [3.0, 4.0, 5.0],\n" +
                " [6.0, 7.0, 8.0]]";

        assertEquals(matrix.toString(), expected);
    }

    @Test
    void buildConstantTest() {
        Matrix matrix = MatrixBuilder.buildConstant(10, 10, 3);
        matrix.compLoop((r, c, v) -> assertTrue(v == 3));
    }

    @Test
    void fixedMatrixBuilder() {
        MatrixBuilder mb = new MatrixBuilder(1000051, 50, true)
                .setEntry(30, 30, 99);

        Matrix matrix = mb.build();

        assertTrue(matrix.getRows() == 1000051);
        assertTrue(matrix.getColumns() == 50);
        assertTrue(matrix.getEntry(30, 30) == 99);
    }

    @Test
    void speedTest() {
        MatrixBuilder mb = new MatrixBuilder(159000, 50, true);

        for (int i = 0; i < 159000 * 50; i++)
            mb.setEntry(i / 50, i % 50, i);

        Matrix matrix = mb.build();

        assertTrue(matrix.getRows() == 159000);
        assertTrue(matrix.getColumns() == 50);
    }

    @Test
    void fixedMatrixEquals() {
        Matrix matrix = new MatrixBuilder()
                .addRow(1, 2, 3)
                .addRow(3, 4, 5)
                .addRow(6, 7, 8)
                .build();

        Matrix matrix2 = new MatrixBuilder(matrix, true).build();

        assertEquals(matrix, matrix2);
    }

    @Test
    void buildVectorList() {
        List<Vector> dense = new ArrayList<>();
        dense.add(VectorBuilder.qBuild(1));
        dense.add(VectorBuilder.qBuild(1, 2));
        dense.add(VectorBuilder.qBuild(1, 2, 3));


        List<Vector> oneHot = new ArrayList<>();
        oneHot.add(VectorBuilder.qBuild(1));
        oneHot.add(VectorBuilder.qBuild(0, 1));
        oneHot.add(VectorBuilder.qBuild(0, 0, 1));

        Matrix denseMatrix = MatrixBuilder.buildVectorList(dense);
        Matrix oneHotMatrix = MatrixBuilder.buildVectorList(oneHot);

        assertEquals(3, denseMatrix.getRows());
        assertEquals(3, denseMatrix.getRows());

        assertTrue(denseMatrix instanceof DenseMatrix);


        assertEquals(3, oneHotMatrix.getRows());
        assertEquals(3, oneHotMatrix.getRows());

        assertTrue(oneHotMatrix instanceof OnehotMatrix);
    }

    @Test
    void addRowVector() {
        Matrix matrix1 = new MatrixBuilder()
                .addRow(0, 1, 2)
                .build();

        MatrixBuilder matrix2 = new MatrixBuilder();
        Vector vector = new VectorBuilder()
                .addEntries(0, 1, 2)
                .build();

        matrix2.addRow(vector);

        assertEquals(matrix1, matrix2.build());
    }

    @Test
    void clearRow() {
        Matrix matrix1 = new MatrixBuilder()
                .addRow(1, 2, 3)
                .addRow(4, 5, 6)
                .addRow(7, 8, 9)
                .clearRow(1)
                .build();

        Matrix matrix2 = new MatrixBuilder()
                .addRow(1, 2, 3)
                .addRow(0, 0, 0)
                .addRow(7, 8, 9)
                .build();

        assertEquals(matrix1, matrix2);
    }

    @Test
    void compMultiplication() {
        Matrix matrix1 = new MatrixBuilder()
                .addRow(1, 2, 3)
                .addRow(4, 5, 6)
                .addRow(7, 8, 9)
                .compMultiplication(2)
                .build();

        Matrix matrix2 = new MatrixBuilder()
                .addRow(2, 4, 6)
                .addRow(8, 10, 12)
                .addRow(14, 16, 18)
                .build();

        assertEquals(matrix2, matrix1);
    }

    @Test
    void compDivision() {
        Matrix matrix1 = new MatrixBuilder()
                .addRow(2, 4, 6)
                .addRow(8, 10, 12)
                .addRow(14, 16, 18)
                .compDivision(2)
                .build();

        Matrix matrix2 = new MatrixBuilder()
                .addRow(1, 2, 3)
                .addRow(4, 5, 6)
                .addRow(7, 8, 9)
                .build();

        assertEquals(matrix2, matrix1);
    }

    @Test
    void importMatrix01() {
        assertThrows(
                RuntimeException.class, () -> MatrixBuilder.importMatrix(Paths.get("thisIsNoPath.txt"))
        );
    }

    @Test
    void importMatrix02() {
        byte myByte = 2;
        ByteBuffer byteBuffer = ByteBuffer.allocate(10);
        for (int i = 0; i < 10; i++) {
            byteBuffer.put(i, myByte);
        }
        assertThrows(
                RuntimeException.class, () -> MatrixBuilder.importMatrix(byteBuffer)
        );
    }

    @Test
    void buildVectorListTest() {
        List<Vector> list = new ArrayList<>();

        assertEquals(1, MatrixBuilder.buildVectorList(list).getRows());
        assertEquals(1, MatrixBuilder.buildVectorList(list).getColumns());

        assertEquals(0, MatrixBuilder.buildVectorList(list).getEntry(0, 0));
    }

    @Test
    void additionToEntries() {
        Matrix matrix1 = new MatrixBuilder()
                .addRow(1, 2, 3)
                .addRow(4, 5, 6)
                .addRow(7, 8, 9)
                .build();

        Matrix matrix2 = new MatrixBuilder()
                .additionToEntries(matrix1)
                .build();

        assertEquals(matrix1, matrix2);
    }

    @Test
    void subtractionToEntries() {
        Matrix matrix1 = new MatrixBuilder()
                .addRow(1, 2, 3)
                .addRow(4, 5, 6)
                .addRow(7, 8, 9)
                .build();

        Matrix matrix2 = new MatrixBuilder()
                .subtractionToEntries(matrix1)
                .build();

        assertNotEquals(matrix1, matrix2);
    }
}