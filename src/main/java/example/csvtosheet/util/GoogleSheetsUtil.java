package example.csvtosheet.util;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.SheetsScopes;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;


/*
Learning: Static methods belong to the class and not an object of the class.
Called by GoogleSheetsUtil.staticMethodName.
benefits memory loaded once on class creation. Can't use this or super.

Credential data type: From Google OAuth Client Library
Represents the users auth info like accessToken and refreshToken.
Allows all API calls we make to be authenticated by the google server

GoogleSheetsUtil.class refers to the class file as a reference point to accesses the resources of this file.
.getResourceAsStream: Method to look for the file give as a parameter returns an input stream

 */


public class GoogleSheetsUtil {

    private static final String APPLICATION_NAME = "AfcuTransactionsUploader";
    private static final GsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
    private static final String CREDENTIALS_FILE_PATH = "/credentials.json";
    private static final List<String> SCOPES = Arrays.asList(
            SheetsScopes.SPREADSHEETS, DriveScopes.DRIVE_METADATA_READONLY
    );


    private static Credential authorize() throws Exception{
        InputStream in = GoogleSheetsUtil.class.getResourceAsStream(CREDENTIALS_FILE_PATH);
        if (in == null){
            throw new RuntimeException("Resource not found: " + CREDENTIALS_FILE_PATH);
        }

        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                GoogleNetHttpTransport.newTrustedTransport(),
                JSON_FACTORY,
                GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in)),
                SCOPES)
                .setAccessType("offline")
                .build();

        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();

        return new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
    }

    public static Sheets getSheetsService() throws Exception{
        Credential credential = authorize();

        return new Sheets.Builder(
                GoogleNetHttpTransport.newTrustedTransport(),
                JSON_FACTORY,
                credential)
                .setApplicationName(APPLICATION_NAME)
                .build();
    }

    //Allows user to pick a spreadsheet
    public static Drive getDriveService() throws Exception{
        Credential credential = authorize();

        return new Drive.Builder(
                GoogleNetHttpTransport.newTrustedTransport(),
                JSON_FACTORY,
                credential)
                .setApplicationName(APPLICATION_NAME)
                .build();
    }


}
