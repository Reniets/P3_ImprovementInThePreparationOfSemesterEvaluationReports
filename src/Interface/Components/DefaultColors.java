package Interface.Components;

import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

// 8 Colors picked to be the main colors used in this application
public class DefaultColors {

    // Field:
    private List<Color> defaultColors = new ArrayList<>();

    // Constructor:
    public DefaultColors() {
        this.defaultColors.add(Color.BLUE);
        this.defaultColors.add(Color.RED);
        this.defaultColors.add(Color.ORANGE);
        this.defaultColors.add(Color.MAGENTA);
        this.defaultColors.add(Color.CYAN);
        this.defaultColors.add(Color.GREEN);
        this.defaultColors.add(Color.YELLOW);
        this.defaultColors.add(Color.GRAY);
    }

    // Methods:
    public static List<HighlightColor> getTextHighlightColors() {
        List<HighlightColor> textHighlightColors = new ArrayList<>();

        textHighlightColors.add(HighlightColor.NONE);
        textHighlightColors.add(HighlightColor.YELLOW);
        textHighlightColors.add(HighlightColor.GREEN);
        textHighlightColors.add(HighlightColor.PINK);
        textHighlightColors.add(HighlightColor.BLUE);
        textHighlightColors.add(HighlightColor.RED);

        return textHighlightColors;
    }

    public static boolean isHighlightColor(Color color) {
        return true;
    }

    public List<Color> getDefaultColors() {
        return this.defaultColors;
    }

    public Color getDefaultColorByIndex(int i) {
        return this.defaultColors.get(i);
    }
}
