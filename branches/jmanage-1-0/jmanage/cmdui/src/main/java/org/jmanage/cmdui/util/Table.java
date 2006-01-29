/**
 * Copyright 2004-2005 jManage.org
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jmanage.cmdui.util;

import org.jmanage.util.StringUtils;

import java.util.List;
import java.util.LinkedList;
import java.util.Iterator;

/**
 * Utility class to print formatted table to command line.
 *
 * date:  Feb 25, 2005
 * @author	Rakesh Kalra
 */
public class Table {

    private static final int COLUMN_SPACING = 5;

    private Object[] header;
    private final int columns;
    private final List rows;
    /* contains the max column sizes */
    private final int[] columnSize;

    public Table(int columns){
        this.columns = columns;
        this.rows = new LinkedList();
        this.columnSize = new int[columns];
    }

    public void add(Object obj1, Object obj2){
        add(new Object[]{obj1, obj2});
    }

    public void add(Object obj1, Object obj2, Object obj3){
        add(new Object[]{obj1, obj2, obj3});
    }

    public void add(Object obj1, Object obj2, Object obj3, Object obj4){
        add(new Object[]{obj1, obj2, obj3, obj4});
    }

    public void add(Object obj1, Object obj2, Object obj3, Object obj4, Object obj5){
        add(new Object[]{obj1, obj2, obj3, obj4, obj5});
    }

    public void add(Object[] objects){
        assert columns == objects.length;
        rows.add(objects);
        setColumnSize(objects);
    }

    public void setHeader(Object[] header) {
        this.header = header;
        setColumnSize(header);
    }

    private void setColumnSize(Object[] objects){
        for(int i=0; i<objects.length; i++){
            if(objects[i].toString().length() > columnSize[i])
                columnSize[i] = objects[i].toString().length();
        }
    }

    public void print(){
        Out.println();
        if(header != null){
            printRow(header);
            printUnderline(header);
        }
        for(Iterator it=rows.iterator(); it.hasNext();){
            Object[] cols = (Object[])it.next();
            printRow(cols);
        }
    }

    private void printRow(Object[] cols){
        for(int i=0; i < columns; i++){
            String columnValue = StringUtils.padRight(cols[i].toString(),
                    columnSize[i] + COLUMN_SPACING);
            Out.print(columnValue);
        }
        Out.println();
    }

    private void printUnderline(Object[] cols){
        for(int i=0; i < columns; i++){
            String underline =
                    StringUtils.getCharSeries('-', cols[i].toString().length());
            underline = StringUtils.padRight(underline,
                    columnSize[i] + COLUMN_SPACING);
            Out.print(underline);
        }
        Out.println();
    }
}
