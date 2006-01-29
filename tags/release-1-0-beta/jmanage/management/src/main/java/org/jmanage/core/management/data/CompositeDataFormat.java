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

import javax.management.openmbean.CompositeData;
import javax.management.openmbean.CompositeType;
import java.util.Iterator;
import java.util.Set;

/**
 * Formats javax.management.openmbbean.CompositeData
 * and array of javax.management.openmbbean.CompositeData.
 * [javax.management.openmbean.CompositeData;
 * <p>
 * Date:  Sep 27, 2005
 * @author	Rakesh Kalra
 */
public abstract class CompositeDataFormat implements DataFormat {

    public String format(Object data) {

        String output = null;
        if(data.getClass().isArray()){
            CompositeData[] array = (CompositeData[])data;
            StringBuffer buff = new StringBuffer();
            for(int i=0; i<array.length; i++){
                if(i > 0){
                    buff.append(DataFormatUtil.getListDelimiter());
                    buff.append(DataFormatUtil.getListDelimiter());
                }
                buff.append(format(array[i]));
            }
            output = buff.toString();
        }else{
            output = format((CompositeData)data);
        }
        return output;
    }

    private String format(CompositeData compositeData){
        if(compositeData == null) return DataFormatUtil.getNullDisplayValue();
        CompositeType type = compositeData.getCompositeType();
        Set itemNamesSet = type.keySet();
        String[] itemNames = new String[itemNamesSet.size()];
        String[] itemValues = new String[itemNamesSet.size()];
        int index = 0;
        for(Iterator it=itemNamesSet.iterator();it.hasNext();){
            itemNames[index] = (String)it.next();
            Object value = compositeData.get(itemNames[index]);
            itemValues[index] = DataFormatUtil.format(value);
            index ++;
        }

        /* draw the table */
        Table table = getTable();
        table.setHeader(itemNames);
        table.addRow(itemValues);
        return table.draw();
    }


    protected abstract Table getTable();
}
