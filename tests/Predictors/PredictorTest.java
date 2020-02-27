package Predictors;

import dbComponent.database.DB;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class PredictorTest {

    // Test Fields:
    private static DB db;
    private static PredictionMethod predictionMethod;
    private static Predictor predictor;
    private static double percentage;

    // Test Constant:
    private static final String TEST_PATH = "tests/Predictors/data/test.txt";

    @BeforeAll
    static void beforeAll() throws IOException, SQLException, ClassNotFoundException {
        db = new DB();
        predictionMethod = new PredictKeyword(db);
        predictor = new Predictor(TEST_PATH);
        percentage = predictor.validate(predictionMethod)[0];
    }

    // Test:
    @Test
    void getFoldTestPath() {
        Predictor predictorB = new Predictor(1);
        assertEquals("data/folds/fold1/test_1.txt", predictorB.getFoldTestPath());
    }

    @Test
    void validatePercentage() {
        assertTrue(0 <= percentage && percentage <= 100);
    }


}