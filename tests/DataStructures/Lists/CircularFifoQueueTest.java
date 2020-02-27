package DataStructures.Lists;

import org.junit.jupiter.api.*;

import java.util.ListIterator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CircularFifoQueueTest {

    // Test Field:
    private CircularFifoQueue<Integer> circularFifoQueue;

    // Test Constant:
    private static final int LIMIT = 10;

    @BeforeEach
    void beforeEach() {
        this.circularFifoQueue = new CircularFifoQueue<>(LIMIT);
        for (int i = 0; i < LIMIT; i++) {
            this.circularFifoQueue.add(i);
        }
    }

    // Tests:
    // Tests that get returns the correct value
    @Test
    void getCorrectValue() {
        assertEquals(0, (int) this.circularFifoQueue.get(0));
    }

    @Test
    void getError() {
        assertEquals(null, circularFifoQueue.get(LIMIT));
    }

    // Tests that getFirst returns the first value specifically
    @Test
    void getFirstValue() {
        assertEquals(0, (int) this.circularFifoQueue.getFirst());
    }

    // Tests that getLast returns the last value specifically
    @Test
    void getLastValue() {
        assertEquals(LIMIT - 1, (int) this.circularFifoQueue.getLast());
    }

    // Tests that size returns the length of the list
    @Test
    void sizeTest() {
        assertEquals(LIMIT, this.circularFifoQueue.size());
    }

    // Tests that it's possible to add an element
    @Test
    void addT() {
        CircularFifoQueue<Integer> circularFifoQueueLocal = new CircularFifoQueue<>(LIMIT);
        assertEquals(0, circularFifoQueueLocal.size());
        circularFifoQueueLocal.add(1);
        assertEquals(1, circularFifoQueueLocal.size());
    }

    // Tests that the lists size stays below the limit
    @Test
    void addSizeLimit() {
        assertEquals(LIMIT, this.circularFifoQueue.size());
        circularFifoQueue.add(LIMIT);
        assertEquals(LIMIT, this.circularFifoQueue.size());
    }

    // Checks that the added element is inserted in the end
    // Checks that the first element is replaced by the second element
    @Test
    void addCorrectRemove() {
        assertEquals(0, (int) this.circularFifoQueue.get(0));
        assertEquals(1, (int) this.circularFifoQueue.get(1));
        this.circularFifoQueue.add(LIMIT);
        assertEquals(1, (int) this.circularFifoQueue.get(0));
        assertEquals(2, (int) this.circularFifoQueue.get(1));
        assertEquals(LIMIT, (int) this.circularFifoQueue.get(LIMIT - 1));
    }

    // Tests that you can remove all elements in the array using clear
    @Test
    void clearList() {
        assertEquals(LIMIT, this.circularFifoQueue.size());
        this.circularFifoQueue.clear();
        assertEquals(0, this.circularFifoQueue.size());
    }

    // Tests that the listIterator has access to the usual ListIterator Methods
    @Test
    void listIterator() {
        ListIterator listIterator = this.circularFifoQueue.listIterator(0);
        assertTrue(listIterator.hasNext());
    }
}
