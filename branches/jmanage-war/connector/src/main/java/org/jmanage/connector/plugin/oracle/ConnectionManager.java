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

import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Map;
import java.util.Properties;
import java.sql.Connection;

import org.jmanage.connector.framework.ConnectorSupport;

/**
 * @author Tak-Sang Chan
 * Apr 29, 2006
 */
public class ConnectionManager extends ConnectorSupport {

    private String driver;
    private String logonUser;
    private String password;
    private String url;
    private boolean dba = false; 
    
    /* 
     * TODO: It keeps a single connection. 
     * What if multiple requests occur to this MBean at the same time - rk
     */
    private Connection conn;    
  
    private static ConnectionManager instance;
    
    
    /**
     * The connector framework uses the getInstance method to create
     * the MBean object when the method is defined.  Otherwise, the
     * object is created with the noarg constructor.
     * 
     * @return The instance of the ConnectorManager.
     */
    public static ConnectionManager getInstance() {
        if (instance == null) {
            instance = new ConnectionManager();
        }
        return instance;
    }

    public String openConnection() {
        if (conn != null) {
            return "Already connected to database";
        }
        else {
            conn = getDBConnection();
            if (conn == null) {
                return "Failed to open database connection.";
            }
            else {
                return "Connected to database successfully.";
            }
        }
    }

    public String closeConnection() throws SQLException {
        if (conn != null) {
            conn.close();
            conn = null;
            return "Database connection closed.";
        }
        else {
            return "Database connection already closed.";
        }
    }
    
    public Connection getConnection() {
        if (conn == null) {
            openConnection();
        }
        return conn;
    }
    
    private Connection getDBConnection() {
        try {            
            Class.forName(getDriver());
            Properties info = new Properties();
            info.put("user", getLogonUser());
            info.put("password", password);
            
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

    public boolean isDBA() {
        return dba;
    }

    public String getDriver() {
        return driver;
    }

    public String getLogonUser() {
        return logonUser;
    }

    public String getPassword() {
        return password;
    }

    public String getUrl() {
        return url;
    }

    public boolean isConnected() {
        return conn == null ? false : true;
    }   
    
    public void initialize(Map configParams) {
        driver = (String) configParams.get("Driver");
        logonUser = (String) configParams.get("Username");
        password = (String) configParams.get("Password");
        url = (String) configParams.get("URL");
        
        String dba = (String) configParams.get("DBA");
        if (dba.equalsIgnoreCase("yes") || dba.equalsIgnoreCase("y")) {
            this.dba = true;
        }        
    }    
}
