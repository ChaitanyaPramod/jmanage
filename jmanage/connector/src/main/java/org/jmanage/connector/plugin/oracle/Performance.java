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
 * Apr 29, 2006
 */
public class Performance extends ConnectorSupport {
    
    // SQL to list all supported INIT.ORA parameters and values
    private final static String SQL_SUPPORTED_INIT_PARAM = "select a.ksppinm name, " +
            "b.ksppstvl value, b.ksppstdf isdefault, " +
            "decode(a.ksppity, 1, 'boolean', 2, 'string', 3, 'number', " +
            "4, 'file', a.ksppity) type, " +
            "a.ksppdesc description from " +
            "sys.x$ksppi a, sys.x$ksppcv b where " +
            "a.indx = b.indx and " +
            "a.ksppinm not like '\\_%' escape '\\' " +
            "order by name";
    
    // SQL to displays details of the session which have used the most CPU. 
    // Needs the Init.Ora parameter timed_statistics set to true to work
    private final static String SQL_TOP_TEN_USERS = "select rownum as rank, a.* " +
            "from ( " +
            "SELECT v.sid, program, to_char(v.value/(60*100), '0999.999999') \"CPU-MINS\" " +
            "FROM v$statname s , v$sesstat v, v$session sess " +
            "WHERE s.name = 'CPU used by this session' and " +
            "sess.sid = v.sid and " +
            "v.statistic#=s.statistic# " +
            "and v.value>0 " +
            "ORDER BY v.value DESC) a " +
            "where rownum < 11";
        
    // SQL to list the total memory in the shared_pool and the amount of free memory. 
    // (Too much free memory in the shared_pool is wasteful and may be better used elsewhere)
    private final static String SQL_SHARED_POOL_STAT = "select pool, " +
            "to_number(v$parameter.value) value, v$sgastat.bytes \"BYTES-FREE\", " +
            "to_char((v$sgastat.bytes/v$parameter.value)*100, '999.99') \"PERCENT-FREE\" from " +
            "v$sgastat, v$parameter where " +
            "v$sgastat.name='free memory' and " +
            "pool='shared pool' and " +
            "v$parameter.name = 'shared_pool_size'";
    
    private TabularData initOraParameters;    
    private TabularData topTenUsers;
    private CompositeData sharedPoolStatistics;
    
    public String retrieveAllData() throws Exception {
        retrieveTopTenUsers();
        retrieveSharedPoolStatistics();
        retrieveInitOraParameters();
        return "OK";
    }
    
    public String retrieveInitOraParameters() throws Exception {
        DataAccessor da = new DataAccessor(SQL_SUPPORTED_INIT_PARAM);
        initOraParameters = da.execute().getTabularData();
        return "OK";
    }
    
    public String retrieveTopTenUsers() throws Exception {
        DataAccessor da = new DataAccessor(SQL_TOP_TEN_USERS);
        topTenUsers = da.execute().getTabularData();
        return "OK";
    }

    public String retrieveSharedPoolStatistics() throws Exception {
        DataAccessor da = new DataAccessor(SQL_SHARED_POOL_STAT);
        sharedPoolStatistics = da.execute().getCompositeData();
        return "OK";
    }
    
    public TabularData getInitOraParameters() {
        return initOraParameters;
    }

    public CompositeData getSharedPoolStatistics() {
        return sharedPoolStatistics;
    }

    public TabularData getTopTenUsers() {
        return topTenUsers;
    } 
}