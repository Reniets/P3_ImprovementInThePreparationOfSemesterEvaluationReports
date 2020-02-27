package LinearAlgebra.Types.Matrices;

import DataStructures.FunctionalInterfaces.TriConsumer;
import LinearAlgebra.Types.Vectors.Vector;

import java.nio.file.Path;
import java.util.List;


public interface Matrix extends Cloneable {
    /*************
     *           *
     * Getters   *
     *           *
     *************/

    int getRows();

    int getColumns();

    Vector getColumnVector(int column);

    Vector getRowVector(int row);

    //Vector getPartialRowVector(int row, int start, int end);

    double[][] toArray();

    double getEntry(int row, int col);

    List<Byte> getByteRepresentation();

    void export(Path path);

    /*************
     *           *
     * Setters   *
     *           *
     *************/

    // Sets a row vector, and returns this matrix.
    Matrix setRowVector(Vector v, int n);

    // Sets a row vector, and returns this matrix.
    Matrix setRowVector(double[] v, int n);

    // Sets a col vector, and returns this matrix.
    Matrix setColumnVector(Vector v, int n);

    // Sets a col vector, and returns this matrix.
    Matrix setColumnVector(double[] v, int n);


    //Removed, encourages bad treatment of matrix (One minor step -> new Matrix)
    //Matrix setEntry(int row, int col, double val);

    //Removed, encourages bad treatment of matrix (One minor step -> new Matrix)
    //Matrix additiveToEntry(int row, int col, double val);

    /****************
     *              *
     * Operations   *
     *              *
     ****************/

    // Multiplies matrix A with matrix B, and returns the result. - A and B are unaltered.
    @SuppressWarnings("Duplicates")
    Matrix mult(Matrix B);

    // Multiplies matrix A with matrix B transposed (Cord flip), and returns the result. - A and B are unaltered.
    Matrix multTrans(Matrix B);

    // Multiplies matrix A transposed (Cord flip) with matrix B transposed, and returns the result. - A and B are unaltered.
    @SuppressWarnings("Duplicates")
    Matrix transMult(Matrix B);

    // Multiplies matrix A with vector B, and returns the result. - A and B are unaltered.
    Matrix mult(Vector v);

    // Scales every entry in the matrix. - The original is unaffected.
    Matrix mult(double scaler);

    // Subtracts matrix B from matrix a, and returns the result. - A and B are unaltered.
    Matrix sub(Matrix B);

    // Add matrix B to matrix A, and returns the result. - A and B are unaltered.
    Matrix add(Matrix B);

    // Adds the values of a vector to all rows of a matrix. The original is unaltered
    Matrix additionOfVectorToRows(Vector B);

    // Adds the values of a vector to all columns of a matrix. The original is unaltered
    Matrix additionOfVectorToColumns(Vector B);

    // Returns a transposed version of the matrix. The original matrix is unaffected.
    Matrix transpose();

    // Returns a soft transposed version of the matrix. The original matrix is unaffected
    Matrix transposeSoft();

    Matrix pow(int n);

    // Component wise division - Divides every entry in A with the corresponding entry in B. - A and B are unaltered.
    Matrix compDivision(Matrix B);

    // Component wise division - Divides every entry in A with the scalar entry scalar. - A are unaltered.
    Matrix compDivision(double scalar);

    // Component wise multiplication - Multiplies every entry in A with the corresponding entry in B. - A and B are unaltered.
    Matrix compMult(Matrix B);

    /********************
     *                  *
     * Other methods    *
     *                  *
     ********************/

    // Loop though every entry of the matrix, and runs the lambda expression
    void compLoop(TriConsumer<Integer, Integer, Double> consumer);

    Matrix clone();

    //List<Byte> getByteRepresentation();

}
