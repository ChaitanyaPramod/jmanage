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

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.management.openmbean.CompositeData;
import javax.management.openmbean.TabularData;

/**
 * @author Tak-Sang Chan
 * Apr 30, 2006
 */
public class DataAccessor {

    ConnectionManager connMgr = ConnectionManager.getInstance();
    
    String sql;
    OpenTypeDataBuilder openTypeData;

    DataAccessor(String sql) {
        this.sql = sql;
    }
        
    public DataAccessor execute() throws Exception {

        ResultSet rs = executeQuery(sql);
        int colctr = rs.getMetaData().getColumnCount();
        openTypeData = new OpenTypeDataBuilder(rs, 
                "JMX Composite type", 
                "JMX Tabular type");
        
        while (rs.next()) {
            Object[] obj = new Object[colctr];

            for(int i = 0; i < colctr; i++ ) {
                obj[i] = rs.getObject(i + 1);
            }

            openTypeData.addCompositeData(obj);
        }
        
        return this;
    }
    
    public TabularData getTabularData() {
        return openTypeData.getTabularData();
    }
    
    public CompositeData getCompositeData() {
        return openTypeData.getCompositeData();
    }

    private ResultSet executeQuery(String sql) throws Exception {
        PreparedStatement stmt = null;
        ResultSet rs = null;        
        Connection conn = connMgr.getConnection();        
        stmt = conn.prepareStatement(sql);
        rs = stmt.executeQuery();  
        return rs;
    }    
}
