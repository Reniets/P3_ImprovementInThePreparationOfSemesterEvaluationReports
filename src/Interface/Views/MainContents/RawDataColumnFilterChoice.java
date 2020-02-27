package Interface.Views.MainContents;

import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.TableColumn;

public class RawDataColumnFilterChoice extends CheckMenuItem {

    // Field:
    private TableColumn column;

    // Constructor:
    public RawDataColumnFilterChoice(TableColumn column) {
        super(column.getText());
        column.setText("");

        this.column = column;
    }

    // Getter:
    public TableColumn getColumn() {
        return column;
    }
}
