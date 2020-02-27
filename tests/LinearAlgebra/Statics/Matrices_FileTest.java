package LinearAlgebra.Statics;

import LinearAlgebra.Types.Matrices.DenseMatrix;
import LinearAlgebra.Types.Matrices.Matrix;
import LinearAlgebra.Types.Matrices.MatrixBuilder;
import LinearAlgebra.Types.Matrices.OnehotMatrix;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

class Matrices_FileTest {

    private Matrix matrixA;
    private Matrix matrixO;
    private Path path;

    @BeforeEach
    void beforeEach() throws IOException {
        path = Paths.get("tests/temp/files/FileTest001.matrix");

        double[][] matrixArray = new double[][]{
                {1, 2, 3},
                {4, 5, 6},
                {7, 8, 9}
        };

        matrixA = new MatrixBuilder(matrixArray, true).build();

        matrixO = new MatrixBuilder(3,3, true)
                .setRow(0,1,0,0)
                .setRow(1,0,1,0)
                .setRow(2,0,0,1)
                .build();

        Files.createDirectories(path.getParent());
    }

    // Tests:
    @Test
    void export() throws IOException {
        Matrix matrixB;

        matrixA.export(path);
        matrixB = MatrixBuilder.importMatrix(path);
        Files.deleteIfExists(path);
        assertEquals(matrixA, matrixB);
        assertTrue(matrixB instanceof DenseMatrix);

        matrixO.export(path);
        matrixB = MatrixBuilder.importMatrix(path);
        Files.deleteIfExists(path);
        assertEquals(matrixO, matrixB);
        assertTrue(matrixB instanceof OnehotMatrix);
    }
}