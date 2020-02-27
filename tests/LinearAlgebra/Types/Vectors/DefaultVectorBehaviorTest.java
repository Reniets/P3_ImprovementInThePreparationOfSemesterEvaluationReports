package LinearAlgebra.Types.Vectors;

import LinearAlgebra.Types.Matrices.Matrix;
import LinearAlgebra.Types.Matrices.MatrixBuilder;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DefaultVectorBehaviorTest {

    // Tests:
    @Test
    void add() {
        Vector vector000 = new VectorBuilder()
                .addEntries(1, 2, 3, 4, 5, 6, 7, 8, 9)
                .build();

        Vector vector001 = new VectorBuilder()
                .addEntries(1, 2, 3, 4, 5, 6, 7, 8, 9)
                .build();

        Vector vector002 = vector000.add(vector001);

        for (int i = 0; i < 9; i++) {
            assertTrue(vector002.getEntry(i) == (i + 1) * 2);
        }

        Vector vector003 = new VectorBuilder()
                .addEntries(1, 2, 3, 4, 5, 6, 7, 8)
                .build();

        assertThrows(IllegalArgumentException.class, () -> vector003.add(vector000));
    }

    @Test
    void cloneTest() {
        Vector vector000 = new VectorBuilder()
                .addEntries(1, 2, 3, 4, 5, 6, 7, 8, 9)
                .build();

        Vector vector001 = vector000.clone();

        assertTrue(vector000.equals(vector001));
        assertFalse(vector000 == vector001);
    }

    // Test that two vectors equal to each other has a cosine similarity of 1
    @Test
    void cosineSimilarity01() {
        Vector a = new VectorBuilder()
                .addEntries(1,1)
                .build();

        Vector b = new VectorBuilder()
                .addEntries(1,1)
                .build();

        assertEquals(1.0, a.cosineSimilarity(b), 10E-5);
    }

    // Test that null vectors are not allowed
    @Test
    void cosineSimilarity02() {
        Vector a = new VectorBuilder()
                .addEntries(0,0)
                .build();

        Vector b = new VectorBuilder()
                .addEntries(1,1)
                .build();

        assertThrows(IllegalArgumentException.class, () -> a.cosineSimilarity(b));
    }

    // Test that the cosine similarity is not affected by the length but the angle, hence cosine
    @Test
    void cosineSimilarity03() {
        Vector a = new VectorBuilder()
                .addEntries(10,0)
                .build();

        Vector b = new VectorBuilder()
                .addEntries(1,0)
                .build();

        assertEquals(1, a.cosineSimilarity(b), 10E-5);
    }

    // Vectors pointing in opposite directions should have a cosine similarity of -1, no similarity
    @Test
    void cosineSimilarity04() {
        Vector a = new VectorBuilder()
                .addEntries(1,0)
                .build();

        Vector b = new VectorBuilder()
                .addEntries(-1,0)
                .build();

        assertEquals(-1, a.cosineSimilarity(b), 10E-5);
    }
}