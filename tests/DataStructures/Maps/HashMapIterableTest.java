package DataStructures.Maps;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

class HashMapIterableTest {

    // Test Field
    private HashMapIterable<String, String> map;

    @BeforeEach
    void beforeEach(){
        this.map = new HashMapIterable<>();
        this.map.put("FirstKey","FirstValue");
        this.map.put("SecondKey","SecondValue");

        // Showing that the other constructors can be initialized too
        HashMapIterable<String, String> map2 = new HashMapIterable<>(5);
        HashMapIterable<String, String> map3 = new HashMapIterable<>(5, 10);
        HashMapIterable<String, String> map4 = new HashMapIterable<>(this.map);
    }

    // Tests
    @Test
    void getFirstEntryException() {
        HashMapIterable<String, String> mapEmpty = new HashMapIterable<>();
        assertThrows(
                (NoSuchElementException.class), () -> mapEmpty.getFirstEntry()
        );
    }

    @Test
    void getFirstKey() {
        assertEquals(this.map.getFirstKey(),"FirstKey");
    }

    @Test
    void getFirstValue() {
        assertEquals(this.map.getFirstValue(),"FirstValue");
    }

    @Test
    void testIterator(){
        Iterator<Map.Entry<String, String>> it = this.map.getIterator();
        Map.Entry<String,String> firstEntry = it.next();
        Map.Entry<String,String> secondEntry = it.next();
        assertFalse(it.hasNext());

        Iterator<String> itKey = this.map.getKeyIterator();
        String firstKey = itKey.next();
        String secondKey = itKey.next();
        assertFalse(itKey.hasNext());

        Iterator<String> itVal = this.map.getValueIterator();
        String firstVal = itVal.next();
        String secondVal = itVal.next();
        assertFalse(itVal.hasNext());
    }
}