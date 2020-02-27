package DataStructures;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BytesTest {

    // Test Fields:
    private List<Byte> byteList;
    private ByteBuffer byteBuffer;

    @BeforeEach
    void beforeEach() {
        this.byteList = new ArrayList<>();
        this.byteBuffer = ByteBuffer.allocate(Integer.BYTES);
    }

    // Tests:
    @Test
    void intToByteList() {
        Byte ByteData = (byte) 0;
        for (int i = 0; i < Integer.BYTES; i++) {
            byteList.add(ByteData);
        }
        assertEquals(byteList, Bytes.intToByteList(0));
    }

    @Test
    void doubleToByteList() {
        Byte ByteData = (byte) 0;
        for (int i = 0; i < Double.BYTES; i++) {
            byteList.add(ByteData);
        }
        assertEquals(byteList, Bytes.doubleToByteList(0));
    }

    @Test
    void listToByteBuffer() {
        for (int i = 0; i < Integer.BYTES; i++) {
            byteBuffer.put((byte) 0);
        }
        assertEquals(byteBuffer, Bytes.listToByteBuffer(byteList));
    }
}