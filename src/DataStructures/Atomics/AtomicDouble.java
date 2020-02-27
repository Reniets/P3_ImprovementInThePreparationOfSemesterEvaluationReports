package DataStructures.Atomics;

public class AtomicDouble extends Number {
    // Field
    private double val;

    // Constructors
    public AtomicDouble(double val) {
        this.val = val;
    }

    public AtomicDouble() {
        val = 0d;
    }

    // Methods
    @Override
    public int intValue() {
        return (int) val;
    }

    @Override
    public long longValue() {
        return (long) val;
    }

    @Override
    public float floatValue() {
        return (float) val;
    }

    @Override
    public double doubleValue() {
        return val;
    }

    public synchronized void add(double add) {
        val += add;
    }

    public double get() {
        return val;
    }

    public synchronized double addAndGet(double add) {
        this.add(add);
        return val;
    }

    public synchronized double getAndAdd(double add) {
        double v = val;
        this.add(add);
        return v;
    }


}
