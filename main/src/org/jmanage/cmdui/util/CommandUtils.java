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

import org.jmanage.core.data.MBeanData;

import java.util.List;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;

/**
 *
 * date:  Feb 23, 2005
 * @author	Rakesh Kalra
 */
public class CommandUtils {

    public static void printMBeans(List mbeanList){
        /* first sort the list */
        Collections.sort(mbeanList, new Comparator(){
            public int compare(Object o1, Object o2) {
                MBeanData mbeanData1 = (MBeanData)o1;
                MBeanData mbeanData2 = (MBeanData)o2;
                return mbeanData1.getName().compareTo(mbeanData2.getName());
            }
        });

        for(Iterator it=mbeanList.iterator(); it.hasNext(); ){
            MBeanData mbeanData = (MBeanData)it.next();
            Out.print(mbeanData.getName());
            if(mbeanData.getConfiguredName() != null){
                Out.println(" [" + mbeanData.getConfiguredName() + "]");
            }else{
                Out.println();
            }
        }
    }
}
