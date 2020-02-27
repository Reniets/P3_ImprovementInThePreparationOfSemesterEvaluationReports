package LinearAlgebra.Types.Vectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class VectorBuilderTest {

    // Test Field:
    private VectorBuilder vb;
    private ArrayList<Double> list;
    private ArrayList<Double> listLong;

    @BeforeEach
    void beforeEach() {
        vb = new VectorBuilder();
        list = new ArrayList<>();
        list.add(1d);
        list.add(2d);
        list.add(3d);

        listLong = new ArrayList<>();
        listLong.add(1d);
        listLong.add(2d);
        listLong.add(3d);
        listLong.add(4d);
        listLong.add(5d);

    }

    // Tests:
    @Test
    void build() {
        Vector vector001 = new VectorBuilder().addEntries(0, 0, 0).build();

        assertTrue(vector001 instanceof NullVector);
        assertTrue(vector001.isNull());
        assertTrue(vector001.size() == 3);
        assertTrue(vector001.getEntry(0) == 0);
        assertTrue(vector001.getEntry(1) == 0);
        assertTrue(vector001.getEntry(2) == 0);


        Vector vector002 = new VectorBuilder().addEntries(0, 1, 0, 0).build();

        assertTrue(vector002 instanceof OnehotVector);
        assertFalse(vector002.isNull());
        assertTrue(vector002.isOnehot());
        assertTrue(vector002.size() == 4);
        assertTrue(vector002.getEntry(0) == 0);
        assertTrue(vector002.getEntry(1) == 1);
        assertTrue(vector002.getEntry(2) == 0);
        assertTrue(vector002.getEntry(3) == 0);

        Vector vector003 = new VectorBuilder().addEntries(1, 2, 3, 4).build();

        assertTrue(vector003 instanceof DenseVector);
        assertFalse(vector003.isNull());
        assertFalse(vector003.isOnehot());
        assertTrue(vector003.isDense());
        assertTrue(vector003.size() == 4);
        assertTrue(vector003.getEntry(0) == 1);
        assertTrue(vector003.getEntry(1) == 2);
        assertTrue(vector003.getEntry(2) == 3);
        assertTrue(vector003.getEntry(3) == 4);

        vector002 = new VectorBuilder(vector002)
                .setEntry(0, 1)
                .build();

        assertTrue(vector002 instanceof DenseVector);
        assertFalse(vector002.isNull());
        assertFalse(vector002.isOnehot());
        assertTrue(vector002.isDense());
        assertTrue(vector002.size() == 4);
        assertTrue(vector002.getEntry(0) == 1);
        assertTrue(vector002.getEntry(1) == 1);
        assertTrue(vector002.getEntry(2) == 0);
        assertTrue(vector002.getEntry(3) == 0);
    }

    @Test
    void setEntryTest() {
        vb.setEntry(100, 1);
        assertTrue(vb.build().getEntry(100) == 1);
        assertThrows(IndexOutOfBoundsException.class, () -> vb.setEntry(-1, 1));
    }

    @Test
    void arrayListAsConstructor() {
        Vector vector = new VectorBuilder(list).build();

        for (int i = 0; i < 3; i++) {
            assertTrue(list.get(i).equals(vector.getEntry(i)));
        }
    }

    @Test
    void qBuildTest() {
        Vector vector000 = VectorBuilder.qBuild(1, 2, 3);
        Vector vector001 = new VectorBuilder()
                .addEntries(1, 2, 3)
                .build();
        assertEquals(vector000, vector001);
    }

    @Test
    void listAsEntries() {
        Vector vector = new VectorBuilder().addEntries(list).build();

        for (int i = 0; i < 3; i++) {
            assertTrue(list.get(i).equals(vector.getEntry(i)));
        }
    }

    @Test
    void additiveSubtractive() {
        Vector listVector = new VectorBuilder(list).build();
        Vector longListVector = new VectorBuilder(listLong).build();
        Vector vector = vb.additive(listVector).subtractive(longListVector).build();

        assertTrue(vector.getEntry(0) == 0);
        assertTrue(vector.getEntry(1) == 0);
        assertTrue(vector.getEntry(2) == 0);
        assertTrue(vector.getEntry(3) == -4.0);
        assertTrue(vector.getEntry(4) == -5.0);
    }

}


