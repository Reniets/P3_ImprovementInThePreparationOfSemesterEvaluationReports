package DataStructures.Atomics;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

class AtomicDoubleTest {

    // Test Field:
    private AtomicDouble atomicDouble;

    @BeforeEach
    void beforeEach() {
        this.atomicDouble = new AtomicDouble(10.2);
    }

    // Tests:
    @Test
    void get() {
        assertEquals(10.2, atomicDouble.get());
    }

    @Test
    void intValue() {
        assertEquals((int) this.atomicDouble.get(), this.atomicDouble.intValue());
    }

    @Test
    void longValue() {
        assertEquals((long) this.atomicDouble.get(), this.atomicDouble.longValue());
    }

    @Test
    void floatValue() {
        assertEquals((float) this.atomicDouble.get(), this.atomicDouble.floatValue());
    }

    @Test
    void doubleValue() {
        assertEquals(this.atomicDouble.get(), this.atomicDouble.doubleValue());
    }

    @Test
    void add() {
        double number = 5;
        this.atomicDouble.add(number);
        assertEquals(15.2, this.atomicDouble.get());
    }

    @Test
    void addAndGet() {
        double number = 5;
        assertEquals(15.2, this.atomicDouble.addAndGet(number));
    }

    @Test
    void getAndAdd() {
        double number = 5;
        assertEquals(10.2, this.atomicDouble.getAndAdd(number));
        assertEquals(15.2, this.atomicDouble.get());
    }
}