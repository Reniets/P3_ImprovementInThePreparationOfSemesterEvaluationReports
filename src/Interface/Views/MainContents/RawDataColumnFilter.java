package Interface.Views.MainContents;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.MenuButton;
import javafx.scene.control.TableColumn;

import java.util.ArrayList;
import java.util.List;

public class RawDataColumnFilter extends MenuButton {

    // Field:
    private List<RawDataColumnFilterChoice> columnChoices;

    // Constructor:
    public RawDataColumnFilter(List<TableColumn> columns) {
        this.setText("Synlige Kolonner");

        this.columnChoices = new ArrayList<>();

        for (TableColumn column : columns) {
            RawDataColumnFilterChoice choice = new RawDataColumnFilterChoice(column);
            choice.selectedProperty().setValue(true);
            choice.selectedProperty().addListener(new ChangeListener<Boolean>() {
                @Override
                public void changed(ObservableValue<? extends Boolean> observableValue, Boolean aBoolean, Boolean t1) {
                    if (t1) {
                        column.setVisible(true);
                    } else {
                        column.setVisible(false);
                    }

                }
            });
            this.columnChoices.add(choice);
        }

        this.getItems().addAll(this.columnChoices);
    }
}
