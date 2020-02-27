package Interface.Components;

import javafx.scene.Node;
import javafx.scene.paint.Color;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class HighlightColor {
    //Fields:
    private Color color;
    private String name;

    //Constants:
    private static final Color[] HIGHLIGHT_COLORS = {Color.GOLD, Color.LIGHTGREEN, Color.LIGHTPINK, Color.MEDIUMTURQUOISE, Color.TOMATO, Color.WHITESMOKE};
    public static final HighlightColor NONE = new HighlightColor(Color.WHITESMOKE);
    public static final HighlightColor YELLOW = new HighlightColor(Color.GOLD);
    public static final HighlightColor GREEN = new HighlightColor(Color.LIGHTGREEN);
    public static final HighlightColor PINK = new HighlightColor(Color.LIGHTPINK);
    public static final HighlightColor BLUE = new HighlightColor(Color.MEDIUMTURQUOISE);
    public static final HighlightColor RED = new HighlightColor(Color.TOMATO);


    public HighlightColor(int rgb) {
        this(Colors.getColor(rgb));
    }

    public HighlightColor(Color color) {
        if (!isHighlightColor(color)) {
            throw new RuntimeException(String.format("Color %s is not a valid highlight color", color.toString()));
        }

        this.color = color;

        if (this.getRGB() == Colors.getRGB(Color.GOLD)) {
            this.name = "Gul";
        } else if (this.getRGB() == Colors.getRGB(Color.LIGHTGREEN)) {
            this.name = "Grøn";
        } else if (this.getRGB() == Colors.getRGB(Color.LIGHTPINK)) {
            this.name = "Lyserød";
        } else if (this.getRGB() == Colors.getRGB(Color.MEDIUMTURQUOISE)) {
            this.name = "Blå";
        } else if (this.getRGB() == Colors.getRGB(Color.TOMATO)) {
            this.name = "Rød";
        } else {
            this.name = "Ingen";
        }

    }

    public String getName() {
        return this.name;
    }

    public Color getColor() {
        return this.color;
    }

    public int getRGB() {
        return Colors.getRGB(this.color);
    }

    public String getHexString() {
        return this.color.toString().substring(2);
    }

    public void setNodeBackgroundColor(Node node) {
        Pattern ptn = Pattern.compile("-fx-background-color:.*;");
        Matcher mtch = ptn.matcher(node.getStyle());
        String result = mtch.replaceAll("");

        if (!this.equals(HighlightColor.NONE)) {
            result += String.format("-fx-background-color: #%s;", getHexString());
        }

        node.setStyle(result);
    }

    public static boolean isHighlightColor(Color color) {
        List<String> colorsAsHex = Arrays.stream(HIGHLIGHT_COLORS).map(c -> c.toString()).collect(Collectors.toList());
        return colorsAsHex.contains(color.toString());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HighlightColor that = (HighlightColor) o;
        return Objects.equals(this.getRGB(), that.getRGB());
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getRGB());
    }
}
