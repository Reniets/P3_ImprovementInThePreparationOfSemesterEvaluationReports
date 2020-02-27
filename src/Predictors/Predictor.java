package Predictors;

import CSVHandler.CSV_Reader;
import Enums.SentimentPolarity;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

public class Predictor {

    // Field:
    private String foldTestPath;

    // Constructors:
    public Predictor(int foldNumber) {
        this.foldTestPath = "data/folds/fold" + foldNumber + "/test_" + foldNumber + ".txt";
    }

    public Predictor(String foldTestPath) {
        this.foldTestPath = foldTestPath;
    }

    // Methods:
    public String getFoldTestPath() {
        return foldTestPath;
    }

    public double[] validate(PredictionMethod predictionMethod) throws IOException, SQLException {
        SentimentPolarity target, prediction;
        String comment;
        Integer classifications = 0, posClas = 0, negClas = 0, pos = 0, neg = 0;

        CSV_Reader csvrTest = new CSV_Reader(this.foldTestPath);
        Integer totalTestLines = csvrTest.getTotalLines();
//        Preprocessor pp = new Preprocessor();

        for (ArrayList<String> readTestPoint : csvrTest) {
            target = SentimentPolarity.fromString(readTestPoint.get(0));
            comment = readTestPoint.get(1);
            prediction = predictionMethod.predictSentiment(comment);

            if (target == prediction) {
                classifications++;
                if (target == SentimentPolarity.POSITIVE) posClas++;
                if (target == SentimentPolarity.NEGATIVE) negClas++;
            } else {
//                System.out.println("Predicted: " + prediction + " : " + pp.process(comment) + " : " + comment + "\n");
            }

            if (target == SentimentPolarity.POSITIVE) pos++;
            if (target == SentimentPolarity.NEGATIVE) neg++;
        }

        csvrTest.close();

        System.out.println("Pos: " + posClas + "/" + pos);
        System.out.println("Neg: " + negClas + "/" + neg);
        System.out.println("Correct: " + classifications + "/" + totalTestLines);
        System.out.println("Wrong: " + (totalTestLines - classifications) + "/" + totalTestLines);

        return new double[]{100 * ((double) classifications / totalTestLines), posClas, negClas, pos, neg};
    }
}
