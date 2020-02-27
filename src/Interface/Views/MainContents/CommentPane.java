package Interface.Views.MainContents;

import Interface.Components.GraphicUpdateListener;
import Interface.Controllers.MainContents.CommentController;
import dbComponent.Models.AnswerModel;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;

public class CommentPane extends AnchorPane {

    private CommentController controller;
    private GraphicUpdateListener showHandler;


    // Constructor:
    public CommentPane(GraphicUpdateListener backAction, GraphicUpdateListener showHandler) {
        this.controller = new CommentController();
        this.showHandler = showHandler;

        FXMLLoader loader = new FXMLLoader(getClass().getResource("Comment.fxml"));
        loader.setRoot(this);
        loader.setController(this.controller);

        try {
            loader.load();
            this.controller.setupView(backAction);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public void showComment(AnswerModel answer) {
        this.controller.setAnswer(answer);
        this.setVisible(true);
        if (this.showHandler != null) {
            this.showHandler.call();
        }
    }

    public void setInsertListener(Node source, GraphicUpdateListener listener) {
        this.controller.setMarkingInsertListener(source, listener);
    }

    public AnswerModel getAnswer() {
        return this.controller.getAnswer();
    }


}
