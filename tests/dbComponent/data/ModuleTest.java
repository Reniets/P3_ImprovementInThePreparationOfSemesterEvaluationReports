package dbComponent.data;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ModuleTest {

    // Test Fields:
    private static Module moduleEmpty;
    private static Module module;
    private static Module moduleB;

    // Test Constant:
    private static final String MODULE_NAME = "module";

    @BeforeAll
    static void beforeAll() {
        moduleEmpty = new Module();
        module = new Module(0, 1, MODULE_NAME);
        moduleB = new Module(1, MODULE_NAME);
    }

    // Tests:
    @Test
    void getId() {
        assertEquals(0, module.getId());
    }

    @Test
    void getSemesterID() {
        assertEquals(1, module.getSemesterID());
    }

    @Test
    void getName() {
        assertEquals(MODULE_NAME, module.getName());
    }

    @Test
    void setId() {
        moduleB.setId(0);
        assertEquals(0, moduleB.getId());
    }

    @Test
    void equals() {
        assertEquals(module, moduleB);
    }

    @Test
    void hashCodeTest() {
        assertEquals(module.hashCode(), moduleB.hashCode());
    }
}