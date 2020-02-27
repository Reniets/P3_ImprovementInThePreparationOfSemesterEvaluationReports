package dbComponent.database;

import CSVHandler.CSV_Reader;
import DataStructures.Bytes;
import Enums.SentimentPolarity;
import LinearAlgebra.Types.Matrices.Matrix;
import LinearAlgebra.Types.Vectors.Vector;
import LinearAlgebra.Types.Vectors.VectorBuilder;
import MachineLearning.NeuralNetwork.ANN.ANN;
import dbComponent.Models.*;
import dbComponent.Models.RawDataTable.Row;
import dbComponent.data.Module;
import dbComponent.data.*;
import dbComponent.enums.InsertNewTypes;
import dbComponent.enums.NetworkType;
import dbComponent.enums.SemesterSeason;

import java.io.IOException;
import java.nio.file.Path;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DB {

    // Fields:
    private Connection connection;
    private Map<InsertNewTypes, PreparedStatement> statements = new HashMap<>();

    // Constants:
    private static final String host = "192.168.1.10";
    private static final String username = "p3User";
    private static final String password = "p3bordtennis123";
    private static final String database = "p3_db";

    // Constructors:
    public DB() throws ClassNotFoundException, SQLException {
        this.connection = newConnection();
    }

    public DB(int seconds) throws ClassNotFoundException, SQLException {
        this.connection = newConnection(seconds);
    }

    public Connection getConnection() {
        return connection;
    }

    //To be able to call for data from static methods
    public static Connection newConnection() throws ClassNotFoundException, SQLException {
        return newConnection(0);
    }

    //use 0 for standard
    private static Connection newConnection(int seconds) throws ClassNotFoundException, SQLException {
        // Load driver
        Class.forName("com.mysql.cj.jdbc.Driver");

        // Create SQL connection
        String dbURL = "jdbc:mysql://" + host + "/" + database + "?serverTimezone=CET&useSSL=false";

        if (seconds > 0) {
            DriverManager.setLoginTimeout(seconds);
        }

        return DriverManager.getConnection(dbURL, username, password);
    }

    // Functions
    public void close() throws SQLException {
        for (PreparedStatement statement : this.statements.values()) {
            statement.close();
        }

        this.connection.close();
    }

    public StructureDB uploadRawData(Path filePath, Coordinator coordinator, Semester semester) throws IOException, SQLException {
        StructureDB structureDB = new StructureDB();

        structureDB.setCoordinator(coordinator);
        int coordinatorID = structureDB.getCoordinator().getId();
        structureDB.setSemester(semester);
        int semesterID = structureDB.getSemester().getId();

        // Get file contents
        List<List<String>> dataFileLines = this.getLinesOfFile(filePath.toString());

        // Question and module id list
        List<Integer> questionIDs = this.loadQuestions(structureDB.getQuestions(), dataFileLines.get(0), semesterID);
        Map<String, Integer> moduleIDs = new HashMap<>();


        for (int i = 1; i < dataFileLines.size(); i++) {
            List<String> readLine = dataFileLines.get(i);

            int respondantID = Integer.parseInt(readLine.get(0));
            String moduleName = readLine.get(1);

            if (!moduleIDs.containsKey(moduleName)) {
                Module newModule = this.INSERT_newModule(semesterID, moduleName);   // Insert new module and return it
                structureDB.getModules().add(newModule);                                 // Add it to the system model
                moduleIDs.put(moduleName, newModule.getId());                       // Store it's id for later use
            }

            int moduleID = moduleIDs.get(moduleName);

            // Load answers
            for (int j = 2; j < readLine.size(); j++) {
                Answer newAnswer = this.INSERT_newAnswer(questionIDs.get(j), respondantID, moduleID, readLine.get(j));
                structureDB.getAnswers().add(newAnswer);
            }
        }

        // Return
        return structureDB;
    }

    private List<List<String>> getLinesOfFile(String path) throws IOException {
        List<List<String>> dataFileLines = new ArrayList<>();

        // Create CSVR
        CSV_Reader CSVR = new CSV_Reader(path);

        // Do stuff
        for (List<String> readLine : CSVR) {
            dataFileLines.add(readLine);
        }

        // Close CSVR
        CSVR.close();

        return dataFileLines;
    }

    private List<Integer> loadQuestions(List<Question> questionList, List<String> questions, int semesterID) throws SQLException {
        List<Integer> questionIDs = new ArrayList<>();

        // For each question insert it into the DB, and store it in system model.
        for (String question : questions) {
            Question newQuestion = this.INSERT_newQuestions(semesterID, question);  // Insert the question in the DB
            questionList.add(newQuestion);                                          // Add the question to system model
            questionIDs.add(newQuestion.getId());                                   // Store the question id for later use
        }

        return questionIDs;
    }

    public Module INSERT_newModule(int semesterID, String name) throws SQLException {
        if (!this.statements.containsKey(InsertNewTypes.MODULE)) {

            String sql = generateINSERT("Modules", "SemesterID", "Name");
            PreparedStatement newStatement = this.connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            this.statements.put(InsertNewTypes.MODULE, newStatement);
        }

        PreparedStatement statement = this.statements.get(InsertNewTypes.MODULE);

        statement.setInt(1, semesterID);
        statement.setString(2, name);

        statement.executeUpdate();
        int id = this.getLatestID(statement);

        return new Module(id, semesterID, name);
    }

    public Answer INSERT_newAnswer(int questionID, int respondantID, int moduleID, String answer) throws SQLException {
        if (!this.statements.containsKey(InsertNewTypes.ANSWER)) {
            String sql = generateINSERT("Answers", "QuestionID", "RespondantID", "ModuleID", "Answer");
            PreparedStatement newStatement = this.connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            this.statements.put(InsertNewTypes.ANSWER, newStatement);
        }

        PreparedStatement statement = this.statements.get(InsertNewTypes.ANSWER);

        statement.setInt(1, questionID);
        statement.setInt(2, respondantID);
        statement.setInt(3, moduleID);
        statement.setString(4, answer);

        statement.executeUpdate();

        return new Answer(questionID, respondantID, moduleID, answer);
    }


    public Question INSERT_newQuestions(int semesterID, String question) throws SQLException {
        if (!this.statements.containsKey(InsertNewTypes.QUESTION)) {

            String sql = DB.generateINSERT("Questions", "SemesterID", "Question");
            PreparedStatement newStatement = this.connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            this.statements.put(InsertNewTypes.QUESTION, newStatement);
        }

        PreparedStatement statement = this.statements.get(InsertNewTypes.QUESTION);

        statement.setInt(1, semesterID);
        statement.setString(2, question);

        statement.executeUpdate();
        int id = this.getLatestID(statement);

        return new Question(id, semesterID, question);
    }

    public Coordinator INSERT_newCoordinator(String email) throws SQLException {
        if (!this.statements.containsKey(InsertNewTypes.COORDINATOR)) {
            String sql = DB.generateINSERT("Coordinators", "Email");
            PreparedStatement newStatement = this.connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            this.statements.put(InsertNewTypes.COORDINATOR, newStatement);
        }

        PreparedStatement statement = this.statements.get(InsertNewTypes.COORDINATOR);

        statement.setString(1, email);

        statement.executeUpdate();
        int id = this.getLatestID(statement);

        return new Coordinator(id, email);
    }

    public Semester INSERT_newSemester(int coordinatorID, String name, int year, SemesterSeason season) throws SQLException {
        if (!this.statements.containsKey(InsertNewTypes.SEMESTER)) {
            String sql = DB.generateINSERT("Semesters", "CoordinatorID", "Name", "Year", "Season");
            PreparedStatement newStatement = this.connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            this.statements.put(InsertNewTypes.SEMESTER, newStatement);
        }

        PreparedStatement statement = this.statements.get(InsertNewTypes.SEMESTER);

        statement.setInt(1, coordinatorID);
        statement.setString(2, name);
        statement.setInt(3, year);
        statement.setString(4, season.toString());

        statement.executeUpdate();
        int id = this.getLatestID(statement);

        return new Semester(id, coordinatorID, name, year, season);
    }

    public static String generateINSERT(String table, String... fields) {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("INSERT INTO `");  // Start of statement
        stringBuilder.append(table);            // Insert the table to insert into
        stringBuilder.append("`(");             // Required end chars

        // For each field
        for (String field : fields) {
            stringBuilder.append("`");          // Insert start char
            stringBuilder.append(field);        // Insert field name
            stringBuilder.append("`,");         // Insert end char and comma
        }

        // Replace the last comma with a ending char.
        replaceLastCharOfStringBuilder(stringBuilder, ")");

        stringBuilder.append(" VALUES (");      // Start of values

        // For each value
        for (String field : fields) {
            stringBuilder.append("?");        // Insert the value
            stringBuilder.append(",");         // Insert end char and comma
        }

        // Replace the last comma with a ending char.
        replaceLastCharOfStringBuilder(stringBuilder, ")");

        // Return the final sql statement as a string.
        return stringBuilder.toString();
    }

    public static String generateMassSentimentINSERT(List<Sentiment> sentimentList) {
        StringBuilder stringBuilder = new StringBuilder("INSERT INTO Sentiment(QuestionID, Responedent, Module, SentimentPolarity) VALUES");

        // For each value
        for (Sentiment sentiment : sentimentList) {
            stringBuilder.append("('");
            stringBuilder.append(sentiment.getQuestionID());
            stringBuilder.append("',");
            stringBuilder.append(sentiment.getRespondentID());
            stringBuilder.append(",");
            stringBuilder.append(sentiment.getModuleID());
            stringBuilder.append(",");
            stringBuilder.append(sentiment.getSentimentPolarity());
            stringBuilder.append("),");
        }
        stringBuilder.deleteCharAt(sentimentList.size() - 1);

        // Return the final sql statement as a string.
        return stringBuilder.toString();
    }

    public void INSERT_newNeuralNetwork(NetworkType type, ANN network) throws SQLException {
        byte[] byteArray = Bytes.listToByteBuffer(network.getByteRepresentation()).array();
        System.out.println(byteArray.length);

        String sql = generateINSERT("NeuralNetworks", "Type", "BinaryRepresentation");
        sql += " ON DUPLICATE KEY UPDATE BinaryRepresentation = ?";

        PreparedStatement statement = this.connection.prepareStatement(sql);

        statement.setString(1, type.toString());
        statement.setBytes(2, byteArray);
        statement.setBytes(3, byteArray);

        statement.executeUpdate();
        statement.close();
    }

    public ANN SELECT_neuralNetwork(NetworkType type) throws SQLException {
        String sql = "SELECT `BinaryRepresentation` FROM `NeuralNetworks` WHERE `Type` = ?";
        PreparedStatement statement = this.connection.prepareStatement(sql);

        statement.setString(1, type.toString());

        ResultSet resultSet = statement.executeQuery();
        resultSet.next();

        Blob blob = resultSet.getBlob("BinaryRepresentation");
        statement.close();

        return new ANN(blob.getBytes(1, (int) blob.length()));
    }

    public void INSERT_newWordEmbeddings(List<String> mostUsedWords, Matrix embeddings) throws SQLException {
        if (mostUsedWords.size() != embeddings.getRows()) {
            throw new IllegalArgumentException();
        }

        int totalEntries = mostUsedWords.size();

        // Create prepared statement
        StringBuilder sql = new StringBuilder("INSERT INTO WordEmbeddings (Ord, EmbeddingVector) VALUES ");
        for (int i = 0; i < totalEntries; i++) {
            sql.append("(?,?),");
        }
        sql.deleteCharAt(sql.length() - 1);

        PreparedStatement statement = this.connection.prepareStatement(sql.toString());

        // Insert values
        for (int i = 0; i < totalEntries; i++) {
            int step = (i * 2) + 1;
            String word = mostUsedWords.get(i);
            byte[] bytes = Bytes.listToByteBuffer(embeddings.getRowVector(i).getByteRepresentation()).array();

            statement.setString(step, word);
            statement.setBytes(step + 1, bytes);
        }

        statement.executeUpdate();
    }

    public void DELETE_allWordEmbeddings() throws SQLException {
        String sql = "DELETE FROM `WordEmbeddings`";

        PreparedStatement statement = this.connection.prepareStatement(sql);

        statement.execute();
        statement.close();
    }

    public Map<String, Vector> SELECT_allWordEmbeddings() throws SQLException {
        String sql = "SELECT * FROM WordEmbeddings";

        PreparedStatement statement = this.connection.prepareStatement(sql);

        ResultSet keywords = statement.executeQuery();

        Map<String, Vector> embeddingMap = new HashMap<>(1500);

        // Iterate over all rows in keywords table in database
        while (keywords.next()) {
            // Read row from database
            String word = keywords.getString("Ord");
            Blob blob = keywords.getBlob("EmbeddingVector");

            Vector embedding = VectorBuilder.importVector(blob.getBytes(1, (int) blob.length()));

            embeddingMap.put(word, embedding);
        }

        return embeddingMap;
    }

    public void INSERT_newKeywords(List<Keyword> keywords) throws SQLException {
        StringBuilder sql = new StringBuilder("INSERT INTO Keywords (Keyword, Positive, Negative) VALUES ");

        for (Keyword keyword : keywords) {
            sql.append("('");
            sql.append(keyword.getKeyword());
            sql.append("',");
            sql.append(keyword.getPositive());
            sql.append(",");
            sql.append(keyword.getNegative());
            sql.append("),");
        }

        sql.deleteCharAt(sql.length() - 1);

        PreparedStatement statement = this.connection.prepareStatement(sql.toString());

        statement.executeUpdate();
    }

    public ArrayList<Keyword> SELECT_allKeywords() throws SQLException {
        String sql = "SELECT * FROM Keywords";

        PreparedStatement statement = this.connection.prepareStatement(sql);

        ResultSet keywords = statement.executeQuery();

        ArrayList<Keyword> keywordList = new ArrayList<>();

        // Iterate over all rows in keywords table in database
        while (keywords.next()) {
            // Read row from database and make a keyword object with it
            Keyword keyword = new Keyword(
                    keywords.getString("Keyword"),
                    keywords.getDouble("Positive"),
                    keywords.getDouble("Negative"));

            // Add keyword to list
            keywordList.add(keyword);
        }

        return keywordList;
    }

    public void DELETE_allKeywords() throws SQLException {
        String sql = "DELETE FROM `Keywords`";

        PreparedStatement statement = this.connection.prepareStatement(sql);

        statement.execute();
    }

    public void INSERT_stopWords(List<String> stopWords) throws SQLException {
        StringBuilder sql = new StringBuilder("INSERT INTO StopWords (StopWord) VALUES ");

        for (String stopWord : stopWords) {
            sql.append("('");
            sql.append(stopWord);
            sql.append("'),");
        }

        sql.deleteCharAt(sql.length() - 1);

        PreparedStatement statement = this.connection.prepareStatement(sql.toString());

        statement.executeUpdate();
    }

    public List<String> SELECT_allStopWords() throws SQLException {
        String sql = "SELECT * FROM StopWords";

        PreparedStatement statement = this.connection.prepareStatement(sql);

        ResultSet stopWords = statement.executeQuery();

        ArrayList<String> stopWordList = new ArrayList<>();

        // Iterate over all rows in stopwords table in database
        while (stopWords.next()) {
            // Add stopword to list
            stopWordList.add(stopWords.getString("StopWord"));
        }

        return stopWordList;
    }

    private static void replaceLastCharOfStringBuilder(StringBuilder stringBuilder, String c) {
        int lengthOfString = stringBuilder.length();
        stringBuilder.replace(lengthOfString - 1, lengthOfString, c);
    }

    private int getLatestID(Statement statement) throws SQLException {
        ResultSet rs = statement.getGeneratedKeys();
        rs.next();
        return rs.getInt(1);
    }

    public void SELECT_answersIntoSemester(SemesterModel semester, List<Row> rawDataList) throws SQLException {
        String query = "SELECT Answers.QuestionID, Answers.RespondantID, Answers.ModuleID, Answers.Answer, MarkedAnswers.Colour FROM Answers LEFT JOIN MarkedAnswers ON Answers.QuestionID = MarkedAnswers.QuestionID AND Answers.RespondantID = MarkedAnswers.RespondantID AND Answers.ModuleID = MarkedAnswers.ModuleID WHERE Answers.ModuleID in (SELECT ModuleID FROM Modules WHERE SemesterID = ?) GROUP BY RespondantID, ModuleID, QuestionID ORDER BY RespondantID";

        PreparedStatement statement = this.connection.prepareStatement(query);
        statement.setInt(1, semester.getId());


        ResultSet rs = statement.executeQuery();
        int respondantID = -1;
        int moduleID = -1;
        List<AnswerModel> answers = null;
        while (rs.next()) {
            AnswerModel answer = new AnswerModel(
                    rs.getInt("QuestionID"),
                    rs.getInt("RespondantID"),
                    rs.getInt("ModuleID"),
                    rs.getString("Answer")
            );

            semester.attachAnswer(answer);
            int colour = rs.getInt("Colour");

            if (colour != 0) {
                MarkedAnswerModel markedAnswer = new MarkedAnswerModel(
                        answer.getQuestionID(),
                        answer.getRespondentID(),
                        answer.getModuleID(),
                        colour
                );

                answer.setMarkedAnswer(markedAnswer);
                //semester.attachMarkedAnswer(markedAnswer);
            }


            if (respondantID != answer.getRespondentID() || moduleID != answer.getModuleID()) {
                if (respondantID != -1 && moduleID != -1) {
                    rawDataList.add(new Row(moduleID, respondantID, answers));
                }

                answers = new ArrayList<>();

                respondantID = answer.getRespondentID();
                moduleID = answer.getModuleID();
            }

            answers.add(answer);


        }

        connection.close();


    }

    public SemesterModel SELECT_SemesterBySemesterID(int semesterID) throws SQLException {
        String query = String.format("SELECT CoordinatorID, Name, Year, Season FROM Semesters WHERE SemesterID = %d", semesterID);
        ResultSet result = this.connection.createStatement().executeQuery(query);
        result.next();
        return new SemesterModel(semesterID, result.getInt("CoordinatorID"), result.getString("Name"), result.getInt("Year"), SemesterSeason.fromString(result.getString("Season")));
    }

    public List<SentimentModel> SELECT_SentimentsWhereModuleID(int moduleID) throws SQLException {
        String query = String.format("SELECT QuestionID, RespondantID, Sentiment FROM Sentiments WHERE ModuleID = %d", moduleID);
        ResultSet result = this.connection.createStatement().executeQuery(query);
        List<SentimentModel> sentimentModelList = new ArrayList<>();

        while (result.next()) {
            sentimentModelList.add(
                    new SentimentModel(result.getInt("QuestionID"),
                            result.getInt("RespondantID"),
                            moduleID,
                            SentimentPolarity.fromString(result.getString("Sentiment"))));
        }
        return sentimentModelList;
    }

    public List<SemesterModel> SELECT_semestersWhereCoordinatorId(int coordinatorId) throws ClassNotFoundException, SQLException {
        List<SemesterModel> semesters = new ArrayList<>();
        String query = String.format("SELECT SemesterID, Name, Year, Season FROM Semesters WHERE CoordinatorID = %d", coordinatorId);
        Statement statement;
        Connection conn = newConnection();


        try {
            statement = conn.createStatement();
            ResultSet rs = statement.executeQuery(query);
            while (rs.next()) {
                SemesterModel semester = new SemesterModel(
                        rs.getInt("SemesterID"),
                        coordinatorId,
                        rs.getString("name"),
                        rs.getInt("Year"),
                        SemesterSeason.fromString(rs.getString("Season"))
                );

                semesters.add(semester);
            }
        } catch (SQLException ex) {
            throw ex;
        } finally {
            conn.close();
        }


        return semesters;
    }

    public List<ModuleModel> SELECT_ModulesWhereSemesterId(int semesterId) throws ClassNotFoundException, SQLException {
        List<ModuleModel> modules = new ArrayList<>();
        String query = String.format("SELECT ModuleID, Name FROM Modules WHERE SemesterID = %d", semesterId);
        Statement statement;
        Connection conn = newConnection();

        try {
            statement = conn.createStatement();
            ResultSet rs = statement.executeQuery(query);
            while (rs.next()) {
                ModuleModel module = new ModuleModel(
                        rs.getInt("ModuleID"),
                        semesterId,
                        rs.getString("Name")
                );

                modules.add(module);
            }
        } catch (SQLException ex) {
            throw ex;
        } finally {
            conn.close();
        }

        return modules;
    }

    public List<QuestionModel> SELECT_QuestionsWhereSemesterId(int semesterId) throws ClassNotFoundException, SQLException {
        List<QuestionModel> questions = new ArrayList<>();
        String query = String.format("SELECT QuestionID, Question FROM Questions WHERE SemesterID = %d", semesterId);
        Statement statement;
        Connection conn = newConnection();

        try {
            statement = conn.createStatement();
            ResultSet rs = statement.executeQuery(query);
            while (rs.next()) {
                QuestionModel question = new QuestionModel(
                        rs.getInt("QuestionID"),
                        semesterId,
                        rs.getString("Question")
                );

                questions.add(question);
            }
        } catch (SQLException ex) {
            throw ex;
        } finally {
            conn.close();
        }

        return questions;
    }

    public List<AnswerModel> SELECT_AnswersWhereSemesterId(int semesterId) throws ClassNotFoundException, SQLException {
        List<AnswerModel> answers = new ArrayList<>();
        String getModulesIds = String.format("SELECT ModuleID FROM Modules WHERE SemesterID = %d", semesterId);
        String query = String.format("SELECT QuestionID, RespondantID, ModuleID, Answer FROM Answers WHERE ModuleID in (%s)", getModulesIds);
        Statement statement;
        Connection conn = newConnection();

        try {
            statement = conn.createStatement();
            ResultSet rs = statement.executeQuery(query);
            while (rs.next()) {
                AnswerModel answer = new AnswerModel(
                        rs.getInt("QuestionID"),
                        rs.getInt("RespondantID"),
                        rs.getInt("ModuleID"),
                        rs.getString("Answer")
                );

                answers.add(answer);
            }
        } catch (SQLException ex) {
            throw ex;
        } finally {
            conn.close();
        }

        return answers;
    }

    public List<SentimentModel> SELECT_SentimentsWhereSemesterId(int semesterId) throws ClassNotFoundException, SQLException {
        List<SentimentModel> sentiments = new ArrayList<>();
        String getModulesIds = String.format("SELECT ModuleID FROM Modules WHERE SemesterID = %d", semesterId);
        String query = String.format("SELECT * FROM Sentiments WHERE ModuleID IN (%s)", getModulesIds);
        Statement statement;
        Connection conn = newConnection();

        try {
            statement = conn.createStatement();
            ResultSet rs = statement.executeQuery(query);
            while (rs.next()) {
                SentimentModel answer = new SentimentModel(
                        rs.getInt("QuestionID"),
                        rs.getInt("RespondantID"),
                        rs.getInt("ModuleID"),
                        SentimentPolarity.fromString(rs.getString("Sentiment"))
                );

                sentiments.add(answer);
            }
        } catch (SQLException ex) {
            throw ex;
        } finally {
            conn.close();
        }

        return sentiments;
    }

    public List<MarkedAnswerModel> SELECT_MarkedAnswersWhereSemesterId(int semesterId) throws ClassNotFoundException, SQLException {
        List<MarkedAnswerModel> answers = new ArrayList<>();
        String getModulesIds = String.format("SELECT ModuleID FROM Modules WHERE SemesterID = %d", semesterId);
        String query = String.format("SELECT QuestionID, RespondantID, ModuleID, Colour FROM MarkedAnswers WHERE ModuleID in (%s)", getModulesIds);
        Statement statement;
        Connection conn = newConnection();

        try {
            statement = conn.createStatement();
            ResultSet rs = statement.executeQuery(query);
            while (rs.next()) {
                MarkedAnswerModel markedAnswer = new MarkedAnswerModel(
                        rs.getInt("QuestionID"),
                        rs.getInt("RespondantID"),
                        rs.getInt("ModuleID"),
                        rs.getInt("Colour")
                );

                answers.add(markedAnswer);
            }
        } catch (SQLException ex) {
            throw ex;
        } finally {
            conn.close();
        }

        return answers;
    }

    public static List<CoordinatorModel> SELECT_CoordinatorsWhereEmail(String email) throws ClassNotFoundException, SQLException {
        List<CoordinatorModel> coordinators = new ArrayList<>();
        String query = String.format("SELECT CoordinatorID, Email FROM Coordinators WHERE Email = '%s'", email);
        Statement statement;


        try (Connection conn = newConnection()) {
            statement = conn.createStatement();
            ResultSet rs = statement.executeQuery(query);
            while (rs.next()) {
                CoordinatorModel coordinator = new CoordinatorModel(
                        rs.getInt("CoordinatorID"),
                        rs.getString("Email")
                );

                coordinators.add(coordinator);
            }
        } catch (SQLException ex) {
            throw ex;
        }

        return coordinators;
    }

//    public void INSERT_Model(ModelInsert model) throws SQLException{
//        PreparedStatement statement = model.getInsertStatement(this.connection);
//
//        statement.executeUpdate();
//    }

    public void UPDATE_Model(ModelUpdate model) throws SQLException {
        PreparedStatement statement = model.getUpdateStatement(this.connection);

        statement.executeUpdate();
    }

    public void DELETE_Model(ModelDelete model) throws SQLException {
        PreparedStatement statement = model.getDeleteStatement(this.connection);

        statement.executeUpdate();
    }


}
