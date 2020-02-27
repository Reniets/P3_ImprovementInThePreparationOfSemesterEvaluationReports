package Interface.Components.MarkingsPaneComponents;

import dbComponent.Models.MarkedAnswerModel;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.TableColumn;

public class RespondantTableColumn extends TableColumn<MarkedAnswerModel, String> {

    // Constructor:
    public RespondantTableColumn() {
        super("Respondant");
        this.setCellValueFactory(markedAnswerModelStringCellDataFeatures -> {

            MarkedAnswerModel markedAnswer = markedAnswerModelStringCellDataFeatures.getValue();
            if (markedAnswer == null) {
                return new SimpleStringProperty("");
            }

            return new SimpleStringProperty(Integer.toString(markedAnswerModelStringCellDataFeatures.getValue().getRespondantID()));
        });
    }
}
