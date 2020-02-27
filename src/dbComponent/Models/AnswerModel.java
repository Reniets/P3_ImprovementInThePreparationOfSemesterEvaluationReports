package dbComponent.Models;

import dbComponent.data.Answer;

public class AnswerModel extends Answer {
    private QuestionModel question;
    private ModuleModel module;
    private MarkedAnswerModel markedAnswer;

    public AnswerModel(int questionId, int respondantID, int moduleId, String answer) {
        super(questionId, respondantID, moduleId, answer);
    }

    public QuestionModel getQuestion() {
        return question;
    }

    public void setQuestion(QuestionModel question) {
        this.question = question;
    }

    public ModuleModel getModule() {
        return module;
    }

    public void setModule(ModuleModel module) {
        this.module = module;
    }

    public MarkedAnswerModel getMarkedAnswer() {
        return markedAnswer;
    }

    public void setMarkedAnswer(MarkedAnswerModel markedAnswer) {
        MarkedAnswerModel oldMarkedAnswer = this.markedAnswer;
        this.markedAnswer = markedAnswer;
        if (this.markedAnswer != null && !this.equals(this.markedAnswer.getAnswer())) {
            this.markedAnswer.setAnswer(this);
        }
        if (this.question != null) {
            if (this.question.getSemester() != null) {
                if (markedAnswer == null) {
                    this.question.getSemester().getMarkedAnswerModels().remove(oldMarkedAnswer);
                } else {
                    this.question.getSemester().addMarkedAnswer(markedAnswer);
                }

            }
        }
    }
}
