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
package org.jmanage.core.management.data;

import org.jmanage.util.display.Table;

import javax.management.openmbean.TabularData;
import javax.management.openmbean.TabularType;
import javax.management.openmbean.CompositeType;
import javax.management.openmbean.CompositeData;
import java.util.Set;
import java.util.Iterator;
import java.util.List;
import java.util.LinkedList;

/**
 *
 * <p>
 * Date:  Oct 1, 2005
 * @author	Rakesh Kalra
 */
public abstract class TabularDataFormat implements DataFormat {

    public String format(Object data) {
        TabularData tabularData = (TabularData)data;

        TabularType type = tabularData.getTabularType();
        CompositeType rowType = type.getRowType();
        Set itemNamesSet = rowType.keySet();
        /* get the header names */
        String[] itemNames = new String[itemNamesSet.size()];
        int index = 0;
        for(Iterator it=itemNamesSet.iterator();it.hasNext();){
            itemNames[index] = (String)it.next();
            index ++;
        }
        /* get the rows */
        List rows = new LinkedList();
        for(Iterator it=tabularData.values().iterator(); it.hasNext();){
            CompositeData compositeData = (CompositeData)it.next();
            String[] itemValues = new String[itemNames.length];
            for(int i=0; i<itemNames.length; i++){
                Object value = compositeData.get(itemNames[i]);
                itemValues[i] = DataFormatUtil.format(value);
            }
            rows.add(itemValues);
        }

        /* draw the table */
        Table table = getTable();
        table.setHeader(itemNames);
        table.addRows(rows);
        return table.draw();
    }

    protected abstract Table getTable();
}

