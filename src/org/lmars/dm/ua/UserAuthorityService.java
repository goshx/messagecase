package org.lmars.dm.ua;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.ResultSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.Map;

import org.lmars.dm.storage.DriverFactory;
import org.lmars.dm.test.BaseDriver;
import org.lmars.dm.util.TaskBuilder;

public class UserAuthorityService implements TaskBuilder{
    
    //private static final BaseDriver d = DriverFactory.helper(DriverFactory.Type.phoenix);

    public UserAuthorityInfo login(String urlStr, String data) {

        String result = "";
        UserAuthorityInfo info = new UserAuthorityInfo();
        info.status = true;
        info.message = "status = 0";
        return info;

/*        HttpURLConnection conn = null;
        BufferedReader br = null;
        try {
            URL url = new URL(urlStr);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setRequestProperty("Content-Type", "application/json");

            conn.connect();
            if (data != null && data.length() > 0) {
                PrintWriter pw = new PrintWriter(conn.getOutputStream());
                pw.write(data);
                pw.flush();
                pw.close();
            }
            br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
            if (br != null) {
                String line = "";
                while ((line = br.readLine()) != null) {
                    result += line + "\n";
                }
            }
            if (conn.getResponseCode() == 200 || conn.getResponseCode() == 202) {
                conn.disconnect();
                if (br != null) {
                    br.close();
                }
                return info.set(true).set(result);
            } else {
                conn.disconnect();
                if (br != null) {
                    br.close();
                }
                return info.set(false).set(result);
            }

        } catch (IOException e) {
            //e.printStackTrace();
            return info.set(false).set(e.getMessage());
        }*/
    }
    
    public Boolean isValid() {
        return false;
    }

}
