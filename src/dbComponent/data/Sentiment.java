package dbComponent.data;

import Enums.SentimentPolarity;

import java.util.Objects;

public class Sentiment {
    private int questionID;
    private int respondentID;
    private int moduleID;
    private SentimentPolarity sentimentPolarity;

    public Sentiment(int questionID, int respondentID, int moduleID, SentimentPolarity sentimentPolarity) {
        this.questionID = questionID;
        this.respondentID = respondentID;
        this.moduleID = moduleID;
        this.sentimentPolarity = sentimentPolarity;
    }

    public int getQuestionID() {
        return questionID;
    }

    public int getRespondentID() {
        return respondentID;
    }

    public int getModuleID() {
        return moduleID;
    }

    public SentimentPolarity getSentimentPolarity() {
        return sentimentPolarity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Sentiment sentiment = (Sentiment) o;
        return questionID == sentiment.questionID &&
                respondentID == sentiment.respondentID &&
                moduleID == sentiment.moduleID &&
                sentimentPolarity == sentiment.sentimentPolarity;
    }

    @Override
    public int hashCode() {

        return Objects.hash(questionID, respondentID, moduleID, sentimentPolarity);
    }
}
