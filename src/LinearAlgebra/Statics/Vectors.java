package LinearAlgebra.Statics;

import LinearAlgebra.Types.Vectors.Vector;

import java.util.List;
import java.util.function.BiConsumer;

public class Vectors {

    // Private constructor to avoid initialization.
    private Vectors() {

    }

    // Returns a orthogonalized vector, from the base vector to the list of vectors.
    public static Vector projectOrthogonal(Vector baseVector, List<Vector> orthoVectors) {
        return projectOrthogonal(baseVector, orthoVectors, (alpha, j) -> {
        });
    }

    // Project Orthogonal with additional possibility to reuse the calculated alpha value together with the index of the current vector being used.
    public static Vector projectOrthogonal(Vector baseVector, List<Vector> orthoVectors, BiConsumer<Double, Integer> alphaHandler) {
        Vector vNew = baseVector;

        for (int j = 0; j < orthoVectors.size(); j++) {
            Vector vLast = orthoVectors.get(j);

            if (!vLast.isNull()) {
                double alpha = vNew.dotProduct(vLast) / vLast.dotProduct(vLast);

                vNew = vNew.sub(vLast.mult(alpha));

                alphaHandler.accept(alpha, j);
            }
        }

        return vNew;
    }
}
