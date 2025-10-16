package com.example.CsvTransactionRepositoryTest.java;
import example.csvtosheet.model.Transaction;
import example.csvtosheet.repository.CsvTransactionRepository;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import java.io.*;
import java.math.BigDecimal;
import java.nio.file.*;
import java.time.LocalDate;
import java.util.List;

public class CsvTransactionRepositoryTest {

    @Test
    public void testLoadTransactions_parsesCsvCorrectly() throws Exception {
        // Arrange: create a temporary CSV file
        Path tempFile = Files.createTempFile("transactions", ".csv");
        Files.write(tempFile, List.of(
                "\"Date\",\"No.\",\"Description\",\"Debit\",\"Credit\"",
                "\"7/21/2025\",\"\",\"PENDING - KOINO POKE\",\"24.06\",\"\"",
                "\"7/21/2025\",\"\",\"VISA - COSTCO\",\"-102.74\",\"\"",
                "\"7/21/2025\",\"\",\"FUNDS TRANSFER FROM SAVINGS\",\"\",\"100\""
        ));

        CsvTransactionRepository repository = new CsvTransactionRepository();

        // Act
        List<Transaction> transactions = repository.readTransactions(tempFile.toString());

        // Assert
        assertEquals(3, transactions.size());

        Transaction t1 = transactions.get(0);
        assertEquals(LocalDate.of(2025, 7, 21), t1.getDate());
        assertEquals("PENDING - KOINO POKE", t1.getDescription());
        assertEquals(new BigDecimal("24.06"), t1.getAmount());

        Transaction t2 = transactions.get(1);
        assertEquals("VISA - COSTCO", t2.getDescription());
        assertEquals(new BigDecimal("-102.74"), t2.getAmount());

        Transaction t3 = transactions.get(2);
        assertEquals("FUNDS TRANSFER FROM SAVINGS", t3.getDescription());
        assertEquals(new BigDecimal("100"), t3.getAmount());

        // Cleanup
        Files.deleteIfExists(tempFile);
    }
}