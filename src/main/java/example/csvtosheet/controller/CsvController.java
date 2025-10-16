package example.csvtosheet.controller;

import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.FileList;
import example.csvtosheet.model.SpreadsheetItem;
import example.csvtosheet.model.Transaction;
import example.csvtosheet.repository.CsvTransactionRepository;
import example.csvtosheet.service.GoogleSheetsWriter;
import example.csvtosheet.util.GoogleSheetsUtil;
import example.csvtosheet.util.TransactionFormatterForGoogle;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CsvController {
    @FXML private Button browseFileButton;
    @FXML private Label messageLabel;
    @FXML private ComboBox<SpreadsheetItem> spreadsheetComboBox;

    private static final String TARGET_RANGE = "Test!A3";

    private List<Transaction> readTransactionsFromCsv(java.io.File csvFile) throws Exception {
        CsvTransactionRepository repo = new CsvTransactionRepository();
        return repo.readTransactions(csvFile.toString());
    }

    private void writeTransactionsToSheet(String spreadsheetId, List<Transaction> transactions) throws Exception {
        List<List<Object>> rows = TransactionFormatterForGoogle.convertTransactionsToSheetRows(transactions);
        GoogleSheetsWriter sheetWriter = new GoogleSheetsWriter();
        sheetWriter.writeDataToSheet(spreadsheetId, TARGET_RANGE, rows);
    }

    private void handleFileUpload(java.io.File csvFile) {
        if (csvFile == null) {
            messageLabel.setText("No file selected");
            return;
        }

        new Thread(() -> {
            try {
                // Step 1: Read CSV
                List<Transaction> transactions = readTransactionsFromCsv(csvFile);

                SpreadsheetItem selectedSpreadsheet = spreadsheetComboBox.getSelectionModel().getSelectedItem();
                if (selectedSpreadsheet == null) {
                    Platform.runLater(() -> messageLabel.setText("Please select a spreadsheet."));
                    return;
                }
                String spreadsheetId = selectedSpreadsheet.getId();

                // Step 3: Write data
                writeTransactionsToSheet(spreadsheetId, transactions);

                Platform.runLater(() -> messageLabel.setText(
                        "CSV data written to selected spreadsheet starting at " + TARGET_RANGE
                ));
            } catch (Exception e) {
                Platform.runLater(() -> messageLabel.setText("Error: " + e.getMessage()));
                e.printStackTrace();
            }
        }).start();
    }

    private void populateSpreadsheetComboBox() {
        new Thread(() -> {
            try {
                Drive driveService = GoogleSheetsUtil.getDriveService();

                String query = "mimeType='application/vnd.google-apps.spreadsheet' and trashed = false";

                FileList result = driveService.files().list()
                        .setQ(query)
                        .setPageSize(20)
                        .setFields("files(id, name)")
                        .execute();

                List<com.google.api.services.drive.model.File> files = result.getFiles();

                System.out.println("Loading spreadsheets...");
                if (files == null || files.isEmpty()) {
                    System.out.println("No spreadsheets found.");
                } else {
                    for (var file : files) {
                        System.out.println("Found: " + file.getName() + " (" + file.getId() + ")");
                    }
                }

                if (files == null || files.isEmpty()) {
                    Platform.runLater(() -> {
                        messageLabel.setText("No spreadsheets found in your Drive.");
                        spreadsheetComboBox.getItems().clear();
                    });
                    return;
                }

                List<SpreadsheetItem> items = new ArrayList<>();
                for (com.google.api.services.drive.model.File file : files) {
                    items.add(new SpreadsheetItem(file.getId(), file.getName()));
                }

                Platform.runLater(() -> {
                    spreadsheetComboBox.getItems().setAll(items);
                    if (!items.isEmpty()) {
                        spreadsheetComboBox.getSelectionModel().selectFirst();
                    }
                    messageLabel.setText("Select a spreadsheet from the dropdown.");
                });

            } catch (Exception e) {
                Platform.runLater(() -> messageLabel.setText("Error loading spreadsheets: " + e.getMessage()));
                e.printStackTrace();
            }
        }).start();
    }

    @FXML
    private void initialize() {
        populateSpreadsheetComboBox();

        browseFileButton.setOnAction(event -> {
            java.io.File csvFile = chooseCsvFile();
            handleFileUpload(csvFile);
        });
    }

    private java.io.File chooseCsvFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select CSV File");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
        return fileChooser.showOpenDialog(browseFileButton.getScene().getWindow());
    }
}
