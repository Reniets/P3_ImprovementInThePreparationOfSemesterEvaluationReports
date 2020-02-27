package Interface.Components;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import javafx.util.StringConverter;

public class HighlightComboBox extends ComboBox<HighlightColor> {

    // Constructors:
    public HighlightComboBox() {
        this(HighlightColor.NONE);
    }

    public HighlightComboBox(HighlightColor value) {

        if (value == null) {
            value = HighlightColor.NONE;
        }


        ObservableList<HighlightColor> colors = FXCollections.observableList(DefaultColors.getTextHighlightColors());

        this.setItems(colors);

        this.setCellFactory(highlightColorListView -> new HighlightComboBoxCell());

        this.setConverter(new StringConverter<HighlightColor>() {
            @Override
            public String toString(HighlightColor highlightColor) {
                return highlightColor.getName();
            }

            @Override
            public HighlightColor fromString(String s) {
                switch (s) {
                    case "Gul":
                        return HighlightColor.YELLOW;
                    case "Grøn":
                        return HighlightColor.GREEN;
                    case "Lyserød":
                        return HighlightColor.PINK;
                    case "Blå":
                        return HighlightColor.BLUE;
                    case "Rød":
                        return HighlightColor.RED;
                    default:
                        return HighlightColor.NONE;
                }
            }
        });

        this.valueProperty().addListener((observableValue, highlightColor, t1) -> {
            t1.setNodeBackgroundColor(this);
        });

        this.setValue(value);
    }
}
