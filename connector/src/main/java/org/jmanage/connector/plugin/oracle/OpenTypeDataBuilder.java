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

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Types;

import javax.management.openmbean.CompositeData;
import javax.management.openmbean.CompositeDataSupport;
import javax.management.openmbean.CompositeType;
import javax.management.openmbean.OpenType;
import javax.management.openmbean.SimpleType;
import javax.management.openmbean.TabularData;
import javax.management.openmbean.TabularDataSupport;
import javax.management.openmbean.TabularType;

/**
 * @author Tak-Sang Chan
 * Apr 29, 2006
 */
public class OpenTypeDataBuilder {

    private String[] itemNames;
    private OpenType[] itemTypes;
    
    private String compTypeDesc;
    private String tabularTypeDesc;
    private CompositeType compositeType;
    
    //hold the last compositeData in the table
    private CompositeData compositeData;
    private TabularData tabularData;
    
    public OpenTypeDataBuilder(ResultSet rs, String compTypeDesc, String tabularTypeDesc) 
            throws Exception {
        
        ResultSetMetaData rsMeta = rs.getMetaData();
        itemNames = new String[rsMeta.getColumnCount()];
        itemTypes = new OpenType[rsMeta.getColumnCount()];
        
        for(int i = 0; i < itemNames.length; i++) {
            itemNames[i] = rsMeta.getColumnName(i + 1);
            itemTypes[i] = convertType(rsMeta.getColumnType(i + 1));
        } 
        
        this.compTypeDesc = compTypeDesc;
        this.tabularTypeDesc = tabularTypeDesc;
        this.compositeType = createCompositeType();
        this.tabularData = createTabularData();
    }
    
    public CompositeData createCompositeData(Object[] obj) throws Exception {
        CompositeData compData = new CompositeDataSupport(
                compositeType, itemNames, obj);
        return compData;
    }

    public void addCompositeData(Object[] obj) throws Exception {
        compositeData = createCompositeData(obj);
        tabularData.put(compositeData);        
    }

    public CompositeData getCompositeData() {
        return compositeData;
    }
    
    public TabularData getTabularData() {
        return tabularData;
    }

    private CompositeType createCompositeType() 
        throws Exception {
        
        String[] itemDescriptions = itemNames;
        
        CompositeType compositeType = new CompositeType(
                CompositeType.class.getName(), compTypeDesc,
                itemNames,
                itemDescriptions,
                itemTypes);
        
        return compositeType;        
    }

    private TabularData createTabularData() throws Exception {
        TabularType tabularType = new TabularType(TabularData.class.getName(),
                tabularTypeDesc,
                compositeType,
                new String[]{itemNames[0]});
         return new TabularDataSupport(tabularType);
    } 
    
    private SimpleType convertType(int type) {
            
        if (type == Types.VARCHAR || type == Types.CHAR || type == Types.LONGVARCHAR) {
            return SimpleType.STRING;
        }
        else if (type == Types.INTEGER) {
            return SimpleType.INTEGER;
        }
        else if (type == Types.SMALLINT) {
            return SimpleType.SHORT;
        }
        else if (type == Types.TINYINT) {
            return SimpleType.BYTE;
        }
        else if(type == Types.DECIMAL || type == Types.NUMERIC) {
            return SimpleType.BIGDECIMAL;
        }
        else if (type == Types.BIGINT){
            return SimpleType.LONG;
        }
        else if (type == Types.REAL) {
            return SimpleType.FLOAT;
        }
        else if (type == Types.DOUBLE) {
            return SimpleType.DOUBLE;
        }
        return null;
    }
}
