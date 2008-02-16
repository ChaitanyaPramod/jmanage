/**
 * jManage - Open Source Application Management
 * Copyright (C) 2006 jManage.org.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 2 as
 * published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License version 2 for more details.
 */
package org.jmanage.connector.plugin.oracle.mbean;

import java.sql.ResultSet;
import java.util.logging.Logger;

import javax.management.openmbean.CompositeData;
import javax.management.openmbean.TabularData;

import org.jmanage.connector.plugin.oracle.DataAccessor;
import org.jmanage.core.util.Loggers;

/**
 * @author Tak-Sang Chan
 * @author Rakesh Kalra
 */
public class Database {

    private static final Logger logger = Loggers.getLogger(Database.class);

    private final static String INIT_SQL =
            "select db.dbid, db.name, inst.instance_name, inst.host_name, "
                    + "to_char(db.created, 'MM/DD/YYYY HH:MM:SS AM') created_time, "
                    + "db.log_mode, db.open_mode, " + "inst.version, "
                    + "to_char(inst.startup_time, 'MM/DD/YYYY HH:MM:SS AM') startup_time, "
                    + "floor(sysdate - startup_time) || ' days(s) ' || "
                    + "trunc( 24*((sysdate-startup_time) - "
                    + "trunc(sysdate-startup_time))) || ' hour(s) ' || "
                    + "mod(trunc(1440*((sysdate-startup_time) - "
                    + "trunc(sysdate-startup_time))), 60) ||' minute(s) ' || "
                    + "mod(trunc(86400*((sysdate-startup_time) - "
                    + "trunc(sysdate-startup_time))), 60) || ' seconds' up_time "
                    + "from v$database db, v$instance inst";

    private final static String DETAILED_VERSION_SQL = "select banner from v$version";

    private final static String LICENSE_INFO_SQL =
            "select SESSIONS_CURRENT, SESSIONS_HIGHWATER, SESSIONS_MAX, SESSIONS_WARNING, USERS_MAX "
                    + "from v$license";

    private final static String OPTION_SQL = "select * from v$option";

    private final static String SQL_HIT_RATIO =
            "select "
                    + "cg.value CGets, db.value DBGets, pr.value PhyGets, "
                    + "to_char(100*(cg.value+db.value-pr.value)/(cg.value+db.value), '999.99') HitRatio "
                    + "from v$sysstat db, v$sysstat cg, v$sysstat pr where "
                    + "db.name = 'db block gets' and " + "cg.name = 'consistent gets' and "
                    + "pr.name = 'physical reads'";

    // Tablespace Storage Allocation and Percent of space used
    private final static String SQL_TABLESPACE_USAGE =
            "select "
                    + "a.tablespace_name table_space, a.file_id, "
                    + "sum(b.bytes)/count(*) bytes, "
                    + "sum(b.bytes)/count(*) - sum(a.bytes) used, "
                    + "sum(a.bytes) free, "
                    + "to_char(nvl(100-(sum(nvl(a.bytes,0))/(sum(nvl(b.bytes,0))/count(*)))*100,0), '999.99') pct_used "
                    + "from " + "sys.dba_free_space a, sys.dba_data_files b where "
                    + "a.tablespace_name = b.tablespace_name and " + "a.file_id = b.file_id "
                    + "group by a.tablespace_name, a.file_id";

    // SQL to display file statistics within the database.
    private final static String SQL_DATAFILE_STATS =
            "select " + "d.name, s.PHYRDS, s.PHYWRTS " + "from v$datafile d, v$filestat s "
                    + "where d.file#=s.file# " + "order by 1";

    private static final long REFRESH_INTERVAL = 15 * 1000; // 15 seconds
    private long lastRefreshed = -1;

    private String createdTime;
    private String DBID;
    private String hostName;
    private String instanceName;
    private String logMode;
    private String name;
    private String openMode;
    private String startupTime;
    private String upTime;
    private String version;
    private String detailedVersion;
    private CompositeData licenseInfo;
    private TabularData optionsInstalled;

    /* MBean Attributes */
    
    public String getCreatedTime() {
        init();
        return createdTime;
    }

    public String getDBID() {
        init();
        return DBID;
    }

    public String getHostName() {
        init();
        return hostName;
    }

    public String getInstanceName() {
        init();
        return instanceName;
    }

    public String getLogMode() {
        init();
        return logMode;
    }

    public String getName() {
        init();
        return name;
    }

    public String getOpenMode() {
        init();
        return openMode;
    }

    public String getStartupTime() {
        init();
        return startupTime;
    }

    public String getUpTime() {
        init();
        return upTime;
    }

    public String getVersion() {
        init();
        return version;
    }

    public String getDetailedVersion() {
        init();
        return detailedVersion;
    }

    public CompositeData getLicenseInfo() {
        init();
        return licenseInfo;
    }

    public TabularData getOptionsInstalled() {
        init();
        return optionsInstalled;
    }

    /* MBean Operations */
    
    public CompositeData getHitRatioData() throws Exception {
        DataAccessor da = new DataAccessor(SQL_HIT_RATIO);
        return da.execute().getCompositeData();
    }

    public TabularData getTablespaceUsage() throws Exception {
        DataAccessor da = new DataAccessor(SQL_TABLESPACE_USAGE);
        return da.execute().getTabularData();
    }

    public TabularData getFileStatistics() throws Exception {
        DataAccessor da = new DataAccessor(SQL_DATAFILE_STATS);
        return da.execute().getTabularData();
    }
    
    /* Helper methods */
    
    private void init() {
        if (lastRefreshed != -1 && (System.currentTimeMillis() - lastRefreshed) < REFRESH_INTERVAL) {
            // it is not time to refresh
            return;
        }
        /* initialize */
        initDetailedVersion();
        initLicenseInfo();
        initOptionsInstalled();

        ResultSet rset = null;
        try {
            rset = DataAccessor.executeQuery(INIT_SQL);
            if (rset.next()) {
                createdTime = rset.getString("CREATED_TIME");
                DBID = rset.getString("DBID");
                hostName = rset.getString("HOST_NAME");
                instanceName = rset.getString("INSTANCE_NAME");
                logMode = rset.getString("LOG_MODE");
                name = rset.getString("NAME");
                openMode = rset.getString("OPEN_MODE");
                startupTime = rset.getString("STARTUP_TIME");
                upTime = rset.getString("UP_TIME");
                version = rset.getString("VERSION");

                lastRefreshed = System.currentTimeMillis();
            }
        }
        catch (Exception e) {
            logger.severe("Error reading resultset. error: " + e.getMessage());
        }
        finally {
            DataAccessor.close(rset);
        }
    }

    private void initDetailedVersion() {
        /* initialize */
        ResultSet rset = null;
        try {
            rset = DataAccessor.executeQuery(DETAILED_VERSION_SQL);
            StringBuffer temp = new StringBuffer();
            while (rset.next()) {
                temp.append(rset.getString("BANNER"));
                temp.append("\n");
            }
            detailedVersion = temp.toString();
        }
        catch (Exception e) {
            logger.severe("Error reading resultset. error: " + e.getMessage());
        }
        finally {
            DataAccessor.close(rset);
        }
    }

    private void initLicenseInfo() {
        try {
            licenseInfo = new DataAccessor(LICENSE_INFO_SQL).execute().getCompositeData();
        }
        catch (Exception e) {
            logger.severe("Error getting licenseInfo. error: " + e.getMessage());
        }
    }

    private void initOptionsInstalled() {
        try {
            optionsInstalled = new DataAccessor(OPTION_SQL).execute().getTabularData();
        }
        catch (Exception e) {
            logger.severe("Error getting optionsInstalled. error: " + e.getMessage());
        }
    }
}
