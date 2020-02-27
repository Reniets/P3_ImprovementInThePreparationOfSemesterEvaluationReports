package Interface.Controllers.MainContents;

import Interface.Components.QuickLongTooltip;
import Interface.Views.MainContents.CommentPane;
import dbComponent.Models.QuestionModel;
import dbComponent.Models.RawDataTable.Row;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.util.Callback;

public class RawDataTableColumn<S extends Row, T extends String> extends TableColumn<Row, String> {

    // Field:
    private QuestionModel question;
    private CommentPane commentPane;

    // Constructor:
    public RawDataTableColumn(QuestionModel question, CommentPane commentPane) {
        super(question.getQuestion());
        this.commentPane = commentPane;
        //Create column header with tooltip
        Label headerLabel = new Label(question.getQuestion());
        headerLabel.setTooltip(new QuickLongTooltip(question.getQuestion()));

        this.setGraphic(headerLabel);

        //Sets the question
        this.question = question;

        //Makes sure this generates the custom TableCells
        this.setCellFactory(createCellFactory());
    }

    public QuestionModel getQuestionModel() {
        return this.question;
    }

    private Callback<TableColumn<Row, String>, TableCell<Row, String>> createCellFactory() {
        return rawDataTableColumn -> new RawDataTableCell(this);
    }

    public CommentPane getCommentPane() {
        return this.commentPane;
    }

}
