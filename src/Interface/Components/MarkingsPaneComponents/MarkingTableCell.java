package Interface.Components.MarkingsPaneComponents;

import Interface.Components.HighlightColor;
import Interface.Components.HighlightComboBox;
import dbComponent.Models.AnswerModel;
import dbComponent.Models.MarkedAnswerModel;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableRow;

public class MarkingTableCell extends TableCell<MarkedAnswerModel, String> {

    private HighlightComboBox color;

    public MarkingTableCell() {
        super();
    }

    public HighlightComboBox getColor() {
        return this.color;
    }

    @Override
    protected void updateItem(String s, boolean b) {
        super.updateItem(s, b);

        this.color = new HighlightComboBox();
        this.color.setOnAction(actionEvent -> {
            HighlightColor pickedColor = this.color.getValue();
            if (HighlightColor.NONE.equals(pickedColor)) {
                getMarkedAnswerModel().remove();
            } else {
                MarkedAnswerModel markedAnswer = getMarkedAnswerModel();

                if (markedAnswer == null) {
                    MarkedAnswerModel newMarkedAnswer = new MarkedAnswerModel(getAnswer(), pickedColor.getRGB());
                    newMarkedAnswer.addInsertListener(this, () -> updateColor());
                    newMarkedAnswer.addToContext();

                } else {
                    markedAnswer.setColour(pickedColor.getRGB());
                }
            }
        });

        if (getMarkedAnswerModel() != null) {
            getMarkedAnswerModel().addUpdateListener(this, () -> updateColor());
        }

        updateColor();
    }

    private void updateColor() {
        MarkedAnswerModel markedAnswer = getMarkedAnswerModel();
        if (markedAnswer != null) {
            HighlightColor highlightColor = new HighlightColor(markedAnswer.getColour());
            this.color.setValue(highlightColor);
            this.setGraphic(this.color);
        } else {
            this.setGraphic(null);
        }
    }

    private AnswerModel getAnswer() {

        MarkedAnswerModel ma = getMarkedAnswerModel();

        if (ma == null) {
            return null;
        }

        AnswerModel answerModel = ma.getAnswer();

        return answerModel;
    }

    private MarkedAnswerModel getMarkedAnswerModel() {
        TableRow<MarkedAnswerModel> tableRow = this.getTableRow();
        if (tableRow == null) {
            return null;
        }

        MarkedAnswerModel ma = tableRow.getItem();

        return ma == null ? null : ma;
    }
}
