package Interface.Views.MainContents;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class GUISemester extends VBox {

    // Fields:
    private int semesterId;
    private Label name;

    // Constructor:
    public GUISemester(int id, String name, Label yearAndSeason) {
        super(5);
        yearAndSeason.setStyle("-fx-padding-bottom: 30px;");

        this.semesterId = id;
        this.name = new Label(name);

        this.setAlignment(Pos.CENTER);
        this.setStyle("-fx-border-color:black; -fx-cursor: hand; -fx-background-radius: 10px; -fx-border-radius: 10px; -fx-padding: 10px; -fx-background-color: lightgrey;");

        this.getChildren().addAll(this.name, yearAndSeason);
    }

    // Getters:
    public int getSemesterId() {
        return semesterId;
    }

    public String getSemesterName() {
        return this.name.getText();
    }
}
