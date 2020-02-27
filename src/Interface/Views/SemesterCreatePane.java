package Interface.Views;

import Interface.Controllers.SemesterCreateController;
import Interface.SceneSwitcher;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;

public class SemesterCreatePane extends AnchorPane {

    // Field:
    private SemesterCreateController controller;

    // Constructor:
    public SemesterCreatePane(SceneSwitcher switcher) {

        this.controller = new SemesterCreateController();
        this.controller.setSwitcher(switcher);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("SemesterCreate.fxml"));
        loader.setRoot(this);
        loader.setController(this.controller);

        try {
            loader.load();
            this.controller.setSemesterNumbers(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14);
            this.controller.setSemesterYears(25, 1);
            this.controller.groupRadioButtons();
            //this.controller.dynamicSemesterName();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}
