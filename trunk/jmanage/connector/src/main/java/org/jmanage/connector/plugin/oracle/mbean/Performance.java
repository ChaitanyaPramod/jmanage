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
public class Performance {

    private static final Logger logger = Loggers.getLogger(Performance.class);

    // The queries taken from the earlier implementation of Oracle connector by Tak-Sang Chan.

    // SQL to list all supported INIT.ORA parameters and values
    private final static String SQL_SUPPORTED_INIT_PARAM =
            "select a.ksppinm name, " + "b.ksppstvl value, b.ksppstdf isdefault, "
                    + "decode(a.ksppity, 1, 'boolean', 2, 'string', 3, 'number', "
                    + "4, 'file', a.ksppity) type, " + "a.ksppdesc description from "
                    + "sys.x$ksppi a, sys.x$ksppcv b where " + "a.indx = b.indx and "
                    + "a.ksppinm not like '\\_%' escape '\\' " + "order by name";

    // SQL to displays details of the session which have used the most CPU.
    // Needs the Init.Ora parameter timed_statistics set to true to work
    private final static String SQL_TOP_TEN_USERS_BY_CPU =
            "select rownum as rank, a.* "
                    + "from ( "
                    + "SELECT v.sid, program, to_char(v.value/(60*100), '0999.999999') \"CPU-MINS\" "
                    + "FROM v$statname s , v$sesstat v, v$session sess "
                    + "WHERE s.name = 'CPU used by this session' and " + "sess.sid = v.sid and "
                    + "v.statistic#=s.statistic# " + "and v.value>0 " + "ORDER BY v.value DESC) a "
                    + "where rownum < 11";

    private final static String SQL_TOP10_EXPENSIVE_QUERY =
            "select "
                    + "rownum as rank, a.* from "
                    + "(select buffer_gets, lpad(rows_processed || "
                    + "decode(users_opening + users_executing, 0, ' ', '*'), 20) \"rows_processed\", "
                    + "executions, loads, (decode(rows_processed,0,1,1)) * "
                    + "buffer_gets/decode(rows_processed,0,1,rows_processed) avg_cost, "
                    + "sql_text from v$sqlarea "
                    + "where hash_value = hash_value order by 5 desc) a where rownum < 11";

    // Query taken from http://www.adp-gmbh.ch/ora/misc/dynamic_performance_views.html
    private final static String SQL_LOCKED_OBJECTS =
            "select oracle_username, os_user_name, locked_mode, object_name, object_type "
                    + "from v$locked_object a,dba_objects b " 
                    + "where a.object_id = b.object_id";

    // should be greater than 95%
    private final static String SQL_DATA_BLOCK_BUFFER_HIT_RATIO =
        "select 1-(sum(decode(name, 'physical reads', value,0))/"
               +  "(sum(decode(name, 'db block gets', value, 0)) + "    
               +  "(sum(decode(name, 'consistent gets', value, 0))))) "
               +  "\"Read Hit Ratio\" " 
         + "from v$sysstat";
         
    // should be greater than 95%
    private final static String SQL_DICTIONARY_HIT_RATIO = 
        "select (1 - (sum(getmisses) / (sum(gets) + sum(getmisses)))) dictionary_hit_ratio "
         + "from v$rowcache";
    
    //  should be greater than 95%
    private final static String SQL_SHARED_SQL_PIN_HIT_RATIO =
        "select ((sum(pinhits) / sum(pins)) * 100) PinHitRatio from v$librarycache";
    
    //  should be greater than 99%
    private final static String SQL_SHARED_SQL_RELOAD_HIT_RATIO =
        "select ((sum(pins) / (sum(pins) + sum(reloads))) * 100) ReloadHitRatio from v$librarycache";
    
    
    // This query is not used as I didn't work on my database -rk
    // SQL to list the total memory in the shared_pool and the amount of free memory. 
    // (Too much free memory in the shared_pool is wasteful and may be better used elsewhere)
    private final static String SQL_SHARED_POOL_STAT = "select pool, " +
            "to_number(v$parameter.value) value, v$sgastat.bytes \"BYTES-FREE\", " +
            "to_char((v$sgastat.bytes/v$parameter.value)*100, '999.99') \"PERCENT-FREE\" from " +
            "v$sgastat, v$parameter where " +
            "v$sgastat.name='free memory' and " +
            "pool='shared pool' and " +
            "v$parameter.name = 'shared_pool_size'";
    
    
    public TabularData getSupportedInitParams() {
        try {
            return new DataAccessor(SQL_SUPPORTED_INIT_PARAM).execute().getTabularData();
        }
        catch (Exception e) {
            logger.severe("Error getting supported init params. error: " + e.getMessage());
            return null;
        }
    }

    public TabularData getTopTenUsers() {
        try {
            return new DataAccessor(SQL_TOP_TEN_USERS_BY_CPU).execute().getTabularData();
        }
        catch (Exception e) {
            logger.severe("Error getting top ten users by cpu. error: " + e.getMessage());
            return null;
        }
    }
    
    public TabularData getTopTenExpensiveQueries() {
        try {
            return new DataAccessor(SQL_TOP10_EXPENSIVE_QUERY).execute().getTabularData();
        }
        catch (Exception e) {
            logger.severe("Error getting top ten users. error: " + e.getMessage());
            return null;
        }
    }
    
    public TabularData getLockedObjects() {
        try {
            return new DataAccessor(SQL_LOCKED_OBJECTS).execute().getTabularData();
        }
        catch (Exception e) {
            logger.severe("Error getting locked objects. error: " + e.getMessage());
            return null;
        }
    }
    

    public Double getBlockBufferHitRatio() {
        ResultSet rset = null;
        try {
            rset = DataAccessor.executeQuery(SQL_DATA_BLOCK_BUFFER_HIT_RATIO);
            if(rset.next()){
                return rset.getDouble(1);
            }
        }
        catch (Exception e) {
            logger.severe("Error reading resultset. error: " + e.getMessage());
        }
        finally {
            DataAccessor.close(rset);
        }
        return null;
    }
    
    public Double getDictionaryHitRatio() {
        ResultSet rset = null;
        try {
            rset = DataAccessor.executeQuery(SQL_DICTIONARY_HIT_RATIO);
            if(rset.next()){
                return rset.getDouble(1);
            }
        }
        catch (Exception e) {
            logger.severe("Error reading resultset. error: " + e.getMessage());
        }
        finally {
            DataAccessor.close(rset);
        }
        return null;
    }

    public Double getSharedSQLPinHitRatio() {
        ResultSet rset = null;
        try {
            rset = DataAccessor.executeQuery(SQL_SHARED_SQL_PIN_HIT_RATIO);
            if(rset.next()){
                return rset.getDouble(1);
            }
        }
        catch (Exception e) {
            logger.severe("Error reading resultset. error: " + e.getMessage());
        }
        finally {
            DataAccessor.close(rset);
        }
        return null;
    }

    public Double getSharedSQLReloadHitRatio() {
        ResultSet rset = null;
        try {
            rset = DataAccessor.executeQuery(SQL_SHARED_SQL_RELOAD_HIT_RATIO);
            if(rset.next()){
                return rset.getDouble(1);
            }
        }
        catch (Exception e) {
            logger.severe("Error reading resultset. error: " + e.getMessage());
        }
        finally {
            DataAccessor.close(rset);
        }
        return null;
    }
}
