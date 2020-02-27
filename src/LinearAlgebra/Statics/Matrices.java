package LinearAlgebra.Statics;

import DataStructures.Atomics.AtomicDouble;
import DataStructures.FunctionalInterfaces.TriConsumer;
import LinearAlgebra.Execution.Parallel;
import LinearAlgebra.Statics.Enums.MatrixDirection;
import LinearAlgebra.Types.Matrices.InternalStorageTypes.SparseDynamicMatrixStorage;
import LinearAlgebra.Types.Matrices.Matrix;
import LinearAlgebra.Types.Matrices.MatrixBuilder;
import LinearAlgebra.Types.Matrices.OnehotMatrix;
import LinearAlgebra.Types.Matrices.SparseMatrix;
import LinearAlgebra.Types.Vectors.Vector;
import LinearAlgebra.Types.Vectors.VectorBuilder;
import javafx.util.Pair;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.BiPredicate;

public class Matrices {

    // Private constructor to avoid initialization.
    private Matrices() {

    }

    /****************************
     *                          *
     * Generate matrices        *
     *                          *
     ****************************/

    // Returns a matrix with all random values between min and max.
    private static Matrix randomMatrix(int rows, int columns, double min, double max, Random random) {
        // Check size and arguments
        if (min >= max)
            throw new IllegalArgumentException("The maximum value has to be greater than the minimum value.");

        // Create matrix array
        MatrixBuilder mb = new MatrixBuilder(rows, columns, true);

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < columns; col++) {
                mb.setEntry(row, col, random.nextDouble() * (max - min) + min);
            }
        }

        return mb.build();
    }

    public static Matrix constantEntry(int rows, int columns, double constant) {
        //return new DenseMatrix(rows, columns, constant);
        return new MatrixBuilder(rows, columns, true)
                .constantEntry(rows, columns, constant)
                .build();
    }

    // Returns a matrix with all random values between min and max. Using seed
    public static Matrix randomMatrix(int rows, int columns, double min, double max, int seed) {
        return randomMatrix(rows, columns, min, max, new Random(seed));
    }

    // Returns a matrix with all random values between min and max. Using no seed
    public static Matrix randomMatrix(int rows, int columns, double min, double max) {
        return randomMatrix(rows, columns, min, max, new Random());
    }

    // Returns a matrix with all random values between 0 and 1.
    public static Matrix randomMatrix(int rows, int columns) {
        return randomMatrix(rows, columns, 0, 1, new Random());
    }

    // Returns a matrix with all random values between 0 and 1.
    public static Matrix randomMatrix(int rows, int columns, int seed) {
        return randomMatrix(rows, columns, 0, 1, new Random(seed));
    }

    // Returns an identity matrix of the desired size
    public static Matrix getIdentityMatrix(int n) {
        MatrixBuilder mb = new MatrixBuilder(n, n, true);

        for (int i = 0; i < n; i++)
            mb.setEntry(i, i, 1);

        return mb.build();
    }

    /***************
     *             *
     * Misc        *
     *             *
     ***************/

    // Returns a simplified Matrix where every value in the matrix is simplified to X decimal points. - The original matrix is unaltered.
    public static Matrix simplify(Matrix A, int places) {
        int rowCount = A.getRows();
        int columnCount = A.getColumns();

        MatrixBuilder out = new MatrixBuilder(rowCount, columnCount, true);

        for (int row = 0; row < rowCount; row++) {
            double[] rowArray = new double[columnCount];
            for (int col = 0; col < columnCount; col++) {
                rowArray[col] = round(A.getEntry(row, col), places);
            }
            out.setRow(row, rowArray);
        }

        return out.build();
    }

    // Default is simplify to 2 places.
    public static Matrix simplify(Matrix A) {
        return simplify(A, 2);
    }

    // Private method used to round a value to a specific amount of decimals
    private static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();
        if (!Double.isFinite(value)) return value;

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    // Returns the summed value of a whole matrix, and all it's values.
    public static double sumMatrix(Matrix A) {

        AtomicDouble atomicDouble = new AtomicDouble();

        final int rows = A.getRows();
        Parallel.forIteration(rows, row -> {
            float rowSum = 0;
            for (int col = 0; col < A.getColumns(); col++) {
                rowSum += A.getEntry(row, col);
            }
            atomicDouble.add(rowSum);
        });
        return atomicDouble.get();
    }

    /* Concatenates a matrix by treating each row as a vector.
     * The 2 Matrices A and B:
     * A A   B B
     * A A & B B
     * Becomes the following matrix:
     * A A B B
     * A A B B
     */
    @SuppressWarnings("Duplicates")
    public static Matrix concatRows(Matrix A, Matrix B) {
        if (A.getRows() != B.getRows())
            throw new IllegalArgumentException("Arguments A[" + A.getRows() + "][" + A.getColumns() + "] and B[" + B.getRows() + "][" + B.getColumns() + "] have different row counts");

        final int colCountA = A.getColumns();
        final int colCountB = B.getColumns();

        final int cols = A.getColumns() + B.getColumns();
        final int rows = A.getRows();
        final MatrixBuilder matrixBuilder = new MatrixBuilder(rows, cols, true);

        /*
        A.compLoop(matrixBuilder::setEntry);
        B.compLoop((row, col, val) -> matrixBuilder.setEntry(row, col + colCountA, val));
        */

        //A
        Parallel.forIteration(rows, (r) -> {
            for (int c = 0; c < colCountA; c++) {
                matrixBuilder.setEntry(r, c, A.getEntry(r, c));
            }
        });

        //B
        Parallel.forIteration(rows, (r) -> {
            for (int c = 0; c < colCountB; c++) {
                matrixBuilder.setEntry(r, colCountA + c, B.getEntry(r, c));
            }
        });

        return matrixBuilder.build();
    }

    /* Concatenates a matrix by treating each column as a vector.
     * The 2 Matrixes A and B:
     * A A   B B
     * A A & B B
     * Becomes the following matrix:
     * A A
     * A A
     * B B
     * B B
     */
    @SuppressWarnings("Duplicates")
    public static Matrix concatColumns(Matrix A, Matrix B) {
        if (A.getColumns() != B.getColumns())
            throw new IllegalArgumentException("Arguments A and B have different column counts");


        final int rowCountA = A.getRows();
        final int rowCountB = B.getRows();

        final int cols = A.getColumns();
        final int rows = A.getRows() + B.getRows();
        final MatrixBuilder matrixBuilder = new MatrixBuilder(rows, cols, true);


        //A
        Parallel.forIteration(rowCountA, (r) -> {
            for (int c = 0; c < cols; c++) {
                matrixBuilder.setEntry(r, c, A.getEntry(r, c));
            }
        });

        //B
        Parallel.forIteration(rowCountB, (r) -> {
            for (int c = 0; c < cols; c++) {
                matrixBuilder.setEntry(rowCountA + r, c, B.getEntry(r, c));
            }
        });

        return matrixBuilder.build();
    }

    // Returns a "row matrix" with all the row vectors summed into a single value.
    public static Matrix sumRows(Matrix A) {
        final int colCountA = A.getColumns();
        final int rowCountA = A.getRows();

        final MatrixBuilder out = new MatrixBuilder(rowCountA, 1, true);

        //for (int row = 0; row < rowCountA; row++) {
        Parallel.forIteration(rowCountA, (row) -> {
            double sum = 0;
            for (int col = 0; col < colCountA; col++) {
                sum += A.getEntry(row, col);
            }

            out.setEntry(row, 0, sum);
        });

        return out.build();
    }

    // Returns a "column matrix" with all the column vectors summed into a single value.
    public static Matrix sumColumns(Matrix A) {
        final int colCountA = A.getColumns();
        final int rowCountA = A.getRows();

        final MatrixBuilder out = new MatrixBuilder(1, colCountA, true);

        //for (int col = 0; col < colCountA; col++) {
        Parallel.forIteration(colCountA, (col) -> {
            double sum = 0;
            for (int row = 0; row < rowCountA; row++) {
                sum += A.getEntry(row, col);
            }
            out.setEntry(0, col, sum);
        });

        return out.build();
    }

    // Returns true if a matrix is a upper or lower triangular matrix - Otherwise false.
    public static boolean isTriangular(Matrix A, MatrixDirection direction) {
        // This if statement is here incase, more directions will be added to the matrix direction in the future.
        if (direction != MatrixDirection.UPPER && direction != MatrixDirection.LOWER) {
            throw new IllegalArgumentException("The matrix can only be upper or lower triangular.");
        }

        BiPredicate<Integer, Integer> upper = (col, row) -> col > row;
        BiPredicate<Integer, Integer> lower = (col, row) -> col < row;

        BiPredicate<Integer, Integer> predicate = direction == MatrixDirection.UPPER ? upper : lower;

        boolean isTriangular = true;

        for (int col = 0; col < A.getColumns() && isTriangular; col++) {
            for (int row = 0; row < A.getRows() && isTriangular; row++) {
                if (predicate.test(col, row)) {
                    isTriangular = (A.getEntry(col, row) == 0);
                }
            }
        }

        return isTriangular;
    }

    // Checks if a matrix is in row echelon form
    public static boolean isEchelonForm(Matrix A) {
        for (int col = 0, row = 0; col < A.getColumns() && row < A.getRows(); col++) {
            // Check that all values beneath the current "pivot point" is zero.
            for (int i = row + 1; i < A.getRows(); i++) {
                if (A.getEntry(i, col) != 0) {
                    return false;
                }
            }

            // If it was an actual pivot point, then go one step down.
            if (A.getEntry(row, col) != 0) {
                row++;
            }
        }

        return true;
    }

    // Swaps two rows in a matrix, the original >IS (not) altered<
    public static Matrix swapRows(Matrix A, int row1, int row2) {
        return new MatrixBuilder(A, true)
                .swapRows(row1, row2)
                .build();
    }

    // Swaps two columns in a matrix, the original >IS (not) altered<
    public static Matrix swapColumns(Matrix A, int col1, int col2) {
        return new MatrixBuilder(A, true)
                .swapColumns(col1, col2)
                .build();
    }

    /**************************
     *                        *
     * Transformations        *
     *                        *
     **************************/

    // Orthonormalising a matrix A and returns it - The original Matrix is unaltered.
    public static Matrix gramSchmidt(Matrix A, BiConsumer<Double, Integer> normHandler, TriConsumer<Double, Integer, Integer> alphaHandler) {
        MatrixBuilder orthoMatrixBuilder = new MatrixBuilder(A.getRows(), A.getColumns(), true);

        List<Vector> orthoVectors = new ArrayList<>(A.getColumns());

        for (int colIndex = 0; colIndex < A.getColumns(); colIndex++) {
            final int finalColIndex = colIndex; // Required, otherwise the lambda expression wont compile.

            //Orthogonalize each vector before adding it to the list of orthoVectors
            Vector orthoVector = Vectors.projectOrthogonal(
                    A.getColumnVector(colIndex),
                    orthoVectors,
                    (alpha, j) -> alphaHandler.accept(alpha, j, finalColIndex)
            );

            orthoVectors.add(orthoVector);

            orthoMatrixBuilder.setColumn(colIndex, orthoVector);
        }

        Matrix orthoMatrix = orthoMatrixBuilder.build();
        MatrixBuilder outMatrixBuilder = new MatrixBuilder(A.getRows(), A.getColumns(), true);

        for (int i = 0; i < A.getColumns(); i++) {
            Vector columnVector = orthoMatrix.getColumnVector(i);

            if (!columnVector.isNull()) {
                double norm = columnVector.norm(2);

                outMatrixBuilder.setColumn(i, columnVector.div(norm));

                normHandler.accept(norm, i);
            }
        }

        return outMatrixBuilder.build();
    }

    // Orthonormalising a matrix A and returns it - The original Matrix is unaltered.
    public static Matrix gramSchmidt(Matrix A) {
        return gramSchmidt(
                A,
                (norm, i) -> {
                },
                (alpha, j, i) -> {
                }
        );
    }

    // Performs a row gauss elimination, and return a matrix in echelon form. The original matrix A is unaltered.
    public static Matrix gaussElimination(Matrix A) {

        Matrix echelon = A;

        // Gauss elimination
        for (int row = 0, col = 0; !Matrices.isEchelonForm(echelon); col++) {

            // Find a row vector which has a non zero value in the current column.
            int nonZero = gaussFindRowWithNonZeroValue(echelon, row, col);

            if (nonZero == -1) {
                continue; // Continue to next col
            } else {
                echelon = Matrices.swapRows(echelon, row, nonZero);
            }

            // Perform basic operations to create echelon form.
            double current = echelon.getEntry(row, col);

            for (int tempRow = row + 1; tempRow < echelon.getRows(); tempRow++) {
                // Find the scalar between the current row value, and the next.
                double next = echelon.getEntry(tempRow, col);

                double scalar = next / current;

                // Calculate the updated vector
                VectorBuilder updated = new VectorBuilder(echelon.getRowVector(tempRow).sub(echelon.getRowVector(row).mult(scalar)));

                // Check if any value in the current row is very close to zero, and if it is. Set it to zero.
                // Used to avoid java rounding errors.

                updated.roundDownRoundingErrors();

                // Set the updated vector
                echelon = new MatrixBuilder(echelon, true)
                        .setRow(tempRow, updated.build())
                        .build();
            }

            // Update the row counter, since pivot point was found
            row++;
        }

        return echelon;
    }

    // Performs a row gauss-jordan elimination, and return a matrix in reduced-echelon form. The original matrix A is unaltered.
    public static Matrix gaussJordanElimination(Matrix A) {
        // Perform normal gauss elimination
        Matrix echelon = Matrices.gaussElimination(A);

        int startRow = echelon.getRows() - 1;
        int startCol = echelon.getColumns() - 1;

        // Find the start row and col pos.
        while (echelon.getEntry(startRow, startCol) == 0) {
            startRow--;

            if (startRow == -1) {
                startCol--;
                startRow = echelon.getRows() - 1;
            }

            // If matrix A only contains 0
            if (startCol < 0) return echelon;
        }

        // Calculate the reduced form.
        for (int row = startRow; row >= 0; row--) {
            // Calculate the column of the pivot point.
            int pivotCol = Matrices.gaussfindPivot(echelon, row);

            // Get the value of the pivot point
            double pivotValue = echelon.getEntry(row, pivotCol); //echelon.toArray()[row][pivotCol];

            // Multiply the current row vector with the inverse of the current value, to obtain a value of 1 at the pivot point
            echelon.setRowVector(echelon.getRowVector(row).mult(1 / pivotValue), row);

            // For each row vector above the pivot point, subtract the current row vector x times from the once above, so all values
            // above the pivot point is zero.
            for (int tempRow = 0; tempRow < row; tempRow++) {
                // Get the value of the entries above the pivot point
                double valueAbovePivot = echelon.getEntry(tempRow, pivotCol);//echelon.toArray()[tempRow][pivotCol];

                // Calculated the updated row vectors
                Vector updatedVector = echelon.getRowVector(tempRow).sub(echelon.getRowVector(row).mult(valueAbovePivot));

                // Set the updated vectors.
                echelon.setRowVector(updatedVector, tempRow);
            }
        }

        return echelon;
    }

    private static int gaussfindPivot(Matrix echelon, int row) {
        int desiredCol = 0;

        for (int col = 0; col < echelon.getColumns(); col++) {
            if (echelon.getEntry(row, col) != 0) {
                desiredCol = col;
            }
        }

        return desiredCol;
    }

    private static int gaussFindRowWithNonZeroValue(Matrix echelon, int row, int col) {
        int tempRow = row; // Find the row in which a non zero value can be found.
        while (tempRow < echelon.getRows() && echelon.getEntry(tempRow, col) == 0) tempRow++;


        // If no such vector exist return -1
        if (tempRow == echelon.getRows()) {
            return -1;
        } else {
            // Else, return the pos of that vector
            return tempRow;
        }
    }

    /******************
     *                *
     * Getters        *
     *                *
     ******************/

    // Returns a new square matrix, where only the diagonal matrix of the given matrix is preserved.
    public static Matrix getDiagonalMatrix(Matrix A) {
        int minSize = A.getRows() > A.getColumns() ? A.getColumns() : A.getRows();

        MatrixBuilder diagonal = new MatrixBuilder(minSize, minSize, true);

        for (int i = 0; i < minSize; i++) {
            diagonal.setEntry(i, i, A.getEntry(i, i));
        }

        return diagonal.build();
    }

    // Returns a new vector with the diagional values of a matrix
    public static Vector getDiagonalVector(Matrix A) {
        int size = A.getColumns();
        Matrix diagonalA = getDiagonalMatrix(A);

        VectorBuilder vectorBuilder = new VectorBuilder();

        for (int i = 0; i < size; i++) {
            vectorBuilder.addEntries(diagonalA.getEntry(i, i));
        }

        return vectorBuilder.build();
    }

    public static Matrix getPartialMatrix(Matrix A, int rowStart, int colStart, int rowEnd, int colEnd) {
        return getPartialMatrix(A, rowStart, colStart, rowEnd, colEnd, true);
    }

    public static Matrix getPartialMatrix(Matrix A, int rowStart, int colStart, int rowEnd, int colEnd, boolean bounderiesEnacted) {
        if (rowStart > rowEnd || colStart > colEnd) {
            throw new IllegalArgumentException("The end values has to be larger than the start values");
        }

        if (rowStart < 0 || colStart < 0) {
            throw new IllegalArgumentException("Must not be outside the matrix bounds.");
        }

        if (bounderiesEnacted && (rowEnd > A.getRows() || colEnd > A.getColumns())) {
            throw new IllegalArgumentException("Must not be outside the matrix bounds.");
        }

        if (A instanceof OnehotMatrix && colEnd == A.getColumns() && colStart == 0) {
            OnehotMatrix oA = (OnehotMatrix) A;

            int[] oneIndexes = new int[rowEnd - rowStart];

            int i, r;
            for (r = rowStart, i = 0; r < rowEnd; r++, i++) {
                oneIndexes[i] = oA.getOneIndex(r);
            }

            return new OnehotMatrix(A.getColumns(), oneIndexes);

        } else if (A instanceof SparseMatrix && colEnd == A.getColumns() && colStart == 0) {
            SparseMatrix oA = (SparseMatrix) A;
            SparseDynamicMatrixStorage storage = new SparseDynamicMatrixStorage();

            Set<Map.Entry<Pair<Integer, Integer>, Double>> entries = oA.getEntrySet();

            for (Map.Entry<Pair<Integer, Integer>, Double> entry : entries) {
                Pair<Integer, Integer> pair = entry.getKey();
                Double value = entry.getValue();
                int row = pair.getKey();
                int col = pair.getValue();

                if (rowStart <= row && row < rowEnd) {
                    storage.setEntry(row, col, value);
                }
            }

            return new SparseMatrix(storage);

        } else {

            int width = rowEnd - rowStart;
            int height = colEnd - colStart;

            int rStart = rowStart;
            int cStart = colStart;
            int rEnd = Math.min(A.getRows(), rowEnd);
            int cEnd = Math.min(A.getColumns(), colEnd);

            MatrixBuilder partialMatrix = new MatrixBuilder(width, height, true);

            for (int r = rStart; r < rEnd; r++) {
                for (int c = cStart; c < cEnd; c++) {
                    double value = A.getEntry(r, c);

                    partialMatrix.setEntry(r - rowStart, c - colStart, value);
                }
            }

            return partialMatrix.build();
        }
    }
}
