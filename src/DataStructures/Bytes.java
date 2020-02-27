package DataStructures;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

public class Bytes {
    private Bytes() {

    }

    public static List<Byte> intToByteList(int value) {
        List<Byte> byteList = new ArrayList<>(4);
        ByteBuffer byteBuffer = ByteBuffer.wrap(new byte[4]).putInt(value);

        for (Byte byt : byteBuffer.array()) {
            byteList.add(byt);
        }

        return byteList;
    }

    public static List<Byte> doubleToByteList(double value) {
        List<Byte> byteList = new ArrayList<>(8);
        ByteBuffer byteBuffer = ByteBuffer.wrap(new byte[8]).putDouble(value);

        for (Byte byt : byteBuffer.array()) {
            byteList.add(byt);
        }

        return byteList;
    }

    public static ByteBuffer listToByteBuffer(List<Byte> bytes) {
        ByteBuffer byteBuffer = ByteBuffer.allocate(bytes.size());
        for (Byte byt : bytes) {
            byteBuffer.put(byt);
        }

        byteBuffer.position(0);
        return byteBuffer;
    }
}
