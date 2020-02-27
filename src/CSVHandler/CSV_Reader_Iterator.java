package CSVHandler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

public class CSV_Reader_Iterator implements Iterator<ArrayList<String>> {

    // Fields
    private final CSV_Reader CSVR;
    private final int totalLines;
    private int currentLine = 0;

    // Constructor
    public CSV_Reader_Iterator(CSV_Reader CSVR) {
        this.CSVR = CSVR;
        this.totalLines = CSVR.getTotalLines();

        this.CSVR.jumpToLine(0);
    }

    // Methods
    // Checks if the file has more lines than the current line.
    @Override
    public boolean hasNext() {
        return this.currentLine != this.totalLines;
    }

    // Gets the next line
    @Override
    public ArrayList<String> next() {
        this.currentLine++;
        try {
            return this.CSVR.nextLine();
        } catch (IOException e) {
            return null;
        }
    }
}
