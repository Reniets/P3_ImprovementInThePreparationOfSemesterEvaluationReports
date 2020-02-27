package Interface.Components;

import javafx.scene.paint.Color;

public class Colors {

    // Getters:
    public static int getRGB(Color color) {
        String colorString = color.toString().substring(2);

        int rgb = (int) Long.parseLong(colorString, 16);

        return rgb;
    }

    public static Color getColor(int rgb) {

        try {
            return Color.web(Integer.toHexString(rgb));
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}
