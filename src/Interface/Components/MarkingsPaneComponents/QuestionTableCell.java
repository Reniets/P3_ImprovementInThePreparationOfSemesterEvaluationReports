package Interface.Components.MarkingsPaneComponents;

import Interface.Components.QuickLongTooltip;
import dbComponent.Models.AnswerModel;
import dbComponent.Models.MarkedAnswerModel;
import dbComponent.Models.QuestionModel;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableRow;

public class QuestionTableCell extends TableCell<MarkedAnswerModel, String> {

    @Override
    public void updateIndex(int i) {
        super.updateIndex(i);
        QuestionModel question = getQuestion();

        if (question == null) {
            setGraphic(null);
        } else {
            updateContents(question);
        }
    }

    private void updateContents(QuestionModel question) {
        Label contents = new Label(Integer.toString(question.getId()));
        contents.setTooltip(new QuickLongTooltip(question.getQuestion()));

        this.setGraphic(contents);
    }

    private QuestionModel getQuestion() {
        TableRow<MarkedAnswerModel> tableRow = getTableRow();
        if (tableRow == null) {
            return null;
        }

        MarkedAnswerModel markedAnswer = tableRow.getItem();
        if (markedAnswer == null) {
            return null;
        }

        AnswerModel am = markedAnswer.getAnswer();

        return am == null ? null : am.getQuestion();
    }
}
