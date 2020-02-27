package Interface.Controllers;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;

import java.util.ArrayList;
import java.util.List;

public class LoginController extends ControllerBase {
    @FXML
    private HBox hbxLoginLoader;
    @FXML
    private HBox hbxLoginDetails;
    @FXML
    private TextField userName;

    @FXML
    private PasswordField password;

    @FXML
    private Label lblLoginError;

    @FXML
    private Button btnLogin;


    public LoginController() {
        Platform.runLater(() -> this.userName.requestFocus());
    }

    public void login(Event event) {

        toggleContent();

        if (this.lblLoginError.isVisible()) {
            this.lblLoginError.setVisible(false);
        }


        Thread contextTask = new Thread(() -> {
            final List<String> t = new ArrayList<>();

            try {
                context.loginCoordinator(userName.getText(), password.getText());
            } catch (Exception ex) {
                t.add(ex.getMessage());
            }

            if (t.size() > 0) {
                Platform.runLater(() -> {
                    Alert loginWarning = new Alert(Alert.AlertType.WARNING, String.format("%s", t.get(0)), ButtonType.CLOSE);
                    loginWarning.setHeaderText("Bruger ikke fundet");
                    loginWarning.setTitle("Login Fejlede");
                    loginWarning.setResizable(true);
                    loginWarning.setOnCloseRequest(dialogEvent -> toggleContent());
                    loginWarning.show();
                });

            } else {
                Platform.runLater(() -> {
                    try {
                        toggleContent();
                        switcher.showSemesterPickerScreen();
                    } catch (Exception ex) {
                        throw new RuntimeException(ex);
                    }
                });
            }


        });

        contextTask.setDaemon(true);
        contextTask.start();


    }

    public void txtLoginEnterPressed(ActionEvent ae) {
        this.password.requestFocus();
        this.password.selectAll();
    }

    private void toggleContent() {
        if (this.hbxLoginDetails.isDisabled()) {
            //this.hbxLoginLoader.setDisable(true);
            this.hbxLoginLoader.setVisible(false);
            this.hbxLoginDetails.setDisable(false);
        } else {
            this.hbxLoginDetails.setDisable(true);
            this.hbxLoginLoader.setVisible(true);
            //this.hbxLoginLoader.setDisable(false);
        }

    }

}
