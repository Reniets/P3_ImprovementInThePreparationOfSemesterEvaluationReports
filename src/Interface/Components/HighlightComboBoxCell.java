package Interface.Components;

import javafx.scene.control.ListCell;

public class HighlightComboBoxCell extends ListCell<HighlightColor> {

    // Method:
    @Override
    protected void updateItem(HighlightColor highlightColor, boolean b) {
        super.updateItem(highlightColor, b);

        if (highlightColor == null) {
            setStyle("");
            setText("");
        } else {
            setStyle(String.format("-fx-background-color: #%s;", highlightColor.getHexString()));
            setText(highlightColor.getName());
        }
    }
}
