package LinearAlgebra.Types.Vectors;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.function.BiConsumer;

public class VectorBuilder {

    // Field:
    private ArrayList<Double> entries;

    /*********************
     *                   *
     *  Constructors
     *                   *
     *********************/

    public VectorBuilder() {
        this.entries = new ArrayList<>();
    }

    public VectorBuilder(Vector vector) {
        this.entries = new ArrayList<>();
        vector.compLoop((index, val) -> this.addEntries(val));
    }

    public VectorBuilder(double... entries) {
        this.entries = new ArrayList<>();
        for (double e : entries)
            this.entries.add(e);
    }

    public VectorBuilder(ArrayList<Double> list) {
        this.entries = new ArrayList<>(list);
    }

    public static Vector qBuild(double... entries) {
        return new VectorBuilder().addEntries(entries).build();
    }

    /*********************
     *                   *
     *  Setters          *
     *                   *
     *********************/


    public VectorBuilder setEntry(int index, double value) {
        while (entries.size() <= index)
            this.addEntries(0d);

        if (index < 0)
            throw new IndexOutOfBoundsException();

        this.entries.set(index, value);

        return this;
    }

    /*********************
     *  Adders           *
     *********************/

    // Adds a list of entries to the vector
    public VectorBuilder addEntries(double... entries) {
        for (double entry : entries)
            this.entries.add(entry);
        return this;
    }

    // Adds a list of entries to the vector
    public VectorBuilder addEntries(ArrayList<Double> entries) {
        this.entries.addAll(entries);
        return this;
    }

    /*********************
     *                   *
     *  Getters          *
     *                   *
     *********************/

    // Gets an entry in the vector with a given index
    private double getEntry(int index) {
        try {
            return this.entries.get(index);
        } catch (IndexOutOfBoundsException e) {
            return 0;
        }
    }

    /*********************
     *                   *
     *  Math             *
     *                   *
     *********************/

    // Subtracts a vector from this vector
    public VectorBuilder subtractive(Vector vector) {
        vector.compLoop((index, val) -> additiveToEntry(index, -vector.getEntry(index)));
        return this;
    }

    // Adds a vector to this vector
    public VectorBuilder additive(Vector vector) {
        vector.compLoop((index, val) -> additiveToEntry(index, vector.getEntry(index)));
        return this;
    }

    // Gets the negated version of this vector. ~~ Opposite direction
    public VectorBuilder negate() {
        return compLoop((index, val) -> setEntry(index, -this.getEntry(index)));
    }

    // Adds an amount to an entry at a given index of the vector
    public VectorBuilder additiveToEntry(int index, double amount) {
        return this.setEntry(index, this.getEntry(index) + amount);
    }

    /*********************
     *                   *
     *  Misc             *
     *                   *
     *********************/

    private int size() {
        return this.entries.size();
    }

    public VectorBuilder compLoop(BiConsumer<Integer, Double> consumer) {
        int len = this.size();
        for (int i = 0; i < len; i++) {
            consumer.accept(i, getEntry(i));
        }
        return this;
    }

    // Checks if all entries are 0
    private boolean isNullVector() {
        for (Double entry : entries)
            if (entry != 0)
                return false;
        return true;
    }

    //Returns -1 if it is NOT onehot.
    private Integer oneHotIndex() {
        boolean oneEncountered = false;
        int oneHotIndex = -1;

        for (int i = 0; i < entries.size(); i++) {
            double entry = entries.get(i);
            if (entry != 0) {
                if (entry == 1) {
                    if (oneEncountered) {
                        return -1;
                    } else {
                        oneHotIndex = i;
                        oneEncountered = true;
                    }
                } else {
                    return -1;
                }
            }
        }

        return oneHotIndex;
    }

    // Multiplies all entries with a given scalar
    public VectorBuilder multiply(double scalar) {
        return this.compLoop((i, v) -> this.setEntry(i, v * scalar));
    }

    private void roundDownRoundErrorEntry(int index) {
        if (this.getEntry(index) < 1e15) {
            this.setEntry(index, 0);
        }
    }

    /*********************
     *                   *
     *  Build            *
     *                   *
     *********************/

    /* Optimizes the vector object by doing calculations ONCE to see if it can be a "cheaper" vector type that requires less calculations in its methods.*/
    public Vector build() {
        if (isNullVector())
            return new NullVector(entries.size());

        int oneHotIndex = oneHotIndex();
        if (oneHotIndex != -1) {
            return new OnehotVector(oneHotIndex, entries.size());
        }

        return new DenseVector(entries);
    }

    // Builds the vector as a dense vector
    public DenseVector buildDenseVector() {
        return new DenseVector(entries);
    }

    public VectorBuilder roundDownRoundingErrors() {
        return this.compLoop((index, val) -> roundDownRoundErrorEntry(index));
    }


    /*********************
     *                   *
     *  Import           *
     *                   *
     *********************/


    public static Vector importVector(Path path) {
        try {
            byte[] byteArray = Files.readAllBytes(path);
            return importVector(byteArray);
        } catch (IOException e) {
            throw new RuntimeException("Could not load matrix at path " + path.toString());
        }
    }

    public static Vector importVector(byte[] bytes) {
        return importVector(ByteBuffer.wrap(bytes));
    }

    public static Vector importVector(ByteBuffer b) {
        int vectorType = b.getInt();
        switch (vectorType) {
            case 0:
                return importDenseVector(b);
            default:
                throw new RuntimeException("Unknown matrix type id of " + vectorType);
        }
    }

    private static DenseVector importDenseVector(ByteBuffer b) {
        double[] entries = new double[b.getInt()];

        int len = entries.length;
        for (int i = 0; i < len; i++) {
            entries[i] = b.getDouble();
        }

        return new DenseVector(entries);
    }


    /*********************
     *                   *
     *  Retired Methods  *
     *                   *
     *********************/




    /* Will only add entries in case the current vector isnt shortner
    public VectorBuilder additive(double ... newEntries) {
        return compLoop((index, val) -> additiveToEntry(index, newEntries[index]));
    }

    public VectorBuilder additive(ArrayList<Double> newEntries) {
        return compLoop((index, val) -> additiveToEntry(index, newEntries.get(index)));
    }

    public VectorBuilder subtractive(double ... newEntries) {
        return compLoop((index, val) -> additiveToEntry(index, -newEntries[index]));
    }

    public VectorBuilder subtractive(ArrayList<Double> newEntries) {
        return compLoop((index, val) -> additiveToEntry(index, -newEntries.get(index)));
    }
    */
            /*
    public VectorBuilder constantEntries(int length, int entry) {
        for (int i = 0; i < length; i++)
            this.addEntries(entry);
        return this;
    }
    */
                /*
    None of these are really needed, or even useful.
    public VectorBuilder setEntries(double... entries) {
        this.clear();
        return this.addEntries(entries);
    }

    public VectorBuilder setEntries(ArrayList<Double> entries) {
        this.clear();
        return this.addEntries(entries);
    }

    public VectorBuilder clear(){
        this.entries.clear();
        return this;
    }


    public VectorBuilder setEntries(Vector vector) {
        this.clear();
        vector.compLoop((index, val) -> this.addEntries(val));
        return this;
    }
    */

}
