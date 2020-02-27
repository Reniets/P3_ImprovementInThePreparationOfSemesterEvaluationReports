package Interface.Controllers.MainContents;

import Interface.Components.GraphicUpdateListener;
import Interface.Components.HighlightColor;
import Interface.Components.HighlightComboBox;
import Interface.Components.QuickLongTooltip;
import dbComponent.Models.AnswerModel;
import dbComponent.Models.MarkedAnswerModel;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;

public class CommentController {

    @FXML
    private AnchorPane anpContents;
    @FXML
    private Button btnBack;
    @FXML
    private TextArea txtComment;
    @FXML
    private Label lblQuestion;
    @FXML
    private Label lblModule;


    private HighlightComboBox cbxColor;

    private GraphicUpdateListener backAction;
    private AnswerModel answer;
    private GraphicUpdateListener markingInsertListener;
    private Node insertListenerSource;

    public void setupView(GraphicUpdateListener backAction) {

        this.backAction = backAction;
        this.configureBackButton();
        this.lblQuestion.setTooltip(new QuickLongTooltip("Spørgsmål"));
        this.lblModule.setTooltip(new QuickLongTooltip("Modul"));
    }

    public void setAnswer(AnswerModel answer) {
        this.answer = answer;

        this.txtComment.setText(this.answer.getAnswer());
        this.lblModule.setText(this.answer.getModule().getName());
        this.lblQuestion.setText(this.answer.getQuestion().getQuestion());
        this.configureColorPicker();
        updateMarkingColor();
    }

    //Configures the HighLightComboBox with which colors are picked
    private void configureColorPicker() {
        this.cbxColor = new HighlightComboBox();
        AnchorPane.setTopAnchor(this.cbxColor, 18.0);
        AnchorPane.setRightAnchor(this.cbxColor, 10.0);
        this.anpContents.getChildren().add(this.cbxColor);

        this.cbxColor.setOnAction(actionEvent -> {
            HighlightColor currentColor = this.answer.getMarkedAnswer() != null ? new HighlightColor(this.answer.getMarkedAnswer().getColour()) : HighlightColor.NONE;
            HighlightColor chosenColor = this.cbxColor.getValue();

            boolean chosenIsNew = currentColor.equals(HighlightColor.NONE) && !chosenColor.equals(HighlightColor.NONE);
            boolean chosenIsChange = !currentColor.equals(HighlightColor.NONE) && !chosenColor.equals(HighlightColor.NONE);
            boolean chosenIsRemove = !currentColor.equals(HighlightColor.NONE) && chosenColor.equals(HighlightColor.NONE);

            if (chosenIsNew) {
                MarkedAnswerModel markedAnswer = new MarkedAnswerModel(this.answer, this.cbxColor.getValue().getRGB());

                if (this.markingInsertListener != null) {

                    markedAnswer.addInsertListener(this.insertListenerSource, this.markingInsertListener);

                }

                markedAnswer.addToContext();

            } else if (chosenIsRemove) {

                this.answer.getMarkedAnswer().remove();

            } else if (chosenIsChange) {

                this.answer.getMarkedAnswer().setColour(this.cbxColor.getValue().getRGB());

            }
        });

    }

    private void configureBackButton() {
        this.btnBack.setOnAction(actionEvent -> {
            this.txtComment.setText("");
            this.backAction.call();
        });
    }

    private void updateMarkingColor() {
        if (this.answer.getAnswer() != null && this.answer.getMarkedAnswer() != null) {
            HighlightColor color = new HighlightColor(this.answer.getMarkedAnswer().getColour());
            this.cbxColor.setValue(color);
        } else {
            this.cbxColor.setValue(HighlightColor.NONE);
        }
    }

    public void setMarkingInsertListener(Node source, GraphicUpdateListener markingInsertListener) {
        this.markingInsertListener = markingInsertListener;
        this.insertListenerSource = source;
    }

    public AnswerModel getAnswer() {
        return this.answer;
    }

}
