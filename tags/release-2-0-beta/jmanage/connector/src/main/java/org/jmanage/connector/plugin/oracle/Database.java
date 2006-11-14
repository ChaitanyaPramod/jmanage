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

import javax.management.openmbean.CompositeData;
import javax.management.openmbean.TabularData;

import org.jmanage.connector.framework.ConnectorSupport;

/**
 * @author Tak-Sang Chan
 * Apr 30, 2006
 */
public class Database extends ConnectorSupport {

    private final static String SQL_BASIC_INFO = "select db.dbid, db.name, " +
            "inst.instance_name, inst.host_name, " +
            "to_char(db.created, 'MM/DD/YYYY HH:MM:SS AM') created_time, " +
            "db.log_mode, db.open_mode, " +
            "inst.version, " +
            "to_char(inst.startup_time, 'MM/DD/YYYY HH:MM:SS AM') startup_time, " +
            "floor(sysdate - startup_time) || ' days(s) ' || " +
            "trunc( 24*((sysdate-startup_time) - " +
            "trunc(sysdate-startup_time))) || ' hour(s) ' || " +
            "mod(trunc(1440*((sysdate-startup_time) - " +
            "trunc(sysdate-startup_time))), 60) ||' minute(s) ' || " +
            "mod(trunc(86400*((sysdate-startup_time) - " +
            "trunc(sysdate-startup_time))), 60) || ' seconds' up_time " +
            "from v$database db, v$instance inst";
    
    private final static String SQL_HIT_RATIO = "select " +
            "cg.value CGets, db.value DBGets, pr.value PhyGets, " +
            "to_char(100*(cg.value+db.value-pr.value)/(cg.value+db.value), '999.99') HitRatio " +
            "from v$sysstat db, v$sysstat cg, v$sysstat pr where " +
            "db.name = 'db block gets' and " +
            "cg.name = 'consistent gets' and " +
            "pr.name = 'physical reads'";
        
    // Tablespace Storage Allocation and Percent of space used
    private final static String SQL_TABLESPACE_USAGE = "select " +
            "a.tablespace_name table_space, a.file_id, " +
            "sum(b.bytes)/count(*) bytes, " +
            "sum(b.bytes)/count(*) - sum(a.bytes) used, " +
            "sum(a.bytes) free, " +
            "to_char(nvl(100-(sum(nvl(a.bytes,0))/(sum(nvl(b.bytes,0))/count(*)))*100,0), '999.99') pct_used " +
            "from " +
            "sys.dba_free_space a, sys.dba_data_files b where " +
            "a.tablespace_name = b.tablespace_name and " +
            "a.file_id = b.file_id " +
            "group by a.tablespace_name, a.file_id";

    // SQL to display file statistics within the database.
    private final static String SQL_DATAFILE_STATS = "select " +
            "d.name, s.PHYRDS, s.PHYWRTS " +
            "from v$datafile d, v$filestat s " +
            "where d.file#=s.file# " +
            "order by 1";
    
    private CompositeData hitRatioData;
    private CompositeData basicData;
    private TabularData tablespaceUsage;
    private TabularData fileStatistics;
     
    public String retrieveAllData() throws Exception {
        retrieveTablespaceUsage();
        retrieveBasicData();
        retrieveHitRatio();
        retrieveFileStatistics();
        return "OK";
    }
    
    public String retrieveTablespaceUsage() throws Exception {
        DataAccessor da = new DataAccessor(SQL_TABLESPACE_USAGE);
        tablespaceUsage = da.execute().getTabularData();
        return "OK";
    }
    
    public String retrieveBasicData() throws Exception {
        DataAccessor da = new DataAccessor(SQL_BASIC_INFO);
        basicData = da.execute().getCompositeData();
        return "OK";
    }
    
    public String retrieveHitRatio() throws Exception {
        DataAccessor da = new DataAccessor(SQL_HIT_RATIO);
        hitRatioData = da.execute().getCompositeData();
        return "OK";
    }

    public String retrieveFileStatistics() throws Exception {
        DataAccessor da = new DataAccessor(SQL_DATAFILE_STATS);
        fileStatistics = da.execute().getTabularData();
        return "OK";
    }
    
    public CompositeData getHitRatioData() {
        return hitRatioData;
    }

    public CompositeData getBasicData() {
        return basicData;
    }
    
    public TabularData getTablespaceUsage() {
        return tablespaceUsage;
    }

    public TabularData getFileStatistics() {
        return fileStatistics;
    }
}
