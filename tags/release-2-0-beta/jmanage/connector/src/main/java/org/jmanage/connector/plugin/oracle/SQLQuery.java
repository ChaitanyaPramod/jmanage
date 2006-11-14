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

import javax.management.openmbean.TabularData;

import org.jmanage.connector.framework.ConnectorSupport;

/**
 * @author Tak-Sang Chan
 * Apr 30, 2006
 */
public class SQLQuery extends ConnectorSupport {

    private final static String SQL_TOP10_EXPENSIVE_QUERY = "select " +
            "rownum as rank, a.* from " +
            "(select buffer_gets, lpad(rows_processed || " +
            "decode(users_opening + users_executing, 0, ' ', '*'), 20) \"rows_processed\", " +
            "executions, loads, (decode(rows_processed,0,1,1)) * " +
            "buffer_gets/decode(rows_processed,0,1,rows_processed) avg_cost, " +
            "sql_text " +
            "from v$sqlarea " +
            "where hash_value = hash_value order by 5 desc) a " +
            "where rownum < 11";
    
    private TabularData topTenExpensiveQueries;
    
    public String retrieveTopTenExpensiveQueries() throws Exception {
        DataAccessor da = new DataAccessor(SQL_TOP10_EXPENSIVE_QUERY);
        topTenExpensiveQueries = da.execute().getTabularData();
        return "OK";        
    }

    public TabularData getTopTenExpensiveQueries() {
        return topTenExpensiveQueries;
    }
    
}
