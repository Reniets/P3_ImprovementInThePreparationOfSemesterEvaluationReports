package dbComponent.data;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CoordinatorTest {

    // Test Fields:
    private static Coordinator coordinatorEmpty;
    private static Coordinator coordinator;
    private static Coordinator coordinatorB;

    // Test Constant:
    private static final String EMAIL = "test@mail.dk";

    @BeforeAll
    static void beforeAll() {
        coordinatorEmpty = new Coordinator();
        coordinator = new Coordinator(0, EMAIL);
        coordinatorB = new Coordinator(EMAIL);
    }

    // Tests:
    @Test
    void getId() {
        assertEquals(0, coordinator.getId());
    }

    @Test
    void getEmail() {
        assertEquals(EMAIL, coordinator.getEmail());
    }

    @Test
    void setId() {
        coordinatorB.setId(0);
        assertEquals(0, coordinatorB.getId());
    }

    @Test
    void equals() {
        assertEquals(coordinator, coordinatorB);
    }

    @Test
    void hashCodeTest() {
        assertEquals(coordinator.hashCode(), coordinatorB.hashCode());
    }
}