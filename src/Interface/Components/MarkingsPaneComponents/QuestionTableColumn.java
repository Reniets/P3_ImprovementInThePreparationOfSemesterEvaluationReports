package Interface.Components.MarkingsPaneComponents;

import dbComponent.Models.MarkedAnswerModel;
import javafx.scene.control.TableColumn;

public class QuestionTableColumn extends TableColumn<MarkedAnswerModel, String> {

    // Constructor:
    public QuestionTableColumn() {
        super("Spørgsmål");
        this.setCellFactory(answerModelStringTableColumn -> new QuestionTableCell());
    }

}
