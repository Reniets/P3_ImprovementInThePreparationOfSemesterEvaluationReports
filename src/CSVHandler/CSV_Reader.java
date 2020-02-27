package CSVHandler;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

public class CSV_Reader implements Iterable<ArrayList<String>> {

    // Fields
    private String filePath;
    private int currentLine;
    private int totalLines;
    private boolean attachedToCSV;
    private File file;
    private BufferedReader bufferedReader;

    // Constant
    private static final String CHARSET = "UTF-8";

    // Constructor
    public CSV_Reader() {
        this.attachedToCSV = false;
        this.currentLine = 0;
    }

    // Constructor - Specific file from beginning
    public CSV_Reader(String filePath) throws IOException {
        this();

        this.targetFile(filePath);
    }

    // Getters, access private fields
    // Gets the index of the current line
    public int getCurrentLine() {
        return this.currentLine;
    }

    // Gets the file path of the CSV file
    public String getFilePath() {
        return this.filePath;
    }

    // Gets the total amount of lines in the attached CSV file
    public int getTotalLines() {
        if (this.attachedToCSV) {
            return this.totalLines;
        } else {
            throw new NullPointerException();
        }
    }

    // Checks if the 'cursor' is located at the last line
    public boolean atLastLine() {
        return attachedToCSV && this.currentLine == this.totalLines;
    }

    // Methods
    // Attach a specific file to this (CSV Reader)
    public void targetFile(String filePath) throws IOException {
        this.filePath = filePath;

        try {

            if (this.attachedToCSV) {
                this.close();
            }

            // Prepare reading from file
            this.currentLine = 0;
            this.file = new File(filePath);
            this.bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(filePath), CHARSET));
            this.attachedToCSV = true;
            this.totalLines = countLines();

            // If an error occurred, catch it and print the error.
        } catch (IOException e) {
            this.attachedToCSV = false;
            throw e;
        }
    }

    // Read the next line and return as an ArrayList. Loop if EOF.
    public ArrayList<String> nextLine() throws IOException {
        if (this.attachedToCSV) {

            // if at EOF, create new buffer and loop to beginning
            if (this.currentLine == this.totalLines) {
                this.bufferedReader.close();
                this.bufferedReader = new BufferedReader(new FileReader(this.file));
                this.currentLine = 0;
            }

            String readLine = this.bufferedReader.readLine();
            this.currentLine++;

            String[] output = readLine.split(";");
            int columns = (int) readLine.chars().filter(num -> num == ';').count() + 1;

            ArrayList<String> row = new ArrayList<>(Arrays.asList(output));

            while (row.size() < columns) {
                row.add("");
            }

            return row;

        } else {
            throw new NullPointerException("CSVR_Reader was not connected to any file");
        }
    }

    public boolean jumpToLine(int line) {
        if (this.totalLines >= line && line >= 0) {
            while (this.currentLine != line) {
                try {
                    this.nextLine();
                } catch (IOException e) {
                    return false;
                }
            }
            return true;
        } else {
            throw new IllegalArgumentException("Line not found. The file has a total of " + this.totalLines + " lines, and you wanted to jump to line " + line + ".");
        }
    }

    // Reads the amount of lines in the attached CSV
    private int countLines() throws IOException {
        if (this.attachedToCSV) {
            int count = 0;

            BufferedReader b = new BufferedReader(new FileReader(this.filePath));
            while (b.readLine() != null) {
                count++;
            }

            return count;

        } else {
            throw new IOException();
        }
    }

    // Closes the bufferedReader and detaches from the CSV file.
    public void close() throws IOException {
        bufferedReader.close();
        this.attachedToCSV = false;
        this.currentLine = 0;
        this.totalLines = -1;
    }

    // Prints the percentage of lines read
    public void printPercentage() {
        if (this.getCurrentLine() != 0 && this.totalLines >= 100 && this.getCurrentLine() % (this.getTotalLines() / 100) == 0) {
            System.out.println(Math.ceil(((double) this.getCurrentLine() / (double) this.getTotalLines()) * 100) + "%");
        }
    }

    // Overrides the existing iterator with the CSV_Reader_Iterator
    @Override
    public Iterator<ArrayList<String>> iterator() {
        return new CSV_Reader_Iterator(this);
    }
}
