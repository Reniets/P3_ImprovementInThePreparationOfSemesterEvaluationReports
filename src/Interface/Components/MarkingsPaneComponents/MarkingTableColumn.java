package Interface.Components.MarkingsPaneComponents;

import dbComponent.Models.MarkedAnswerModel;
import javafx.scene.control.TableColumn;


public class MarkingTableColumn extends TableColumn<MarkedAnswerModel, String> {

    // Constructor:
    public MarkingTableColumn() {
        super("Markering");
        this.setCellFactory(answerModelStringTableColumn -> new MarkingTableCell());
    }
}
