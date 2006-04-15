/**
 * Copyright (c) 2004-2005 jManage.org
 *
 * This is a free software; you can redistribute it and/or
 * modify it under the terms of the license at
 * http://www.jmanage.org.
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jmanage.connector.plugin.oracle;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Map;
import java.util.Properties;

import org.jmanage.connector.framework.ConnectorSupport;

/**
 * @author Tak-Sang Chan
 *
 */
public class Session extends ConnectorSupport {

    /*
    private static String SQL_SESSION = "select LOGON_TIME, USERNAME, " +
            "MACHINE, Server, PROGRAM, OSUSER, SID, STATUS " +
            "from V$SESSION s " +
            "where (s.username is not null) and (s.osuser is not null) " +
            "and s.osuser <> 'SYSTEM' and (s.type <> 'BACKGROUND')";
    */
    
    private static String SQL_USER_SESSION = "select LOGON_TIME, USERNAME, " +
        "MACHINE, Server, PROGRAM, OSUSER, SID, STATUS " +
        "from V$SESSION s " +
        "where s.username=? " +
        "order by sid";
    
    private static String SQL_ACTIVE_COUNT = "select count(*) from " +
        "V$SESSION where username=?";
    
    private final static SimpleDateFormat sdf;

    static {
        sdf = new SimpleDateFormat("MM/dd/yyyy hh:mm a");
    }

    private int sessionCount;
    
    private String driver = "";
    private String logonUser = "";
    private String password = "";
    private String url = "";
    private boolean dba = false;

    private Connection conn;
    private String logonTime="";
    private String userName="";
    private String machine="";
    private String server="";
    private String program="";
    private String osUser="";
    private String sid="";
    private String status="";

    public int getSessionCount() throws Exception {
        return sessionCount;
    }
    
    private int getSessionCount(String name) throws Exception {
        PreparedStatement stmt = null;
        ResultSet rs = null;

        openConnection();
        
        stmt = conn.prepareStatement(SQL_ACTIVE_COUNT);
        stmt.setString(1, name.toUpperCase());
        rs = stmt.executeQuery();
        
        if (rs.next()) {
            return rs.getInt(1);
        }
        
        return 0;
    }
    
    public String retrieveUserSession(String name) throws Exception {
        PreparedStatement stmt = null;
        ResultSet rs = null;

        if (name == null) {
            return "Missing input userName";
        }
        
        reset();
        
        sessionCount = getSessionCount(name);        
        if (sessionCount == 0) {
            return "Session not found for the userName specified";
        }
        
        openConnection();        
        stmt = conn.prepareStatement(SQL_USER_SESSION);
        stmt.setString(1, name.toUpperCase());
        rs = stmt.executeQuery();
        
        int c = 0;
        while (rs.next()) {
            logonTime = logonTime + " | " + sdf.format(rs.getDate(1));
            userName = rs.getString(2);
            machine = machine + " | " + rs.getString(3);
            server = server + " | " + rs.getString(4);
            program = program + " | " + rs.getString(5);
            osUser = osUser + " | " + rs.getString(6);
            sid = sid + " | " + rs.getString(7);
            status = status + " | " + rs.getString(8);
            c++;
        } 
        
        return "Done";
    }

    public String openConnection() {
        if (this.conn != null) {
            return "Already connected to database";
        }
        else {
            this.conn = getDBConnection();
            if (this.conn == null) {
                throw new RuntimeException("Failed to open database connection.");
            }
            else {
                return "Connected to database successfully.";
            }
        }
    }

    public String closeConnection() throws SQLException {
        if (this.conn != null) {
            this.conn.close();
            this.url = null;
            return "Database connection closed.";
        }
        else {
            return "Database connection already closed.";
        }
    }

    public void initialize(Map configParams) {
        this.driver = (String) configParams.get("Driver");
        this.logonUser = (String) configParams.get("Username");
        this.password = (String) configParams.get("Password");
        this.url = (String) configParams.get("URL");
        
        String dba = (String) configParams.get("DBA");
        if (dba.equalsIgnoreCase("yes") || dba.equalsIgnoreCase("y")) {
            this.dba = true;
        }        
    }

    private Connection getDBConnection() {
        try {
            Class.forName(getDriver());
            Properties info = new Properties();
            info.put("user", getLogonUser());
            info.put("password", this.password);
            
            if(dba) {
                info.put("internal_logon", "sysdba");
            }
            
            Connection conn = DriverManager.getConnection(url, info);
            return conn;
        }
        catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }
    }

    private void reset() {
        logonTime="";
        userName="";
        machine="";
        server="";
        program="";
        osUser="";
        sid="";
        status="";        
    }
    
    public String getLogonTime() {
        return logonTime;
    }

    public String getUserName() {
        return userName;
    }

    public String getMachine() {
        return machine;
    }

    public String getServer() {
        return server;
    }

    public String getProgram() {
        return program;
    }

    public String getOsUser() {
        return osUser;
    }

    public String getSid() {
        return sid;
    }

    public String getStatus() {
        return this.status;
    }

    public String getDriver() {
        return driver;
    }

    public String getLogonUser() {
        return logonUser;
    }

    public String getUrl() {
        return url;
    }

    public String getDBA() {
        return dba ? "Yes" : "No";
    }    
}
