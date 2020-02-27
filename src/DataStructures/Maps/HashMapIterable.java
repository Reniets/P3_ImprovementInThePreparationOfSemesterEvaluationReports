package DataStructures.Maps;

import java.util.*;

public class HashMapIterable<K, V> extends HashMap<K, V> {

    // Constructors
    public HashMapIterable(int initialCapacity, float loadFactor) {
        super(initialCapacity, loadFactor);
    }

    public HashMapIterable(int initialCapacity) {
        super(initialCapacity);
    }

    public HashMapIterable() {
    }

    public HashMapIterable(Map<? extends K, ? extends V> m) {
        super(m);
    }

    // Methods
    // Getters
    public Entry<K, V> getFirstEntry() {
        Optional optional = this.entrySet().stream().findFirst();
        if (optional.isPresent())
            return (Entry<K, V>) optional.get();
        else throw new NoSuchElementException("No first element of hashmap of size " + this.size());
    }

    public K getFirstKey() {
        return getFirstEntry().getKey();
    }

    public V getFirstValue() {
        return getFirstEntry().getValue();
    }

    public Iterator<Entry<K, V>> getIterator() {
        return this.entrySet().iterator();
    }

    public Iterator<K> getKeyIterator() {
        return this.keySet().iterator();
    }

    public Iterator<V> getValueIterator() {
        return this.values().iterator();
    }
}
