package dbComponent.Models;

import Interface.Components.GraphicUpdate;
import Interface.Components.GraphicUpdateListener;
import dbComponent.Context.DataModelContext;
import dbComponent.data.MarkedAnswer;
import dbComponent.database.DB;
import javafx.application.Platform;
import javafx.scene.Node;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MarkedAnswerModel extends MarkedAnswer implements ModelInsert, ModelDelete, ModelUpdate {

    // Fields:
    private List<GraphicUpdate> insertListeners = new ArrayList<>();
    private List<GraphicUpdate> updateListeners = new ArrayList<>();
    private List<GraphicUpdate> removeListeners = new ArrayList<>();
    private AnswerModel answer;

    // Constants:
    private static final String INSERT_STATEMENT = "INSERT INTO MarkedAnswers(QuestionID, RespondantID, ModuleID, Colour) VALUES(?, ?, ?, ?)";
    private static final String UPDATE_STATEMENT = "UPDATE MarkedAnswers SET Colour = ? WHERE QuestionID = ? AND RespondantID = ? AND ModuleID = ?";
    private static final String DELETE_STATEMENT = "DELETE FROM MarkedAnswers WHERE QuestionID = ? AND RespondantID = ? AND ModuleID = ?";

    // Constructors:
    public MarkedAnswerModel(AnswerModel answer, int colour) {
        super(answer.getQuestionID(), answer.getRespondentID(), answer.getModuleID(), colour);
        this.answer = answer;
    }

    public MarkedAnswerModel(int QuestionID, int RespondantID, int ModuleID, int Colour) {
        super(QuestionID, RespondantID, ModuleID, Colour);
    }

    // Methods:
    @Override
    public void addToContext() {
        DataModelContext.getInstance().addToInsert(this);
    }

    @Override
    public PreparedStatement getUpdateStatement(Connection connection) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_STATEMENT);

        preparedStatement.setInt(1, this.getColour());
        preparedStatement.setInt(2, this.getQuestionID());
        preparedStatement.setInt(3, this.getRespondantID());
        preparedStatement.setInt(4, this.getModuleID());

        return preparedStatement;
    }

    @Override
    public PreparedStatement getDeleteStatement(Connection connection) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(DELETE_STATEMENT);

        preparedStatement.setInt(1, this.getQuestionID());
        preparedStatement.setInt(2, this.getRespondantID());
        preparedStatement.setInt(3, this.getModuleID());

        return preparedStatement;
    }

    @Override
    public void InsertModel(DB database) throws SQLException {
        PreparedStatement statement = database.getConnection().prepareStatement(INSERT_STATEMENT);

        statement.setInt(1, this.getQuestionID());
        statement.setInt(2, this.getRespondantID());
        statement.setInt(3, this.getModuleID());
        statement.setInt(4, this.getColour());

        statement.executeUpdate();

        if (this.answer == null) {
            Platform.runLater(() -> DataModelContext.getInstance().getCurrentSemester().attachMarkedAnswer(this));

        } else {
            this.answer.setMarkedAnswer(this);
        }

        Platform.runLater(() -> this.insertListeners.forEach(graphicUpdate -> graphicUpdate.call()));

    }

    @Override
    public void UpdateModel(DB database) throws SQLException {
        database.UPDATE_Model(this);
        Platform.runLater(() -> this.updateListeners.forEach(ul -> ul.call()));

    }

    @Override
    public void DeleteModel(DB database) throws SQLException {
        database.DELETE_Model(this);
        Platform.runLater(() -> this.removeListeners.forEach(ul -> ul.call()));
    }

    @Override
    public void remove() {
        this.answer.setMarkedAnswer(null);
        DataModelContext.getInstance().addToRemove(this);
    }

    @Override
    public void setColour(int colour) {
        super.setColour(colour);
        DataModelContext.getInstance().addToUpdate(this);
    }

    @Override
    public void addInsertListener(Node source, GraphicUpdateListener listener) {
        GraphicUpdate update = new GraphicUpdate(source, listener);
        this.insertListeners.remove(update);
        this.insertListeners.add(update);
    }

    @Override
    public void addUpdateListener(Node source, GraphicUpdateListener listener) {
        GraphicUpdate update = new GraphicUpdate(source, listener);
        this.updateListeners.remove(update);
        this.updateListeners.add(update);
    }

    @Override
    public void addRemoveListener(Node source, GraphicUpdateListener listener) {
        GraphicUpdate update = new GraphicUpdate(source, listener);
        this.removeListeners.remove(update);
        this.removeListeners.add(update);
    }

    public AnswerModel getAnswer() {
        return this.answer;
    }

    public void setAnswer(AnswerModel answer) {
        this.answer = answer;
    }
}
