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

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Logger;

import org.jmanage.core.util.CoreUtils;
import org.jmanage.core.util.Loggers;

/** 
 * Contains utility functions for reading/writing to the DB
 * 
 * @author Rakesh Kalra
 */
public class DBUtils {
    
    private static final Logger logger = Loggers.getLogger(DBUtils.class);
    
    private static final String URL = "jdbc:hsqldb:file:" + CoreUtils.getDataDir() + "/db";

    static{
        logger.info("DataBase URL: " + URL);
        try {
            Class.forName("org.hsqldb.jdbcDriver");
        } catch (Exception e) {
            throw new RuntimeException("HSQLDB Driver not found", e);
        }
    }


    public static Connection getConnection(){
        try {
            return DriverManager.getConnection(URL,"sa","");
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
}
