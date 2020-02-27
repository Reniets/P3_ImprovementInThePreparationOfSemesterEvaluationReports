package Interface.Controllers.MainContents;

import Interface.Components.HighlightColor;
import Interface.Components.HighlightComboBox;
import dbComponent.Models.AnswerModel;
import dbComponent.Models.MarkedAnswerModel;
import dbComponent.Models.RawDataTable.Row;
import javafx.event.EventHandler;
import javafx.scene.control.TableCell;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

public class RawDataTableCell extends TableCell<Row, String> {

    // Fields:
    private RawDataTableColumn column;
    private EventHandler doubleOrRightClickHandler;
    private boolean colorPickingEnabled;
    private HighlightComboBox colorPicker;

    // Constructors:
    public RawDataTableCell(RawDataTableColumn column) {
        this.column = column;

        this.colorPickingEnabled = false;
        this.setWrapText(true);

    }

    // Methods:
    private EventHandler createClickHandler() {
        if (this.doubleOrRightClickHandler == null) {
            this.doubleOrRightClickHandler = (EventHandler<MouseEvent>) mouseEvent -> {
                if (mouseEvent.getClickCount() > 1) {
                    column.getCommentPane().setInsertListener(this, () -> this.updateItem(null, false));
                    column.getCommentPane().showComment(getAnswer());
                } else if (mouseEvent.getButton() == MouseButton.SECONDARY) {
                    initColorPicker();
                    activateColorPicker();
                }
            };
        }

        return this.doubleOrRightClickHandler;
    }

    private Row getRow() {
        return this.getTableRow() == null ? null : this.getTableRow().getItem();
    }

    private AnswerModel getAnswer() {
        if (getRow() != null) {
            return getRow().getAnswer(this.column.getQuestionModel());
        }
        return null;
    }

    private void initColorPicker() {
        HighlightColor color = null;

        if (getAnswer() != null) {
            MarkedAnswerModel markedAnswer = getAnswer().getMarkedAnswer();
            if (markedAnswer != null) {
                color = new HighlightColor(markedAnswer.getColour());
            }
        }

        this.colorPicker = new HighlightComboBox(color);

        this.colorPicker.setOnAction(actionEvent -> {

            setGraphic(null);
            HighlightColor pickedColor = this.colorPicker.getValue();

            if (HighlightColor.NONE.equals(pickedColor)) {
                getAnswer().getMarkedAnswer().remove();
            } else {
                MarkedAnswerModel markedAnswer = getAnswer().getMarkedAnswer();

                if (markedAnswer == null) {
                    MarkedAnswerModel newMarkedAnswer = new MarkedAnswerModel(getAnswer(), pickedColor.getRGB());
                    newMarkedAnswer.addInsertListener(this, () -> this.updateItem(null, false));
                    newMarkedAnswer.addToContext();

                } else {
                    markedAnswer.setColour(pickedColor.getRGB());
                }
            }

        });

        this.colorPicker.setOnHidden(actionEvent -> {
            setGraphic(null);
        });
    }

    @Override
    public void updateItem(String s, boolean b) {
        super.updateItem(s, b);
        this.initColorPicker();


        Row row = getRow();
        String value = row == null ? "" : row.getAnswerToQuestion(this.column.getQuestionModel());
        setText(value);
        if (value.replace(" ", "").length() > 1) {
            this.enableColorChange();
            if (getAnswer() != null && getAnswer().getMarkedAnswer() != null) {
                getAnswer().getMarkedAnswer().addUpdateListener(this, () -> this.updateColor());
                getAnswer().getMarkedAnswer().addRemoveListener(this, () -> this.updateColor());//new HighlightColor(null).setNodeBackgroundColor(this));
            }
        } else {
            this.disableColorChange();
        }

        updateColor();
    }

    public void updateColor() {
        HighlightColor savedColour = getMarkedAnswerHighlightedColour();
        if (savedColour == null) {
            HighlightColor.NONE.setNodeBackgroundColor(this);
        } else {
            savedColour.setNodeBackgroundColor(this);
        }
    }

    public HighlightColor getMarkedAnswerHighlightedColour() {
        if (getAnswer() != null && getAnswer().getMarkedAnswer() != null) {
            int rgb = getAnswer().getMarkedAnswer().getColour();
            return new HighlightColor(rgb);
        }
        return null;
    }

    private void activateColorPicker() {
        setGraphic(this.colorPicker);
        this.colorPicker.show();
    }


    private void enableColorChange() {
        if (!this.colorPickingEnabled) {
            this.addEventFilter(MouseEvent.MOUSE_CLICKED, createClickHandler());
        }
        this.colorPickingEnabled = true;
    }

    private void disableColorChange() {
        if (colorPickingEnabled) {
            this.removeEventFilter(MouseEvent.MOUSE_CLICKED, createClickHandler());
        }
        this.colorPickingEnabled = false;
    }
}
