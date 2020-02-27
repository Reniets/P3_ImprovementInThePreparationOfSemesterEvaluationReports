package Interface.Components;

import dbComponent.Models.Named;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;


public class NamedObjectCellFactory<T extends Named> extends CellFactory<T> {

    // Method:
    @Override
    public ListCell<T> call(ListView<T> param) {

        // call() generates the Cells for the ListView
        return new ListCell<T>() {

            // updateItem is responsible for how the cell will be displayed
            // so we override it with how we want the cell to be displayed:
            @Override
            protected void updateItem(T item, boolean empty) {

                // Do the normal Cell.updateItem() things
                super.updateItem(item, empty);

                // If the item in question exists, use its name as text instead
                if (item != null) {
                    setText(item.getName());
                }
            }
        };
    }
}
