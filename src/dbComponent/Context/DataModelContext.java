package dbComponent.Context;

import Exceptions.LoginFailedException;
import Interface.Components.GraphicUpdateListener;
import Interface.Controllers.LoadToggler;
import dbComponent.Models.*;
import dbComponent.Models.RawDataTable.Row;
import dbComponent.Models.RawDataTable.TableData;
import dbComponent.database.DB;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class DataModelContext {

    // Fields:
    private static DataModelContext ourInstance = new DataModelContext();

    private CoordinatorModel coordinator;
    private SemesterModel currentSemester;
    private Queue<PendingOperation> pendingOperations;
    private Thread dataSync;
    private boolean runDataSync;
    private List<LoadToggler> controlsToUpdate;
    private boolean rawDataUpdateFinished;
    private DB syncronousDatabase; //Never to be used in 'new threads'

    // Constructor:
    private DataModelContext() {
        this.runDataSync = false;
        this.pendingOperations = new LinkedList<>();
        this.controlsToUpdate = new ArrayList<>();
        this.rawDataUpdateFinished = false;

        try {
            this.syncronousDatabase = new DB();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }

    }

    //Getters;
    public DB getSyncronousDatabase() {
        return this.syncronousDatabase;
    }

    // Methods:
    public static DataModelContext getInstance() {
        return ourInstance;
    }

    public void loginCoordinator(String emailAddress, String password) throws LoginFailedException {
        List<CoordinatorModel> coordinators;
        try {
            coordinators = DB.SELECT_CoordinatorsWhereEmail(emailAddress);
            if (coordinators.size() == 0) {
                throw new LoginFailedException("Forsøget på at logge ind fejlede, da den valgte bruger ikke kunne findes. Tjek at email addresse og password er korrekt, og prøv igen.");
            } else {
                this.coordinator = coordinators.get(0);
            }
        } catch (LoginFailedException ex) {

            throw ex;

        } catch (Exception ex) {
            throw new LoginFailedException(

                    String.format("Forsøget på at logge ind fejlede af tekniske årsager.", emailAddress), ex);
        }
    }

    public void logoutCoordinator() {
        this.coordinator = null;
    }

    private void addOperation(PendingOperation operation) {
        this.pendingOperations.add(operation);
        if (!this.runDataSync) {
            this.startDataSynchronization();
        }
    }

    public void addToUpdate(ModelUpdate model) {
        this.addOperation(new PendingUpdate(model));
    }

    public void addToInsert(ModelInsert model) {
        this.addOperation(new PendingInsert(model));
    }

    public void addToRemove(ModelDelete model) {
        this.addOperation(new PendingDelete(model));
    }

    public List<SemesterModel> getSemesters() {
        return this.coordinator.getSemesters();
    }

    public CoordinatorModel getCoordinator() {
        return this.coordinator;
    }

    public SemesterModel getCurrentSemester() {
        return this.currentSemester;
    }

    public void setCurrentSemester(int id) {
        this.currentSemester = getCoordinator().getSemesters().stream().filter(semesterModel -> semesterModel.getId() == id).findFirst().get();
        this.currentSemester.loadRelatedData();
    }

    public void setCurrentSemester(int id, GraphicUpdateListener listener) {
        Thread loadSemester = new Thread(() -> {

            this.setCurrentSemester(id);
            Platform.runLater(() -> listener.call());
        });

        loadSemester.setDaemon(true);
        loadSemester.start();
    }

    public void startDataSynchronization() {
        this.runDataSync = true;

        if (this.dataSync == null || !this.dataSync.isAlive()) {
            this.dataSync = new Thread(() -> {

                DB database = null;

                try {
                    database = new DB();

                    while (this.runDataSync) {
                        if (!this.pendingOperations.isEmpty()) {

                            PendingOperation operation = this.pendingOperations.remove();

                            switch (operation.getOperationType()) {
                                case DELETE:
                                    operation.getModelDelete().DeleteModel(database);
                                    break;
                                case INSERT:
                                    operation.getModelInsert().InsertModel(database);
                                    break;
                                case UPDATE:
                                    operation.getModelUpdate().UpdateModel(database);
                                    break;
                            }
                        } else {
                            this.runDataSync = false;
                        }
                    }
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                } finally {
                    try {
                        database.close();
                    } catch (Exception sqlCloseEx) {
                        throw new RuntimeException(sqlCloseEx);
                    }
                }
            });

            this.dataSync.setDaemon(true);
        }

        if (!this.dataSync.isAlive()) {
            this.dataSync.start();
        }
    }

    public void stopDataSyncronization() {
        this.runDataSync = false;
    }

    public TableData getRawTableData() {
        TableData rawData = new TableData(this.currentSemester);

        return rawData;
    }

    public void loadRawDataInto(ObservableList<Row> rawDataList) {
        Thread rawDataLoader = new Thread(() -> {

            try {
                DB database = new DB();

                database.SELECT_answersIntoSemester(this.currentSemester, rawDataList);

                database.close();
            } catch (Exception ex) {
                Platform.runLater(() -> {
                    new Alert(Alert.AlertType.ERROR, String.format("Database Connection Failed: %s", ex.getMessage()), ButtonType.CLOSE).show();
                });

            }

            Platform.runLater(() -> {
                this.controlsToUpdate.forEach(c -> c.toggleLoading());
                this.controlsToUpdate.clear();
            });
        });

        rawDataLoader.setDaemon(true);
        rawDataLoader.start();
    }

    public void addRawDataLoadedListener(LoadToggler control) {
        if (!this.rawDataUpdateFinished) {
            control.toggleLoading();
            this.controlsToUpdate.add(control);
        }
    }
}
