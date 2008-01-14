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
package org.jmanage.monitoring.downtime;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.EventObject;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.jmanage.core.config.ApplicationConfig;
import org.jmanage.core.config.ApplicationConfigManager;
import org.jmanage.core.config.event.ApplicationEvent;
import org.jmanage.core.config.event.NewApplicationEvent;
import org.jmanage.core.util.Loggers;
import org.jmanage.event.EventListener;
import org.jmanage.monitoring.downtime.event.ApplicationDownEvent;
import org.jmanage.monitoring.downtime.event.ApplicationUpEvent;
import org.jmanage.util.db.DBUtils;

/**
 * Records application downtime in the database.
 * 
 * TODO: This reads all applications from the DB. This should be changed to just read the
 * current applications - rk
 * 
 * 
 * @author Rakesh Kalra
 */
public class DowntimeRecorder implements EventListener {

    private static final Logger logger = Loggers.getLogger(DowntimeRecorder.class);
    
    private final Map<ApplicationConfig, ApplicationDowntimeHistory> downtimesMap = 
        new HashMap<ApplicationConfig, ApplicationDowntimeHistory>();
    
    private static final DowntimeRecorder recorder = new DowntimeRecorder();
    
    static DowntimeRecorder getInstance(){
        return recorder;
    }
    
    private DowntimeRecorder(){
        // load applications from DB
        initDowntimeMapFromDB();
        // now add new applications to the DB
        final long recordingSince = System.currentTimeMillis();
        for (ApplicationConfig appConfig : ApplicationConfigManager.getAllApplications()) {
            if(!downtimesMap.containsKey(appConfig)){
                addApplicationToDB(appConfig.getApplicationId(), recordingSince);
                downtimesMap.put(appConfig, new ApplicationDowntimeHistory(recordingSince));    
            }
        }
    }
    
    public boolean isApplicationUp(ApplicationConfig appConfig){
        if(appConfig == null){
            throw new NullPointerException("missing appConfig");
        }
        ApplicationDowntimeHistory history = getDowntimeHistory(appConfig);
        assert history != null;
        return history.isApplicationUp();
    }
    
    public ApplicationDowntimeHistory getDowntimeHistory(ApplicationConfig appConfig){
        if(appConfig == null){
            throw new NullPointerException("appConfig must be specified");
        }
        ApplicationDowntimeHistory history = downtimesMap.get(appConfig);
        assert history != null;
        return history;
    }

    private void addApplication(NewApplicationEvent event) {
        ApplicationConfig appConfig = event.getApplicationConfig();
        if(logger.isLoggable(Level.INFO)){
            logger.info("Added application " + appConfig.getName() + " to the downtimesMap.");
        }
        final long recordingSince = event.getTime();
        addApplicationToDB(appConfig.getApplicationId(), recordingSince);
        downtimesMap.put(appConfig, new ApplicationDowntimeHistory(recordingSince));
    }

    public void handleEvent(EventObject event) {
        if(!(event instanceof ApplicationEvent)){
            throw new IllegalArgumentException("event must of type ApplicationEvent");
        }
        // Handle new application event
        if(event instanceof NewApplicationEvent){
            addApplication((NewApplicationEvent)event);
            return;
        }
        // Note that application removed event is not being handled. We probably don't care about
        //   the downtime of a removed application.
        
        ApplicationEvent appEvent = (ApplicationEvent)event;
        ApplicationDowntimeHistory downtimeHistory = getDowntimeHistory(appEvent.getApplicationConfig());
        assert downtimeHistory != null;
        if(appEvent instanceof ApplicationUpEvent){
            // application must have went down earlier
            assert downtimeHistory.getDowntimeBegin() != null;
            // log the downtime to the db
            recordDowntime(appEvent.getApplicationConfig().getApplicationId(), 
                    downtimeHistory.getDowntimeBegin(), appEvent.getTime());
            downtimeHistory.applicationCameUp(appEvent.getTime());
        }else if(event instanceof ApplicationDownEvent){
            downtimeHistory.applicationWentDown(appEvent.getTime());
        }
    }

    public double getUnavailablePercentage(ApplicationConfig appConfig) {
        final ApplicationDowntimeHistory history = getDowntimeHistory(appConfig);
        return history.getUnavailablePercentage();
    }
    
    private void addApplicationToDB(String applicationId, long recordingSince){
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rset = null;
        try{
            conn = DBUtils.getConnection();
            stmt = conn.prepareStatement("INSERT INTO t_application_downtime(application_id, " +
                    "recording_start) VALUES (?, ?)");
            stmt.setString(1, applicationId);
            stmt.setTimestamp(2, new Timestamp(recordingSince));
            int result = stmt.executeUpdate();
            assert result == 1;
            conn.commit();
        }catch(SQLException e){    
            throw new RuntimeException(e);
        }finally{
            DBUtils.close(rset);
            DBUtils.close(stmt);
            DBUtils.close(conn);
        }
    }
    
    private void recordDowntime(String applicationId, long downtimeBegin, long downtimeEnd){
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rset = null;
        try{
            conn = DBUtils.getConnection();
            stmt = conn.prepareStatement("INSERT INTO t_application_downtime_history(application_id, " +
                    "start_time, end_time) VALUES (?, ?, ?)");
            stmt.setString(1, applicationId);
            stmt.setTimestamp(2, new Timestamp(downtimeBegin));
            stmt.setTimestamp(3, new Timestamp(downtimeEnd));
            int result = stmt.executeUpdate();
            assert result == 1;
            conn.commit();
        }catch(SQLException e){    
            throw new RuntimeException(e);
        }finally{
            DBUtils.close(rset);
            DBUtils.close(stmt);
            DBUtils.close(conn);
        }
    }

    private void initDowntimeMapFromDB(){
        Connection conn = null;
        Statement stmt = null;
        ResultSet rset = null;
        try{
            conn = DBUtils.getConnection();
            stmt = conn.createStatement();
            rset = stmt.executeQuery("SELECT application_id, recording_start " +
                    "FROM t_application_downtime" +
                    " WHERE recording_end is null");
            while(rset.next()){
                String applicationId = rset.getString(1);
                long recordingSince = rset.getTimestamp(2).getTime();
                if(logger.isLoggable(Level.FINE)){
                    logger.fine("ApplicationId: " + applicationId + ", recordingSince: " + 
                            recordingSince);
                }
                ApplicationConfig appConfig = 
                    ApplicationConfigManager.getApplicationConfig(applicationId);
                if(appConfig == null){
                    logger.log(Level.INFO, "Application with Id {0} no longer exists",  applicationId);
                    continue;
                }
                ApplicationDowntimeHistory history = new ApplicationDowntimeHistory(recordingSince);
                history.setTotalDowntime(getTotalDowntime(conn, applicationId));
                downtimesMap.put(appConfig, history);
            }
        }catch(SQLException e){    
            throw new RuntimeException(e);
        }finally{
            DBUtils.close(rset);
            DBUtils.close(stmt);
            DBUtils.close(conn);
        }
    }

    
    private long getTotalDowntime(Connection conn, String applicationId) {
        PreparedStatement stmt = null;
        ResultSet rset = null;
        try{
            stmt = conn.prepareStatement("SELECT start_time, end_time" +
                    " FROM t_application_downtime_history" +
                    " WHERE application_id =?");
            stmt.setString(1, applicationId);
            rset = stmt.executeQuery();
            long totalDowntime = 0;
            while(rset.next()){
                Timestamp startTime = rset.getTimestamp(1);
                Timestamp endTime = rset.getTimestamp(2);
                if(endTime == null){
                    //TODO: we don't take into account the time when jmanage didn't do any recording
                    //    this should be recorded as different time.
                    logger.warning("No end time found for applicationId:" + applicationId 
                            + " startTime:" + startTime 
                            + ". This record has been ignored");
                    continue;
                }
                totalDowntime += (endTime.getTime() - startTime.getTime());
            }
            return totalDowntime;
        }catch(SQLException e){    
            throw new RuntimeException(e);
        }finally{
            DBUtils.close(rset);
            DBUtils.close(stmt);
        }
    }
}
