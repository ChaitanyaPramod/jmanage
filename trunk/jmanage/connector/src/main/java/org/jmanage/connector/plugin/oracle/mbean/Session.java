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
package org.jmanage.connector.plugin.oracle.mbean;

import javax.management.openmbean.TabularData;

import org.jmanage.connector.framework.ConnectorSupport;
import org.jmanage.connector.plugin.oracle.DataAccessor;

/**
 * @author Tak-Sang Chan
 * @author Rakesh Kalra
 */
public class Session extends ConnectorSupport {
    
    private static String SQL_SESSION = "select SID, USERNAME, " +
            "TO_CHAR(LOGON_TIME, 'MM/DD/YYYY hh:mi:ss') LOGON_TIME, " +
            "RTRIM(MACHINE) MACHINE, " +
            "PROGRAM, OSUSER, STATUS, " +
            "trunc(last_call_et/60) IDLE_MINS " +
            "from V$SESSION s " +
            "where (s.username is not null)";

    private static String SQL_SESS_HIT_RATIO = "select v$sess_io.sid, Username, " +
            "consistent_gets, block_gets, physical_reads, " +
            "to_char(100*(consistent_gets+block_gets-physical_reads)/ " +
            "(consistent_gets+block_gets), '999.99') HitRatio " +
            "from v$session, v$sess_io where " +
            "v$session.sid = v$sess_io.sid and " +
            "(consistent_gets + block_gets) > 0 and " +
            "Username is NOT NULL";

    public TabularData getSessionHitRatio() throws Exception {
        DataAccessor da = new DataAccessor(SQL_SESS_HIT_RATIO);
        return da.execute().getTabularData();
    }

    public TabularData getSessionInfo() throws Exception {
        DataAccessor da = new DataAccessor(SQL_SESSION);
        return da.execute().getTabularData();
    } 
}
