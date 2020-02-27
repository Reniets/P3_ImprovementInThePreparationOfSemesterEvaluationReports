package Interface.Views.MainContents;

import Interface.Controllers.MainContents.MarkingsController;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;

public class MarkingsPane extends AnchorPane {
    // Field:
    private MarkingsController controller;

    // Constant:
    private static final String FXML_NAME = "Markings.fxml";

    // Constructor:
    public MarkingsPane() {
        this.controller = new MarkingsController();

        FXMLLoader loader = new FXMLLoader(getClass().getResource(FXML_NAME));
        loader.setRoot(this);
        loader.setController(this.controller);

        try {
            loader.load();
            this.controller.setupView();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}
