package example.csvtosheet.service;


import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.ValueRange;
import example.csvtosheet.util.GoogleSheetsUtil;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;

public class GoogleSheetsWriter {

    public void writeDataToSheet(String spreadsheetId, String range, List<List<Object>> values) throws Exception, IOException, GeneralSecurityException {
        Sheets service = GoogleSheetsUtil.getSheetsService();

        ValueRange body = new ValueRange();
        body.setValues(values);

        Sheets.Spreadsheets.Values.Update request =
                service.spreadsheets().values()
                        .update(spreadsheetId, range, body)
                        .setValueInputOption("USER_ENTERED");

        request.execute();

    }
}
