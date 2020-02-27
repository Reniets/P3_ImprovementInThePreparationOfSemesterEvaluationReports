package LinearAlgebra.Execution;

import DataStructures.Atomics.AtomicDouble;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

class ParallelTest {

    @Test
    void forIteration() {
        AtomicInteger count = new AtomicInteger();
        Parallel.forIteration(0, 1000, true, number -> {
            count.addAndGet(1);
        });
        assertEquals(1000, count.get());
    }

    @Test
    void forIteration1() {
        AtomicInteger count = new AtomicInteger();
        Parallel.forIteration(0, 1000, number -> {
            count.addAndGet(1);
        });
        assertEquals(1000, count.get());
    }

    @Test
    void forIteration2() {
        AtomicInteger count = new AtomicInteger();
        Parallel.forIteration(1000, number -> {
            count.addAndGet(1);
        });
        assertEquals(1000, count.get());
    }

    @Test
    void forIteration3() {
        AtomicInteger count = new AtomicInteger();
        Parallel.forIteration(1000, true, number -> {
            count.addAndGet(1);
        });
        assertEquals(1000, count.get());
    }
}