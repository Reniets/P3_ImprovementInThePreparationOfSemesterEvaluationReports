package Interface.Controllers;

import Enums.SentimentPolarity;
import Exceptions.UnsupportedToggleException;
import Predictors.PredictHybrid;
import Predictors.PredictKeyword;
import Predictors.PredictionMethod;
import dbComponent.Models.SemesterModel;
import dbComponent.Models.SentimentModel;
import dbComponent.data.Answer;
import dbComponent.data.Sentiment;
import dbComponent.data.StructureDB;
import dbComponent.database.DB;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Window;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SemesterCreateController extends ControllerBase {

    @FXML
    private TextArea txtFinalName;
    @FXML
    private TextField txtSemesterDataPath;
    @FXML
    private Group grpSemesterSeason;
    @FXML
    private ComboBox<Integer> cbxSemesterYear;
    @FXML
    private ComboBox<Integer> cbxSemesterNumber;
    @FXML
    private ComboBox<String> cbxSemesterName;
    @FXML
    private Button btnSemesterDataFind;
    @FXML
    private Button btnCreateSemester;
    @FXML
    private RadioButton radAutumn;
    @FXML
    private RadioButton radSpring;
    @FXML
    private Label lblIndicator;
    @FXML
    private HBox hbxIndicator;
    @FXML
    private HBox hbxControls;

    private ToggleGroup radioGroup;

    private ObservableList semesterNumbers = FXCollections.observableArrayList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

    private File dataFile;
    private String semesterName;
    private String shortSemesterName;
    private int semesterNumber;
    private int semesterYear;
    private String season = "";

    public SemesterCreateController() {
    }


    public void findSemesterDataFile(ActionEvent event) {
        Window window = ((Node) event.getTarget()).getScene().getWindow();

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Find GUISemester Data");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("CSV Files", "*.csv")
        );

        File chosenFile = fileChooser.showOpenDialog(window);

        if (chosenFile != null && chosenFile.canRead()) {
            this.dataFile = chosenFile;
            this.txtSemesterDataPath.setText(chosenFile.getPath());
        }
    }


    @SuppressWarnings("WeakerAccess")
    public void updateSemesterName() {
        semesterName = cbxSemesterName.getValue();
        shortSemesterName = semesterName.length() > 3 ? semesterName.substring(0, 3) : semesterName;
        semesterNumber = cbxSemesterNumber.getValue();
        semesterYear = cbxSemesterYear.getValue();

        // If the selected toggle is a RadioButton, get its text.
        // Support for other Toggles can be added here if neccesary.
        if (radioGroup.getSelectedToggle() instanceof RadioButton) {
            season = ((RadioButton) radioGroup.getSelectedToggle()).getText();
        } else if (radioGroup.getSelectedToggle() != null) {
            throw new UnsupportedToggleException();
        }

        String finalName = String.format("%s%d\n%d-%s", semesterName, semesterNumber, semesterYear, season);
        txtFinalName.setText(finalName);

        // If there is a name and a season, activate the finish button, otherwise disable it
        if (!semesterName.isEmpty() && !season.isEmpty()) {
            btnCreateSemester.setDisable(false);
        } else {
            btnCreateSemester.setDisable(true);
        }
    }

    public void setSemesterNumbers(int... numbers) {
        List<Integer> numberList = new ArrayList<>();
        Arrays.stream(numbers).forEach(number -> numberList.add(number));
        this.setSemesterNumbers(numberList);
    }

    public void groupRadioButtons() {
        radioGroup = new ToggleGroup();

        this.radAutumn.setToggleGroup(radioGroup);
        this.radSpring.setToggleGroup(radioGroup);
    }

    public void setSemesterNumbers(List<Integer> numbers) {
        ObservableList<Integer> observableList = FXCollections.observableList(numbers);
        this.cbxSemesterNumber.setItems(observableList);
        this.cbxSemesterNumber.setValue(observableList.get(0));
    }

    public void setSemesterYears(int yearsPast, int yearsFuture) {
        int thisYear = LocalDate.now().getYear();
        List<Integer> years = new ArrayList<>();

        for (int i = yearsFuture; i > 0; i--) {
            years.add(thisYear + i);
        }

        years.add(thisYear);

        for (int i = 1; i <= yearsPast; i++) {
            years.add((thisYear - i));
        }
        cbxSemesterYear.setItems(FXCollections.observableList(years));
        this.cbxSemesterYear.setValue(thisYear);
    }

//    public void dynamicSemesterName() {
//        cbxSemesterName.valueProperty().addListener(new ChangeListener<String>(){
//            @Override public void changed(ObservableValue ov, String t, String t1){
//                updateSemesterName();
//            }
//        });
//
//        cbxSemesterNumber.valueProperty().addListener(new ChangeListener<Integer>() {
//            @Override
//            public void changed(ObservableValue observableValue, Integer o, Integer t1) {
//                updateSemesterName();
//            }
//        });
//
//        cbxSemesterYear.valueProperty().addListener(new ChangeListener<Integer>() {
//            @Override
//            public void changed(ObservableValue observableValue, Integer o, Integer t1) {
//                updateSemesterName();
//            }
//        });
//
//    }

    // Method of the createSemesterButton
    public void createSemesterButtonAction() throws IOException, SQLException, ClassNotFoundException {

        // Generate a SemesterModel of the given input, and insert it in the datamodelcontext for
        SemesterModel semester = new SemesterModel(0, context.getCoordinator().getId(), semesterName + semesterNumber, semesterYear, season);
        semester.addInsertListener(btnCreateSemester, () -> this.uploadRawDataForSemester(semester));
        semester.addToContext();

        this.enableIndicator("Opretter Semester");


    }

    public void disableIndicator() {
        this.hbxIndicator.setVisible(true);
        this.hbxControls.setDisable(false);
    }

    public void enableIndicator(String message) {
        this.hbxControls.setDisable(true);
        this.hbxIndicator.setVisible(true);
        this.lblIndicator.setText(message);
    }

    //
    private void uploadRawDataForSemester(SemesterModel semester) {
        Thread uploadDataThread = new Thread(() -> {

            DB database = null;

            try {
                database = new DB();
                List<Sentiment> sentiments = new ArrayList<>();
                PredictionMethod predictionMethod = new PredictKeyword(database);

                StructureDB databaseStructure = database.uploadRawData(Paths.get(txtSemesterDataPath.getText()), context.getCoordinator(), semester);

                for (Answer answer : databaseStructure.getAnswers()) {
                    SentimentPolarity sentimentPolarity;

                    // For hvert spørgsmål med et svar, skriv det til filen
                    String answerText = answer.getAnswer();
                    if (answerText.length() > 1) {
                        sentimentPolarity = predictionMethod.predictSentiment(answerText);
                        if (sentimentPolarity != null) {
                            sentiments.add(new Sentiment(answer.getQuestionID(), answer.getRespondentID(), answer.getModuleID(), sentimentPolarity));
                        }
                    }
                }

                // Insert it all with a single database call
                SentimentModel.MassInsertModel(database, sentiments);

                // Disable loading spinner
                //Platform.runLater(()->disableIndicator());

            } catch (SQLException | ClassNotFoundException | IOException e) {
                throw new RuntimeException(e);
            } finally {
                try {
                    database.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }

            }
            Platform.runLater(() -> {
                try {
                    switcher.showSemesterPickerScreen();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });

        });

        uploadDataThread.setDaemon(true);
        uploadDataThread.start();
    }
}
