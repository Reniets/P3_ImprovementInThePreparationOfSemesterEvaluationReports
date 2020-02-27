package Interface.Views.MainContents;

import Interface.Controllers.MainContents.SentimentController;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.sql.SQLException;

public class SentimentPane extends AnchorPane {

    // Field:
    private SentimentController controller;

    // Constant:
    private static final String FXML_NAME = "Sentiment.fxml";

    // Constructor:
    public SentimentPane() throws SQLException, ClassNotFoundException {
        this.controller = new SentimentController();

        FXMLLoader loader = new FXMLLoader(getClass().getResource(FXML_NAME));
        loader.setRoot(this);
        loader.setController(this.controller);

        try {
            loader.load();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }
}
