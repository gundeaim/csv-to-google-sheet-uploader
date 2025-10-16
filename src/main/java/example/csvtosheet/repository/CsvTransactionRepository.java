package example.csvtosheet.repository;

import com.opencsv.CSVReader;
import example.csvtosheet.model.Transaction;

import java.io.FileReader;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class CsvTransactionRepository {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("M/d/yyyy");

    /**
     * Reads transactions from a CSV file exported by the bank.
     *
     * @param csvFilePath Path to CSV file
     * @return List of Transactions
     * @throws Exception on file/parse errors
     */

    public List<Transaction> readTransactions(String csvFilePath) throws Exception {
        List<Transaction> transactions = new ArrayList<>();

        try(CSVReader reader = new CSVReader(new FileReader(csvFilePath))){
            String[] line;

            reader.readNext();
            while ((line = reader.readNext()) != null){
                if (line.length < 5) continue;

                LocalDate date = LocalDate.parse(line[0].replace("\"", ""), FORMATTER);

                String description = line[2].replace("\"", "").trim();

                String debitStr = line[3].replace("\"", "").trim();
                String creditStr = line[4].replace("\"", "").trim();

                BigDecimal amount;
                if(!debitStr.isEmpty()){
                    amount = new BigDecimal(debitStr);
                } else if (!creditStr.isEmpty()){
                    amount = new BigDecimal(creditStr);
                } else{
                    continue;
                }

                Transaction transaction = new Transaction(date, description, amount);
                transactions.add(transaction);
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to read transactions from CSV: " + e.getMessage(), e);
        }

        return transactions;
    }
}
