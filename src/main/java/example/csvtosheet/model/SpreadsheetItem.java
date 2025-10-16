package example.csvtosheet.model;  // or your package

public class SpreadsheetItem {
    private final String id;
    private final String name;

    public SpreadsheetItem(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name; // This makes the ComboBox show the spreadsheet name
    }
}