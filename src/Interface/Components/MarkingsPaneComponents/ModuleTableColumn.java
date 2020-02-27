package Interface.Components.MarkingsPaneComponents;

import dbComponent.Models.MarkedAnswerModel;
import javafx.scene.control.TableColumn;


public class ModuleTableColumn extends TableColumn<MarkedAnswerModel, String> {

    // Constructor:
    public ModuleTableColumn() {
        super("Modul");
        this.setCellFactory(answerModelStringTableColumn -> new ModuleTableCell());
    }

}
