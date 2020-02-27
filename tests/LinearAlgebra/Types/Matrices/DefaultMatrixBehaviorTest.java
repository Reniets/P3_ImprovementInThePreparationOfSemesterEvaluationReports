package LinearAlgebra.Types.Matrices;

import LinearAlgebra.Types.Vectors.Vector;
import LinearAlgebra.Types.Vectors.VectorBuilder;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

class DefaultMatrixBehaviorTest {

    // Tests:
    @Test
    void setRowVectorImmutable() {
        Matrix matrix001 = new MatrixBuilder()
                .addRow(1, 2, 3, 4)
                .addRow(5, 6, 7, 8)
                .build();

        Vector vector001 = new VectorBuilder()
                .addEntries(1, 2, 3, 4)
                .build();


        Vector vector002 = new VectorBuilder()
                .addEntries(99, 99, 99, 99)
                .build();

        Matrix matrix002 = matrix001.setRowVector(vector002, 0);

        assertTrue(matrix001.getRowVector(0).equals(vector001));
        assertTrue(matrix002.getRowVector(0).equals(vector002));

        assertTrue(matrix001.getRows() == 2);
        assertTrue(matrix001.getColumns() == 4);


        assertTrue(matrix002.getRows() == 2);
        assertTrue(matrix002.getColumns() == 4);

    }

    @Test
    void setRowVector1() {
        Matrix matrix001 = new MatrixBuilder()
                .addRow(1, 2, 3, 4)
                .addRow(5, 6, 7, 8)
                .build();

        Vector vector001 = new VectorBuilder()
                .addEntries(9, 9, 9, 9)
                .build();


        Matrix matrix002 = matrix001.setRowVector(vector001, 10);

        assertTrue(matrix002.getRows() == 11);
        assertTrue(matrix002.getColumns() == 4);
    }


    @Test
    void additionOfVector() {
        Matrix matrix000 = new MatrixBuilder()
                .addRow(1, 1)
                .addRow(1, 1)
                .build();


        Vector vector000 = new VectorBuilder().addEntries(1, 2).build();
        Vector vector001 = new VectorBuilder().addEntries(7, 8, 9).build();

        assertThrows(IllegalArgumentException.class, () -> matrix000.additionOfVectorToRows(vector001));
        assertThrows(IllegalArgumentException.class, () -> matrix000.additionOfVectorToColumns(vector001));

        Matrix matrix002 = matrix000.additionOfVectorToRows(vector000);

        assertTrue(matrix002.getEntry(0, 0) == 2);
        assertTrue(matrix002.getEntry(0, 1) == 3);

        assertTrue(matrix002.getEntry(0,0) == 2);
        assertTrue(matrix002.getEntry(0,1) == 3);

        assertTrue(matrix002.getEntry(1, 0) == 2);
        assertTrue(matrix002.getEntry(1, 1) == 3);


        Matrix matrix001 = matrix000.additionOfVectorToColumns(vector000);

        assertTrue(matrix001.getEntry(0, 0) == 2);
        assertTrue(matrix001.getEntry(0, 1) == 2);

        assertTrue(matrix001.getEntry(1, 0) == 3);
        assertTrue(matrix001.getEntry(1, 1) == 3);
    }

    @Test
    void testEquals() {
        Matrix matrix000 = new MatrixBuilder()
                .addRow(1, 1)
                .addRow(1, 1)
                .build();

        Matrix matrix001 = new MatrixBuilder()
                .addRow(1, 1)
                .addRow(1, 1, 1)
                .build();

        Matrix matrix002 = new MatrixBuilder()
                .addRow(1, 1)
                .addRow(1, 2)
                .build();

        Matrix matrix003 = new MatrixBuilder()
                .addRow(1, 1)
                .addRow(1, 1)
                .build();

        assertFalse(matrix000.equals("Not even a matrix lol"));

        assertFalse(matrix000.equals(matrix001));

        assertFalse(matrix000.equals(matrix002));

        assertTrue(matrix000.equals(matrix003));
    }

    @Test
    void toArray() {
        double[][] matrixA = new MatrixBuilder()
                .addRow(1, 2, 3)
                .addRow(4, 5, 6)
                .addRow(7, 8, 9)
                .build()
                .transposeSoft()
                .toArray();

        double[][] matrixB =
                {{1, 4, 7},
                 {2, 5, 8},
                 {3, 6, 9}};

        // Iterate through the rows
        for(int i = 0; i < 3; i++) {
            assertArrayEquals(matrixA[i], matrixB[i]);
        }
    }

    @Test
    void transposeSoft() {
        Matrix matrixA = new MatrixBuilder()
                .addRow(1, 2, 3)
                .addRow(4, 5, 6)
                .addRow(7, 8, 9)
                .build();

        Matrix matrixB = new MatrixBuilder()
                .addRow(1, 4, 7)
                .addRow(2, 5, 8)
                .addRow(3, 6, 9)
                .build();

        assertEquals(matrixB, matrixA.transposeSoft());
    }

    @Test
    void exportTest() throws IOException{
        Matrix matrix = new MatrixBuilder(10,10,true)
                .build();

        Path path = Paths.get("tests/LinearAlgebra/Types/Matrices/data/testPath.txt");

        // Export the matrix
        matrix.export(path);
        assertTrue(Files.size(path) > 0);

        Files.delete(path);
    }

    @Test
    void exportTestException() {
        Matrix matrix = new MatrixBuilder(10,10,true)
                .build();

        assertThrows(
                RuntimeException.class, () -> matrix.export(Paths.get("nopath.txt"))
        );

    }
}