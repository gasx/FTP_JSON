package interfaces;

import org.apache.commons.net.ftp.FTPClient;
import org.json.simple.JSONObject;

public interface FTP {

    FTPClient ftpClient = null;
    String fileName = null;

    String host = null;
    String login = null;
    String password = null;

    public JSONObject getJSON();
    public boolean Upload(JSONObject jsonObject);

}
