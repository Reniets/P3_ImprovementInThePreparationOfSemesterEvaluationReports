package Interface.Components.MarkingsPaneComponents;

import Interface.Views.MainContents.CommentPane;
import dbComponent.Models.MarkedAnswerModel;
import javafx.scene.control.TableColumn;


public class AnswerTableColumn extends TableColumn<MarkedAnswerModel, String> {

    // Constructor:
    public AnswerTableColumn(CommentPane commentPane, MarkingTableColumn markingColumn) {
        super("Kommentar");

        this.setCellFactory(markedAnswerModelStringTableColumn -> new AnswerTableCell(commentPane, markingColumn));
    }
}
