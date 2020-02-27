package dbComponent.Models;

import Enums.SentimentPolarity;
import Interface.Components.GraphicUpdateListener;
import dbComponent.Context.DataModelContext;
import dbComponent.data.Sentiment;
import dbComponent.database.DB;
import javafx.scene.Node;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Collection;

public class SentimentModel extends Sentiment implements ModelInsert {
    private static final String INSERT_STATEMENT = "INSERT INTO Sentiment(QuestionID, Responedent, Module, SentimentPolarity) VALUES(?, ?, ?, ?)";
    private GraphicUpdateListener insertListener;

    public SentimentModel(int questionID, int respondentID, int moduleID, SentimentPolarity sentimentPolarity) {
        super(questionID, respondentID, moduleID, sentimentPolarity);
    }

    @Override
    public void InsertModel(DB database) throws SQLException {
        PreparedStatement statement = database.getConnection().prepareStatement(INSERT_STATEMENT);

        statement.setInt(1, this.getQuestionID());
        statement.setInt(2, this.getRespondentID());
        statement.setInt(3, this.getModuleID());
        statement.setString(4, this.getSentimentPolarity().name());

        statement.executeUpdate();
    }

    public static void MassInsertModel(DB database, Collection<? extends Sentiment> sentiments) throws SQLException {
        StringBuilder massInsertStatementString = new StringBuilder("INSERT INTO Sentiments(QuestionID, RespondantID, ModuleID, Sentiment) VALUES ");

        //database.getConnection().prepareStatement("INSERT INTO Sentiments(QuestionID, RespondantID, ModuleID, Sentiment) VALUES(?,?,?,?)");

        for (Sentiment sentiment : sentiments) {
            massInsertStatementString.append('(');
            massInsertStatementString.append(sentiment.getQuestionID());
            massInsertStatementString.append(',');
            massInsertStatementString.append(sentiment.getRespondentID());
            massInsertStatementString.append(',');
            massInsertStatementString.append(sentiment.getModuleID());
            massInsertStatementString.append(",\"");
            massInsertStatementString.append(sentiment.getSentimentPolarity().name());
            massInsertStatementString.append("\"),");
        }
        massInsertStatementString.deleteCharAt(massInsertStatementString.length() - 1);

        PreparedStatement statement = database.getConnection().prepareStatement(massInsertStatementString.toString());
        statement.executeUpdate();
    }

    @Override
    public void addToContext() {
        DataModelContext.getInstance().addToInsert(this);
    }

    @Override
    public void addInsertListener(Node node, GraphicUpdateListener listener) {
        this.insertListener = listener;
    }
}
