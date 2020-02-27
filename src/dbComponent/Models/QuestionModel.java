package dbComponent.Models;

import dbComponent.data.Question;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class QuestionModel extends Question {
    // Field:
    private SemesterModel semester;
    private Set<AnswerModel> answers = new HashSet<>();

    // Constructor
    public QuestionModel(int questionID, int semesterID, String question) {
        super(questionID, semesterID, question);
    }

    // Methods:
    public List<AnswerModel> getAnswers() {
        return new ArrayList<>(this.answers);
    }

    public void setAnswers(List<AnswerModel> answers) {
        this.answers.addAll(answers);
        answers.forEach(answer -> answer.setQuestion(this));
    }

    public void setSemester(SemesterModel semesterModel) {
        this.semester = semesterModel;
    }

    public SemesterModel getSemester() {
        return this.semester;
    }
}
