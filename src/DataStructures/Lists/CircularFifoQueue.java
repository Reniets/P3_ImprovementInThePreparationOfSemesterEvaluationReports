package DataStructures.Lists;

import java.util.AbstractSequentialList;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

public class CircularFifoQueue<T> extends AbstractSequentialList<T> {

    // Fields:
    private List<T> list;
    private int limit;

    // Constructor:
    public CircularFifoQueue(int limit) {
        this.list = new LinkedList<>();
        this.limit = limit;
    }

    // Methods:
    @Override
    public boolean add(T t) {
        this.list.add(t);

        while (this.size() > this.limit) {
            this.list.remove(0);
        }

        return true;
    }

    @Override
    public T get(int index) {
        try {
            return this.list.get(index);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public void clear() {
        this.list.clear();
    }

    @Override
    public ListIterator<T> listIterator(int index) {
        return this.list.listIterator(index);
    }

    @Override
    public int size() {
        return this.list.size();
    }

    public T getFirst() {
        return this.get(0);
    }

    public T getLast() {
        return this.get(this.size() - 1);
    }
}
