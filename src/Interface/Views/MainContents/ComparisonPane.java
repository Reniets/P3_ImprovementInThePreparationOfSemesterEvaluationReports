package Interface.Views.MainContents;

import Interface.Controllers.MainContents.ComparisonController;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class ComparisonPane extends AnchorPane {
    // Field:
    private ComparisonController controller;

    // Constructor:
    public ComparisonPane() {
        controller = new ComparisonController();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("Comparison.fxml"));
        loader.setRoot(this);
        loader.setController(this.controller);

        try {
            loader.load();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }
}
