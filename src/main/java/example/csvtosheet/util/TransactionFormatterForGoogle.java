package example.csvtosheet.util;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import example.csvtosheet.model.Transaction;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TransactionFormatterForGoogle {

    public static List<List<Object>> convertTransactionsToSheetRows(List<Transaction> transactions) {
        List<List<Object>> rows = new ArrayList<>();

        for (Transaction tx : transactions) {
            rows.add(List.of(
                    tx.getDate().toString(),
                    tx.getDescription(),
                    tx.getAmount().toString()
            ));
        }

        return rows;
    }





}
