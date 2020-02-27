package dbComponent.Models.RawDataTable;

import dbComponent.Models.AnswerModel;
import dbComponent.data.Question;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;

public class Row {

    // Fields:
    private int moduleID;
    private int respondantID;
    private HashMap<Integer, AnswerModel> answers;

    // Constructor:
    public Row(int moduleID, int respondantID, List<AnswerModel> answers) {
        this.moduleID = moduleID;
        this.respondantID = respondantID;


        this.answers = new HashMap<>();
        for (AnswerModel answer : answers) {
            if (answer != null) {
                this.answers.put(answer.getQuestionID(), answer);
            }
        }
    }

    // Methods:
    public int getModuleID() {
        return this.moduleID;
    }

    public int getRespondantID() {
        return this.respondantID;
    }

    public AnswerModel getAnswer(Question question) {
        if (question == null) {
            return null;
        }

        AnswerModel answer = this.answers.get(question.getId());

        return answer == null ? null : answer;
    }

    public String getAnswerToQuestion(Question question) {
        AnswerModel answer = this.getAnswer(question);
        if (answer == null) {
            return "";
        } else {
            return answer.getAnswer();
        }
    }

    public boolean anyColumnMatchesPattern(Pattern pattern) {
        return this.answers.values().stream().filter(a -> pattern.matcher(a.getAnswer()).find()).count() > 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Row row = (Row) o;
        return this.moduleID == row.moduleID &&
                this.respondantID == row.respondantID;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.moduleID, this.respondantID);
    }
}
