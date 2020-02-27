package Enums;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SentimentPolarityTest {

    @Test
    void getValue() {
        assertEquals(0, SentimentPolarity.POSITIVE.getValue());
    }
}