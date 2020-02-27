package dbComponent.data;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class KeywordTest {

    // Test Field:
    private Keyword keywordA;
    private Keyword keywordB;

    @BeforeEach
    void beforeEach() {
        this.keywordA = new Keyword("keyword", 1.0, 0.0);
        this.keywordB = new Keyword("keyword", 1.0, 0.0);
    }

    // Tests:
    @Test
    void equals() {
        assertEquals(keywordA, keywordB);
    }

    @Test
    void hashCodeTest() {
        assertEquals(keywordA.hashCode(), keywordB.hashCode());
    }
}