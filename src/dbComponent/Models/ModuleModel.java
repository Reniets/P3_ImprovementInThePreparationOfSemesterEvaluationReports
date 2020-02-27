package dbComponent.Models;

import Interface.Components.GraphicUpdateListener;
import dbComponent.Context.DataModelContext;
import dbComponent.data.Module;
import dbComponent.database.DB;
import javafx.scene.Node;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

public class ModuleModel extends Module implements ModelInsert {
    // Fields:
    private static final String INSERT_STATEMENT = "INSERT INTO Modules(ModuleID, SemesterID, Name) VALUES(?, ?, ?)";

    private SemesterModel semester;
    private Set<AnswerModel> answers = new HashSet<>();
    private GraphicUpdateListener insertListener;

    // Constructor:
    public ModuleModel(int moduleID, int semesterID, String name) {
        super(moduleID, semesterID, name);
    }

    // Methods:
    void setSemester(SemesterModel semester) {
        this.semester = semester;
    }

    public void setAnswers(Collection<AnswerModel> answers) {
        this.answers.addAll(answers);
        answers.forEach(answer -> answer.setModule(this));
    }

    public SemesterModel getSemester() throws SQLException, ClassNotFoundException {
        if (this.semester == null) {
            DB database = new DB();
            this.semester = database.SELECT_SemesterBySemesterID(this.getSemesterID());
        }
        return this.semester;
    }

    public List<AnswerModel> getAnswers() {
        return new ArrayList<>(this.answers);
    }

    public String getDisplayName() {
        if (this.getName().length() > 28) {
            return this.getName().substring(0, 28);
        } else {
            return this.getName();
        }
    }

    @Override
    public void InsertModel(DB database) throws SQLException {
        PreparedStatement statement = database.getConnection().prepareStatement(INSERT_STATEMENT, Statement.RETURN_GENERATED_KEYS);

        statement.setInt(1, this.getId());
        statement.setInt(2, this.getSemesterID());
        statement.setString(3, this.getName());

        int rowsAffected = statement.executeUpdate();

        if (rowsAffected == 0) {
            throw new SQLException("Module insert failed, no row update");
        }

        ResultSet generatedKeys = statement.getGeneratedKeys();
        if (generatedKeys.next()) {
            this.setId(generatedKeys.getInt(1));
        } else {
            throw new SQLException("Module insert failed, no ID");
        }
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
