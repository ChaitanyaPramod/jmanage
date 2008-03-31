/**
 * Copyright 2004-2006 jManage.org
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jmanage.util.db;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Logger;

import org.jmanage.core.util.CoreUtils;
import org.jmanage.core.util.JManageProperties;
import org.jmanage.core.util.Loggers;

/** 
 * Contains utility functions for reading/writing to the DB
 * 
 * @author Rakesh Kalra, Shashank Bellary
 */
public class DBUtils {
    
    private static final Logger logger = Loggers.getLogger(DBUtils.class);
    
    private static final String HSQLDB_DRIVER_CLASS = "org.hsqldb.jdbcDriver";
    private static final String DRIVER_CLASS;
    private static final String URL;
    private static final String USERNAME;
    private static final String PASSWORD;

    static{
    	if(JManageProperties.getDatabaseDriverClass() != null){
    		DRIVER_CLASS = JManageProperties.getDatabaseDriverClass();
    	}else{
    		DRIVER_CLASS = HSQLDB_DRIVER_CLASS;
    	}
    	if(JManageProperties.getDatabaseURL() != null){
    		URL = JManageProperties.getDatabaseURL();
    	}else{
    		URL = "jdbc:hsqldb:file:" + CoreUtils.getDataDir() + "/db";
    	}
    	if(JManageProperties.getDatabaseUsername() != null){
    		USERNAME = JManageProperties.getDatabaseUsername();
    	}else{
    		USERNAME = "sa";
    	}
    	if(JManageProperties.getDatabasePassword() != null){
    		PASSWORD = JManageProperties.getDatabasePassword();
    	}else{
    		PASSWORD = "";
    	}
    	logger.info("DataBase driver class: " + DRIVER_CLASS  + ", URL: " + URL);
        try {
            Class.forName(DRIVER_CLASS);
        } catch (Exception e) {
            throw new RuntimeException("HSQLDB Driver not found", e);
        }
    }

    /**
     * TODO: we need to use a connection pool here -- rk
     * @return
     */
    public static Connection getConnection(){
        try {
            Connection connection = DriverManager.getConnection(URL,USERNAME,PASSWORD);
            connection.setAutoCommit(false);
            return connection;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    
    public static void close(Connection conn){
        if(conn != null){
            try {
                conn.close();
            } catch (SQLException e) {
                logger.warning("Error closing connection: " + e.getMessage());
            }
        }
    }
    
    public static void close(Statement statement){
        if(statement != null){
            try {
                statement.close();
            } catch (SQLException e) {
                logger.warning("Error closing statement: " + e.getMessage());
            }
        }
    }
    
    public static void close(ResultSet rset){
        if(rset != null){
            try {
                rset.close();
            } catch (SQLException e) {
                logger.warning("Error closing resultset: " + e.getMessage());
            }
        }
    }
    
    public static void createTables(){
        Connection connection = null;
        Statement statement = null;
        try{
            connection = getConnection();
            statement = connection.createStatement();
            /* T_APPLICATION_DOWNTIME */
            statement.executeUpdate(
                    "CREATE CACHED TABLE T_APPLICATION_DOWNTIME " +
                        "(APPLICATION_ID VARCHAR NOT NULL PRIMARY KEY, " +
                        "RECORDING_START TIMESTAMP NOT NULL, " +
                        "RECORDING_END TIMESTAMP)");
            /* T_APPLICATION_DOWNTIME_HISTORY */
            statement.executeUpdate(
                    "CREATE CACHED TABLE T_APPLICATION_DOWNTIME_HISTORY " +
                        "(APPLICATION_ID VARCHAR NOT NULL, " +
                        "START_TIME TIMESTAMP NOT NULL, " +
                        "END_TIME TIMESTAMP NOT NULL)");
            // TODO: New tables in war version -- will need a upgrade script for existing installtion
            /* MBEAN_ATTRIBUTE */
            statement.executeUpdate(
                    "CREATE CACHED TABLE MBEAN_ATTRIBUTE " +
                        "(ID INT NOT NULL," +
                        "APPLICATION_ID VARCHAR NOT NULL, " +
                        "MBEAN_NAME VARCHAR NOT NULL, " +
                        "ATTRIBUTE_NAME VARCHAR NOT NULL)");
            /* MBEAN_ATTRIBUTE_VALUE */
            statement.executeUpdate(
                    "CREATE CACHED TABLE MBEAN_ATTRIBUTE_VALUE " +
                        "(WHEN_COLLECTED TIMESTAMP NOT NULL," +
                        "MBEAN_ATTRIBUTE_ID INT NOT NULL, " +
                        "VALUE VARCHAR NOT NULL)");
            statement.execute("SET WRITE_DELAY FALSE");
            // TODO: Add FK from history to downtime
            //connection.commit();
        }catch(SQLException e){
            throw new RuntimeException(e);
        }finally{
            close(statement);
            close(connection);
        }
    }
    
    public static void shutdownDB(){
        logger.info("Shutting down HSQLDB");
        Connection connection = null;
        Statement statement = null;
        try{
            connection = getConnection();
            statement = connection.createStatement();
            statement.execute("SHUTDOWN COMPACT");
        }catch(SQLException e){
            throw new RuntimeException(e);
        }finally{
            close(statement);
            close(connection);
        }
    }
    
    public static void dropTables(){
        Connection connection = null;
        Statement statement = null;
        try{
            connection = getConnection();
            statement = connection.createStatement();
            /* T_APPLICATION_DOWNTIME_HISTORY */
            statement.executeUpdate(
                    "DROP TABLE T_APPLICATION_DOWNTIME_HISTORY");
            /* T_APPLICATION_DOWNTIME */
            statement.executeUpdate(
                    "DROP TABLE T_APPLICATION_DOWNTIME");
            
        }catch(SQLException e){
            throw new RuntimeException(e);
        }finally{
            close(statement);
            close(connection);
        }
    }
    
    public static void main(String[] args){
        createTables();
        dropTables();
    }
    
    public static void init() {
    	if(HSQLDB_DRIVER_CLASS.equals(DRIVER_CLASS)){
    		logger.info("Using inbuilt HSQLDB for data storage");
    		String dataDir = CoreUtils.getDataDir();
            assert dataDir != null;     
            /* create db tables if they don't exist */
            File dbFile = new File(dataDir+"/db.properties");
            if(!dbFile.exists()){
                logger.info("Creating HSQLDB tables under folder " + dataDir);
                DBUtils.createTables();
            }else{
                /* if lock file was left around -- try to delete it */
                File dbLockFile = new File(dataDir + "/db.lck");
                if(dbLockFile.exists()){
                    logger.warning("DB lock file exists. Trying to delete.");
                    dbLockFile.delete();
                }
            }	
    	}
    }
}
