package Interface.Components.MarkingsPaneComponents;

import Interface.Views.MainContents.CommentPane;
import dbComponent.Models.AnswerModel;
import dbComponent.Models.MarkedAnswerModel;
import javafx.event.EventHandler;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableRow;
import javafx.scene.input.MouseEvent;

public class AnswerTableCell extends TableCell<MarkedAnswerModel, String> {

    private CommentPane commentPane;
    private EventHandler<MouseEvent> doubleOrRightClickHandler;
    private MarkingTableColumn markingColumn;

    public AnswerTableCell(CommentPane commentPane, MarkingTableColumn markingColumn) {
        this.commentPane = commentPane;
        this.addEventFilter(MouseEvent.MOUSE_CLICKED, createClickHandler());
        this.markingColumn = markingColumn;
    }

    @Override
    protected void updateItem(String s, boolean b) {
        super.updateItem(s, b);

        if (getAnswer() != null) {
            this.setText(getAnswer().getAnswer());
        } else {
            this.setText("");
        }
    }

    private MarkedAnswerModel getMarkedAnswer() {
        TableRow<MarkedAnswerModel> tr = this.getTableRow();

        if (tr == null) {
            return null;
        }

        return tr.getItem() != null ? tr.getItem() : null;
    }

    private AnswerModel getAnswer() {
        MarkedAnswerModel markedAnswer = getMarkedAnswer();

        if (markedAnswer != null) {
            return markedAnswer.getAnswer();
        }

        return null;
    }

    private EventHandler createClickHandler() {
        if (this.doubleOrRightClickHandler == null) {
            this.doubleOrRightClickHandler = new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    if (mouseEvent.getClickCount() > 1) {
                        commentPane.showComment(getAnswer());
                    }
                }
            };
        }

        return this.doubleOrRightClickHandler;
    }

}
