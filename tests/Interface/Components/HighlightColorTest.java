package Interface.Components;

import javafx.application.Application;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

class HighlightColorTest {

    private static final HighlightColor[] HIGHLIGHT_COLORS = {
            HighlightColor.RED,
            HighlightColor.NONE,
            HighlightColor.BLUE,
            HighlightColor.PINK,
            HighlightColor.GREEN,
            HighlightColor.YELLOW
    };

    @RepeatedTest(6)
    void getName(RepetitionInfo repetitionInfo) {
        int iteration = repetitionInfo.getCurrentRepetition() - 1;
        HighlightColor currentColor = HIGHLIGHT_COLORS[iteration];
        String expectedString = "";

        if(currentColor.equals(HighlightColor.NONE)){
            expectedString = "Ingen";
        }

        if(currentColor.equals(HighlightColor.BLUE)){
            expectedString = "Blå";
        }

        if(currentColor.equals(HighlightColor.GREEN)){
            expectedString = "Grøn";
        }

        if(currentColor.equals(HighlightColor.PINK)){
            expectedString = "Lyserød";
        }

        if(currentColor.equals(HighlightColor.RED)){
            expectedString = "Rød";
        }

        if(currentColor.equals(HighlightColor.YELLOW)){
            expectedString = "Gul";
        }

        assertEquals(expectedString, currentColor.getName());
    }

    @RepeatedTest(6)
    void getColor(RepetitionInfo repetitionInfo) {
        int iteration = repetitionInfo.getCurrentRepetition() - 1;
        HighlightColor currentColor = HIGHLIGHT_COLORS[iteration];
        Color expectedColor = null;

        if(currentColor.equals(HighlightColor.NONE)){
            expectedColor = Color.WHITESMOKE;
        }

        if(currentColor.equals(HighlightColor.RED)){
            expectedColor = Color.TOMATO;
        }

        if(currentColor.equals(HighlightColor.BLUE)){
            expectedColor = Color.MEDIUMTURQUOISE;
        }

        if(currentColor.equals(HighlightColor.PINK)){
            expectedColor = Color.LIGHTPINK;
        }

        if(currentColor.equals(HighlightColor.GREEN)){
            expectedColor = Color.LIGHTGREEN;
        }

        if(currentColor.equals(HighlightColor.YELLOW)){
            expectedColor = Color.GOLD;
        }

        assertEquals(Colors.getRGB(expectedColor), currentColor.getRGB());
    }

    @RepeatedTest(6)
    void getRGB(RepetitionInfo repetitionInfo) {
        int iteration = repetitionInfo.getCurrentRepetition() - 1;
        HighlightColor currentColor = HIGHLIGHT_COLORS[iteration];
        Color expectedColor = null;

        if(currentColor.equals(HighlightColor.NONE)){
            expectedColor = Color.WHITESMOKE;
        }

        if(currentColor.equals(HighlightColor.RED)){
            expectedColor = Color.TOMATO;
        }

        if(currentColor.equals(HighlightColor.BLUE)){
            expectedColor = Color.MEDIUMTURQUOISE;
        }

        if(currentColor.equals(HighlightColor.PINK)){
            expectedColor = Color.LIGHTPINK;
        }

        if(currentColor.equals(HighlightColor.GREEN)){
            expectedColor = Color.LIGHTGREEN;
        }

        if(currentColor.equals(HighlightColor.YELLOW)){
            expectedColor = Color.GOLD;
        }

        assertEquals(Colors.getRGB(expectedColor), currentColor.getRGB());
    }

    @RepeatedTest(6)
    void getHexString(RepetitionInfo repetitionInfo) {
        int iteration = repetitionInfo.getCurrentRepetition() - 1;
        HighlightColor currentColor = HIGHLIGHT_COLORS[iteration];
        String expectedColorString = "";

        if(currentColor.equals(HighlightColor.NONE)){
            expectedColorString = Color.WHITESMOKE.toString().substring(2);
        }

        if(currentColor.equals(HighlightColor.RED)){
            expectedColorString = Color.TOMATO.toString().substring(2);
        }

        if(currentColor.equals(HighlightColor.BLUE)){
            expectedColorString = Color.MEDIUMTURQUOISE.toString().substring(2);
        }

        if(currentColor.equals(HighlightColor.PINK)){
            expectedColorString = Color.LIGHTPINK.toString().substring(2);
        }

        if(currentColor.equals(HighlightColor.GREEN)){
            expectedColorString = Color.LIGHTGREEN.toString().substring(2);
        }

        if(currentColor.equals(HighlightColor.YELLOW)){
            expectedColorString = Color.GOLD.toString().substring(2);
        }

        assertEquals(expectedColorString, currentColor.getHexString());
    }


    @Disabled//@RepeatedTest(6)
    void setNodeBackgroundColor(RepetitionInfo repetitionInfo) {
        int iteration = repetitionInfo.getCurrentRepetition() - 1;
        HighlightColor currentColor = HIGHLIGHT_COLORS[iteration];
        String expectedColorString = "";
        Label label = new Label();

        if(currentColor.equals(HighlightColor.NONE)){
            expectedColorString = Color.WHITESMOKE.toString().substring(2);
        }

        if(currentColor.equals(HighlightColor.RED)){
            expectedColorString = Color.TOMATO.toString().substring(2);
        }

        if(currentColor.equals(HighlightColor.BLUE)){
            expectedColorString = Color.MEDIUMTURQUOISE.toString().substring(2);
        }

        if(currentColor.equals(HighlightColor.PINK)){
            expectedColorString = Color.LIGHTPINK.toString().substring(2);
        }

        if(currentColor.equals(HighlightColor.GREEN)){
            expectedColorString = Color.LIGHTGREEN.toString().substring(2);
        }

        if(currentColor.equals(HighlightColor.YELLOW)){
            expectedColorString = Color.GOLD.toString().substring(2);
        }

        currentColor.setNodeBackgroundColor(label);

        assertTrue(label.getStyle().contains(String.format("-fx-background-color: #%s;", expectedColorString)));
    }

    @RepeatedTest(7)
    void isHighlightColor(RepetitionInfo repetitionInfo) {
        int iteration = repetitionInfo.getCurrentRepetition() - 1;
        Color toTest = null;
        boolean expectation = false;

        switch (iteration){
            case 0:
                toTest = Color.TOMATO;
                expectation = true;
                break;
            case 1:
                toTest = Color.WHITESMOKE;
                expectation = true;
                break;
            case 2:
                toTest = Color.MEDIUMTURQUOISE;
                expectation = true;
                break;
            case 3:
                toTest = Color.LIGHTPINK;
                expectation = true;
                break;
            case 4:
                toTest = Color.LIGHTGREEN;
                expectation = true;
            case 5:
                toTest = Color.GOLD;
                expectation = true;
                break;
                default:
                    toTest = Color.MAGENTA;

        }

        assertEquals(expectation, HighlightColor.isHighlightColor(toTest));
    }

    @RepeatedTest(6)
    void equalsTest(RepetitionInfo repetitionInfo) {
        int iteration = repetitionInfo.getCurrentRepetition() - 1;
        HighlightColor currentColor = HIGHLIGHT_COLORS[iteration];
        HighlightColor toTest = null;

        switch (iteration){
            case 0:
                toTest = new HighlightColor(Color.TOMATO);
                break;
            case 1:
                toTest = new HighlightColor(Color.WHITESMOKE);
                break;
            case 2:
                toTest = new HighlightColor(Color.MEDIUMTURQUOISE);
                break;
            case 3:
                toTest = new HighlightColor(Color.LIGHTPINK);
                break;
            case 4:
                toTest = new HighlightColor(Color.LIGHTGREEN);
                break;
            case 5:
                toTest = new HighlightColor(Color.GOLD);
                break;

        }

        assertTrue(currentColor.equals(toTest));


    }

    @RepeatedTest(6)
    void newHighlightColorTestRGB(RepetitionInfo repetitionInfo){
        int iteration = repetitionInfo.getCurrentRepetition() - 1;
        HighlightColor currentColor = HIGHLIGHT_COLORS[iteration];
        int rgb = HIGHLIGHT_COLORS[iteration].getRGB();
        HighlightColor newColor = null;
        Exception creationException = null;

        try{
            newColor = new HighlightColor(rgb);
        }catch (Exception ex){
            creationException = ex;
        }

        assertNull(creationException);
        assertEquals(currentColor, newColor);
    }

    @RepeatedTest(6)
    void newHighlightColorTestFails(RepetitionInfo repetitionInfo){
        int iteration = repetitionInfo.getCurrentRepetition() - 1;
        HighlightColor currentColor = HIGHLIGHT_COLORS[iteration];
        Color col = HIGHLIGHT_COLORS[iteration].getColor().deriveColor(10.0, 0.0, 0.0, 0.0);
        HighlightColor newColor = null;
        Exception creationException = null;

        try{
            newColor = new HighlightColor(col);
        }catch (Exception ex){
            creationException = ex;
        }

        assertNotNull(creationException);
    }

    @RepeatedTest(6)
    void newHighlightColorTest(RepetitionInfo repetitionInfo){
        int iteration = repetitionInfo.getCurrentRepetition() - 1;
        HighlightColor currentColor = HIGHLIGHT_COLORS[iteration];
        Color col = HIGHLIGHT_COLORS[iteration].getColor();
        HighlightColor newColor = null;
        Exception creationException = null;



        try{
            newColor = new HighlightColor(col);
        }catch (Exception ex){
            creationException = ex;
        }

        assertNull(creationException);
        assertEquals(currentColor, newColor);
    }

}