package LinearAlgebra.Types.Vectors;

import LinearAlgebra.Types.Matrices.Matrix;

import java.nio.file.Path;
import java.util.List;
import java.util.function.BiConsumer;

public interface Vector extends Cloneable {

    @Deprecated
    double[] toArray();

    double dotProduct(Vector B);

    Matrix mult(Matrix B);

    Vector mult(double scalar);

    Vector div(double scalar);

    Vector sub(Vector B);

    Vector add(Vector B);

    double norm(int p);

    double normMax();

    double getEntry(int index);

    /* NOTE: This will return a NEW vector IF IT CHANGES TYPE. So when used, always re-declare the reference. Pwease ÓwÒ */
    //Vector setValue(int index, int value);

    int size();

    boolean isNull();

    boolean isDense();

    boolean isOnehot();

    Matrix toRowMatrix();

    Matrix toColumnMatrix();

    Vector clone();

    void compLoop(BiConsumer<Integer, Double> consumer);

    double cosineSimilarity(Vector b);

    int getMaxValuePos();

    int getMinValuePos();

    double getMaxValue();

    double getMinValue();

    List<Byte> getByteRepresentation();

    void export(Path path);

}
