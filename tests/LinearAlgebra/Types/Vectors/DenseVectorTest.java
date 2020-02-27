package LinearAlgebra.Types.Vectors;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DenseVectorTest {

    //Tests:
    @Test
    void isOneHot() {
        DenseVector vector000 = (new VectorBuilder().addEntries(0, 0, 0, 1, 0).buildDenseVector());

        DenseVector vector001 = (new VectorBuilder().addEntries(0, 1, 0, 1, 0).buildDenseVector());
        DenseVector vector002 = (new VectorBuilder().addEntries(1, 0, 0, 5, 0).buildDenseVector());
        DenseVector vector003 = (new VectorBuilder().addEntries(5, 0, 0, 2, 0).buildDenseVector());

        assertTrue(vector000.isOnehot());

        assertFalse(vector001.isOnehot());
        assertFalse(vector002.isOnehot());
        assertFalse(vector003.isOnehot());

    }

    @Test
    void isGetEntryOutOfIndex() {
        DenseVector vector000 = new VectorBuilder().addEntries(1, 2, 3).buildDenseVector();

        assertThrows(IndexOutOfBoundsException.class, () -> vector000.getEntry(-1));
        assertThrows(IndexOutOfBoundsException.class, () -> vector000.getEntry(3));

    }
}