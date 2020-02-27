package Interface.Views.MainContents;

import Interface.Controllers.MainContents.RawdataController;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;

public class RawdataPane extends AnchorPane {

    // Field:
    private RawdataController controller;

    // Constructor:
    public RawdataPane() {
        this.controller = new RawdataController();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("Rawdata.fxml"));
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
