package methods;

import org.apache.commons.net.ftp.FTPClient;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FTP implements interfaces.FTP {

    /**
     *  Class for working with FTPClient.
     */

    FTPClient ftpClient;
    String fileName;

    String host;
    String login;
    String password;


    // Constructor
    public FTP(String host, String login, String password, String fileName) {
        this.ftpClient = new FTPClient();
        this.fileName = fileName;

        this.host = host;
        this.login = login;
        this.password = password;
    }

    // Function for open connect on FTP server.
    private void open() {
        try {
            this.ftpClient.connect(this.host);
            this.ftpClient.enterLocalPassiveMode();
            this.ftpClient.login(this.login, this.password);
        } catch(IOException e) {
            Logger.getAnonymousLogger().log(Level.WARNING, "Не удалось подключиться к FTP серверу:");
            e.printStackTrace();
            System.exit(-1);
        }
    }

    // Function for close connect from FTP server.
    private void close() {
        try {
            this.ftpClient.logout();
            this.ftpClient.disconnect();
        } catch(IOException e) {
            Logger.getAnonymousLogger().log(Level.WARNING, "Не удалось завершить FTP-соединение:");
            e.printStackTrace();
            System.exit(-1);
        }
    }

    // Function for get decoded JSON from server file.
    public JSONObject getJSON(){
        // Read file (without download from FTP server) - https://stackoverflow.com/a/2834853
        BufferedReader reader = null;
        String jsonString = "";

        try {
            this.open(); // Open FTP connect..
            InputStream stream = this.ftpClient.retrieveFileStream(this.fileName);
            this.close(); // Close FTP connect..
            if(stream == null) {
                return null;
            }

            reader = new BufferedReader(new InputStreamReader(stream, "UTF-8"));
            while(reader.ready()) { // Read all lines from file...
                jsonString += reader.readLine() + " ";
            }
            stream.close();
        } catch (IOException ignored) {
            return null;
        } finally {
            if (reader != null) try { reader.close(); } catch (IOException ignored) {}
        }

        // if file is not null try to decode json...
        if(!jsonString.equals("")) {
            JSONParser parser = new JSONParser();

            // try decode json
            Object obj;
            try {
                obj = parser.parse(jsonString);
            } catch(ParseException ignored) {
                return null;
            }

            return (JSONObject) obj;
        }

        // or if file is null...
        return null;
    }

    // Function for upload in ftp file new json object.
    public boolean Upload(JSONObject jsonObject) {
        String jsonString = jsonObject.toJSONString();
        byte[] jsonBytes = jsonString.getBytes();
        InputStream is = new ByteArrayInputStream(jsonBytes); // encode json object - https://stackoverflow.com/a/23816624

        try {
            this.open(); // Open FTP connection.

            boolean ok = this.ftpClient.storeFile(this.fileName, is);

            is.close(); // Close input stream.
            this.close(); // Close FTP connection.

            return ok;
        } catch(IOException ignored) {
            return false; // if it has exception - return false...
        }
    }
}
