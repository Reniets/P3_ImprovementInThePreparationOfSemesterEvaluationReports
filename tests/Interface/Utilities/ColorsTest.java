package Interface.Utilities;

import Interface.Components.Colors;
import javafx.scene.paint.Color;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ColorsTest {

    // Tests:
    @Test
    void getRGB() {
        int value = Colors.getRGB(Color.GRAY);

        System.out.println(value);

        assertEquals(-2139062017, value);
    }

    @Test
    void getColor() {
        Color color = Colors.getColor(-2139062017);

        assertEquals(Color.GRAY.toString(), color.toString());
    }
}