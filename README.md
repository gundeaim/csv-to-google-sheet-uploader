# CSV to Google Sheets Uploader

CSV to Google Sheets Uploader is a JavaFX desktop application that automates transferring CSV transaction data into Google Sheets. The project is structured 
using the Model-View-Controller (MVC) architecture, with clear separation of concerns: model for data structures, controller for UI logic, and service for API 
integration. It integrates with the Google Sheets and Drive APIs to streamline data auditing and workflow automation, featuring a responsive GUI, secure
handling of API credentials, and automated formatting of CSV rows. This project demonstrates skills in Java development, API integration, GUI design, 
software architecture, and real-world data automation.

---
**Features**

- **Simple GUI:** Select a CSV file and upload directly to Google Sheets.  
- **Google Sheets Integration:** Automatically writes data to a target spreadsheet using the Google Sheets API.  
- **Error Handling:** Displays clear success or error messages in the UI.  
- **Data Formatting:** Converts CSV rows into properly structured Google Sheet rows.  
- **Responsive:** Uses background threads to prevent UI freezing during uploads.  

---
**Tech Stack**
- Java 17+  
- JavaFX for GUI  
- Google Sheets API & Google Drive API  
- OpenCSV for CSV parsing  
- Maven for project management  
- IntelliJ IDEA for development  

---

## 💡 How It Works

1. User selects a CSV file using the GUI.  
2. Application reads and parses the CSV file.  
3. Data is formatted into Google Sheets-compatible rows.  
4. Selected Google Sheet is updated starting at a target range.  
5. Success or error messages are displayed in the interface.  

---

> **Note:** `credentials.json` contains your Google API credentials and is **excluded from Git** for security.

