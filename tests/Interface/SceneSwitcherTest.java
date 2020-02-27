package Interface;

import javafx.stage.Stage;
import javafx.stage.Window;
import org.junit.jupiter.api.Test;

import java.lang.invoke.SwitchPoint;

import static org.junit.jupiter.api.Assertions.*;

class SceneSwitcherTest {

    SceneSwitcher getSceneSwitcher(){
        Stage stage = new Stage();
        return new SceneSwitcher(stage);
    }

    @Test
    void showLoginScreen() {
        /*SceneSwitcher switcher = getSceneSwitcher();

        Exception e = null;
        try{
            switcher.showLoginScreen();
        }catch(Exception ex){
            e = ex;
        }

        assertNull(e);*/
    }

    @Test
    void showSemesterPickerScreen() {
    }

    @Test
    void showSemesterCreateScreen() {
    }
}